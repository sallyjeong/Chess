//imports for network communication
import java.io.*;
import java.net.*;

// remember to call closeConnection after the game ends/client leaves (IN SERVER CLASS)
class Client {
    final String LOCAL_HOST = "127.0.0.1";
    final int PORT = 5000;
    private ClientHandler connection;
    private boolean myTurn = false; // more like a variable part of Player

    Socket clientSocket;      //client socket for connection
    //BufferedReader input;     //reader for the input stream
    //PrintWriter output;       //writer for the output stream
    //boolean running = true;   //program status

    public static void main (String[] args) {
        Client client = new Client();
        client.connectToServer();
        // client.startReceivingMoves();

        // client.go();
    }

    public Client() {
        //create a socket (try-catch required) and attempt a connection to the local IP address
//        System.out.println("Attempting to establish a connection ...");
//        try {
//            clientSocket = new Socket(LOCAL_HOST, PORT);    //create and bind a socket, and request connection
//            InputStreamReader stream= new InputStreamReader(clientSocket.getInputStream());
//            input = new BufferedReader(stream);
//            output = new PrintWriter(clientSocket.getOutputStream());
//        } catch (IOException e) {
//            System.out.println("Connection to Server Failed");
//            e.printStackTrace();
//        }
//        System.out.println("Connection to server established!");
    }

    public void connectToServer() {
        connection = new ClientHandler();
    }

//    public void startReceivingMoves() {
//        Thread thread = new Thread(new Runnable() {
//            public void run() {
//                while (true) {
//                    connection.receiveMove();
//                }
//            }
//        });
//        thread.start();
//
//    }

    //1:11:55 they run this in a new thread if its the second player...
    //so how does this translate for us
    //removed need for startReceivingMoves?
    //
    public void updateTurn() {
        String move = connection.receiveMove();
        // call another method to actually use the move String for graphics and board updates
        myTurn = true; //?
    }

    // Client Connection Inner Class
    private class ClientHandler {
        private Socket socket;
        private DataInputStream in;
        BufferedReader dataIn;
        private DataOutputStream dataOut;

        public ClientHandler() {
            System.out.println("Attempting to establish a connection ...");
            try {
                socket = new Socket(LOCAL_HOST, PORT);
                in = new DataInputStream(socket.getInputStream());
                dataIn = new BufferedReader(new InputStreamReader(in));
//                dataIn = new DataInputStream(socket.getInputStream());
                dataOut = new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Connection to server established!");
        }

        // call this in the mouselistener after action
        public void sendMove(String move) {
            try {
                dataOut.writeChars(move);
                dataOut.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            myTurn = false;
            // this will deal with the player being able to make multiple pre-moves until they decide
            // ^^ essentially everything looks normal for them but they just have to wait until
            // they receive a move before sending?

            Thread thread = new Thread(new Runnable() {
                public void run() {
                    updateTurn();
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
            try {
                socket.close();
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
}