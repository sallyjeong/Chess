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
		//just testing
		if(this.piece!=null) {
			g.setColor(Color.BLACK);
			g.fillRect(row*LENGTH, column*LENGTH, LENGTH, LENGTH);
		}else {
			g.setColor(this.colour);
			g.fillRect(row*LENGTH, column*LENGTH, LENGTH, LENGTH);
		}

	} 

	public Piece getPiece() {
		return this.piece;
	}
}
