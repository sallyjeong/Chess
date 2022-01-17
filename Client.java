import java.io.*;
import java.net.*;
import java.util.Scanner;

// remember to call closeConnection after the game ends/client leaves (IN SERVER CLASS)
public class Client {
    private String username;
    private String room;
    private Socket socket;
    private BufferedReader dataIn;
    private BufferedWriter dataOut;

    public static void main(String[] args) {
        Client client = new Client();
        client.listenForUpdates();
        client.sendMessage();
    }

    public Client() {
        try {
            socket = new Socket(Constants.LOCAL_HOST, Constants.PORT);
            dataIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            dataOut = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            String result;

            //for username --
            do {
                askForUsername();
                sendUsername();
                result = dataIn.readLine();
                System.out.println(result.substring(1)); // output to user abt success/failure
            } while (result.equals(Constants.CHAT_DATA + Constants.USERNAME_ERROR));

            //for joining a private room
            do {
                dataOut.write(Constants.JOIN_PRIV_ROOM_DATA);
                dataOut.newLine();
                dataOut.flush();
                askForRoom();
                result = dataIn.readLine();
                System.out.println(result.substring(1)); // output to user abt success/failure
            } while (result.equals(Constants.CHAT_DATA + Constants.JOIN_ROOM_ERROR));


            //for creating a private room
            dataOut.write(Constants.CREATE_ROOM_DATA);
            dataOut.newLine();
            dataOut.flush();
            result = dataIn.readLine();
            setRoom(result);
            System.out.println(result.substring(1)); // output room code

            //join quick match
            dataOut.write(Constants.QUICK_MATCH_DATA);
            dataOut.newLine();
            dataOut.flush();
            result = dataIn.readLine();
            //in the game loop, maybe constantly check if quickMatch.size()%2==0  -- if its even


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void askForUsername() {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter a username");
        this.username = input.next();
    }

    public void sendUsername() {
        try {
            dataOut.write(Constants.USERNAME_DATA + username);
            dataOut.newLine();
            dataOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setRoom(String room) { // assuming they click into public room/create the private room
        this.room = room;
    }

    public void askForRoom() { // called when they try to join a private game - for the room code
        Scanner input = new Scanner(System.in);
        System.out.println("Enter a room");
        this.room = input.next();
        this.room.toLowerCase();
    }

//    public void sendRoom() {
//        try {
//            dataOut.write(Constants.JOIN_PRIV_ROOM_DATA + room);
//            dataOut.newLine();
//            dataOut.flush();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

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

    public void listenForUpdates() { // this will be the place you determine what type of data it is?
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
                        }else if (type==Constants.JOIN_PRIV_ROOM_DATA){

                            //@kat
                            //wasnt really sure on what to do for this part
                            //so i left it blank for now
                        }else if (type == Constants.CREATE_ROOM_DATA){

                        }else if (type == Constants.QUICK_MATCH_DATA){

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }).start();
    }
}