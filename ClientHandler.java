//import java.io.*;
//import java.net.Socket;
//import java.util.ArrayList;
//
//public class ClientHandler implements Runnable {
//    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>(); //maybe set
//    private String username;
//    private Socket socket;
//    private BufferedReader dataIn;
//    private BufferedWriter dataOut;
//
//    public ClientHandler(Socket socket) {
//        System.out.println("Attempting to establish a connection ...");
//        try {
//            this.socket = socket;
//            dataIn = new BufferedReader(new InputStreamReader(socket.getInputStream())); // will this work with the server though
//            dataOut = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
//            this.username = dataIn.readLine(); // first thing sent is username
//            clientHandlers.add(this);
//            broadcastMessage(username + "HAS ENTERED");
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        System.out.println("Connection to server established!");
//    }
//
//    @Override
//    public void run() {
//        String message;
//        // the demo had while outside try catch and then break in the catch so..?
//        while (socket.isConnected()) {
//            try {
//                message = dataIn.readLine();
//                broadcastMessage(message);
//
//            } catch (IOException e) {
//                e.printStackTrace();
//                break;
//            }
//        }
//    }
//
//
//    public void broadcastMessage(String msg) {
//        for (ClientHandler clientHandler: clientHandlers) {
//            try {
//                if (!clientHandler.username.equals(username)) {
//                    clientHandler.dataOut.write(msg);
//                    clientHandler.dataOut.newLine();
//                    clientHandler.dataOut.flush();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    public void remove() {
//        clientHandlers.remove(this);
//        broadcastMessage(username + "has left");
//    }
//
//    public void closeConnection() {
//        remove();
//        try {
//            if (socket!=null) {
//                socket.close();
//            }
//            if (dataIn!=null) {
//                dataIn.close();
//            }
//            if (dataOut!=null) {
//                dataOut.close();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}