//imports for network communication
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

// remember to call closeConnection after the game ends/client leaves (IN SERVER CLASS)
class Client {
    private ClientHandler connection;
    final String LOCAL_HOST = "127.0.0.1";
    final int PORT = 6000;
    private String username;
    private Socket socket;
    private BufferedReader dataIn;
    private BufferedWriter dataOut;

    private boolean myTurn = false; // more like a variable part of Player

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter a username");
        String username = input.nextLine();
        Client client = new Client(username);

        System.out.println("Start chatting");
        client.listenForMessage();
        client.sendMessage();
        // client.connectToServer();
        // client.startReceivingMoves();

        // client.go();
    }

    public Client(String username) {
        try {
            socket = new Socket(LOCAL_HOST, PORT);
            //in = new DataInputStream(socket.getInputStream());
            dataIn = new BufferedReader(new InputStreamReader(socket.getInputStream())); // will this work with the server though
            dataOut = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.username = username;
            // ^^ replace with some jtextfield input
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connectToServer() {
       // connection = new ClientHandler();
    }

    public void sendMessage() {

        try {
            dataOut.write(username);
            dataOut.newLine();
            dataOut.flush();

            Scanner input = new Scanner(System.in);
            while (socket.isConnected()) {
                String message = input.nextLine(); //replace with jtextfield input
                dataOut.write(username + ": " + message);
                dataOut.newLine();
                dataOut.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listenForMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String messageReceived;
                while (socket.isConnected()) {
                    try {
                        messageReceived = dataIn.readLine();
                        System.out.println(messageReceived); // display message (maybe store chat in a multiline string
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        });
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
}