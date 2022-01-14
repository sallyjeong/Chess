//imports for network communication
import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Server {
    ArrayList<ClientHandler> clientHandlers = new ArrayList<>(); //maybe set
    final String LOCAL_HOST = "127.0.0.1";
    final int PORT = 6000;

    ServerSocket serverSocket;//server socket for connection

    public static void main(String[] args) {
        Server server = new Server();
    }

    public Server() {
        try {
            serverSocket = new ServerSocket(PORT);
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

    public class ClientHandler implements Runnable { //not sure if client handler is the best name?
        private String username;
        private Socket socket;
        private BufferedReader dataIn;
        private BufferedWriter dataOut;

        public ClientHandler(Socket socket) {
            System.out.println("Attempting to establish a connection ...");
            try {
                this.socket = socket;
                dataIn = new BufferedReader(new InputStreamReader(socket.getInputStream())); // will this work with the server though
                dataOut = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                this.username = dataIn.readLine(); // first thing sent is username
                clientHandlers.add(this);
                broadcastMessage(username + "HAS ENTERED");

            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Connection to server established!");
        }

        @Override
        public void run() {
            String message;
            // the demo had while outside try catch and then break in the catch so..?
            while (socket.isConnected()) {
                try {
                    message = dataIn.readLine();
                    broadcastMessage(message);

                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }


        public void broadcastMessage(String msg) {
            for (ClientHandler clientHandler : clientHandlers) {
                try {
                    if (!clientHandler.username.equals(username)) {
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

//    class ServerHandler extends Thread{
//        private Socket socket;
//        private DataInputStream in;
//        BufferedReader input;
//        private DataOutputStream output;
//
//        private int playerID;
//        //create a socket with the local IP address (try-catch required) and wait for connection request
//
//        public ServerHandler(Socket s, int id){
////            this.socket = s;
////            this.playerID = id;
//            socket = s;
//            playerID = id;
//
//            try{
////                input = new DataInputStream(socket.getInputStream());
//                in = new DataInputStream(socket.getInputStream());
//                input = new BufferedReader(new InputStreamReader(in));
//                output = new DataOutputStream(socket.getOutputStream());
//                System.out.println("input and output stream connected");
//            }catch (IOException e){
//                e.printStackTrace();
//            }
//        }
//
//        public void run(){
//            try{
//                System.out.println("before write int");
//                output.writeInt(playerID);
//                output.flush();
//                System.out.println("after flush");
//                /* need to modify this while loop for chess */
//                while (true){ //true
////                    if (playerID ==1){
////                        player1Input= input.readLine();
////                        System.out.println();
////                        connections.get(0).sendMove("a","b");
////                    }
////                    if (playerID==32323){ // if smth happens or put it as a while loop condition
////                        break;
////                    }
////58:37
//                    break;
//                }
////                for (int i = 0; i<connections.size(); i++){
////                    connections.get(i).closeConnection();
////                }
//            }catch(IOException e){
//                e.printStackTrace();
//            }
//        }
//
//        public void sendMove(String startPos, String endPos){
//            try{
//                output.writeChars(startPos+" "+endPos);
//                output.flush();
//            }catch (IOException e){
//                e.printStackTrace();
//            }
//        }
//
//        public void closeConnection(){
//            try{
//                socket.close();
//                System.out.println("Socket closed.");
//            }catch(IOException e){
//                e.printStackTrace();
//            }
//        }
//    }
//
//}