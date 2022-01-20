
package chessproject;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;

public class InvalidUserFrame extends JFrame {

    private JPanel contentPane;
    private boolean closed = false;
    //    public static void main(String[] args) {
    //        EventQueue.invokeLater(new Runnable() {
    //            public void run() {
    //                try {
    //                    InvalidUserFrame frame = new InvalidUserFrame();
    //                } catch (Exception e) {
    //                    e.printStackTrace();
    //                }
    //            }
    //        });
    //    }

    /**
     * Create the frame.
     */
    public InvalidUserFrame() {
        JFrame frame = this;
        setTitle("Invalid Username");
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 292, 200);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(238, 238, 238));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel invalidUser = new JLabel(Constants.USERNAME_ERROR);
        invalidUser.setBounds(58, 51, 177, 16);
        contentPane.add(invalidUser);

        JButton okayButton = new JButton("Okay");
        okayButton.setBounds(58, 87, 177, 29);
        contentPane.add(okayButton);

        okayButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                closed = true;
                frame.dispose();

                // new EnterUsernameFrame();
                // new ();    new frame here
            }
        });

    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean x) {
        this.closed = x;
    }
}