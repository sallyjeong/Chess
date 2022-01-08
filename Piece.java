package chessproject;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.HashSet;

public abstract class Piece implements Drawable {
	private boolean white;
	private boolean moved; 
	private int points;
	private HashSet<Spot> possibleMoves;
	private BufferedImage image; 
	private int symbol;
	private boolean captured = false;
	
	public Piece(boolean w, boolean m, int p, BufferedImage i, int s) {
		this.white = w;
		this.moved = m;
		this.points = p;
		this.image = i;
		this.symbol = s;
	}
	
	@Override
	public void draw(Graphics g) {
		
	}
	
	public abstract void loadImage();
	
}
