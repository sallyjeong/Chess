package chessproject;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Color;
/** [FindingRoomFrame.java]
 * The JFrame that opens when the client is waiting to enter a public lobby
 * @author Rachel Liu
 * @version 1.0 Jan 25, 2021
 */
public class FindingRoomFrame extends JFrame {

    private JPanel contentPane;

    /**
	 * FindingRoomFrame
	 * This constructor creates a FindingRoomFrame
	 */
    public FindingRoomFrame() {
        setTitle("Finding match");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setBounds(100, 100, 292, 200);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(238, 238, 238));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel connectionStatus = new JLabel("Connecting...");
        connectionStatus.setBounds(95, 65, 106, 16);
        contentPane.add(connectionStatus);
        setVisible(true);
    }

}
