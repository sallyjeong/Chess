package chessproject;

import javax.swing.*;
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
    }

    public Client() {
        GameFrame thisGame = new GameFrame();
        // add variable to see if the game has been closed/left
        // send msg to client handler to remove the person from that room
        // do we want spectators to be able to go look at another room?
        // is there a way to detect if X was clicked and to remove the user when that happens?
        // otherwise a lot of null threads and all past usernames can't be used

        try {
            socket = new Socket(Constants.LOCAL_HOST, Constants.PORT);
            dataIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            dataOut = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            String result;

            //for username
            do {
                askForData(Constants.USERNAME_DATA);
                result = verifyData(Constants.USERNAME_DATA);
                // int n = stuff below
                JOptionPane.showOptionDialog(null, result,
                        "USERNAME CREATION",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
                        null, null, null); // output to user abt success/failure
//                if (n == JOptionPane.OK_OPTION) {
//                    System.out.println("OK CHOSEN");
//                    // System.exit(0); appare
//                } else { // cancel or exit
//                    System.out.println("OK NOT CHOSEN");
//                    // System.exit(0);
//                }

            } while (result.equals(Constants.USERNAME_ERROR));

            //for joining a private room
            do {
                askForData(Constants.JOIN_PRIV_ROOM_DATA);
                result = verifyData(Constants.JOIN_PRIV_ROOM_DATA);
                JOptionPane.showOptionDialog(null, result,
                        "JOIN ROOM",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
                        null, null, null); // output to user abt success/failure
            } while (result.equals(Constants.JOIN_ROOM_ERROR));

        } catch (IOException e) {
            e.printStackTrace();
        }

        listenForUpdates();
        sendMessage();
    }

    public void askForData(char type) {
        // do smth about the cancel/X buttons?
            // right now it just takes input as null if cancel is pressed and goes in an endless loop for room
            // and username gets saved as literally "null"

        if (type == Constants.USERNAME_DATA) {
            // username = JOptionPane.showInputDialog("Enter a username: ");
            username = JOptionPane.showInputDialog(null, "Enter a username: ",
                    "USERNAME CREATION", JOptionPane.PLAIN_MESSAGE);
            // can use other constructor to add custom Icon as well

            //popUp.getRootFrame().dispose(); // doesn't actually do anything though

        } else if (type == Constants.JOIN_PRIV_ROOM_DATA) {
            room = JOptionPane.showInputDialog(null, "Enter a room code: ",
                    "JOIN ROOM", JOptionPane.PLAIN_MESSAGE);

        }
    }

    // not sure if we merge sendMessage/sendMove stuff with this or not
    public String verifyData(char type) {
        String result = "";
        try {
            if (type == Constants.USERNAME_DATA) {
                dataOut.write(type + username);
            } else if (type == Constants.JOIN_PRIV_ROOM_DATA) {
                dataOut.write(type + room);
            }
            dataOut.newLine();
            dataOut.flush();

            result = dataIn.readLine().substring(1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void setRoom(String room) { // assuming they click into public room/create the private room
        this.room = room;
    }

    public void quickMatch() {
        try {
            dataOut.write(Constants.QUICK_MATCH_DATA);
            dataOut.newLine();
            dataOut.flush();
            String result = dataIn.readLine();
            //in the game loop, maybe constantly check if quickMatch.size()%2==0  -- if its even
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void createRoom() {
        try {
            dataOut.write(Constants.CREATE_ROOM_DATA);
            dataOut.newLine();
            dataOut.flush();
            String result = dataIn.readLine();
            room = result.substring(1);
            System.out.println("YOUR ROOM CODE IS: " + room); // output room code
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

                            /**
                             * @sally i think we don't need this stuff here because
                             * 1) does client side need to take in these input?
                             * 2) these are like 1-time inputs
                             *    but this method is constantly waiting to receive info
                             *    that would update the game (like chat and move)
                             *    so tbh i don't even think they need to be here?
                             */
                        }else if (type == Constants.CREATE_ROOM_DATA){

                        }else if (type == Constants.QUICK_MATCH_DATA){

                        }
                        // might need a leave room?
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }).start();
    }
}
