package chessproject;

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
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.GridLayout;
import javax.swing.JTextField;
import java.awt.ScrollPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class GameFrame extends JFrame {

    static Game game;
    static Player curPlayer;
    static Player p1, p2;
    private JFrame frame;
    private JTextField userInputField;

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        //GameFrame game = new GameFrame(false, true);

    }

    public GameFrame(Client client, boolean isPlayer) {
        /**
         * replace boolean white with Client client -- that way we can get client.isWhite() for that variable)
         */
        frame = this;
        GamePanel board = new GamePanel(isPlayer);
        p1 = new Player(true);
        p2 = new Player(false);
        curPlayer = p1;
        game = new Game(client.isWhite(), p1, p2); // again probably

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(100, 100, 1313, 715);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        frame.setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel boardPanel = new JPanel();
        //boardPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
//        boardPanel.add(board);
        //board.setBounds(0,0, 576, 576);

        boardPanel.setBounds(674, 50, 600, 600); // 576/8 = 72
        contentPane.add(boardPanel);
        //frame.add(board);

        JPanel movesPanel = new JPanel();
        movesPanel.setBorder(new TitledBorder(null, "Moves", TitledBorder.CENTER, TitledBorder.TOP, null, null));
        movesPanel.setBounds(52, 118, 564, 237);
        contentPane.add(movesPanel);

        JPanel chatPanel = new JPanel();
        chatPanel.setBorder(new TitledBorder(null, "Chat", TitledBorder.CENTER, TitledBorder.TOP, null, null));
        chatPanel.setBounds(52, 395, 564, 237);
        contentPane.add(chatPanel);
        chatPanel.setLayout(null);

        userInputField = new JTextField();
        userInputField.setBounds(6, 198, 437, 33);
        chatPanel.add(userInputField);
        userInputField.setColumns(10);

        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        sendButton.setBounds(441, 201, 117, 29);
        chatPanel.add(sendButton);

        JTextArea textArea = new JTextArea();
        textArea.setBounds(549, 32, -531, 163);
        chatPanel.add(textArea);

        /**
         * add JLabel with room code in the corner with text being client.getRoom()
         *
         * add actual jtextfield and "send" button
         * maybe see if we can store a long string into a scroll-y thing
         */

        if (isPlayer) {
            JButton drawButton = new JButton("Draw");
            drawButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    frame.dispose();
                    // send information to other user
                    // (request to opponent and not spectators)
                    // ^^ first 2 ClientHandlers of a room (HashMap) will be the players
                    // so we can differentiate sending data this way
                    // new ();    new frame here
                }
            });
            drawButton.setBounds(52, 50, 267, 29);
            contentPane.add(drawButton);

            JButton surrenderButton = new JButton("Surrender");
            surrenderButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    frame.dispose();
                    // send to new page, remove user from the room
                    // new ();    new frame here
                }
            });
            surrenderButton.setBounds(343, 50, 267, 29);
            contentPane.add(surrenderButton);
        } else {

            /**
             *  yoyo add a leave game button for spectators
             */

            JButton flipButton = new JButton("Flip board");
            flipButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {

//                frame.dispose();
                    // remove the user from game (if player, do the same thing as surrender)
                    // new ();    new frame here
                }
            });
            flipButton.setBounds(343, 50, 267, 29);
            contentPane.add(flipButton);

            JButton leaveButton = new JButton("Leave");
            leaveButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    frame.dispose();
//
                }
            });
            leaveButton.setBounds(52, 50, 267, 29);
            contentPane.add(leaveButton);



        }
        frame.setVisible(true);
    }
    //}
    public static class GamePanel extends JPanel implements MouseListener {

        private Spot source = null;
        private boolean isPlayer;

        public GamePanel(boolean isPlayer) {
            this.isPlayer = isPlayer;
            //JPanel board = new JPanel();
            //board.setBounds(0,0, 576, 576); // 576/8 = 72
            setPreferredSize(new Dimension(576, 576));
            addMouseListener(this);
            setFocusable(true);
            //requestFocusInWindow();
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            game.getBoard().draw(g);
//            p1.displayCaptured(g);
//            p2.displayCaptured(g);
            repaint();
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (isPlayer) {
                if (e.getX() < 8 * game.getBoard().LENGTH && e.getY() < 8 * game.getBoard().LENGTH) {
                    Spot spot = game.getBoard().getBoard()[e.getY() / game.getBoard().LENGTH][e.getX() / game.getBoard().LENGTH];
                    if (source == null) {
                        if (spot.getPiece() == null) {
                            return;
                        } else {
                            if (game.getTurn().isWhite() == spot.getPiece().isWhite()) {
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
                            if (source.getPiece().isWhite()) {
                                game.playerMove(p1, source, spot);
                            } else {
                                game.playerMove(p2, source, spot);
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