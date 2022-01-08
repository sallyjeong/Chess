package chessproject;

import java.awt.Color;
import java.awt.Graphics;

public class Spot implements Drawable {
	private final int LENGTH = 20;
	private int row, column;
	private String id;
	private Piece piece;
	private Color colour;
	
	public Spot(int r, int c, String id, Piece p, Color co) {
		this.row = r;
		this.column = c;
		this.id = id;
		this.piece = p;
		this.colour = co;
	}
	
	@Override
	public void draw(Graphics g) {
		g.setColor(this.colour);
		g.fillRect(row*LENGTH, column*LENGTH, LENGTH, LENGTH);
	} 
	
	public Piece getPiece() {
		return this.piece;
	}
}
