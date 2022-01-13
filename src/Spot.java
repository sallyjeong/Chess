import java.awt.Color;
import java.awt.Graphics;

public class Spot implements Drawable {
	private final int LENGTH = 50;
	private int row, column;
	private String id;
	private Piece piece;
	private Color colour;
	private boolean highlighted, clicked;

	public Spot(int r, int c, String id, Piece p, Color co) {
		this.row = r;
		this.column = c;
		this.id = id;
		this.piece = p;
		this.colour = co;
	}

	@Override
	public void draw(Graphics g) {
		if(this.clicked) {
			g.setColor(Color.green);
		}else {
			g.setColor(this.colour);
		}
		g.fillRect(column*this.LENGTH, row*this.LENGTH, this.LENGTH, this.LENGTH);
		if(this.piece!=null) {
			if(this.piece.isWhite()) {
				g.drawImage(this.piece.getImage()[0], column*this.LENGTH, row*this.LENGTH, this.LENGTH, this.LENGTH, null);
			}else {
				g.drawImage(this.piece.getImage()[1], column*this.LENGTH, row*this.LENGTH, this.LENGTH, this.LENGTH, null);
			}
		}else if(this.highlighted) {
			g.setColor(Color.green);
			g.fillOval(column*this.LENGTH+LENGTH/2-5, row*this.LENGTH+LENGTH/2-5, 10, 10);
		}
	} 
	
	public String getID() {
		return this.id;
	}
	
	public int getRow() {
		return this.row;
	}
	
	public int getColumn() {
		return this.column;
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
	
	public void setHighlight(boolean b) {
		this.highlighted = b;
	}
	
	public void setClicked(boolean b) {
		this.clicked = b;
	}
}
