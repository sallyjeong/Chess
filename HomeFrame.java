package chessproject;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.BoxLayout;
import javax.swing.border.BevelBorder;
import javax.swing.border.MatteBorder;
import java.awt.Color;
import javax.swing.border.EtchedBorder;
import java.awt.GridLayout;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import java.awt.SystemColor;
import javax.swing.JSplitPane;

public class HomeFrame extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HomeFrame frame = new HomeFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public HomeFrame() {
		setTitle("HomeFrame");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1313, 715);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel mainPanel = new JPanel();
		mainPanel.setBackground(new Color(255, 255, 255));
		mainPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		mainPanel.setBounds(14, 10, 184, 669);
		contentPane.add(mainPanel);
		mainPanel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Chess");
		lblNewLabel.setBounds(71, 10, 40, 16);
		lblNewLabel.setFont(new Font("Apple Braille", Font.PLAIN, 13));
		mainPanel.add(lblNewLabel);
		
		JButton instructionsButton = new JButton("Instructions");
		instructionsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		instructionsButton.setBounds(27, 133, 134, 45);
		mainPanel.add(instructionsButton);
		
		JButton settingsButton = new JButton("Settings");
		settingsButton.setBounds(26, 65, 134, 45);
		mainPanel.add(settingsButton);
		
		JPanel lobbyPanel = new JPanel();
		lobbyPanel.setBackground(new Color(255, 255, 255));
		lobbyPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		lobbyPanel.setBounds(207, 9, 1095, 669);
		contentPane.add(lobbyPanel);
		lobbyPanel.setLayout(null);
		
		JLabel lobbyLabel = new JLabel("Lobbies");
		lobbyLabel.setBounds(175, 10, 57, 16);
		lobbyPanel.add(lobbyLabel);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(3, 30, 1086, 12);
		lobbyPanel.add(separator);
		
		JLabel publicLabel = new JLabel("Public ");
		publicLabel.setBounds(15, 43, 61, 16);
		lobbyPanel.add(publicLabel);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(3, 203, 1086, 12);
		lobbyPanel.add(separator_1);
		
		JButton openServer = new JButton("put variable for open server");
		openServer.setHorizontalAlignment(SwingConstants.LEFT);
		openServer.setBounds(7, 69, 437, 23);
		lobbyPanel.add(openServer);
		
		JSeparator separator_1_1 = new JSeparator();
		separator_1_1.setBounds(3, 276, 1086, 12);
		lobbyPanel.add(separator_1_1);
		
		JButton openServer_1 = new JButton("put variable for open server");
		openServer_1.setHorizontalAlignment(SwingConstants.LEFT);
		openServer_1.setBounds(7, 100, 437, 23);
		lobbyPanel.add(openServer_1);
		
		JLabel statusLabel = new JLabel("STATUS");
		statusLabel.setBounds(456, 43, 61, 16);
		lobbyPanel.add(statusLabel);
		
		JLabel status = new JLabel("var");
		status.setForeground(Color.RED);
		status.setHorizontalAlignment(SwingConstants.CENTER);
		status.setBounds(456, 72, 61, 16);
		lobbyPanel.add(status);
		
		JLabel status_1 = new JLabel("var");
		status_1.setHorizontalAlignment(SwingConstants.CENTER);
		status_1.setForeground(new Color(50, 205, 50));
		status_1.setBounds(456, 102, 61, 16);
		lobbyPanel.add(status_1);
		
		JButton playButton = new JButton("Enter matchmaking");
		playButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		playButton.setBackground(SystemColor.windowBorder);
		playButton.setBounds(13, 160, 1062, 40);
		lobbyPanel.add(playButton);
		
		JLabel privateLabel = new JLabel("Private");
		privateLabel.setBounds(19, 220, 61, 16);
		lobbyPanel.add(privateLabel);
		
		JButton codeLabel = new JButton("Enter code");
		codeLabel.setBounds(11, 241, 510, 29);
		lobbyPanel.add(codeLabel);
		
		JButton roomLabel = new JButton("Create room");
		roomLabel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		roomLabel.setBounds(565, 241, 510, 29);
		lobbyPanel.add(roomLabel);
		
		JButton openServer_1_1 = new JButton("put variable for open server");
		openServer_1_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		openServer_1_1.setHorizontalAlignment(SwingConstants.LEFT);
		openServer_1_1.setBounds(7, 130, 437, 23);
		lobbyPanel.add(openServer_1_1);
		
		JLabel status_1_1 = new JLabel("var");
		status_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		status_1_1.setForeground(new Color(50, 205, 50));
		status_1_1.setBounds(456, 132, 61, 16);
		lobbyPanel.add(status_1_1);
		
		JLabel computerLabel = new JLabel("Computer");
		computerLabel.setBounds(19, 290, 96, 16);
		lobbyPanel.add(computerLabel);
		
		JButton playButton_1 = new JButton("Play computer");
		playButton_1.setBackground(SystemColor.windowBorder);
		playButton_1.setBounds(15, 315, 1060, 24);
		lobbyPanel.add(playButton_1);
		
		JButton openServer_2 = new JButton("put variable for open server");
		openServer_2.setHorizontalAlignment(SwingConstants.LEFT);
		openServer_2.setBounds(565, 69, 437, 23);
		lobbyPanel.add(openServer_2);
		
		JButton openServer_1_2 = new JButton("put variable for open server");
		openServer_1_2.setHorizontalAlignment(SwingConstants.LEFT);
		openServer_1_2.setBounds(565, 100, 437, 23);
		lobbyPanel.add(openServer_1_2);
		
		JButton openServer_1_1_1 = new JButton("put variable for open server");
		openServer_1_1_1.setHorizontalAlignment(SwingConstants.LEFT);
		openServer_1_1_1.setBounds(565, 130, 437, 23);
		lobbyPanel.add(openServer_1_1_1);
		
		JLabel statusLabel_1 = new JLabel("STATUS");
		statusLabel_1.setBounds(1014, 43, 61, 16);
		lobbyPanel.add(statusLabel_1);
		
		JLabel status_2 = new JLabel("var");
		status_2.setHorizontalAlignment(SwingConstants.CENTER);
		status_2.setForeground(Color.RED);
		status_2.setBounds(1014, 72, 61, 16);
		lobbyPanel.add(status_2);
		
		JLabel status_1_2 = new JLabel("var");
		status_1_2.setHorizontalAlignment(SwingConstants.CENTER);
		status_1_2.setForeground(new Color(50, 205, 50));
		status_1_2.setBounds(1014, 102, 61, 16);
		lobbyPanel.add(status_1_2);
		
		JLabel status_1_1_1 = new JLabel("var");
		status_1_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		status_1_1_1.setForeground(new Color(50, 205, 50));
		status_1_1_1.setBounds(1014, 132, 61, 16);
		lobbyPanel.add(status_1_1_1);
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setOrientation(SwingConstants.VERTICAL);
		separator_2.setBounds(529, 69, 12, 84);
		lobbyPanel.add(separator_2);
	}
}
