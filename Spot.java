package chessproject;

import java.awt.Color;
import java.awt.Graphics;

public class Spot implements Drawable {
	private final int LENGTH = 50;
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
		g.fillRect(column*this.LENGTH, row*this.LENGTH, this.LENGTH, this.LENGTH);
		if(this.piece!=null) {
			if(this.piece.isWhite()) {
				g.drawImage(this.piece.getImage()[0], column*this.LENGTH, row*this.LENGTH, this.LENGTH, this.LENGTH, null);
			}else {
				g.drawImage(this.piece.getImage()[1], column*this.LENGTH, row*this.LENGTH, this.LENGTH, this.LENGTH, null);
			}
		}
	} 

	public void addPiece(Piece p) {
		this.piece = p;
	}
	
	public void removePiece() {
		this.piece = null;
	}
	
	public Piece getPiece() {
		return this.piece;
	}
}
