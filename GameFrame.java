package chessproject;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
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
import javax.swing.JLabel;
import java.awt.Color;

public class GameFrame extends JFrame {

    static Game game;
    //static Player curPlayer;
    // static Player p1, p2;
    private GameFrame frame;
    private JTextField userInputField;
    private Client client;

//    public static void main(String[] args) {
//        // TODO Auto-generated method stub
//        GameFrame game = new GameFrame(false, true);
//
//    }

    public GameFrame(Client client, boolean isPlayer) {
        frame = this;
        this.client = client;
        GamePanel board = new GamePanel(isPlayer);
//        p1 = new Player(true);
//        p2 = new Player(false);
        // curPlayer = p1;
        game = new Game(client); // again probably

        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setBounds(100, 100, 1313, 715);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        frame.setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel boardPanel = new JPanel();
        //boardPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
        boardPanel.add(board);
        board.setBounds(0,0, 576, 576);

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

//        userInputField = new JTextField();
//        userInputField.setBounds(6, 198, 437, 33);
//        chatPanel.add(userInputField);
//        userInputField.setColumns(10);
//
//        JButton sendButton = new JButton("Send");
//        sendButton.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                // client.sendMessage(userInputField.getText());
//                // userInputField.setText("");
//            }
//        });
//        sendButton.setBounds(441, 201, 117, 29);
//        chatPanel.add(sendButton);

        JTextArea textArea = new JTextArea(5,5);
        textArea.setEditable(false);
        textArea.setVisible(true);

        JScrollPane scroll = new JScrollPane (textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVisible(true);
        //scroll.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        chatPanel.add(scroll);
        //chatPanel.add(textArea);
        //chatPanel.setVisible(true);
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        textArea.setText("getContentPane().setLayout(\n"
                + "                new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));\n"
                + "        setResizable(false);\n"
                + "\n"
                + "        JTextArea window1 = new JTextArea(\"text\");\n"
                + "        window1.setEditable(false);\n"
                + "        window1.setBorder(BorderFactory.createLineBorder(Color.BLACK));\n"
                + "        window1.setLineWrap(true);\n"
                + "\n"
                + "\n"
                + "        JScrollPane scroll1 = new JScrollPane(window1);\n"
                + "        scroll1.setPreferredSize(new Dimension(200, 250));\n"
                + "        scroll1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);\n"
                + "        scroll1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);\n"
                + "        add(scroll1);\n"
                + "\n"
                + "        JTextArea window2 = new JTextArea();\n"
                + "        window2.setEditable(true);\n"
                + "        window2.setBorder(BorderFactory.createLineBorder(Color.BLACK));\n"
                + "        window2.setLineWrap(true);\n"
                + "        add(window2);\n"
                + "\n"
                + "        JScrollPane scroll2 = new JScrollPane(window2);\n"
                + "        scroll2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);\n"
                + "        scroll2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);\n"
                + "        scroll2.setPreferredSize(new Dimension(100, 50));\n"
                + "        add(scroll2);\n"
                + "\n"
                + "        setDefaultCloseOperation(EXIT_ON_CLOSE);");


        JLabel roomCodeLabel = new JLabel();
        roomCodeLabel.setForeground(new Color(0, 100, 0));
        roomCodeLabel.setBounds(52, 652, 267, 16);
        roomCodeLabel.setText("Room Code: " + client.getRoom());
        contentPane.add(roomCodeLabel);

        if (isPlayer) {

            JButton drawButton = new JButton("Draw");
            drawButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    //frame.dispose();
                    // send information to other user
                    // (request to opponent and not spectators)
                    // ^^ first 2 ClientHandlers of a room (HashMap) will be the players
                    // so we can differentiate sending data this way
                    // new ();    new frame here
                }
            });
            drawButton.setBounds(52,  50, 175, 29);
            contentPane.add(drawButton);

            JButton surrenderButton = new JButton("Surrender");
            surrenderButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    new ConfirmFrame(frame);
                     }
            });
            surrenderButton.setBounds(237,  50, 175, 29);
            contentPane.add(surrenderButton);

            JButton boardFlipButton = new JButton("Flip Board");
            boardFlipButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                }
            });
            boardFlipButton.setBounds(426,  50, 175, 29);
            contentPane.add(boardFlipButton);
        } else {

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
                    new ConfirmFrame(frame);
                }
            });
            leaveButton.setBounds(52, 50, 267, 29);
            contentPane.add(leaveButton);

        }
        frame.setVisible(true);
    }
    public Client getClient() {
        return client;
    }
        class GamePanel extends JPanel implements MouseListener {

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
            client.displayCaptured(g);
//            p1.displayCaptured(g);
//            p2.displayCaptured(g);
            repaint();
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (isPlayer && client.getTurn()) {
                if (e.getX() < 8 * game.getBoard().LENGTH && e.getY() < 8 * game.getBoard().LENGTH) {
                    Spot spot = game.getBoard().getBoard()[e.getY() / game.getBoard().LENGTH][e.getX() / game.getBoard().LENGTH];
                    if (source == null) {
                        if (spot.getPiece() == null) {
                            return;
                        } else {
                            if (client.isWhite() == spot.getPiece().isWhite()) {
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
                            game.playerMove(client, source, spot);
//                            if (source.getPiece().isWhite()) {
//                                game.playerMove(p1, source, spot);
//                            } else {
//                                game.playerMove(p2, source, spot);
//                            }
                            // ^^ not sure about this change
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