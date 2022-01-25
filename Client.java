package chessproject;

// imports
import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

/** [Client.java]
 * Represents each person joining the chess program
 * Connects to the server and has the ability to play a chess game
 * @author Katherine Liu, Sally Jeong
 * @version 1.0 Jan 25, 2021
 */
public class Client extends Player {
    // client identity variables
    private Client thisClient;
    private String username = "!";
    private String room;
    private String colour = " "; // "white" or "black"

    // chess game variables
    private GameFrame gameFrame;
    private Game game;
    private MessageFrame messageFrame;
    private boolean isPlayer;
    private boolean turn = false;
    private Board board;
    private Spot opponentStart = null;

    // client networking variables
    private Socket socket;
    private BufferedReader dataIn;
    private BufferedWriter dataOut;
    private String result = "";

    /**
     * Client
     * This constructor ensures each Client connects to the server once the object is created
     */
    public Client() {
        super();
        this.thisClient = this;

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

    /**
     * verifyData
     * Sends Client identity data to the server to get confirmation or rejection of input
     * @param type is the character from the Constants class which represents the data type being sent
     * @return result is the feedback provided once data is sent, used to check if actions were successful
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

    /**
     * sendData
     * Sends the data specified to the server side
     * When this method is called, the type (from Constants) is always the first character
     * If the Client is sending a move, display the move on their own screen
     * @param data is the String of information being sent to
     */
    public void sendData(String data) {
        try {
            dataOut.write(data);
            dataOut.newLine();
            dataOut.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

        // adding the move to their own screen
        if (data.charAt(0) == Constants.MOVE_DATA) {
            if (data.charAt(1) == Constants.PAWN_INDICATOR) {
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

    /**
     * askForData
     * Creates a pop-up frame for the user to enter identity data
     * @param type is the character type in Constants that corresponds to the data we're asking for
     */
    public void askForData(char type) {
        EnterDataFrame enterDataFrame = new EnterDataFrame(type, this);

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

    /**
     * getUsernameInput
     * Continuously prompts the user for username input until a valid response is received
     * Controls the timing of the error message
     */
    public void getUsernameInput() {
        do {
            askForData(Constants.USERNAME_DATA);
            result = verifyData(Constants.USERNAME_DATA);

            if (result.equals(Constants.USERNAME_ERROR)) {
                messageFrame = new MessageFrame(result);
            }

            waitTillClosed(messageFrame);

        } while (result.equals(Constants.USERNAME_ERROR));
    }

    /**
     * createRoom
     * Creates a pop-up frame with a randomly generated room code
     * Shows options for picking a side/colour
     * Starts the game
     */
    public void createRoom() {
        CreatePrivateRoomFrame roomFrame = new CreatePrivateRoomFrame(this);
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

    /**
     * getRoomInput
     * Continuously prompts the user for room code input until a valid response is received
     * Controls the timing of the error message
     */
    public void getRoomInput() {
        do {
            askForData(Constants.JOIN_PRIV_ROOM_DATA);
            result = verifyData(Constants.JOIN_PRIV_ROOM_DATA);

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

    /**
     * pickSpectateColour
     * For users joining a room after there are two existing players
     * Prompts user input, if the choice is invalid they will get a random colour
     */
    public void pickSpectateColour() {
        EnterDataFrame colourChoice = new EnterDataFrame(Constants.COLOUR_DATA, this);
        do {
            colour = colourChoice.getDataEntered().toLowerCase();
        } while (colourChoice.isClosed() == false);

        if (!(colour.equals("white") || colour.equals("black"))) {
            randomizeColour();
        }

    }

    /**
     * randomizeColour
     * For room creators or invalid spectator colour options
     * Randomly chooses and assigns either "black" or "white"
     */
    public void randomizeColour() {
        int choice = (int) Math.round(Math.random());
        if (choice == 0) {
            colour = "white";
        } else {
            colour = "black";
        }
    }

    /**
     * waitTillClosed
     * Stops the main thread from continuing until the message frame is closed
     * @param frame is the pop-up MessageFrame that displays an error message during input
     */
    public void waitTillClosed(MessageFrame frame) {
        while (frame != null && !frame.isClosed()) {
            try {
                Thread.sleep(0, 1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /*
    METHODS FOR PUBLIC ROOMS
     */

    /**
     * getRoomNames
     * Used to update the list of public rooms on the Home Frame
     * @return an ArrayList of Strings, each holding the name of a room
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

    /**
     * quickMatch
     * Used for Clients trying to match with a random player online
     * Shows a pop-up while waiting for another player to enter matchmaking
     * Adds client information based on server feedback
     * Starts the game
     */
    public void quickMatch() {
        try {
            FindingRoomFrame findRoom = new FindingRoomFrame();
            sendData(Constants.QUICK_MATCH_DATA + "");
            String result = dataIn.readLine();

            result = dataIn.readLine();
            if (result.equals(Constants.QUICK_MATCH_JOINED)){
                room = dataIn.readLine();
                colour = dataIn.readLine();
                isPlayer=true;

                findRoom.dispose();
                if (isWhite()) {
                    turn = true;
                }
                startGame();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * spectate
     * Allows the user to spectate a public room
     * @param roomName is the name of the public room displayed on the Home Frame
     */
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

    /**
     * startGame
     * Opens a new Game Frame with the Client as a player
     * Starts listening for in-Game updates such as chat and moves
     */
    public void startGame() {
        gameFrame = new GameFrame(thisClient, thisClient.isPlayer);
        listenForUpdates();
    }

    /**
     * listenForUpdates
     * Called once the Game is started in order to receive in-Game updates
     * Receives chat, move, and ending game information
     */

    public void listenForUpdates() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (socket.isConnected()) {
                    try {
                        try {
                            String data = dataIn.readLine();
                            char type = data.charAt(0); // corresponds to the data type identifiers in Constants
                            data = data.substring(1); // actual information

                            if (type == Constants.START_DATA) { // for room creators
                                if (isWhite()) {
                                    turn = true;
                                }

                            } else if (type == Constants.CHAT_DATA) {
                                gameFrame.addMessage(data);
                            } else if (type == Constants.MOVE_DATA) {
                                readMove(data);
                            } else if (type == Constants.UPDATE_LIST) {
                                HomeFrame.roomNames = getRoomNames();
                                HomeFrame.list.setListData(HomeFrame.roomNames.toArray());
                            } else if (type == Constants.BOARD_DATA) {
                                if (isPlayer) {
                                    sendBoard(data);
                                } else {
                                    receiveBoard(data);
                                }

                            } else if (type == Constants.DRAW_DATA) {
                                receiveDrawInfo(data);
                            } else if (type == Constants.LEAVE_ROOM_DATA) {
                                try {
                                    Thread.sleep(2000);
                                } catch (InterruptedException ex) {
                                }
                                if (data.equals("true")) { // a player has surrendered the game
                                    leaveRoom(); // everyone else must leave
                                }
                            } else if (type == Constants.GAME_OVER_DATA) {
                                // for checkmate, stalemate, and when draw requests are successful
                                String winner = data.substring(0, data.indexOf("!"));
                                String score = data.substring(data.indexOf("!")+1);
                                new EndFrame(gameFrame, winner, score);
                            }
                        } catch (SocketException e) {
                        }
                    } catch (IOException e) {
                        break;
                    }
                }
            }
        }).start();
    }

    /**
     * readMove
     * Determines how to interpret the data/move
     * Called when a move string is received
     * @param data is the String representation of a move
     */
    public void readMove(String data) {
        boolean pawnPromotion = false;

        // castling moves
        if (data.equals(Constants.CASTLE_1) || data.equals(Constants.CASTLE_2)) {
            receiveMove(data);

        } else {
            String startId = data.substring(1, 3);
            String endId;

            // check
            if (data.charAt(data.length()-1) == Constants.CHECK) {
                endId = data.substring(data.length() - 3, data.length() - 1);
                if (isWhite()) {
                    board.setWhiteKingChecked(true);
                } else {
                    board.setBlackKingChecked(true);
                }

            } else {
                if (!isWhite()) {
                    board.setWhiteKingChecked(false);
                } else {
                    board.setBlackKingChecked(false);
                }

                // pawn promotion
                if (data.charAt(data.length()-2) == Constants.PROMOTE) {
                    endId = data.substring(data.length() - 4, data.length() - 2);
                    char symbol = data.charAt(data.length() - 1);
                    promotePawn(startId, endId, symbol);
                    pawnPromotion = true;

                // regular move
                } else {
                    endId = data.substring(data.length() - 2);
                }
            }

            if (!pawnPromotion) {
                if (data.charAt(0) == Constants.PAWN_INDICATOR) { // checks for en passant
                    receiveMove(startId, endId, true);
                    data = " " + data.substring(1);
                } else {
                    receiveMove(startId, endId, false);
                }
            }
        }

        if (isPlayer) {
            board.setEnPassant(isWhite()); // reset all pawns as not be able to be captured by enpassant
            board.getPseudoLegal(); // calculate valid moves for each piece
            if (!checkGameState()) { // checks endGame
                turn = true;
            }
        }
        gameFrame.addMove(data); // display the move to the screen
    }

    /**
     * receiveMove
     * Modifies the Client's in-Game board based on opponent's move
     * Called when a non-castling and non-pawn promotion move occurs
     * @param startId is the start position of the piece moved
     * @param endId is the end position of the piece moved
     * @param enPassant is true when the move is en passant, false otherwise
     */
    public void receiveMove(String startId, String endId, boolean enPassant) {
        if (!isPlayer && opponentStart != null) { // for graphics
            opponentStart.setLeft(false);
        }

        Spot[][] temp = board.getBoard();
        Spot end = null;
        Piece piece = null;

        // identify which indexes of the board store the actual start and end Spot
        for (int i = 0; i<temp.length; i++) {
            for (int j = 0; j<temp.length; j++) {
                if (temp[i][j].getID().equals(startId)) {
                    opponentStart = temp[i][j];
                    piece = opponentStart.removePiece(); // clear the start Spot
                } else if (temp[i][j].getID().equals(endId)) {
                    end = temp[i][j];

                    // special indexing for en passant
                    if (enPassant) {
                        temp[i-1][j].removePiece();
                    }
                }
            }
        }
        end.addPiece(piece); // place the piece at the end spot

        opponentStart.setLeft(true);
    }

    /**
     * receiveMove
     * Modifies the Client's in-Game board based on opponent's move
     * Called when a castling move is received
     * @param castle is the String holding the castling move (either CASTLE_1 or CASTLE_2 from Constants)
     */
    public void receiveMove(String castle) {
        if (isWhite()) {
            if (castle.equals(Constants.CASTLE_1)) {
                castle("right");

            } else {
                castle("left");
            }
        } else if (!isWhite()) {
            if (castle.equals(Constants.CASTLE_2)) {
                castle("left");
            } else {
                castle("right");
            }
        }
    }

    /**
     * castle
     * Modifies the Client's in-Game board based on opponent's move
     * Called for castling move once direction is determined
     * @param direction is either "left" or "right" and describes the opponent king's movement relative to the Client
     */
    public void castle(String direction) {
        Spot[][] temp = board.getBoard();
        Spot kingSpot;
        Piece king, rook;
        int col;

        // king can only be on 2 different spots since castling move verification was already done
        if (temp[0][3].getPiece() instanceof King) {
            kingSpot = temp[0][3];
            col = 3;
        } else {
            kingSpot = temp[0][4];
            col = 4;
        }
        king = kingSpot.removePiece();

        // moving rook and king on the board
        if (direction.equals("left")) {
            rook = temp[0][0].removePiece();
            temp[0][col-2].addPiece(king);
            temp[0][col-2+1].addPiece(rook);
        } else {
            rook = temp[0][7].removePiece();
            temp[0][col+2].addPiece(king);
            temp[0][col+2-1].addPiece(rook);
        }
    }

    /**
     * promotePawn
     * Modifies the Client's in-Game board based on opponent's move
     * Called when a pawn promotion move occurs
     * @param startId is the start position of the piece moved
     * @param endId is the end position of the piece moved
     * @param symbol is the character corresponding to the piece the Pawn has been promoted to
     */
    public void promotePawn(String startId, String endId, char symbol) {
        Spot[][] temp = board.getBoard();
        Piece newPiece = null;
        Spot end = null;
        int row = 0, col = 0;

        for (int i = 0; i<temp.length; i++) {
            for (int j = 0; j<temp.length; j++) {
                if (temp[i][j].getID().equals(startId)) {
                    opponentStart = temp[i][j];
                    opponentStart.removePiece(); // clear the starting spot
                } else if (temp[i][j].getID().equals(endId)) {
                    end = temp[i][j];
                    row = i;
                    col = j;
                }
            }
        }
        // replacing the pawn with a new piece
        if (symbol == 'R') {
            newPiece = new Rook(!isWhite(),  5, symbol, row, col);
        } else if (symbol == 'N') {
            newPiece = new Knight(!isWhite(),  3, symbol, row, col);
        } else if (symbol == 'B') {
            newPiece = new Bishop(!isWhite(),  3, symbol, row, col);
        } else if (symbol == 'Q') {
            newPiece = new Queen(!isWhite(),  9, symbol, row, col);
        }

        end.addPiece(newPiece);

        opponentStart.setLeft(true);
    }

    /**
     * checkGameState
     * Checks for checkmate and stalemate upon receiving a move
     * Called after each move is received
     * Opens an EndFrame based on the winner
     * Sends information to the other users in the room that the game has ended
     * @return true if an end game situation (checkmate, stalemate) is reached
     */
    public boolean checkGameState() {
        //checkmate
        if(board.isCheckmateOrStalemate(isWhite())==1) {
            game.getPastMoves().get(game.getPastMoves().size()-1).setCheckmatingMove();

            if(isWhite()) {
                new EndFrame(gameFrame,"Black wins", "0 - 1");
                sendData(Constants.GAME_OVER_DATA + "Black wins" + "!0 - 1");
            }else {
                new EndFrame(gameFrame,"White wins", "1 - 0");
                sendData(Constants.GAME_OVER_DATA + "White wins" + "!1 - 0");
            }
            return true;
            //stalemate
        }else if(board.isCheckmateOrStalemate(isWhite())==2 || board.isInsufficientMat()) {
            new EndFrame(gameFrame,"Draw", "1/2 - 1/2");
            sendData(Constants.GAME_OVER_DATA + "Draw" + "!1/2 - 1/2");
            return true;
        }
        return false;
    }

    /**
     * sendBoard
     * Sends the necessary information for each Piece/non-empty Spot on the in-Game Board
     * Called on a player when a spectator joins the game
     * @param spectator is the String of the spectator's username which is used on the server side
     *                  to identify who will receive the board information
     */
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
                        sendData(Constants.BOARD_DATA + spectator + " " + i + j + Constants.PAWN_INDICATOR + pieceColour);
                    } else {
                        sendData(Constants.BOARD_DATA + spectator + " " + i + j + temp.getSymbol() + pieceColour);
                    }
                }
            }
        }
        sendData(Constants.BOARD_DATA  + spectator + " " + Constants.DONE); //indicates that entire board has been sent
    }

    /**
     * receiveBoard
     * Saves the necessary information for each Piece/non-empty Spot on the in-Game Board
     * Called on a spectator when joining a game in order to replicate the board
     * @param piece is the String holding information for the piece being read
     *              input format: [row][col][piece symbol][b/w]
     *              example: 00Rw would mean a white Rook at index [0][0]
     */
    public void receiveBoard(String piece) {
            // storing the piece data sent over
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
            if (symbol == Constants.PAWN_INDICATOR) {
                newPiece = new Pawn(whitePiece,  1, '\u0000', i, j, whitePiece);
            } else if (symbol == 'R') {
                newPiece = new Rook(whitePiece,  5, symbol, i, j);
            } else if (symbol == 'N') {
                newPiece = new Knight(whitePiece,  3, symbol, i, j);
            } else if (symbol == 'B') {
                newPiece = new Bishop(whitePiece,  3, symbol, i, j);
            } else if (symbol == 'Q') {
                newPiece = new Queen(whitePiece,  9, symbol, i, j);
            } else if (symbol == 'K') {
                newPiece = new King(whitePiece,  1000, symbol, i, j);
                if (whitePiece) {
                    board.setWhiteKing((King)newPiece);
                } else {
                    board.setBlackKing((King)newPiece);
                }
            }
            board.getBoard()[i][j].addPiece(newPiece); // adding the new Piece to the correct Spot on the board
    }

    /**
     * flipBoard
     * Flips the orientation of the board (from white to black POV or vice versa)
     */
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
            }
        }
    }

    /**
     * receiveDrawInfo
     * Called when receiving a draw request from your opponent OR
     * Called when receiving the result of your draw request
     * Either shows an End Frame indicating a draw or continues game as per usual
     * @param data is the String holding either the request or result of a draw
     */
    public void receiveDrawInfo(String data) {

        if (data.equals(Constants.REQUEST)) {

            // shows a pop-up for the player to accept or reject the draw

            DrawFrame drawFrame = new DrawFrame(gameFrame);
            do {
                result = drawFrame.getResult();
            } while (result.equals(""));
            drawFrame.dispose();

            // checks whether the request was accepted or rejected
            if (result.equals("confirmed")) {
                new EndFrame(gameFrame, "Draw", "1/2 - 1/2");
                // informs the other users in the room (including opponent who requested)
                sendData(Constants.GAME_OVER_DATA + "Draw" + "!1/2 - 1/2");
            } else {
                sendData(Constants.DRAW_DATA + "denied");
            }

        // for player who requested an unsuccessful draw
        } else {
            gameFrame.addMessage("*** DRAW REQUEST DENIED ***");
        }
    }

    /**
     * leaveRoom
     * Removes the Client from the room and informs the rest of the users in the chat that they have left
     *
     * Closes the Game Frame and opens a new Home Frame
     * Closes the Client connection to the server
     */
    public void leaveRoom() {
        sendData(Constants.LEAVE_ROOM_DATA + "" + isPlayer);
        if (isPlayer && CreatePrivateRoomFrame.roomCodes.contains(room)) {
            CreatePrivateRoomFrame.roomCodes.remove(room);
        }
        gameFrame.dispose();
        new Thread(new Runnable() {
            @Override
            public void run() {
                new HomeFrame();
            }
        }).start();
        quitGame(false);
    }

    /**
     * quitGame
     * Closes the Client's connection to the server
     * @param real is true when the Client wants to entirely quit the program, false otherwise
     */
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



    /*
    METHODS FOR EXTRACTING VALUES (setters and getters)
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
    public String getColour() {
        return colour;
    }
    public boolean getTurn() {
        return turn;
    }
    public void setTurn(boolean turn) {
        this.turn = turn;
    }
    public void setGame(Game game) {
        this.game = game;
        this.board = game.getBoard();
    }
    public Spot getOpponentStart() {
        return opponentStart;
    }
    public boolean getIsPlayer() {
        return isPlayer;
    }
    public GameFrame getGameFrame() {
        return gameFrame;
    }
}
