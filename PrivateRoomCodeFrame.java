package chessproject;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class PrivateRoomCodeFrame extends JFrame {

	private JPanel contentPane;
	private JTextField codeText;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PrivateRoomCodeFrame frame = new PrivateRoomCodeFrame();
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
	public PrivateRoomCodeFrame() {
		setTitle("Enter room code");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 292, 200);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel enterCodeLabel = new JLabel("Room code:");
		enterCodeLabel.setBounds(43, 71, 79, 16);
		contentPane.add(enterCodeLabel);
		
		codeText = new JTextField();
		codeText.setBounds(124, 66, 140, 26);
		contentPane.add(codeText);
		codeText.setColumns(10);
	}
}
