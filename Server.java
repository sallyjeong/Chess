/**
 * attempt at fixing listenForUpdates version
 *
 */
package chessproject;

//imports for network communication
import java.io.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.lang.reflect.Array;
import java.net.*;
import java.util.*;

public class Server {
    public ServerSocket serverSocket;//server socket for connection
    public ArrayList<ClientHandler> clientHandlers = new ArrayList<>(); //maybe set
    public Map <String, ArrayList<ClientHandler>> publicRooms = new LinkedHashMap<>();
    public Map <String, ArrayList<ClientHandler>> privateRooms = new HashMap<>();
    public static ArrayList<String> roomNames = new ArrayList<>();
    public BlockingQueue<ClientHandler> quickMatch = new LinkedBlockingQueue<>();
    public Server() {
//        // for room testing
//        rooms.put("123", new ArrayList<ClientHandler>());
//        rooms.put("abc", new ArrayList<ClientHandler>());
//        // end room testing
        try {
            serverSocket = new ServerSocket(Constants.PORT);
            System.out.println("Waiting for connections");
            while (!serverSocket.isClosed()) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Server-Client connection made");

                ClientHandler clientHandler = new ClientHandler(clientSocket);
                Thread thread = new Thread(clientHandler);
                thread.start();
                //clientHandler.setThread(thread);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Server server = new Server();

    }

    private class ClientHandler implements Runnable { //not sure if client handler is the best name?
        // private Thread thread; //maybe use to terminate the thread when quit game is chosen
        private String username = " ";
        private String colour;
        private String room = ""; //i need this for broadcast -- theres prob a better idea
        private Socket socket;
        private BufferedReader dataIn;
        private BufferedWriter dataOut;
        private boolean priv; //private room

        public ClientHandler(Socket socket) {
            //System.out.println("Attempting to establish a connection ...");
            try {
                this.socket = socket;
                dataIn = new BufferedReader(new InputStreamReader(socket.getInputStream())); // will this work with the server though
                dataOut = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                clientHandlers.add(this); // replace with a method to

            } catch (IOException e) {
                e.printStackTrace();
            }
            //System.out.println("Connection to server established!");
        }

        @Override
        public void run() {
            while (socket.isConnected()) {
                try {
                    String input = dataIn.readLine(); // = get input thing
                    if (input != null) {

                        char type = input.charAt(0);
                        input = input.substring(1);

                        //System.out.println("TYPE: " + type);
                        //System.out.println("INPUT: " + input);
                        if (type == Constants.CHAT_DATA) {
                            //System.out.println("chat data received");
                            broadcastMessage(Constants.CHAT_DATA + input);
                        } else if (type == Constants.MOVE_DATA) {
                            // send movement stuff
                            broadcastMessage(Constants.MOVE_DATA + input);

                        } else if (type == Constants.USERNAME_DATA) {
                            System.out.println("USERNAME DATA IN");
                            if (validUsername(input)) {
                                username = input;
                                writeData(Constants.USERNAME_DATA + "success. welcome " + username);
                            } else {
                                writeData(Constants.USERNAME_DATA + Constants.USERNAME_ERROR);
                            }
                            System.out.println("USERNAME DATA OUT");

                        } else if (type == Constants.JOIN_PRIV_ROOM_DATA) { //join private room
                            System.out.println("JOIN ROOM DATA IN");
                            if (privateRooms.containsKey(input)) {
                                privateRooms.get(input).add(this);
                                priv = true;
                                room = input;
                                writeData(Constants.JOIN_PRIV_ROOM_DATA + "success. welcome " + username);
                                broadcastMessage(Constants.CHAT_DATA + username + " has joined the chat");
                                System.out.println("SUCCESS JOIN ROOM DATA OUT");
                                if (privateRooms.get(room).size() > 1) {
                                    //writeData(Constants.START_DATA + "");
                                    broadcastMessage(Constants.START_DATA + "");
                                    System.out.println("START DATA OUT");
                                }

                            } else {
                                writeData(Constants.JOIN_PRIV_ROOM_DATA + Constants.JOIN_ROOM_ERROR);
                                System.out.println("FAILED JOIN ROOM DATA OUT");
                            }
                        } else if (type == Constants.CREATE_ROOM_DATA) { //creating new room
                            System.out.println("CREATE ROOM DATA IN");
                            privateRooms.put(input, new ArrayList<ClientHandler>());
                            privateRooms.get(input).add(this);
                            room = input;
                            priv = true;
                            writeData(Constants.CREATE_ROOM_DATA + "room [" + input + "] created successfully");
                            System.out.println("CREATE ROOM DATA OUT");
                            // pretty sure we don't acc need to receive create room data though

                        } else if (type == Constants.QUICK_MATCH_DATA) { //public room
                            writeData(Constants.QUICK_MATCH_DATA + Constants.QUICK_MATCH_WAIT);
                            quickMatch.add(this);

                            if (quickMatch.size()%2==1){
                                System.out.println("after while");
                                room = CodeGenerator.generateCode();
                                publicRooms.put(room, new ArrayList<ClientHandler>());
                                this.colour="white";
                                while (quickMatch.size()%2!=0){

                                }
                            } else{ //if they press x
                                System.out.println("quick match:"+quickMatch);
                                room = quickMatch.peek().getRoom();
                                //rooms.get(room).add(this);
                                this.colour="black";
                                quickMatch.poll();
                                quickMatch.poll();
                                broadcastMessageToAll(Constants.UPDATE_LIST+"");

                            }
                            priv = false;
                            publicRooms.get(room).add(this);
                            writeData(Constants.QUICK_MATCH_DATA + Constants.QUICK_MATCH_JOINED);
                            writeData(room);
                            writeData(colour);

                            if (this.colour.equals("black")){
                                String roomName = publicRooms.get(room).get(0).username +" vs "+ publicRooms.get(room).get(1).username;
                                roomNames.add(roomName);
                                // writeData(roomName);
                                System.out.println("room names : "+roomNames);
                            }

                            //useless
//                            if (this.colour.equals("black")){
//                                String roomName = publicRooms.get(room).get(0).username +" vs "+ publicRooms.get(room).get(1).username;
//                                roomNames.add(roomName);
//                               // writeData(roomName);
//                                System.out.println("room names : "+roomNames);
//                            }

//                            for (String key: publicRooms.keySet()) {
//                                System.out.println(username + " key: " + key);
//                            }
                        } else if (type == Constants.COLOUR_DATA) {
                            System.out.println("COLOUR DATA IN");

                            // first player/room creator
                            if (input.equals("black") || input.equals("white")) {
                                colour = input;
                                writeData(Constants.COLOUR_DATA + colour);
                                System.out.println("COLOUR DATA OUT CREATOR");
                            } else {
                                ArrayList<ClientHandler> existingPlayers = privateRooms.get(room);

                                // second player
                                if (existingPlayers.size() < 3) {
                                    //System.out.println("SECOND PLAYER");
                                    String existingColour = existingPlayers.get(0).colour;
                                    if (existingColour.equals("white")) {
                                        colour = "black";
                                    } else if (existingColour.equals("black")) {
                                        colour = "white";
                                    }
                                    writeData(Constants.COLOUR_DATA + colour);
                                    System.out.println("COLOUR DATA OUT 2ND");

                                    // spectators
                                } else {
                                    writeData(Constants.COLOUR_DATA + "");
                                    System.out.println("COLOUR DATA OUT SPECTATOR");
                                }
                            }
                        } else if (type == Constants.JOIN_PUB_ROOM_DATA){
                            List<String> keys = new ArrayList<String>(publicRooms.keySet());
                            String room = keys.get((roomNames.indexOf(input)));
                            publicRooms.get(room).add(this);

                            broadcastMessage(Constants.CHAT_DATA + username + " has joined the chat");
                        } else if (type == Constants.LEAVE_ROOM_DATA) {
                            Map<String, ArrayList<ClientHandler>> rooms;
                            if (this.priv){
                                rooms = privateRooms;
                            }else{
                                rooms = publicRooms;
                            }

                            if (input.equals("true")) { // tell everybody that a player has left the room

                                //need most/all of the commented code if you wanna make the room close once a player leaves --JK prob need ot change most of this code
//                                writeData(rooms.get(room).get(0).username+" vs "+rooms.get(room).get(0).username);
                                broadcastMessage(Constants.LEAVE_ROOM_DATA + input);
                                // ^^ might need to add colour/more info so they know who won the game
                                if (this.priv) {
                                    rooms.remove(room);
                                } else {
                                    roomNames.remove(rooms.get(room).get(0).username + " vs " + rooms.get(room).get(0).username);
                                    publicRooms.remove(room);
                                    System.out.println("room removed       " + rooms.get(room).get(0).username + " vs " + rooms.get(room).get(0).username);
                                    broadcastMessageToAll(Constants.UPDATE_LIST + "");
                                }
//
                            } else { // spectator leaves room
                                if (rooms.get(room) != null) {
                                    rooms.get(room).remove(username);
                                }
                                broadcastMessage(Constants.CHAT_DATA + username + " has left the chat");
                            }
                            room = "";
                        } else if(type==Constants.ROOM_NAMES_DATA){
                            writeData(""+roomNames.size());
                            for (String roomName: roomNames){
                                writeData(Constants.ROOM_NAMES_DATA + roomName);
                            }

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

        public void writeData(String data) {
            try {
                dataOut.write(data);
                dataOut.newLine();
                dataOut.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        public void broadcastMessage(String msg) { //change for rooms, make leaveroom method,
            ArrayList<ClientHandler> roomMembers;
            if (this.priv){
                roomMembers = privateRooms.get(this.room);
            }else{
                roomMembers = publicRooms.get(this.room);
            }
            if (roomMembers!= null && roomMembers.size() > 1) {
                for (ClientHandler member : roomMembers) {
                    try {
                        // some should only send to opponent/players and not spectators
                        // ^^ first 2 ClientHandlers of a room (HashMap) will be the players
                        // so we can differentiate sending data this way
                        if ((!member.username.equals(username)) && (!member.username.equals(" "))) {

                            member.dataOut.write(msg);
                            member.dataOut.newLine();
                            member.dataOut.flush();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

//        public void setThread(Thread thread) {
//            this.thread = thread;
//        }

        public void broadcastMessageToAll(String msg) {

            for (ClientHandler clientHandler : clientHandlers) {
                try {
                    if ((!clientHandler.username.equals(username)) && (!clientHandler.username.equals(" "))) {
                        clientHandler.dataOut.write(msg);
                        clientHandler.dataOut.newLine();
                        clientHandler.dataOut.flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public boolean validUsername(String user){
            //System.out.println("entered valid username method");
            for (ClientHandler clientHandler : clientHandlers){
                if (clientHandler.username.equals(user)){
                    return false;
                }
                //System.out.println("ent3ered for loop");
            }
            if (!user.matches("[a-zA-Z0-9]*")){
                //System.out.println("entered 2nd if");
                return false;
            }
            if ((user.equals("")) || (user.equals(null))){
                //System.out.println("user is ___" + user);
                return false;
            }
            return true;
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
            // we don't have to but we can interrupt/yield the thread? not too sure if its necessary
        }
        public String getRoom() {
            return room;
        }

        public ArrayList getGetRoomNames() {
            return roomNames;
        }

    }
}