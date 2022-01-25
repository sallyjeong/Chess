package chessproject;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.Color;

public class ConfirmFrame extends JFrame {

    private JPanel contentPane;

    /**
     * Create the frame.
     */
    public ConfirmFrame(GameFrame currentGameFrame, boolean leave) {
        JFrame frame = this;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 292, 200);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel;
        if (leave) {
            lblNewLabel = new JLabel("Leave current game");
        } else {
            lblNewLabel = new JLabel("Request draw");
        }
        lblNewLabel.setBounds(81, 52, 122, 16);
        contentPane.add(lblNewLabel);

        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                if (leave) {
                    // surrender or leave button
                    currentGameFrame.dispose();
                    String message = Constants.CHAT_DATA + "*** PLAYER " + currentGameFrame.getClient().getUsername() + " HAS LEFT GAME ***";
                    currentGameFrame.getClient().sendData(message);
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException ex) {
                    }
                    currentGameFrame.getClient().leaveRoom();

                    HomeFrame.roomNames = currentGameFrame.getClient().getRoomNames();
                    HomeFrame.list.setListData(HomeFrame.roomNames.toArray());
                } else {
                    // draw
                    currentGameFrame.addMessage("*** DRAW REQUEST SENT ***");
                    currentGameFrame.getClient().sendData(Constants.DRAW_DATA + Constants.REQUEST);
                }
            }
        });
        confirmButton.setForeground(new Color(0, 100, 0));
        confirmButton.setBounds(24, 90, 117, 29);
        contentPane.add(confirmButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
        cancelButton.setForeground(new Color(220, 20, 60));
        cancelButton.setBounds(151, 90, 117, 29);
        contentPane.add(cancelButton);
        frame.setVisible(true);
    }
}