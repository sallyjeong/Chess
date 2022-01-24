package chessproject;

import java.awt.Dimension;
import java.awt.FlowLayout;
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

import java.awt.CardLayout;
import java.awt.Color;

public class GameFrame extends JFrame {

    public static Game game;
    //static Player curPlayer;
    // static Player p1, p2;
    private GameFrame frame;
    private JTextField userInputField;
    private JTextArea textArea;
    private JTextArea movesArea;
    private Client client;
    private int numMoves;

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
        game = new Game(client);

        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setBounds(100, 100, 1313, 715);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        frame.setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel boardPanel = new JPanel();
        //boardPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
        boardPanel.add(board);
        board.setBounds(0, 0, 576, 576);

        boardPanel.setBounds(674, 50, 600, 600); // 576/8 = 72
        contentPane.add(boardPanel);
        //frame.add(board);

        JPanel movesPanel = new JPanel();
        movesPanel.setBorder(new TitledBorder(null, "Moves", TitledBorder.CENTER, TitledBorder.TOP, null, null));
        movesPanel.setBounds(52, 118, 564, 237);
        //movesPanel.setLayout(new FlowLayout());
        contentPane.add(movesPanel);

        movesArea = new JTextArea(12, 43);
        movesArea.setEditable(false);

        JScrollPane scrollMoves = new JScrollPane(movesArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollMoves.setVisible(true);

        movesPanel.add(scrollMoves);
        // movesArea.setText(pastMoves);

        JPanel chatPanel = new JPanel();
        chatPanel.setBorder(new TitledBorder(null, "Chat", TitledBorder.CENTER, TitledBorder.TOP, null, null));
        chatPanel.setBounds(52, 395, 564, 237);
        contentPane.add(chatPanel);
        //chatPanel.setLayout(new FlowLayout());;

        textArea = new JTextArea(10, 43);
        textArea.setEditable(false);

        JScrollPane scroll = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVisible(true);

        chatPanel.add(scroll);

        //textArea.setText(message);

        JTextField userInputField = new JTextField();
        //userInputField.setBounds(6, 198, 437, 33);
        chatPanel.add(userInputField);
        userInputField.setColumns(37);

        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String text = userInputField.getText();
                client.sendData(Constants.CHAT_DATA + client.getUsername() + ": " + text);
                textArea.append("me: " + text + "\n");
                textArea.setCaretPosition(textArea.getDocument().getLength());
                userInputField.setText("");
            }
        });
        sendButton.setBounds(441, 201, 117, 29);
        chatPanel.add(sendButton);


        JLabel roomCodeLabel = new JLabel();
        roomCodeLabel.setForeground(new Color(0, 100, 0));
        roomCodeLabel.setBounds(52, 652, 267, 16);
        roomCodeLabel.setText("Room Code: " + client.getRoom());
        contentPane.add(roomCodeLabel);

        if (isPlayer) {

            JButton drawButton = new JButton("Draw");
            drawButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    new ConfirmFrame(frame, false);
                }
            });
            drawButton.setBounds(52, 50, 175, 29);
            contentPane.add(drawButton);

            JButton surrenderButton = new JButton("Surrender");
            surrenderButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    new ConfirmFrame(frame, true);
                }
            });
            surrenderButton.setBounds(237, 50, 175, 29);
            contentPane.add(surrenderButton);

            JButton boardFlipButton = new JButton("Flip Board");
            boardFlipButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    client.flipBoard();
                }
            });
            boardFlipButton.setBounds(426, 50, 175, 29);
            contentPane.add(boardFlipButton);
        } else {

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
    }

    public Client getClient() {
        return client;
    }

    public void addMessage(String text) {
        textArea.append(text + "\n");
    }

    public void addMove(String move) {
        movesArea.append(move + "\n");
        movesArea.setCaretPosition(movesArea.getDocument().getLength());
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
