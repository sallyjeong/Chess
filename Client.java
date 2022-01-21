package chessproject;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.Locale;
import java.util.Scanner;

// remember to call closeConnection after the game ends/client leaves (IN SERVER CLASS)
public class Client {
    private JFrame home;
    private String username = "!";
    private String room;
    private String colour = " "; // "white" or "black"
    private boolean isPlayer;
    private boolean myTurn = false; // set white to true once both players are in the game
    private Socket socket;
    private GameFrame gameFrame;
    private BufferedReader dataIn;
    private BufferedWriter dataOut;
    private MessageFrame messageFrame;
    private String result = "";

//    public static void main(String[] args) {
//
//        Client client = new Client(new HomeFrame());
//    }

    public Client(JFrame home) {
        this.home = home; // use this variable to setVisible after you leave the game

        // add variable to see if the game has been closed/left
        // send msg to client handler to remove the person from that room
        // do we want spectators to be able to go look at another room?
        // is there a way to detect if X was clicked and to remove the user when that happens?
        // otherwise a lot of null threads and all past usernames can't be used

        try {
            socket = new Socket(Constants.HOST, Constants.PORT);
            dataIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            dataOut = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void askForData(char type) {
        EnterDataFrame enterDataFrame = new EnterDataFrame(type);

        if (type == Constants.USERNAME_DATA) {
            do {
                username = enterDataFrame.getDataEntered();
            } while (enterDataFrame.isClosed()==false);

        } else if (type == Constants.JOIN_PRIV_ROOM_DATA) {
            do {
                room = enterDataFrame.getDataEntered();
            } while (enterDataFrame.isClosed()==false);
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
            } else if (type == Constants.COLOUR_DATA) {
                dataOut.write(type + colour);
            }
            dataOut.newLine();
            dataOut.flush();

            result = dataIn.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void getUsernameInput() {
        do {
            askForData(Constants.USERNAME_DATA);
            result = verifyData(Constants.USERNAME_DATA);
            System.out.println("USERNAME CREATION: " + result);

            if (result.equals(Constants.USERNAME_ERROR)) {
                messageFrame = new MessageFrame(result);
            }

            waitTillClosed(messageFrame);

        } while (result.equals(Constants.USERNAME_ERROR));
    }

//    public String askForDataCondensed(char type) {
//        EnterDataFrame enterDataFrame = new EnterDataFrame(type);
//        /**
//         * me tryna make it more efficient except it doesn't work
//         * and obviously you would set username = askForData or room = askForData
//         */
//        String data;
//        do {
//            data = enterDataFrame.getDataEntered();
//        } while (enterDataFrame.isClosed() == false);
//
//        return data;
//    }

    public void getRoomInput() {
        do {
            askForData(Constants.JOIN_PRIV_ROOM_DATA);
            result = verifyData(Constants.JOIN_PRIV_ROOM_DATA);
            System.out.println("JOIN ROOM: [" + room + "] "+ result);

            if (result.equals(Constants.JOIN_ROOM_ERROR)) {
                messageFrame = new MessageFrame(result);
            }

            waitTillClosed(messageFrame);
        } while (result.equals(Constants.JOIN_ROOM_ERROR));

        colour = verifyData(Constants.COLOUR_DATA);
        System.out.println("JOINING AS: " + colour);

        // spectator or not
        if (colour.charAt(0) == Constants.COLOUR_DATA) {
            pickSpectateColour();
            verifyData(Constants.COLOUR_DATA);
            startGame(false);
        } else {
            startGame(true);
        }
    }

    public void createRoom() {
        CreatePrivateRoomFrame roomFrame = new CreatePrivateRoomFrame();
        room = roomFrame.getCode();
        do {
            colour = roomFrame.getColourChosen();;
        } while (roomFrame.isClosed()==false);

        if (colour.equals("random")) {
            randomizeColour();
        }

        verifyData(Constants.CREATE_ROOM_DATA);
        System.out.println("CREATE ROOM: [" + room + "] success");
        System.out.println("CREATOR: " + verifyData(Constants.COLOUR_DATA)); // printing just to check

        // waitTillClosed(roomFrame);

        startGame(true);
    }
    public void startGame(boolean isPlayer) {
        listenForUpdates();
        sendMessage();
        gameFrame = new GameFrame(this, isPlayer);
    }

    public void pickSpectateColour() {
        EnterDataFrame colourChoice = new EnterDataFrame(Constants.COLOUR_DATA);
        do {
            colour = colourChoice.getDataEntered();
            waitTillClosed(messageFrame);
        } while (colourChoice.isClosed() == false);

        if (!(colour.equals("white") || colour.equals("black"))) {
            randomizeColour();
        }

    }

    public void randomizeColour() {
        int choice = (int) Math.round(Math.random());
        if (choice == 0) {
            colour = "white";
        } else {
            colour = "black";
        }
    }
    public boolean isWhite() {
        if (colour.equals("white")) {
            return true;
        }
        return false;
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

    public void waitTillClosed(MessageFrame frame) {
        while (frame != null && !frame.isClosed()) {
            try {
                Thread.sleep(0, 1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public String getUsername() {
        return username;
    }
}
