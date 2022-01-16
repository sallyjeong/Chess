package chessproject;

//imports for network communication
import java.io.*;
import java.net.*;
import java.util.Scanner;

// remember to call closeConnection after the game ends/client leaves (IN SERVER CLASS)
class Client {
    private String username;
    private Socket socket;
    private BufferedReader dataIn;
    private BufferedWriter dataOut;

    public static void main(String[] args) {
        Client client = new Client();
        client.listenForMessage();
        client.sendMessage();
    }

    public Client() {
        try {
            socket = new Socket(Constants.LOCAL_HOST, Constants.PORT);
            dataIn = new BufferedReader(new InputStreamReader(socket.getInputStream())); // will this work with the server though
            dataOut = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.askForUsername();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void askForUsername() {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter a username");
        this.username = input.next();
        sendUsername();
    }

    public void sendUsername() {
        try {
            dataOut.write(Constants.USERNAME_DATA + username);
            //System.out.println("username written");
            dataOut.newLine();
            dataOut.flush();

            //if (dataIn.ready()) { // only works when this part is commented out
                String result = dataIn.readLine();
                System.out.println(result.substring(1));
                while (result.equals(Constants.CHAT_DATA + Constants.USERNAME_ERROR)) { // never runs ;-;
                    askForUsername();
                }
            //}
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage() {
        try {
            Scanner input = new Scanner(System.in);
            while (socket.isConnected()) {
                String message = input.nextLine(); //replace with jtextfield input
                dataOut.write(Constants.CHAT_DATA + username + ": " + message);
                dataOut.newLine();
                dataOut.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listenForMessage() { // this will be the place you determine what type of data it is?
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (socket.isConnected()) {
                    try {
                        String data = dataIn.readLine();
                        char type = data.charAt(0);
                        // check the char stuff
                        if (type == Constants.CHAT_DATA) {
                            System.out.println(data.substring(1)); // display message (maybe store chat in a multiline string
                        } else if (type == Constants.MOVE_DATA) {
                            // run a diff method that digests the move lmao
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }).start();
    }
}