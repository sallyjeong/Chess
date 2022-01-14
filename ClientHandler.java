import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>(); //maybe set
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

            // i don't think it's added and/or broadcast properly, maybe check if the arraylist is empty..?
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
        for (ClientHandler clientHandler: clientHandlers) {
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

    // call this in the mouselistener after action
    public void sendMove(String move) {
        try {
            dataOut.write(move);
            dataOut.newLine();
            dataOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //myTurn = false;
        // this will deal with the player being able to make multiple pre-moves until they decide
        // ^^ essentially everything looks normal for them but they just have to wait until
        // they receive a move before sending?

        Thread thread = new Thread(new Runnable() {
            public void run() {
                //Client.updateTurn();
            }
        });
        thread.start();
    }
    public String receiveMove() {
        String move = "";
        try {
            move = dataIn.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return move;
    }
    public void closeConnection() {
        remove();
        try {
            if (socket!=null) {
                socket.close();
            }
            if (dataIn!=null) {
                dataIn.close();
            }
            if (dataOut!=null) {
                dataOut.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

//    public void inputListener(){
//        //Scanner
//        while(running) {
//            try {
//                if (input.ready()) {                        //check for an incoming messge
//                    String msg = input.readLine();          //read the message
//                    System.out.println("Message from the server: " + msg);
//                    running = false;                        //change the status to end the client program
//                }
//            } catch (IOException e) {
//                System.out.println("Failed to receive message from the server.");
//                e.printStackTrace();
//            }
//        }
//    }
//
//    public void go() {
//
//        output.println("Hi. I am a basic client!");         //send a message to the server
//        output.flush();                                     //flush the output stream to make sure the message
//        //was sent but not kept in the buffer (very important!)
//        //wait for response from the server
//        while(running){
//            try {
//                if (input.ready()) {                        //check for an incoming messge
//                    String msg = input.readLine();          //read the message
//                    System.out.println("Message from the server: " + msg);
//                    running = false;                        //change the status to end the client program
//                }
//            }catch (IOException e) {
//                System.out.println("Failed to receive message from the server.");
//                e.printStackTrace();
//            }
//        }
//
//        //after completing the communication close all streams and sockets
//        try {
//            input.close();
//            output.close();
//            clientSocket.close();
//        }catch (Exception e) {
//            System.out.println("Failed to close stream or/and socket.");
//        }
//    }

