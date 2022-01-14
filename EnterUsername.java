package chess;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class EnterUsername extends JFrame {

	private JPanel contentPane;
	private JTextField usernameValue;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					EnterUsername frame = new EnterUsername();
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
	public EnterUsername() {
		setTitle("Enter username");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 292, 200);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel username = new JLabel("Username:");
		username.setBounds(43, 71, 79, 16);
		contentPane.add(username);
		
		usernameValue = new JTextField();
		usernameValue.setBounds(117, 66, 130, 26);
		contentPane.add(usernameValue);
		usernameValue.setColumns(10);
		
		
	}
}
