package chessproject;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;

public class InvalidUserFrame extends JFrame {

    private JPanel contentPane;
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
        setTitle("Invalid username");
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 292, 200);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(238, 238, 238));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel invalidUser = new JLabel("Invalid username. Try again.");
        invalidUser.setBounds(58, 51, 177, 16);
        contentPane.add(invalidUser);

        JButton doneButton = new JButton("Okay");
        doneButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                // new ();    new frame here
            }
        });
        doneButton.setBounds(58, 87, 177, 29);
        contentPane.add(doneButton);
    }

}