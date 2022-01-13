import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class tester {

	static Board test = new Board();
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JFrame frame = new JFrame();
		Visualizer v = new Visualizer();
		frame.setSize(1000, 1000);
		frame.setVisible(true);
		frame.getContentPane().add(v);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		Game game = new Game();
	}

	
	public static class Visualizer extends JPanel {
		
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			test.draw(g);
			repaint();
		}
	}
	
}
