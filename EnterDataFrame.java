package chessproject;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.Color;

/** [EnterDataFrame.java]
 * The JFrame that opens when the client needs to enter information (e.g. username)
 * @author Rachel Liu, Katherine Liu
 * @version 1.0 Jan 25, 2022
 */
public class EnterDataFrame extends JFrame {

    private JPanel contentPane;
    private JTextField text;
    private JLabel prompt;
    private String dataEntered;
    private boolean closed = false;
    private JButton cancelButton;
    private boolean cancel = false;

    /**
	 * EnterDataFrame
	 * This constructor creates an EnterDataFrame
	 * @param data represents the type of data (ex.username, room...)
	 * @param client is the user/player 
	 */
    public EnterDataFrame(char data, Client client) {
        JFrame frame = this;
        if (data == Constants.USERNAME_DATA) {
            setTitle("Enter username");
        } else if (data == Constants.JOIN_PRIV_ROOM_DATA)  {
            setTitle("Enter room code");
        } else if (data == Constants.COLOUR_DATA) {
            setTitle("Enter board perspective");
        }

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setBounds(100, 100, 292, 200);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        if (data == Constants.USERNAME_DATA) {
            prompt = new JLabel("Username: ");
        } else if (data == Constants.JOIN_PRIV_ROOM_DATA) {
            prompt = new JLabel("Room Code: ");
        } else if (data == Constants.COLOUR_DATA) {
            prompt = new JLabel("Black/White: ");
        }
        prompt.setBounds(44, 55, 90, 16);
        contentPane.add(prompt);

        text = new JTextField();
        text.setBounds(130, 50, 130, 26);
        contentPane.add(text);
        text.setColumns(10);

        if ((data == Constants.JOIN_PRIV_ROOM_DATA) || (data == Constants.COLOUR_DATA)) {
            JButton doneButton = new JButton("Done");
            doneButton.setForeground(new Color(0, 100, 0));
            doneButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    dataEntered = text.getText();
                    frame.dispose();
                    closed = true;
                }
            });
            doneButton.setBounds(44, 92, 104, 29);
            contentPane.add(doneButton);

            cancelButton = new JButton("Cancel");
            cancelButton.setForeground(new Color(178, 34, 34));
            cancelButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    frame.dispose();
                    client.quitGame(false);
                    new HomeFrame();
                }
            });
            cancelButton.setBounds(152, 92, 104, 29);
            contentPane.add(cancelButton);
        }else {
            JButton doneUserButton = new JButton("Done");
            doneUserButton.setForeground(new Color(0, 100, 0));
            doneUserButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    dataEntered = text.getText();
                    frame.dispose();
                    closed = true;
                }
            });
            doneUserButton.setBounds(44, 92, 208, 29);
            contentPane.add(doneUserButton);
        }

        setVisible(true);
    }
    /*
    METHODS FOR EXTRACTING VALUES (setters and getters)
     */
    public String getDataEntered() {
        if (dataEntered == null) {
            dataEntered = text.getText();
        }
        return dataEntered;
    }

    public boolean isClosed() {
        return closed;
    }

}
