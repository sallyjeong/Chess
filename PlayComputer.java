package chess;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JComboBox;

public class PlayComputer extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PlayComputer frame = new PlayComputer();
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
	public PlayComputer() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 292, 200);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		JLabel colourLabel = new JLabel("Colour:");
		colourLabel.setBounds(63, 46, 55, 16);
		contentPane.add(colourLabel);
		
		
		JLabel levelLabel = new JLabel("Difficulty:");
		levelLabel.setBounds(47, 91, 71, 16);
		contentPane.add(levelLabel);
		
		String[] colours = {"black", "white", "random"};
//		JComboBox colourChoices = new JComboBox<>(colours);//use this one for drop down list 
		JComboBox colourChoices = new JComboBox();
		colourChoices.setBounds(130, 42, 125, 27);
		contentPane.add(colourChoices);
		
		String[] levels = {"easy", "medium", "hard"};
//		JComboBox difficulty = new JComboBox<>(levels);//use this one for drop down list 
		JComboBox difficulty = new JComboBox();
		difficulty.setBounds(130, 87, 125, 27);
		contentPane.add(difficulty);
	}

}
