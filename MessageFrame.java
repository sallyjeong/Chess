
package chessproject;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;

public class MessageFrame extends JFrame {

    private JPanel contentPane;
    private boolean closed = false;

    /**
     * Create the frame.
     */
    public MessageFrame(String message) {
        JFrame frame = this;
        setTitle("Message");
        setVisible(true);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setBounds(100, 100, 292, 200);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(238, 238, 238));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel invalidUser = new JLabel(message);
        invalidUser.setBounds(58, 51, 177, 16);
        contentPane.add(invalidUser);

        JButton okayButton = new JButton("Okay");
        okayButton.setBounds(58, 87, 177, 29);
        contentPane.add(okayButton);

        okayButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                closed = true;
                frame.dispose();
            }
        });

    }

    public boolean isClosed() {
        return closed;
    }
//
//    public void setClosed(boolean x) {
//        this.closed = x;
//    }
}