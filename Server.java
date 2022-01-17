//imports for network communication
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

import java.io.*;
import java.lang.reflect.Array;
import java.net.*;
import java.util.*;

public class Server {
    public ServerSocket serverSocket;//server socket for connection
    public ArrayList<ClientHandler> clientHandlers = new ArrayList<>(); //maybe set
    public Map <String, ArrayList<ClientHandler>> rooms = new HashMap<>();
    public Queue<ClientHandler> quickMatch = new LinkedList<>();
    public static void main(String[] args) {
        Server server = new Server();

    }

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

    private class ClientHandler implements Runnable { //not sure if client handler is the best name?
        private String username = " ";
        private String room = ""; //i need this for broadcast -- theres prob a better idea
        private Socket socket;
        private BufferedReader dataIn;
        private BufferedWriter dataOut;

        public ClientHandler(Socket socket) {
            System.out.println("Attempting to establish a connection ...");
            try {
                this.socket = socket;
                dataIn = new BufferedReader(new InputStreamReader(socket.getInputStream())); // will this work with the server though
                dataOut = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                clientHandlers.add(this);

            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Connection to server established!");
        }

        @Override
        public void run() {
            while (socket.isConnected()) {
                try {
                    if (dataIn.ready()) {
                        String totalInput = dataIn.readLine(); // = get input thing
                        // System.out.println("client: " + clientInput);
                        char type = totalInput.charAt(0);
                        String clientInput = totalInput.substring(1);
                        if (type == Constants.CHAT_DATA) {
                            // System.out.println("chat data!!");
                            broadcastMessage(Constants.CHAT_DATA + clientInput);
                        } else if (type == Constants.MOVE_DATA){
                            // send movement stuff
                            // broadcastMessage(Constants.moveData + "theactualmove");

                        } else if (type == Constants.USERNAME_DATA) {
                            // System.out.println("username data !!");
                            if (validUsername(clientInput)) {
                                this.username = clientInput;
                                //can change to a new popupData char?
                                dataOut.write(Constants.CHAT_DATA + "success. welcome " + this.username);
                                dataOut.newLine();
                                dataOut.flush();
                                broadcastMessage(Constants.CHAT_DATA + this.username + " has joined the chat");
                            } else {
                                //can change to a new popupData char?
                                dataOut.write(Constants.CHAT_DATA + Constants.USERNAME_ERROR);
                                dataOut.newLine();
                                dataOut.flush();
                                // is this a bad way to use constant? because i don't wanna "hard code" the message

                                //print (not a valid username. your username must not include special characters or your username is already used
                            }
                        }else if (type == Constants.JOIN_PRIV_ROOM_DATA){ //join private room
                            if (rooms.containsKey(clientInput)){
                                rooms.put(clientInput, new ArrayList<ClientHandler>());
                                rooms.get(clientInput).add(this);
                                room = clientInput;

                            }else{
                                dataOut.write(Constants.JOIN_PRIV_ROOM_DATA + Constants.JOIN_ROOM_ERROR);
                                dataOut.newLine();
                                dataOut.flush();
                            }
                        }else if (type == Constants.CREATE_ROOM_DATA) { //join private room
                            String roomCode = CreatePrivateRoomFrame.roomCodes.get(CreatePrivateRoomFrame.roomCodes.size()-1);
                            rooms.put(roomCode, new ArrayList<ClientHandler>());
                            rooms.get(roomCode).add(this);
                            dataOut.write(roomCode); //CREATE_ROOM_DATA -- add this before roomcode?
                            dataOut.newLine();
                            dataOut.flush();
                            room = roomCode;

                        }else if (type== Constants.QUICK_MATCH_DATA){ //public room
                            if (quickMatch.isEmpty()){
                                dataOut.write(Constants.QUICK_MATCH_WAIT);
                                dataOut.newLine();
                                dataOut.flush();
                                quickMatch.add(this);

                                //in the game loop, maybe constantly check if quickMatch.size()%2==0  -- if its even
                            }
                        } //else if clicking into a public room??

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }

        public void broadcastMessage(String msg) { //change for rooms, make leaveroom method,
            ArrayList<ClientHandler> roomMembers = rooms.get(this.room);
            for (ClientHandler member : roomMembers) {
                try {
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

        public void broadcastMessageToAll(String msg) { //change for rooms, make leaveroom method,

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

        public void remove() {
            clientHandlers.remove(this);
            broadcastMessage(username + "has left");
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
            return true;
        }

        public void closeConnection() {
            remove();
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
    }
}