package chessproject;

import javax.swing.*;
import java.awt.*;
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
    // private EnterUsernameFrame enterUsernameFrame;
    private MessageFrame messageFrame;

    public static void main(String[] args) {
        Client client = new Client(false);
    }

    public Client(boolean createRoom) {
        // add variable to see if the game has been closed/left
        // send msg to client handler to remove the person from that room
        // do we want spectators to be able to go look at another room?
        // is there a way to detect if X was clicked and to remove the user when that happens?
        // otherwise a lot of null threads and all past usernames can't be used

        try {
            socket = new Socket(Constants.HOST, Constants.PORT);
            dataIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            dataOut = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            String result = "";

            do {
                askForData(Constants.USERNAME_DATA);
                result = verifyData(Constants.USERNAME_DATA);
                System.out.println("USERNAME CREATION: " + result);

                if (result.equals(Constants.USERNAME_ERROR)) {
                    messageFrame = new MessageFrame(Constants.USERNAME_ERROR);
                }

                while (messageFrame != null && !messageFrame.isClosed()) {
                    try {
                        Thread.sleep(0,1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            } while (result.equals(Constants.USERNAME_ERROR));

            //

            if (createRoom == true) {
                CreatePrivateRoomFrame roomFrame = new CreatePrivateRoomFrame();
                room = roomFrame.getCode();
                result = verifyData(Constants.CREATE_ROOM_DATA);
                messageFrame = new MessageFrame("ROOM CREATION: " + result);

            } else {
                //for joining a private room
                do {
                    askForData(Constants.JOIN_PRIV_ROOM_DATA);
                    result = verifyData(Constants.JOIN_PRIV_ROOM_DATA);
                    System.out.println("JOIN ROOM: [" + room + "] "+ result);

                    if (result.equals(Constants.JOIN_ROOM_ERROR)) {
                        messageFrame = new MessageFrame(Constants.JOIN_ROOM_ERROR);
                    }

                    while (messageFrame != null && !messageFrame.isClosed()) {
                        try {
                            Thread.sleep(0,1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                } while (result.equals(Constants.JOIN_ROOM_ERROR));
            }

            // only create frame after the room code is entered properly
            GameFrame thisGame = new GameFrame();

        } catch (IOException e) {
            e.printStackTrace();
        }

        listenForUpdates();
        sendMessage();
    }

    public void askForData(char type) {

        Scanner input = new Scanner(System.in);
        if (type == Constants.USERNAME_DATA) {
//            EventQueue.invokeLater(new Runnable() {
//                public void run() {
//                    enterUsernameFrame = new EnterUsernameFrame();
//                }
//            });

            EnterUsernameFrame enterUsernameFrame = new EnterUsernameFrame();
            do {
                username = enterUsernameFrame.getUsernameEntered();
            } while (enterUsernameFrame.isClosed()==false);

        } else if (type == Constants.JOIN_PRIV_ROOM_DATA) {
            PrivateRoomCodeFrame privateRoomCodeFrame = new PrivateRoomCodeFrame();
            do {
                room = privateRoomCodeFrame.getRoomEntered();
            } while (privateRoomCodeFrame.isClosed()==false);
        }
    }

    // not sure if we merge sendMessage/sendMove stuff with this or not
    public String verifyData(char type) {
        String result = "";
        try {
            if (type == Constants.USERNAME_DATA) {
                dataOut.write(type + username);
                // maybe combine both below into an
            } else if ((type == Constants.JOIN_PRIV_ROOM_DATA) || (type == Constants.CREATE_ROOM_DATA)) {
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

//    public void setRoom(String room) { // assuming they click into public room/create the private room
//        this.room = room;
//    }

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
