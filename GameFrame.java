package chessproject;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.JScrollBar;
import javax.swing.JButton;
import javax.swing.border.EtchedBorder;

public class GameFrame extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GameFrame frame = new GameFrame();
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
	public GameFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1313, 715);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel boardPanel = new JPanel();
		boardPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		boardPanel.setBounds(674, 56, 576, 576); // 576/8 = 72
		contentPane.add(boardPanel);
		
		JPanel movesPanel = new JPanel();
		movesPanel.setBorder(new TitledBorder(null, "Moves", TitledBorder.CENTER, TitledBorder.TOP, null, null));
		movesPanel.setBounds(52, 118, 564, 237);
		contentPane.add(movesPanel);
		
		JPanel chatPanel = new JPanel();
		chatPanel.setBorder(new TitledBorder(null, "Chat", TitledBorder.CENTER, TitledBorder.TOP, null, null));
		chatPanel.setBounds(52, 395, 564, 237);
		contentPane.add(chatPanel);
		
		JButton drawButton = new JButton("Draw");
		drawButton.setBounds(52, 56, 172, 29);
		contentPane.add(drawButton);
		
		JButton surrenderButton = new JButton("Surrender");
		surrenderButton.setBounds(246, 56, 172, 29);
		contentPane.add(surrenderButton);
		
		JButton leaveButton = new JButton("Leave game");
		leaveButton.setBounds(444, 56, 172, 29);
		contentPane.add(leaveButton);
	}
}
