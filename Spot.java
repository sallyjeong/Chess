package chessproject;

import java.awt.Color;
import java.awt.Graphics;

/** [Spot.java]
 * Represents each person joining the chess program
 * Connects to the server and has the ability to play a chess game
 * @author Katherine Liu, Sally Jeong, Stanley Wang
 * @version 1.0 Jan 25, 2021
 */
public class Spot implements Drawable {

	private final Color clickedColour = Color.green,
			capturedColour = Color.lightGray,
			checkedColour = Color.red,
			movedColour = Color.darkGray;

	private Color defaultColour;
	private int row, column;
	private String id;
	private Piece piece;
	private boolean highlighted, clicked, checked, left;
	private int length;

	/**
	 * Spot
	 * This constructor creates a Spot on the chess board
	 * @param row is the row in a Spot[row][column] 2d array
	 * @param column is the column in a Spot[row][column] 2d array
	 * @param id is the String id of a Spot on the chess board (i.e. a1, b3, f4)
	 * @param piece is the Piece on the Spot (i.e. King, Pawn)
	 * @param colour is the colour of the Spot (depends on if clicked, threatened etc.)
	 * @param length is the size of the side length of one Spot
	 */
	public Spot(int row, int column, String id, Piece piece, Color colour, int length) {
		this.row = row;
		this.column = column;
		this.id = id;
		this.piece = piece;
		this.defaultColour = colour;
		this.length = length;
	}

	/**
	 * draw
	 * Allows the Spot to be displayed graphically
	 * Changes the colour of the Spot 
	 * based on if its clicked, threatened, checked, just left, or part of valid moves
	 * Overrides method from Drawable interface
	 * @param g the Graphics object used to display pieces
	 */
	@Override
	public void draw(Graphics g) {
		
		//colors for the tile underneath
		if(clicked) {
			g.setColor(clickedColour);
			//can be captured
		}else if(piece!=null && highlighted) {
			g.setColor(capturedColour);
		}else if(checked) {
			if(piece == null){ //King has moved out
				checked= false;
			}
			else{
				g.setColor(checkedColour);
			}
		} else if(left){
			g.setColor(movedColour);
		} else {
			g.setColor(defaultColour);

		}
		g.fillRect(column*length, row*length, length, length);
		
		// drawing the pieces onto the Spot
		if(piece!=null) {
			if(piece.isWhite()) {
				g.drawImage(piece.getImage()[0], column*length, row*length, length, length, null);
			}else {
				g.drawImage(piece.getImage()[1], column*length, row*length, length, length, null);
			}
		}else if(highlighted) {
			g.setColor(Color.green);
			g.fillOval(column*length+length/2-5, row*length+length/2-5, 10, 10);
		}
	}

	/*
	CHANGING THE PIECE ON THE SPOT
	 */
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

	/*
	GETTERS AND SETTERS
	 */
	public Piece getPiece() {
		return this.piece;
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

	public void setHighlight(boolean b) {
		this.highlighted = b;
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

	public void setRow(int row) {
		this.row = row;
	}

	public void setColumn(int column) {
		this.column = column;
	}
}
