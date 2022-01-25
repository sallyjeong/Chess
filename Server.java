package chessproject;

//imports for network communication
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Server {
    public ServerSocket serverSocket;
    public Set<ClientHandler> clientHandlers = new HashSet<>();
    public Map <String, ArrayList<ClientHandler>> publicRooms = new LinkedHashMap<>();
    public Map <String, ArrayList<ClientHandler>> privateRooms = new HashMap<>();
    public static ArrayList<String> roomNames = new ArrayList<>();
    public BlockingQueue<ClientHandler> quickMatch = new LinkedBlockingQueue<>();

    public Server() {
        try {
            serverSocket = new ServerSocket(Constants.PORT);
            System.out.println("Waiting for connections");
            while (!serverSocket.isClosed()) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Server-Client connection made");

                ClientHandler clientHandler = new ClientHandler(clientSocket);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
    }

    private class ClientHandler implements Runnable {
        private String username = " ";
        private String colour;
        private String room = "";
        private Socket socket;
        private BufferedReader dataIn;
        private BufferedWriter dataOut;
        private boolean priv; //in private room or not

        public ClientHandler(Socket socket) {
            try {
                this.socket = socket;
                dataIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                dataOut = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                clientHandlers.add(this);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            while (!socket.isClosed() && socket.isConnected()) {
                try {
                    String input = dataIn.readLine();
                    if (input != null) {
                        char type = input.charAt(0); // corresponds to the data type identifiers in Constants
                        input = input.substring(1); // actual message

                        // in-game data
                        if (type == Constants.CHAT_DATA) {
                            broadcastMessage(Constants.CHAT_DATA + input);
                        } else if (type == Constants.MOVE_DATA) {
                            broadcastMessage(Constants.MOVE_DATA + input);

                        // start game data
                        } else if (type == Constants.USERNAME_DATA) {
                            addUsername(input);
                        } else if (type == Constants.CREATE_ROOM_DATA) {
                            createRoom(input);
                        } else if (type == Constants.JOIN_PRIV_ROOM_DATA) {
                            joinPrivateRoom(input);
                        } else if (type == Constants.QUICK_MATCH_DATA) {
                            startMatchmaking(input);
                        } else if (type == Constants.JOIN_PUB_ROOM_DATA) {
                            joinPublicRoom(input);
                        } else if (type == Constants.COLOUR_DATA) {
                            addColour(input);
                        } else if (type == Constants.BOARD_DATA) {
                            shareBoard(input);
                        } else if (type==Constants.ROOM_NAMES_DATA) {
                            sendRoomNames(input);

                        // end game data
                        } else if (type == Constants.LEAVE_ROOM_DATA) {
                            leaveRoom(input);
                        } else if (type == Constants.DRAW_DATA) {
                            sendDraw(input);
                        }  else if (type==Constants.GAME_OVER_DATA){
                            broadcastMessage(Constants.GAME_OVER_DATA + input);
                            broadcastMessage(Constants.LEAVE_ROOM_DATA + "true"); // mimics player leaving room
                        } else if (type == Constants.QUIT_DATA) {
                            closeConnection();
                            System.out.println("CLIENT HANDLER " + username + " CLOSED");
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }

        /*
        METHODS FOR SENDING DATA
         */

        // sends information to everyone else in the same room as the origin
        public void broadcastMessage(String msg) {
            ArrayList<ClientHandler> roomMembers;
            if (priv) {
                roomMembers = privateRooms.get(room);
            } else {
                roomMembers = publicRooms.get(room);
            }

            if (roomMembers!= null && roomMembers.size() > 1) {
                for (ClientHandler member : roomMembers) {

                        if ((!member.username.equals(username)) && (!member.username.equals(" "))) {
                            member.writeData(msg);
                        }

                }
            }
        }

        public void writeData(String data) {
            try {
                dataOut.write(data);
                dataOut.newLine();
                dataOut.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // sends information to everyone connected to the server
        public void broadcastMessageToAll(String msg) {
            for (ClientHandler clientHandler : clientHandlers) {
                if ((!clientHandler.username.equals(username)) && (!clientHandler.username.equals(" "))) {
                    clientHandler.writeData(msg);
                }   
            }
        }

        /*
        METHODS FOR JOINING GAMES
         */
        public void addUsername(String input) {
            if (validUsername(input)) {
                username = input;
                writeData("success. welcome " + username);
            } else {
                writeData(Constants.USERNAME_ERROR);
            }
        }

        public boolean validUsername(String user){
            for (ClientHandler clientHandler : clientHandlers){
                if (clientHandler.username.equals(user)){
                    return false;
                }
            }
            if (!user.matches("[a-zA-Z0-9]*")){
                return false;
            }
            if ((user.equals("")) || (user.equals(null))){
                return false;
            }
            return true;
        }

        public void createRoom(String input) {
            priv = true;
            room = input;
            privateRooms.put(input, new ArrayList<ClientHandler>());
            privateRooms.get(input).add(this);
            writeData(Constants.CREATE_ROOM_DATA + "room [" + input + "] created successfully");
        }

        public void joinPrivateRoom(String input) {
            if (privateRooms.containsKey(input)) {
                priv = true;
                room = input;
                privateRooms.get(input).add(this);
                writeData("success. welcome " + username);
                broadcastMessage(Constants.CHAT_DATA + "*** " + username + " has joined the chat ***");
                if (privateRooms.get(room).size() == 2) {
                    broadcastMessage(Constants.START_DATA + "");
                }
            } else {
                writeData(Constants.JOIN_ROOM_ERROR);
            }
        }

        public void startMatchmaking(String input) {
            writeData(Constants.QUICK_MATCH_WAIT);
            quickMatch.add(this);

            if (quickMatch.size()%2==1){
                System.out.println("after while");
                room = CodeGenerator.generateCode();
                publicRooms.put(room, new ArrayList<ClientHandler>());
                colour="white";

                while (quickMatch.size()%2!=0){
                    // waiting for another user to click matchmaking
                }
            } else {
                System.out.println("quick match:"+quickMatch);
                room = quickMatch.peek().getRoom();
                colour="black";
                quickMatch.poll();
                quickMatch.poll();
                broadcastMessageToAll(Constants.UPDATE_LIST+"");

            }
            priv = false;
            publicRooms.get(room).add(this);
            writeData(Constants.QUICK_MATCH_JOINED);
            writeData(room);
            writeData(colour);

            if (colour.equals("black")) { // only adds the roomName once per quick match
                String roomName = publicRooms.get(room).get(0).username +" vs "+ publicRooms.get(room).get(1).username;
                roomNames.add(roomName);
            }
        }

        public void joinPublicRoom(String input) {
            List<String> keys = new ArrayList<String>(publicRooms.keySet());
            String room = keys.get((roomNames.indexOf(input)));
            publicRooms.get(room).add(this);

            broadcastMessage(Constants.CHAT_DATA + username + " has joined the chat");
        }

        public void addColour(String input) {
            // first player (room creator)
            if (input.equals("black") || input.equals("white")) {
                colour = input;
                writeData(colour);
            } else {
                ArrayList<ClientHandler> existingPlayers = privateRooms.get(room);

                // second player (opposite colour of first)
                if (existingPlayers.size() == 2) {
                    String existingColour = existingPlayers.get(0).colour;
                    if (existingColour.equals("white")) {
                        colour = "black";
                    } else if (existingColour.equals("black")) {
                        colour = "white";
                    }
                    writeData(colour);

                    // spectators
                } else {
                    writeData(Constants.COLOUR_DATA + "");
                }
            }
        }

        public void shareBoard(String input) {
            Map<String, ArrayList<ClientHandler>> rooms;
            if (priv) {
                rooms = privateRooms;
            } else {
                rooms = publicRooms;
            }

            // spectator sends request to copy board from player of the same colour POV
            if (input.equals(Constants.REQUEST)) {
                    if (rooms.get(room).get(0).colour.equals(colour)) { //colour chosen matches first player
                        rooms.get(room).get(0).writeData(Constants.BOARD_DATA + username);
                      
                    } else { //colour matches second player
                        rooms.get(room).get(1).writeData(Constants.BOARD_DATA + username);
                    }
                

            // player sending board data back to spectator that requested it
            } else {
                // input format: username + " " + [row][col][piece symbol][b/w])
                // example: chess 00Pw
                String sendToUser = input.substring(0, input.indexOf(" "));
                for (ClientHandler spectator : rooms.get(room)) {
                        if (spectator.username.equals(sendToUser)) {
                            spectator.writeData(Constants.BOARD_DATA + input.substring(input.indexOf(" ")+1));
                            
                        }
                    
                }
            }
        }

        public void sendRoomNames(String input) {
            writeData(""+roomNames.size());
            for (String roomName: roomNames){
                writeData(roomName);
            }
        }

        /*
        METHODS FOR ENDING GAMES
         */

        public void sendDraw(String input) {
            // input is either "!request" or "denied"
            Map<String, ArrayList<ClientHandler>> rooms;
            if (priv) {
                rooms = privateRooms;
            } else {
                rooms = publicRooms;
            }

            
                if (rooms.get(room).size() > 1) {
                    // sending draw request or result to opponent
                    if (rooms.get(room).get(0).username.equals(username)) {
                        rooms.get(room).get(1).writeData(Constants.DRAW_DATA + input);

                    } else {
                        rooms.get(room).get(0).writeData(Constants.DRAW_DATA + input);
                    }
                }
                
        }

        public void leaveRoom(String input) {
            Map<String, ArrayList<ClientHandler>> rooms;
            if (priv){
                rooms = privateRooms;
            }else{
                rooms = publicRooms;
            }

            if (input.equals("true")) { // player has left the room
                broadcastMessage(Constants.LEAVE_ROOM_DATA + input);
                if (priv) {
                    rooms.remove(room);
                } else {
                    String roomName =  rooms.get(room).get(0).username + " vs " + rooms.get(room).get(1).username;
                    if (roomNames.contains(roomName)){
                        roomNames.remove(roomName);
                        publicRooms.remove(roomName);
                        System.out.println("room removed       " + rooms.get(room).get(0).username + " vs " + rooms.get(room).get(1).username);
                    }

                    broadcastMessageToAll(Constants.UPDATE_LIST + "");
                }

            } else { // spectator leaves room
                if (rooms.get(room) != null) {
                    rooms.get(room).remove(username);
                }
                broadcastMessage(Constants.CHAT_DATA + "*** " + username + " has left the chat ***");
            }
        }

        public void closeConnection() {
            clientHandlers.remove(this);
            try {
                if (socket != null) {
                    socket.close();
                }
                if (dataIn != null) {
                    dataIn.close();
                }
                if (dataOut != null) {
                    dataOut.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        public String getRoom() {
            return room;
        }

        public ArrayList getGetRoomNames() {
            return roomNames;
        }

    }
}
