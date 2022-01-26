package chessproject;

// imports
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import java.awt.Color;

/** [GameFrame.java]
 * Main JFrama during gameplay
 * Includes chess board, display of past moves, chat box
 * @author Rachel Liu, Katherine Liu, Sally Jeong
 * @version 1.0 Jan 25, 2022
 */
public class GameFrame extends JFrame {

    // variables
    public static Game game;
    private GameFrame frame;
    private JTextArea textArea;
    private JTextArea movesArea;
    private Client client;
    private Player p1, p2;
    private boolean computerGame;
    private String difficulty;

    /**
     * GameFrame
     * This constructor is used when creating a GameFrame for a player
     * that is connected through networking/playing online
     * Creates a Game
     * @param client the Client which represents a player
     * @param isPlayer is true when the Client is playing, false for spectators
     */
    public GameFrame(Client client, boolean isPlayer) {
        this.client = client;
        computerGame = false;
        GamePanel board = new GamePanel(isPlayer);
        game = new Game(client);

        create(board, isPlayer);
    }

    /**
     * GameFrame
     * This constructor is used when creating a GameFrame for a player
     * that is playing against the computer AI
     * Creates a Game
     * @param difficulty is a String ("easy" or "medium") that represents the AI difficulty
     * @param white is true if the player is playing white
     */
    public GameFrame(String difficulty, boolean white) {
        this.difficulty = difficulty;
        computerGame = true;
        p2 = new Player(white);
        p1 = new ComputerPlayer(!white);
        game = new Game(white, p1, p2, this);
        GamePanel board = new GamePanel(true);

        create(board,  true);
    }

    /**
     * create
     * Sets up the Game Frame graphics depending on the type of game and role of user
     * @param board is the GamePanel that holds the chess board
     * @param isPlayer is true when the user is playing, false if spectating
     */
    public void create(GamePanel board, boolean isPlayer) {

        // setting up frame
        frame = this;
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setBounds(100, 100, 1313, 715);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        frame.setContentPane(contentPane);
        contentPane.setLayout(null);

        // panel for the board
        JPanel boardPanel = new JPanel();
        boardPanel.add(board);
        board.setBounds(0, 0, 576, 576);
        boardPanel.setBounds(674, 50, 600, 600); // 576/8 = 72
        contentPane.add(boardPanel);

        // panel for displaying moves
        JPanel movesPanel = new JPanel();
        movesPanel.setBorder(new TitledBorder(null, "Moves", TitledBorder.CENTER, TitledBorder.TOP, null, null));
        movesPanel.setBounds(52, 118, 564, 237);
        contentPane.add(movesPanel);
        movesArea = new JTextArea(12, 43);
        movesArea.setEditable(false);
        JScrollPane scrollMoves = new JScrollPane(movesArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollMoves.setVisible(true);
        movesPanel.add(scrollMoves);

        if (!computerGame) { // for non-computer AI games
            // panel for chat box
            JPanel chatPanel = new JPanel();
            chatPanel.setBorder(new TitledBorder(null, "Room", TitledBorder.CENTER, TitledBorder.TOP, null, null));
            chatPanel.setBounds(52, 395, 564, 237);
            contentPane.add(chatPanel);
            textArea = new JTextArea(10, 43);
            textArea.setEditable(false);
            JScrollPane scroll = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scroll.setVisible(true);
            chatPanel.add(scroll);

            JTextField userInputField = new JTextField();
            chatPanel.add(userInputField);
            userInputField.setColumns(37);

            JButton sendButton = new JButton("Send");
            sendButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String text = userInputField.getText();
                    if ((text!=null) && (!text.equals(""))) {
                        client.sendData(Constants.CHAT_DATA + client.getUsername() + ": " + text);
                        textArea.append("you: " + text + "\n");
                        // sets so the chat box shows the most recent text inputs
                        textArea.setCaretPosition(textArea.getDocument().getLength());
                        userInputField.setText("");
                    }
                }
            });
            sendButton.setBounds(441, 201, 117, 29);
            chatPanel.add(sendButton);

            // room code label
            JLabel roomCodeLabel = new JLabel();
            roomCodeLabel.setForeground(new Color(0, 100, 0));
            roomCodeLabel.setBounds(52, 652, 267, 16);
            roomCodeLabel.setText("Room Code: " + client.getRoom());
            contentPane.add(roomCodeLabel);

            if (isPlayer) { // for players
                // draw button
                JButton drawButton = new JButton("Draw");
                drawButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        new ConfirmFrame(frame, false);
                    }
                });
                drawButton.setBounds(52, 50, 267, 29);
                contentPane.add(drawButton);

                // surrender button
                JButton surrenderButton = new JButton("Surrender");
                surrenderButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        new ConfirmFrame(frame, true);
                    }
                });
                surrenderButton.setBounds(343, 50, 267, 29);
                contentPane.add(surrenderButton);

            } else if (!computerGame && !isPlayer) { // for spectators
                // flip board button
                JButton flipButton = new JButton("Flip board");
                flipButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        client.flipBoard();
                    }
                });
                flipButton.setBounds(343, 50, 267, 29);
                contentPane.add(flipButton);

                JButton leaveButton = new JButton("Leave");
                leaveButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        new ConfirmFrame(frame, true);
                    }
                });
                leaveButton.setBounds(52, 50, 267, 29);
                contentPane.add(leaveButton);

            }
            frame.setVisible(true);

        } else { // for computer AI games
            // surrender button
            JButton surrenderButton = new JButton("Surrender");
            surrenderButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    frame.dispose();
                    new HomeFrame();
                }
            });
            surrenderButton.setBounds(343, 50, 267, 29);
            contentPane.add(surrenderButton);

            // game type label
            JLabel roomLabel = new JLabel();
            roomLabel.setForeground(new Color(0, 100, 0));
            roomLabel.setBounds(52, 652, 267, 16);
            roomLabel.setText("Playing Computer");
            contentPane.add(roomLabel);
            frame.setVisible(true);

        }
    }

    /**
     * determineDepth
     * Randomly generates a depth for the computer AI depending on difficulty level
     * @return a number for the depth of the AI move calculation
     */
    public int determineDepth() {
        if (difficulty.equals("easy")) {
            return (int)((2) * Math.random()) + 1;
        } else {
            return (int)((2) * Math.random()) + 3;
        }
    }

    /*
    adding messages or moves to the screen
     */
    public void addMessage(String text) {
        textArea.append(text + "\n");
    }

    public void addMove(String move) {
        movesArea.append(move + "\n");
        movesArea.setCaretPosition(movesArea.getDocument().getLength());
    }

    public Client getClient() {
        return client;
    }

    /** [GamePanel.java]
     * Inner class that holds the board
     * Takes user mouse input to determine moves
     * @author Rachel Liu, Katherine Liu
     * @version 1.0 Jan 25, 2021
     */
    class GamePanel extends JPanel implements MouseListener {

        private Spot source = null;
        private boolean isPlayer;
        private Player player;

        /**
         * GamePanel
         * This constructor sets up the panel and variables
         * @param isPlayer true if the user is a player, false if spectator
         */
        public GamePanel(boolean isPlayer) {
            this.isPlayer = isPlayer;
            setPreferredSize(new Dimension(576, 576));
            addMouseListener(this);
            setFocusable(true);
            if (!computerGame) {
                player = client;
            } else {
                player = p2;
            }
        }

        /**
         * paintComponent
         * Repaints the screen to update as moves are made
         * Draws the board and player's captured pieces
         * @param g is the graphics object required to draw to the screen
         */
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            game.getBoard().draw(g);
            player.displayCaptured(g);
            repaint();
        }

        /**
         * mouseClicked
         * Figures out which Spot and Piece is being clicked
         * Displays and limits to valid moves for the piece
         * Makes the move and updates in the game/board
         * @param e is the mouse click action
         */
        @Override
        public void mouseClicked(MouseEvent e) {
            if (computerGame || (isPlayer && client.getTurn()))  {
                if (e.getX() < 8 * game.getBoard().LENGTH && e.getY() < 8 * game.getBoard().LENGTH) {
                    Spot spot = game.getBoard().getBoard()[e.getY() / game.getBoard().LENGTH][e.getX() / game.getBoard().LENGTH];
                    if (source == null) {
                        if (spot.getPiece() == null) {
                            return;
                        } else {
                            if (player.isWhite() == spot.getPiece().isWhite()) {
                                spot.setClicked(true);
                                spot.getPiece().displayValidMoves(true);
                                source = spot;
                            }
                        }
                    } else {
                        if (spot.equals(source)) {
                            source.setClicked(false);
                            spot.getPiece().displayValidMoves(false);
                            source = null;
                        } else if (source.getPiece().getMoveList().contains(spot)) {
                            if (!computerGame) {
                                game.playerMove(client, source, spot);
                            } else {
                                if (game.playerMove(p2, source, spot) && !game.getBoard().isGameOver()) {
                                    Move m = ((ComputerPlayer) p1).makeMove(game.getBoard(), determineDepth());
                                    game.playerMove(p1, m.getStart(), m.getEnd());
                                }

                            }
                            source.setClicked(false);
                            if (source.getPiece() != null) {
                                source.getPiece().displayValidMoves(false);
                            }
                            source = null;
                        }
                    }
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }
        @Override
        public void mouseReleased(MouseEvent e) {
        }
        @Override
        public void mouseEntered(MouseEvent e) {
        }
        @Override
        public void mouseExited(MouseEvent e) {
        }
    }
}
