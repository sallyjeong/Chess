package chessproject;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Color;

public class FindingRoomFrame extends JFrame {

    private JPanel contentPane;

    /**
     * Create the frame.
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

        JLabel connectionStatus = new JLabel("var (connecting)");
        connectionStatus.setBounds(95, 65, 106, 16);
        contentPane.add(connectionStatus);
        setVisible(true);
    }

}