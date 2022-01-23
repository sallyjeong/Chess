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
//	public static String leave = "unconfirmed";

    /**
     * Launch the application.
     */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
////					ConfirmFrame frame = new ConfirmFrame();
//					//frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

    /**
     * Create the frame.
     */
    public ConfirmFrame(GameFrame currentGameFrame) {
        JFrame frame = this;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 292, 200);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("Leave current game");
        lblNewLabel.setBounds(81, 52, 122, 16);
        contentPane.add(lblNewLabel);

        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                currentGameFrame.dispose();
                currentGameFrame.getClient().leaveRoom();
            }
        });
        confirmButton.setForeground(new Color(0, 100, 0));
        confirmButton.setBounds(24, 90, 117, 29);
        contentPane.add(confirmButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                //frame.dispose();
                //client.leaveRoom();
            }
        });
        cancelButton.setForeground(new Color(220, 20, 60));
        cancelButton.setBounds(151, 90, 117, 29);
        contentPane.add(cancelButton);
        frame.setVisible(true);
    }
}