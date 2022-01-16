package chessproject;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;

public class CreatePrivateRoomFrame extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CreatePrivateRoomFrame frame = new CreatePrivateRoomFrame();
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
	public CreatePrivateRoomFrame() {
		setTitle("Creating private room");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 292, 200);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(238, 238, 238));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel roomCodeLabel = new JLabel("Room code:");
		roomCodeLabel.setBounds(33, 50, 79, 16);
		contentPane.add(roomCodeLabel);
		
		JLabel codeValue = new JLabel("ADBMNSD1233");
		codeValue.setHorizontalAlignment(SwingConstants.CENTER);
		codeValue.setBounds(124, 50, 131, 16);
		contentPane.add(codeValue);
		
		JLabel colourLabel = new JLabel("Colour:");
		colourLabel.setBounds(60, 89, 52, 16);
		contentPane.add(colourLabel);
		
		String[] colours = {"black", "white", "random"};
//		JComboBox colourChoices = new JComboBox<>(colours);//use this one for drop down list 
		JComboBox colourChoices = new JComboBox();
		colourChoices.setBounds(134, 85, 121, 27);
		contentPane.add(colourChoices);
	}
}
