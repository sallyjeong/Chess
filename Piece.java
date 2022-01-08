package chessproject;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.HashSet;

public abstract class Piece {
	private boolean white;
	private boolean moved; 
	private int points;
	private HashSet<Spot> possibleMoves;
	private BufferedImage image; 
	private int symbol;
	private boolean captured = false;
	
	public Piece(boolean w, boolean m, int p, int s) {
		this.white = w;
		this.moved = m;
		this.points = p;
		this.symbol = s;
	}
	
	public abstract void loadImage();
	
	public BufferedImage getImage() {
		return this.image;
	}
}
