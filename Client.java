package chessproject;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
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
    private boolean turn = false; // set white to true once both players are in the game
    private Board board;
    //private boolean boardChanged = false;
    private Spot opponentStart = null;
    private boolean inGame = false;
    private ArrayList<Piece> captured;
    private Socket socket;
    private GameFrame gameFrame;
    private BufferedReader dataIn;
    private BufferedWriter dataOut;
    private MessageFrame messageFrame;
    private String result = "";
    private Client thisClient;
    //private Thread updateThread;
    final int LENGTH = 35;

//    public static void main(String[] args) {
//
//        Client client = new Client(new HomeFrame());
//    }

    public Client(JFrame homeFrame) {
        this.homeFrame = homeFrame; // use this variable to setVisible after you leave the game
        this.thisClient = this;
        this.captured = new ArrayList<Piece>();

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
                // System.out.println("data written");
            } else if (type == Constants.COLOUR_DATA) {
                dataOut.write(type + colour);
            }
            dataOut.newLine();
            dataOut.flush();

            result = dataIn.readLine();
            //System.out.println(dataIn.readLine());
            //System.out.println("data saved");
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
        //System.out.println("done username");
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
            System.out.println("spectator");
            isPlayer = false;
        } else {
            System.out.println("player 2");
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

        //System.out.println("before verify");
        verifyData(Constants.CREATE_ROOM_DATA);
        //System.out.println("after verify");
        System.out.println("CREATE ROOM: [" + room + "] success");
        System.out.println("CREATOR: " + verifyData(Constants.COLOUR_DATA)); // printing just to check

        // waitTillClosed(roomFrame);

        isPlayer = true;
        startGame();
    }

    public void startGame() {
        //System.out.println("start game called");
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
        //System.out.println("quick game called");
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
                // System.out.println("message: " + message);
                dataOut.write(Constants.CHAT_DATA + username + ": " + message);
                dataOut.newLine();
                dataOut.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void receiveMove(String startId, String endId) {
        //System.out.println("MOVE DIGESTION");
        Spot[][] temp = board.getBoard();
        Spot end = null;
        Piece piece = null;
        for (int i = 0; i<temp.length; i++) {
            for (int j = 0; j<temp.length; j++) {
                if (temp[i][j].getID().equals(startId)) {
                    opponentStart = temp[i][j];
                    piece = opponentStart.removePiece();
                } else if (temp[i][j].getID().equals(endId)) {
                    end = temp[i][j];
                }
            }
        }
        end.addPiece(piece);
        piece.setCol(end.getColumn());
        piece.setRow(end.getRow());
        opponentStart.setLeft(true);
        board.getPseudoLegal();

        if (isPlayer) {
            turn = true;
        }

        //System.out.println("MOVE RECEIVED");
    }

//    public void receiveMove(String castle) {
//        if (isWhite()) {
//            if (castle.equals("O-O")) {
//                castle("right");
//
//            } else {
//                castle("left");
//            }
//        } else if (!isWhite()) {
//            if (castle.equals("O-O")) {
//                castle("left");
//            } else {
//                castle("right");
//            }
//        }
//
//        if (isPlayer) {
//            turn = true;
//        }
//    }

//    public void castle(String direction) {
//        Spot[][] temp = board.getBoard();
//        Piece king, rook;
//        int col;
//        if (temp[0][3].getPiece() instanceof King) {
//            col = 3;
//        } else {
//            col = 4;
//        }
//        king = temp[0][col].removePiece();
//
//        if (direction.equals("left")) {
//            rook = temp[0][0].removePiece();
//            temp[0][1].addPiece(king);
//            temp[0][2].addPiece(rook);
//            king.setCol(1);
//            rook.setCol(2);
//        } else {
//            rook = temp[0][7].removePiece();
//            temp[0][6].addPiece(king);
//            temp[0][5].addPiece(rook);
//            king.setCol(6);
//            rook.setCol(5);
//        }
//
//    }
    public void leaveRoom() {
        try {
            dataOut.write(Constants.LEAVE_ROOM_DATA + "" + isPlayer);
            dataOut.newLine();
            dataOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        gameFrame.dispose();
        //gameFrame = null;
        homeFrame.setVisible(true);
        System.out.println(username + " left room [" + room +"]");
        room = "";
        inGame = false;
    }

    public void listenForUpdates() { // this will be the place you determine what type of data it is?
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (socket.isConnected()) {
                    try {
                        String data = dataIn.readLine();
                        char type = data.charAt(0);
                        data = data.substring(1);
                        // check the char stuff
                        if (type == Constants.START_DATA) {
                            System.out.println("GAME CAN START NOW");
                            if (isWhite()) {
                                turn = true;
                                //boardChanged = true;
                            }
                        } else if (type == Constants.CHAT_DATA) {
                            System.out.println(data); // display message (maybe store chat in a multiline string
                        } else if (type == Constants.MOVE_DATA) {
                            System.out.println("MOVE: " + data);
                            if (data.equals("O-O") || data.equals("O-O-O")) {
                                 // receiveMove(data);
                                System.out.println("CASTLE HASN'T BEEN ACCOUNTED FOR YET");
                            } else {
                                String startId = data.substring(1, 3);
                                String endId = data.substring(data.length() - 2);
                                receiveMove(startId, endId);
                                System.out.println(board == gameFrame.game.getBoard());
                                System.out.println("board changed from receive move");
                            }

                            // avoids multiple gray boxes but also... no dark gray
                            if (!isPlayer) {
                                opponentStart.setLeft(false);
                            }
                            //boardChanged = true;

                        } else if (type == Constants.QUICK_MATCH_DATA){

                        } else if (type == Constants.LEAVE_ROOM_DATA) {
                            // System.out.println("DATA: " + data);
                            if (data.equals("true")) { // a player has left the game
                                // show pop-up that game over/which side won
                                leaveRoom();
                            }
                        }
                        // scuffed "solution"
                        else if (type == Constants.CREATE_ROOM_DATA) {
                            System.out.println("oh..");
                        }
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



    public void displayCaptured(Graphics g) {
        for(int i=0; i<captured.size(); i++) {
            Piece p = captured.get(i);
            if(p.isWhite()) {
                g.drawImage(p.getImage()[0], 576+i*LENGTH, 0, LENGTH, LENGTH, null);
            }else {
                g.drawImage(p.getImage()[1], 576+i*LENGTH, 520, LENGTH, LENGTH, null);
            }

        }
    }

    public ArrayList<Piece> getCaptured() {
        return this.captured;
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
    public boolean getTurn() {
        return turn;
    }
    public void setTurn(boolean turn) {
        this.turn = turn;
    }
    public void setBoard(Board board) {
        this.board = board;
    }
    public Spot getOpponentStart() {
        return opponentStart;
    }
//    public void setBoardChanged(boolean boardChanged) {
//        this.boardChanged = boardChanged;
//    }
//    public boolean getBoardChanged() {
//        return boardChanged;
//    }
}