package chessproject;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.Locale;
import java.util.Scanner;

// remember to call closeConnection after the game ends/client leaves (IN SERVER CLASS)
public class Client {
    //private Thread gameThread;
    private JFrame homeFrame;
    private String username = "!";
    private String room;
    private String colour = " "; // "white" or "black"
    private boolean isPlayer;
    private boolean myTurn = false; // set white to true once both players are in the game
    private boolean inGame = false;
    private Socket socket;
    private GameFrame gameFrame;
    private BufferedReader dataIn;
    private BufferedWriter dataOut;
    private MessageFrame messageFrame;
    private String result = "";
    private Client thisClient;

//    public static void main(String[] args) {
//
//        Client client = new Client(new HomeFrame());
//    }

    public Client(JFrame homeFrame) {
        this.homeFrame = homeFrame; // use this variable to setVisible after you leave the game
        this.thisClient = this;

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

    public void sendData(String data) {
        try {
            dataOut.write(data);
            dataOut.newLine();
            dataOut.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
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
        System.out.println("done username");
    }

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

        // spectator or not
        if (colour.charAt(0) == Constants.COLOUR_DATA) {
            pickSpectateColour();
            verifyData(Constants.COLOUR_DATA);

            isPlayer = false;
        } else {
            isPlayer = true;
        }

        System.out.println("JOINING AS: " + colour);
        startGame();
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

        isPlayer = true;
        startGame();
    }

    public void startGame() {
        inGame = true;
//        gameThread = new Thread(new Runnable() {
//            @Override
//            public void run() {
        gameFrame = new GameFrame(thisClient, thisClient.isPlayer);
//            }
//        });
//
//        gameThread.start();

        listenForUpdates();
        sendMessage();
    }

    public void pickSpectateColour() {
        EnterDataFrame colourChoice = new EnterDataFrame(Constants.COLOUR_DATA);
        do {
            colour = colourChoice.getDataEntered();
            // waitTillClosed(messageFrame);
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
        System.out.println("quick game called");
        try {
            FindingRoomFrame findRoom = new FindingRoomFrame();
            dataOut.write(Constants.QUICK_MATCH_DATA);
            dataOut.newLine();
            dataOut.flush();
            String result = dataIn.readLine();
            System.out.println(result);

            result = dataIn.readLine();
            if (result.equals(Constants.QUICK_MATCH_JOINED)){
                System.out.println(result);
                room = dataIn.readLine();
                colour = dataIn.readLine();
                isPlayer=true;
                System.out.println("room: "+room+"     colour: "+colour);
                findRoom.dispose();
                startGame();
            }
            //in the game loop, maybe constantly check if quickMatch.size()%2==0  -- if its even
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // tbh i don't think we'll need this anymore because it'll send one msg at a time based on gameFrame's jtextfield
    // replace with sendData(msg) with the msg having the right char at the front alr
    public void sendMessage() {
        try {
            Scanner input = new Scanner(System.in);
            while (socket.isConnected() && inGame == true) {
                String message = input.nextLine(); //replace with jtextfield input
                dataOut.write(Constants.CHAT_DATA + username + ": " + message);
                dataOut.newLine();
                dataOut.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void leaveRoom() {
        try {
            dataOut.write(Constants.LEAVE_ROOM_DATA + "" + isPlayer);
            dataOut.newLine();
            dataOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        gameFrame.dispose();
        homeFrame.setVisible(true);
        System.out.println(username + " left room [" + room +"]");
        room = "";
        inGame = false;
        // gameThread.interrupt();
    }

    public void listenForUpdates() { // this will be the place you determine what type of data it is?
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (socket.isConnected() && inGame == true) {
                    try {
                        String data = dataIn.readLine();
                        char type = data.charAt(0);
                        data = data.substring(1);
                        // check the char stuff
                        if (type == Constants.CHAT_DATA) {
                            System.out.println(data); // display message (maybe store chat in a multiline string
                        } else if (type == Constants.MOVE_DATA) {
                            // run a diff method that digests the move lmao

                        } else if (type == Constants.QUICK_MATCH_DATA){

                        } else if (type == Constants.LEAVE_ROOM_DATA) {
                            // System.out.println("DATA: " + data);
                            if (data.equals("true")) { // a player has left the game
                                // show pop-up that game over/which side won
                                leaveRoom();
                            }
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

    public void quitGame() {
        sendData(Constants.QUIT_DATA + "");
        try {
            if (socket != null) {
                socket.close();
            }
            if (dataIn != null) {
                dataIn.close();
            }
            if (dataOut != null) {
                dataOut.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    public String getUsername() {
        return username;
    }
    public JFrame getHomeFrame() {
        return homeFrame;
    }
    public String getRoom() {
        return room;
    }
}
