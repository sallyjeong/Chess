package chessproject;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.HashSet;

public abstract class Piece {
	private boolean white;
	private boolean moved; 
	private int points;
	private HashSet<Spot> possibleMoves;
	private int symbol;
	private BufferedImage image[] = new BufferedImage[2];
	private boolean captured = false;
	
	public Piece(boolean w, boolean m, int p, int s) {
		this.white = w;
		this.moved = m;
		this.points = p;
		this.symbol = s;
		loadImage();
	}
	
	public abstract void loadImage();
	
	public BufferedImage[] getImage() {
		return this.image;
	}
	
	public void setImage(BufferedImage img, int ind) {
		this.image[ind] = img;
	}
	
	public boolean isWhite() {
		return this.white;
	}
	
	
}
