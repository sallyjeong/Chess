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

/** [Server.java]
 * Multi-threaded Server
 * Inner class contains code for network communication
 * among various Clients running our chess program
 * Accepts and handles connections to individual clients on a new thread
 * @author Katherine Liu, Sally Jeong
 * @version 1.0 Jan 25, 2022
 */

public class Server {
    public ServerSocket serverSocket;
    public Set<ClientHandler> clientHandlers = new HashSet<>();
    public Map <String, ArrayList<ClientHandler>> publicRooms = new LinkedHashMap<>();
    public Map <String, ArrayList<ClientHandler>> privateRooms = new HashMap<>();
    public static ArrayList<String> roomNames = new ArrayList<>();
    public BlockingQueue<ClientHandler> quickMatch = new LinkedBlockingQueue<>();


    /**
     * Server
     * This constructor creates a ServerSocket and waits for Client connections
     * Creates a new Thread to handle each connection
     */
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

    /** [ClientHandler.java]
     * Inner class that handles each Client on a new thread
     * Stores data specific to each Client
     * Various methods for receiving and sending data out to different Clients
     * @author Katherine Liu, Sally Jeong
     * @version 1.0 Jan 25, 2022
     */
    private class ClientHandler implements Runnable {
        private String username = " ";
        private String colour;
        private String room = "";
        private Socket socket;
        private BufferedReader dataIn;
        private BufferedWriter dataOut;
        private boolean priv; //in private room or not

        /**
         * ClientHandler
         * This constructor is used to connect the ClientHandler to the correct socket
         * and establish the BufferedReader and BufferedWriter
         * @param socket is the Socket used for sending and receiving data
         */
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

        /**
         * run
         * Overrides method since this class implements runnable
         * This allows the ClientHandler to consistently receive data
         * and decide what to do/how to send it
         */
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
                            startMatchmaking();
                        } else if (type == Constants.JOIN_PUB_ROOM_DATA) {
                            joinPublicRoom(input);
                        } else if (type == Constants.COLOUR_DATA) {
                            addColour(input);
                        } else if (type == Constants.BOARD_DATA) {
                            shareBoard(input);
                        } else if (type==Constants.ROOM_NAMES_DATA) {
                            sendRoomNames();

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



        /**
         * broadcastMessage
         * This method sends information to everyone else
         * in the same room as the Client the data was received from (excluding the sender)
         * @param msg is the String that should be sent
         */
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


        /**
         * writeData
         * Writes the data specified back to the Client
         * When this method is called, the type (from Constants) is always the first character
         * @param data is the String holding the data type and actual information
         */
        public void writeData(String data) {
            try {
                dataOut.write(data);
                dataOut.newLine();
                dataOut.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * broadcastMessageToAll
         * This method sends information to everyone connected to the server
         * Used when updating public room list
         * @param msg is the String that should be sent
         */

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

        /**
         * addUsername
         * Checks if the username is valid and sends back either a "success" or "error" message
         * @param user is the String with the username
         */
        public void addUsername(String user) {
            if (validUsername(user)) {
                username = user;
                writeData("success. welcome " + username);
            } else {
                writeData(Constants.USERNAME_ERROR);
            }
        }

        /**
         * validUsername
         * Checks if the username is valid
         * @param user is the String with the username
         * @return true if the username is unique and only contains characters, false otherwise
         */
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

        /**
         * createRoom
         * Creates a new room and adds to the Map holding rooms and corresponding ClientHandlers
         * Sends back a verificaton message
         * @param roomCode the String with the room code for the room created
         */
        public void createRoom(String roomCode) {
            priv = true;
            room = roomCode;
            privateRooms.put(roomCode, new ArrayList<ClientHandler>());
            privateRooms.get(roomCode).add(this);
            writeData(Constants.CREATE_ROOM_DATA + "room [" + roomCode + "] created successfully");
        }

        /**
         * joinPrivateRoom
         * Checks if the room code is valid
         * Adds current ClientHandler to Map holding rooms and corresponding ClientHandlers if valid
         * Sends back a verification or error message
         * @param roomCode the String with the room code for the room created
         */
        public void joinPrivateRoom(String roomCode) {
            if (privateRooms.containsKey(roomCode)) {
                priv = true;
                room = roomCode;
                privateRooms.get(roomCode).add(this);
                writeData("success. welcome " + username);
                broadcastMessage(Constants.CHAT_DATA + "*** " + username + " has joined the room ***");
                if (privateRooms.get(room).size() == 2) {
                    broadcastMessage(Constants.START_DATA + "");
                }
            } else {
                writeData(Constants.JOIN_ROOM_ERROR);
            }
        }

        /**
         * startMatchmaking
         * Sends to client wait
         * Once a client joins, add them to quickMatch ArrayList and assign a colour and room code
         * Once there are 2 or more players in the quickMatch ArrayList, add both clients to the public room and update the roomName
         * Sends joined status, room, and colour information back to the client
         */
        public void startMatchmaking() {
            writeData(Constants.QUICK_MATCH_WAIT);
            quickMatch.add(this);

            if (quickMatch.size()%2==1){
                room = CodeGenerator.generateCode();
                publicRooms.put(room, new ArrayList<ClientHandler>());
                colour="white";

                while (quickMatch.size()%2!=0){
                    // waiting for another user to click matchmaking
                }
            } else {
                room = quickMatch.peek().getRoom();
                colour="black";
                publicRooms.get(room).add(this);
                writeData(Constants.QUICK_MATCH_JOINED);
                quickMatch.poll();
                quickMatch.poll();
            }

            if (colour.equals("white")){
                publicRooms.get(room).add(this);
                writeData(Constants.QUICK_MATCH_JOINED);
                String roomName = publicRooms.get(room).get(0).username +" vs "+ publicRooms.get(room).get(1).username;
                roomNames.add(roomName);
            }

            priv = false;
            writeData(room);
            writeData(colour);
        }


        /**
         * joinPublicRoom
         * Gets the room code that corresponds with the room name
         * Add current ClientHandler the publicRoom ArrayList
         * @param roomName the String with the room name for the room created
         */
        public void joinPublicRoom(String roomName) {
            List<String> keys = new ArrayList<String>(publicRooms.keySet());
            String roomCode = keys.get((roomNames.indexOf(roomName)));
            System.out.println(roomCode);
            System.out.println("room names: "+roomNames+ "    \n room:"+roomCode);
            System.out.println(publicRooms);
            room = roomCode;
            writeData(room);
            publicRooms.get(roomCode).add(this);
            priv = false;

            broadcastMessage(Constants.CHAT_DATA + username + " has joined the chat");
        }

        /**
         * addColour
         * Determines the colour for the Clients in the room
         * 1) Stores the colour sent from room creator
         * 2) Sets the colour of the 2nd player to opposite of the first
         * 3) Sends only COLOUR_DATA so we know to ask spectator for a colour upon reception
         * Sends verification message containing assigned colour
         * @param input is the String holding the colour ("white" or "black")
         */
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

        /**
         * shareBoard
         * Receives and sends either requests for sending board data, or actual board data
         * @param input is the String being sent ("request" or piece data)
         *              piece data format: [username] + " " + [row][col][piece symbol][b/w]
         *              example: chess 00Rw would mean to send the "chess" user "00Rw"
         */
        public void shareBoard(String input) {
            Map<String, ArrayList<ClientHandler>> rooms;
            if (priv) {
                rooms = privateRooms;
            } else {
                rooms = publicRooms;
            }

            // spectator sends request to copy board from player of the same colour POV
            if (input.equals(Constants.REQUEST)) {
                System.out.println(rooms.get(room));
                System.out.println("LINE 407 COLOUR: "+rooms.get(room).get(0).colour);
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

        /**
         * sendRoomNames
         * Sends the length of the roomNames array and all the room names as a string
         */
        public void sendRoomNames() {
            writeData(""+roomNames.size());
            for (String roomName: roomNames){
                writeData(roomName);
            }
        }

        /*
        METHODS FOR ENDING GAMES
         */

        /**
         * sendDraw
         * Receives draw information from one player and sends it to their opponent
         * @param input is the String indicating the request of rejection of a draw ("!request" or "denied")
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

        /**
         * leaveRoom
         * Relays information to other users in the room
         * Removes the room from list of rooms if a player left
         * Sends message in chat if a spectator left
         * @param input is the String ("true" or "false") indicating if the user who left is a player
         */
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
                  //  if (roomNames.contains(roomName)){
                        roomNames.remove(roomName);
                        rooms.remove(room);
               //     }

                    broadcastMessageToAll(Constants.UPDATE_LIST + "");
                }

            } else { // spectator leaves room
                if (rooms.get(room) != null) {
                    rooms.get(room).remove(username);
                }
                broadcastMessage(Constants.CHAT_DATA + "*** " + username + " has left the chat ***");
            }
        }

        /**
         * closeConnection
         * Disconnects the ClientHandler from the Socket
         */
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

    }
}