package chessproject;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;

public class EnterDataFrame extends JFrame {

    private JPanel contentPane;
    private JTextField text;
    private JLabel prompt;
    private String dataEntered;
    private boolean closed = false;


    /**
     * Create the frame.
     */
    public EnterDataFrame(char data) {
        JFrame frame = this;
        if (data == Constants.USERNAME_DATA) {
            setTitle("Enter username");
        } else {
            setTitle("Enter room code");
        }

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 292, 200);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        if (data == Constants.USERNAME_DATA) {
            prompt = new JLabel("Username:");
        } else {
            prompt = new JLabel("Room Code:");
        }
        prompt.setBounds(44, 55, 79, 16);
        contentPane.add(prompt);

        text = new JTextField();
        text.setBounds(118, 50, 130, 26);
        contentPane.add(text);
        text.setColumns(10);

        JButton doneButton = new JButton("Done");
        doneButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dataEntered = text.getText();

                closed = true;
                frame.dispose();
                //closed = false;
            }
        });

        doneButton.setBounds(44, 92, 204, 29);
        contentPane.add(doneButton);
        setVisible(true);
    }

    public String getDataEntered() {
        return dataEntered;
    }

    public boolean isClosed() {
        return closed;
    }

//    public void setClosed (boolean x ) { //better vairable name
//        this.closed = x;
//    }
}