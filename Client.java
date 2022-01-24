package chessproject;

import javax.swing.JFrame;
import javax.swing.JList;
import java.awt.Graphics;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

public class Client {
    // client identity variables
    private JFrame homeFrame;
    private Client thisClient;
    private String username = "!";
    private String room;
    private String colour = " "; // "white" or "black"

    // chess game variables
    private GameFrame gameFrame;
    private MessageFrame messageFrame;
    private boolean isPlayer;
    private boolean turn = false;
    private Board board;
    private Spot opponentStart = null;
    private ArrayList<Piece> captured;
    final int LENGTH = 35;

    // client networking variables
    private Socket socket;
    private BufferedReader dataIn;
    private BufferedWriter dataOut;
    private String result = "";

    public Client(JFrame homeFrame) {
        this.homeFrame = homeFrame;
        this.thisClient = this;
        this.captured = new ArrayList<Piece>();

        // connecting to server
        try {
            socket = new Socket(Constants.HOST, Constants.PORT);
            dataIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            dataOut = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    METHODS FOR NETWORK COMMUNICATION (sending)
     */

    public String verifyData(char type) {
        String result = "";
        try {
            if (type == Constants.USERNAME_DATA) {
                dataOut.write(type + username);
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

        if (data.charAt(0) == Constants.MOVE_DATA) {
            if (data.charAt(1) == 'P') {
                data = " " + data.substring(2);
            } else {
                data = data.substring(1);
            }
            gameFrame.addMove(data);
        }
    }

    /*
    METHODS FOR INPUT BEFORE GAME
     */
    public void askForData(char type) {
        EnterDataFrame enterDataFrame = new EnterDataFrame(type, homeFrame);

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

    public void createRoom() {
        CreatePrivateRoomFrame roomFrame = new CreatePrivateRoomFrame(homeFrame);
        room = roomFrame.getCode();
        do {
            colour = roomFrame.getColourChosen();;
        } while (roomFrame.isClosed()==false);

        if (colour.equals("random")) {
            randomizeColour();
        }

        verifyData(Constants.CREATE_ROOM_DATA);
        verifyData(Constants.COLOUR_DATA);

        isPlayer = true;
        startGame();
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

        // spectator
        if (colour.charAt(0) == Constants.COLOUR_DATA) {
            pickSpectateColour();
            verifyData(Constants.COLOUR_DATA);
            isPlayer = false;

        // 2nd player
        } else {
            isPlayer = true;
        }
        if (isWhite()) {
            turn = true;
        }
        startGame();
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

    public void pickSpectateColour() {
        EnterDataFrame colourChoice = new EnterDataFrame(Constants.COLOUR_DATA, homeFrame);
        do {
            colour = colourChoice.getDataEntered().toLowerCase();
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

    /*
    METHODS FOR PUBLIC ROOMS
     */
    public ArrayList<String> getRoomNames(){
        ArrayList <String> roomNames = new ArrayList<>();
        sendData(Constants.ROOM_NAMES_DATA+"");
        try {
            int size = Integer.parseInt(dataIn.readLine());
            for (int i = 0; i<size; i++){
                roomNames.add(dataIn.readLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return roomNames;
    }

    public void quickMatch() {
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
                System.out.println("room name list : "+ HomeFrame.roomNames);
                findRoom.dispose();
                if (isWhite()) {
                    turn = true;
                }
                startGame();
            }
            //in the game loop, maybe constantly check if quickMatch.size()%2==0  -- if its even
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void spectate(String roomName) {
        sendData(Constants.JOIN_PUB_ROOM_DATA + roomName);
        pickSpectateColour();
        verifyData(Constants.COLOUR_DATA);

        isPlayer = false;
        startGame();
    }

    /*
    IN GAME METHODS
     */
    public void startGame() {
        //inGame = true;
        gameFrame = new GameFrame(thisClient, thisClient.isPlayer);
        listenForUpdates();
    }

    // determine how to interpret the move
    public void readMove(String data) {
        if (data.equals("O-O") || data.equals("O-O-O")) {
            receiveMove(data);
        } else {
            String startId = data.substring(1, 3);
            String endId = data.substring(data.length() - 2);

            if (data.charAt(0) == 'P') {
                receiveMove(startId, endId, true);
                data = " " + data.substring(1);
            } else {
                receiveMove(startId, endId, false);
            }
        }
        gameFrame.addMove(data);
    }

    // regular moves
    public void receiveMove(String startId, String endId, boolean enPassant) {
        if (!isPlayer && opponentStart != null) {
            opponentStart.setLeft(false);
        }

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
                    if (enPassant) {
                        temp[i-1][j].removePiece();
                    }
                }
            }
        }
        // moving pieces on the board
        end.addPiece(piece);
        piece.setCol(end.getColumn());
        piece.setRow(end.getRow());

        opponentStart.setLeft(true);

        if (isPlayer) {
            board.getPseudoLegal(); // calculate valid moves for each piece
            turn = true;
        }
    }

    // understanding castling moves
    public void receiveMove(String castle) {
        if (isWhite()) {
            if (castle.equals("O-O")) {
                castle("right");

            } else {
                castle("left");
            }
        } else if (!isWhite()) {
            if (castle.equals("O-O")) {
                castle("left");
            } else {
                castle("right");
            }
        }

        if (isPlayer) {
            turn = true;
        }
    }

    // moving pieces based on castle
    public void castle(String direction) {
        Spot[][] temp = board.getBoard();
        Spot kingSpot;
        Piece king, rook;
        int col;

        if (temp[0][3].getPiece() instanceof King) {
            kingSpot = temp[0][3];
            col = 3;
        } else {
            kingSpot = temp[0][4];
            col = 4;
        }
        king = kingSpot.removePiece();

        // moving pieces on the board
        if (direction.equals("left")) {
            rook = temp[0][0].removePiece();
            temp[0][col-2].addPiece(king);
            temp[0][col-2+1].addPiece(rook);
            king.setCol(col-2);
            rook.setCol(col-2+1);
        } else {
            rook = temp[0][7].removePiece();
            temp[0][col+2].addPiece(king);
            temp[0][col+2-1].addPiece(rook);
            king.setCol(col+2);
            rook.setCol(col+2-1);
        }
    }

    // called on players when spectators join a new game in order to replicate board
    public void sendBoard(String spectator) {
        for (int i=0; i<board.getBoard().length; i++) {
            for (int j=0; j<board.getBoard()[i].length; j++) {
                Piece temp = board.getBoard()[i][j].getPiece();
                if (temp != null) {

                    String pieceColour = "b";
                    if (temp.isWhite()) {
                        pieceColour = "w";
                    }

                    if (temp instanceof Pawn) {
                        sendData(Constants.BOARD_DATA + spectator + " " + i + j + "P" + pieceColour);
                    } else {
                        sendData(Constants.BOARD_DATA + spectator + " " + i + j + temp.getSymbol() + pieceColour);
                    }
                }
            }
        }
        sendData(Constants.BOARD_DATA  + spectator + " " + Constants.DONE); //indicates that entire board has been sent
    }

    // called on spectators to replicate the board at the right moment in game
    public void receiveBoard(String piece) {

        // considers when both players on are the same POV but spectator requests opposite
        if (piece.equals(Constants.DONE)) {
            if ((isWhite() && board.getBoard()[0][0].getID().equals("h1"))
                    || (!isWhite() && board.getBoard()[0][0].getID().equals("a8"))) {
                flipBoard();
            }

        } else {
            // reading the piece data sent over
            // input format: [row][col][piece symbol][b/w])
            // example: 00Rw would mean a white Rook at index [0][0]
            int i = Character.getNumericValue(piece.charAt(0));
            int j = Character.getNumericValue(piece.charAt(1));
            char symbol = piece.charAt(2);
            char pieceColour = piece.charAt(3);
            boolean whitePiece = false;
            Piece newPiece = null;
            if (pieceColour == 'w') {
                whitePiece = true;
            }

            // creating a new Piece based on data interpreted
            if (symbol == 'P') {
                newPiece = new Pawn(whitePiece, false, 1, '\u0000', i, j, whitePiece);
            } else if (symbol == 'R') {
                newPiece = new Rook(whitePiece, false, 5, symbol, i, j);
            } else if (symbol == 'N') {
                newPiece = new Knight(whitePiece, false, 3, symbol, i, j);
            } else if (symbol == 'B') {
                newPiece = new Bishop(whitePiece, false, 3, symbol, i, j);
            } else if (symbol == 'Q') {
                newPiece = new Queen(whitePiece, false, 9, symbol, i, j);
            } else if (symbol == 'K') {
                newPiece = new King(whitePiece, false, 1000, symbol, i, j);
            }
            board.getBoard()[i][j].addPiece(newPiece);
        }
    }

    public void flipBoard() {
        Spot[][] oldBoard = new Spot[8][8];
        for(int i=0; i<board.getBoard().length; i++) {
            for(int j=0; j<board.getBoard()[i].length; j++) {
                oldBoard[i][j] = board.getBoard()[i][j];
            }
        }

        for (int i = 0; i<board.getBoard().length; i++) {
            for (int j = 0; j<board.getBoard()[i].length; j++) {
                board.getBoard()[i][j] = oldBoard[7-i][7-j];
                board.getBoard()[i][j].setRow(i);
                board.getBoard()[i][j].setColumn(j);

                if (board.getBoard()[i][j].getPiece() != null) {
                    board.getBoard()[i][j].getPiece().setRow(i);
                    board.getBoard()[i][j].getPiece().setCol(j);
                }
            }
        }
    }

    public void receiveDrawInfo(String data) {
        // shows a pop for the player to accept or reject the draw
        if (data.equals(Constants.REQUEST)) {
            DrawFrame drawFrame = new DrawFrame();
            do {
                result = drawFrame.getResult();
            } while (result.equals(""));
            drawFrame.dispose();

            if (result.equals("confirmed")) {
                new EndFrame(gameFrame); // might need to send more data to determine the text on the screen
                sendData(Constants.GAME_OVER_DATA + "draw");
            } else {
                sendData(Constants.DRAW_DATA + "denied");
            }

        // for player who requested an unsuccessful draw
        } else {
            gameFrame.addMessage("*** DRAW REQUEST DENIED ***");
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
        homeFrame.dispose(); // call earlier up if this works
        //room = "";
        new HomeFrame();
        quitGame(false);
    }

    public void quitGame(boolean real) {
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
        if (real) {
            System.exit(0);
        }
    }

    // main method that receives socket inputs during the game
    public void listenForUpdates() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (socket.isConnected()) {
                    try {
                        String data = dataIn.readLine();
                        char type = data.charAt(0); // corresponds to the data type identifiers in Constants
                        data = data.substring(1); // actual information

                        if (type == Constants.START_DATA) {
                            if (isWhite()) {
                                turn = true;
                            }

                        } else if (type == Constants.CHAT_DATA) {
                            gameFrame.addMessage(data);
                        } else if (type == Constants.MOVE_DATA) {
                            readMove(data);

                        } else if (type == Constants.UPDATE_LIST) {
                            HomeFrame.roomNames = getRoomNames();
                            HomeFrame.list = new JList(HomeFrame.roomNames.toArray());

                        } else if (type == Constants.BOARD_DATA) {
                            if (isPlayer) {
                                sendBoard(data);
                            } else {
                                receiveBoard(data);
                            }
                        } else if (type == Constants.DRAW_DATA) {
                            receiveDrawInfo(data);
                        } else if (type == Constants.LEAVE_ROOM_DATA) {
                            if (data.equals("true")) { // a player has surrendered the game
                                // show pop-up that game over/which side won
                                leaveRoom();
                            }
                        } else if (type == Constants.GAME_OVER_DATA) {
                            // for checkmate, stalemate, and when draw requests are successful
                            new EndFrame(gameFrame);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        break;
                    }
                }
            }
        }).start();
    }

    public void displayCaptured(Graphics g) {
        for(int i=0; i<captured.size(); i++) {
            Piece p = captured.get(i);
            if(p.isWhite()) {
                g.drawImage(p.getImage()[0], i*LENGTH, 0, LENGTH, LENGTH, null);
            }else {
                g.drawImage(p.getImage()[1], i*LENGTH, 520, LENGTH, LENGTH, null);
            }

        }
    }

    /*
    METHODS FOR EXTRACTING VALUES
     */
    public boolean isWhite() {
        if (colour.equals("white")) {
            return true;
        }
        return false;
    }


    public String getUsername() {
        return username;
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
    public boolean getIsPlayer() {
        return isPlayer;
    }
    public ArrayList<Piece> getCaptured() {
        return this.captured;
    }
}