//imports for network communication
import java.io.*;
import java.net.*;
import java.util.Scanner;

// remember to call closeConnection after the game ends/client leaves (IN SERVER CLASS)
class Client {
    final String LOCAL_HOST = "127.0.0.1";
    final int PORT = 6000;
    final public static String usernameError = "error. not a valid username - must only contain letters/numbers OR username is in use";

    final public static char chatData = '1';
    final public static char moveData = '2';
    final public static char usernameData = '3';

    private static String username;
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
        System.out.println("Enter a username");
        this.username = input.next();
        sendUsername();
    }

    public void sendUsername() {
        try {
            dataOut.write(usernameData + username);
            //System.out.println("username written");
            dataOut.newLine();
            dataOut.flush();

            //if (dataIn.ready()) { // only works when this part is commented out
                String result = dataIn.readLine();
                while (result.equals(Client.chatData + usernameError)) { // never runs ;-;
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
                dataOut.write(chatData + username + ": " + message);
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
                String data;
                while (socket.isConnected()) {
                    try {
                        data = dataIn.readLine();
                        char type = data.charAt(0);
                        // check the char stuff
                        if (type == chatData) {
                            System.out.println(data.substring(1)); // display message (maybe store chat in a multiline string
                        } else if (type == moveData) {

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }).start();
    }
}