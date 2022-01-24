package chessproject;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import java.awt.Color;

public class EndFrame extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					EndFrame frame = new EndFrame();
//					frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Create the frame.
	 */
	public EndFrame(String playerWins, String score) {
		JFrame frame = this;
		frame.setVisible(true);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel overLabel = new JLabel("GAME OVER");
		overLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		overLabel.setBounds(100, 37, 250, 36);
		overLabel.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(overLabel);
		
		JLabel winnerLabel = new JLabel(playerWins);
		winnerLabel.setBounds(100, 96, 250, 16);
		winnerLabel.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(winnerLabel);
		
		JLabel resultLabel = new JLabel(score);
		resultLabel.setBounds(100, 139, 250, 16);
		resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(resultLabel);
		
		JButton exitButton = new JButton("Exit");
		exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                //currentGameFrame.getClient().leaveRoom();
            }
        });
		exitButton.setForeground(new Color(0, 100, 0));
		exitButton.setBounds(167, 184, 117, 29);
		contentPane.add(exitButton);
	}
}