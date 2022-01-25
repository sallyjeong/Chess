package chessproject;

import java.awt.Color;
import java.awt.Graphics;

public class Spot implements Drawable {

	private final Color clickedColour = Color.green, capturedColour = Color.lightGray, checkedColour = Color.red, movedColour = Color.darkGray;

	private Color defaultColour;
	private int row, column;
	private String id;
	private Piece piece;
	private boolean highlighted, clicked, checked, left;
	private int length;

	public Spot(int r, int c, String id, Piece p, Color co, int l) {
		this.row = r;
		this.column = c;
		this.id = id;
		this.piece = p;
		this.defaultColour = co;
		this.length = l;
	}

	@Override
	public void draw(Graphics g) {
		//colors for the tile underneath
		if(this.clicked) {
			g.setColor(clickedColour);
			//can be captured
		}else if(this.piece!=null && this.highlighted) {
			g.setColor(capturedColour);
		}else if(this.checked) {
			if(piece == null){ //King has moved out
				checked= false;
			}
			else{
				g.setColor(checkedColour);
			}
		} else if(this.left){
			g.setColor(movedColour);
		} else {
			g.setColor(this.defaultColour);

		}
		g.fillRect(column*this.length, row*this.length, this.length, this.length);
		if(this.piece!=null) {
			if(this.piece.isWhite()) {
				g.drawImage(this.piece.getImage()[0], column*this.length, row*this.length, this.length, this.length, null);
			}else {
				g.drawImage(this.piece.getImage()[1], column*this.length, row*this.length, this.length, this.length, null);
			}
		}else if(this.highlighted) {
			g.setColor(Color.green);
			g.fillOval(column*this.length+length/2-5, row*this.length+length/2-5, 10, 10);
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
		if(p!=null) {
			p.setRow(row); p.setCol(column);
		}
	}

	public Piece removePiece() {
		Piece p= piece;
		this.piece = null;
		return p;
	}

	public Piece getPiece() {
		return this.piece;
	}

	public void setHighlight(boolean b) {
		this.highlighted = b;
	}

	public boolean isClicked() {
		return this.clicked;
	}

	public void setClicked(boolean b) {
		this.clicked = b;
	}

	public void setChecked(boolean b) {
		this.checked = b;
	}

	public void setLeft(boolean b) {
		this.left = b;
	}
}