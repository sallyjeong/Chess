package chessproject;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

public class PrivateRoomCodeFrame extends JFrame {

    private JPanel contentPane;
    private JTextField codeText;
    private JButton doneButton;
    private String roomEntered;

//    public static void main(String[] args) {
//        EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                try {
//                    PrivateRoomCodeFrame frame = new PrivateRoomCodeFrame();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }

    /**
     * Create the frame.
     */
    public PrivateRoomCodeFrame() {
        JFrame frame = this;
        setTitle("Enter room code");
        setVisible(true);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 292, 200);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel enterCodeLabel = new JLabel("Room code:");
        enterCodeLabel.setBounds(36, 52, 79, 16);
        contentPane.add(enterCodeLabel);

        codeText = new JTextField();
        codeText.setBounds(117, 47, 140, 26);
        contentPane.add(codeText);
        codeText.setColumns(10);

        doneButton = new JButton("Done");
        doneButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                roomEntered = codeText.getText();
                frame.dispose();
            }
        });
        doneButton.setBounds(36, 92, 221, 29);
        contentPane.add(doneButton);
    }

    public String getRoomEntered() {
        return roomEntered.toLowerCase();
    }
}