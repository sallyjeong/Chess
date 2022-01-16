//imports for network communication
import java.io.*;
import java.net.*;
import java.util.Scanner;

// remember to call closeConnection after the game ends/client leaves (IN SERVER CLASS)
class Client {
    final String LOCAL_HOST = "127.0.0.1";
    final int PORT = 6000;
    final public static String usernameError = "error. not a valid username - must only contain letters/numbers OR username is in use";
    private String username;
    private Socket socket;
    private BufferedReader dataIn;
    private BufferedWriter dataOut;

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter a username");
        String username = input.nextLine();
        Client client = new Client();

        System.out.println("Start chatting " + username);
        client.listenForMessage();
        client.sendMessage();
    }

    public Client() {
        try {
            socket = new Socket(LOCAL_HOST, PORT);
            dataIn = new BufferedReader(new InputStreamReader(socket.getInputStream())); // will this work with the server though
            dataOut = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.askForUsername();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void askForUsername() {
        Scanner input = new Scanner(System.in);
        username = input.next();
        sendUsername();
    }

    public void sendUsername() {
        try {
            dataOut.write(username);
            dataOut.newLine();
            dataOut.flush();

            // do we need this in a while loop or no
            if (dataIn.ready()) {
                String success = dataIn.readLine();
                while (success.equals(usernameError)) { // not sure if this logic makes sense
                    askForUsername();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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

        }).start();
    }
}