package chessproject;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;

public class EnterUsernameFrame extends JFrame {

    private JPanel contentPane;
    private JTextField usernameValue;
    private String usernameEntered;
    private boolean closed = false;

//    public static void main(String[] args) {
//        EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                try {
//                    EnterUsernameFrame frame = new EnterUsernameFrame();
//                    frame.setVisible(true);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }

    /**
     * Create the frame.
     */
    public EnterUsernameFrame() {
        JFrame frame = this;
        setTitle("Enter username");
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 292, 200);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel username = new JLabel("Username:");
        username.setBounds(44, 55, 79, 16);
        contentPane.add(username);

        usernameValue = new JTextField();
        usernameValue.setBounds(118, 50, 130, 26);
        contentPane.add(usernameValue);
        usernameValue.setColumns(10);

        JButton doneButton = new JButton("Done");
        doneButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                usernameEntered = usernameValue.getText();
                closed = true;
                frame.dispose();
                // new ();    new frame here
            }
        });

        doneButton.setBounds(44, 92, 204, 29);
        contentPane.add(doneButton);

    }

    public String getUsernameEntered() {
        return usernameEntered;
    }

    public boolean isClosed() {
        return closed;
    }
}
