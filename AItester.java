package chessproject;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class AItester {

	static Game game;
	static Player curPlayer;
	static Player p1, p2;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		p2 = new Player(false); p1 = new ComputerPlayer(true);
		curPlayer = p1;
		game = new Game(false, p1, p2);

		Move m = ((ComputerPlayer) p1).makeMove(game.getBoard(), 5);
		game.playerMove(p1, m.getStart(), m.getEnd());
		
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GamePanel v = new GamePanel();
		frame.setSize(1000, 1000);
		frame.setVisible(true);
		frame.getContentPane().add(v);
		
		
	}


	public static class GamePanel extends JPanel implements MouseListener, KeyListener {

		private Spot source = null;

		public GamePanel() {
			setPreferredSize(new Dimension(1000, 1000));
			addMouseListener(this);
			addKeyListener(this);
			setFocusable(true);
			requestFocusInWindow();
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			p1.displayCaptured(g);
			p2.displayCaptured(g);
			game.getBoard().draw(g);
			repaint();
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			if(e.getX()<8*game.getBoard().LENGTH && e.getY()<8*game.getBoard().LENGTH) {
				Spot spot = game.getBoard().getBoard()[e.getY()/game.getBoard().LENGTH][e.getX()/game.getBoard().LENGTH];
				if(source==null) {
					if(spot.getPiece()==null) {
						return;
					}else {
						if (game.getTurn().isWhite() == spot.getPiece().isWhite()) {
							spot.setClicked(true);
							spot.getPiece().displayValidMoves(true);
							source = spot;
						}
					}
				}else {
					if(spot.equals(source)) {
						source.setClicked(false);
						spot.getPiece().displayValidMoves(false);
						source = null;
					}else if(source.getPiece().getMoveList().contains(spot)) {
						if(source.getPiece().isWhite()) {
							game.playerMove(p1, source, spot);
						}else {
							game.playerMove(p2, source, spot);
						}
						source.setClicked(false);
						if(source.getPiece()!=null) {
							source.getPiece().displayValidMoves(false);
						}
						source = null;
					}
				}
			}
			
		}



		@Override
		public void mousePressed(MouseEvent e) {
		}


		@Override
		public void mouseReleased(MouseEvent e) {
		}


		@Override
		public void mouseEntered(MouseEvent e) {
		}


		@Override
		public void mouseExited(MouseEvent e) {
		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyPressed(KeyEvent e) {
			
		}

		@Override
		public void keyReleased(KeyEvent e) {
			if(e.getKeyChar()=='z') {
				Move m = ((ComputerPlayer) p1).makeMove(game.getBoard(), 5);
				game.playerMove(p1, m.getStart(), m.getEnd());
			}
		}

	}

}
