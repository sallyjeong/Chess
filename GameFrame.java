package chessproject;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.JScrollBar;
import javax.swing.JButton;
import javax.swing.border.EtchedBorder;

public class GameFrame extends JFrame {

    private JPanel contentPane;
//    public static void main(String[] args) {
//        EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                try {
//                    GameFrame frame = new GameFrame();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }

    /**
     * Create the frame.
     */
    public GameFrame() {
        // take in param to decide which way to display the board!!
        // take in param to see if its spectator or player (e.g. surrender button not available for spectators)
        JFrame frame = this;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1313, 715);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel boardPanel = new JPanel();
        boardPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
        boardPanel.setBounds(674, 56, 576, 576); // 576/8 = 72
        contentPane.add(boardPanel);

        JPanel movesPanel = new JPanel();
        movesPanel.setBorder(new TitledBorder(null, "Moves", TitledBorder.CENTER, TitledBorder.TOP, null, null));
        movesPanel.setBounds(52, 118, 564, 237);
        contentPane.add(movesPanel);

        JPanel chatPanel = new JPanel();
        chatPanel.setBorder(new TitledBorder(null, "Chat", TitledBorder.CENTER, TitledBorder.TOP, null, null));
        chatPanel.setBounds(52, 395, 564, 237);
        contentPane.add(chatPanel);

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
        drawButton.setBounds(52, 56, 172, 29);
        contentPane.add(drawButton);

        JButton surrenderButton = new JButton("Surrender");
        surrenderButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                // send to new page, remove user from the room
                // new ();    new frame here
            }
        });
        surrenderButton.setBounds(246, 56, 172, 29);
        contentPane.add(surrenderButton);

        JButton leaveButton = new JButton("Leave game");
        leaveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                // remove the user from game (if player, do the same thing as surrender)
                // new ();    new frame here
            }
        });
        leaveButton.setBounds(444, 56, 172, 29);
        contentPane.add(leaveButton);
        setVisible(true);
    }
}