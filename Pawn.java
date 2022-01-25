package chessproject;

// imports
import java.io.File;
import java.io.IOException;
import java.util.Set;
import javax.imageio.ImageIO;

/** [Pawn.java]
 * Represents each Pawn on the board
 * @author
 * @version 1.0 Jan 25, 2021
 */
public class Pawn extends Piece {

	private boolean enPassant;
	private boolean forward;

	/**
	 * Pawn
	 * This constructor creates a Pawn by passing the necessary variables into the super class
	 * Sets moved and castled to false to allow castling
	 * @param white is true when the Pawn is white
	 * @param points is the number of points gained when capturing the Pawn (used for AI)
	 * @param symbol is the character that represents the Pawn (used for displaying Move)
	 * @param row is an int representing the row the piece is in a Spot[row][col] 2d array
	 * @param col is an int representing the column the piece is in a Spot[row][col] 2d array
	 * @param forward is true when the Pawn moves "up" visually for the player
	 */
	public Pawn(boolean white, int points, char symbol, int row, int col, boolean forward) {
		super(white, points, symbol, row, col);
		this.forward = forward;
	}

	/**
	 * loadImage
	 * Loads the sprite for the white or black Pawn
	 */
	@Override
	public void loadImage() {
		try {
			this.setImage(ImageIO.read(new File("06_classic2/w-pawn2.png")), 0);
			this.setImage(ImageIO.read(new File("06_classic2/b-pawn2.png")), 1);
		} catch (IOException e) {
			System.out.println("error loading sprite");
		}

	}

	/**
	 * validMoves
	 * Calculates the legal moves for the Pawn including en passant
	 * @param board is the in-game Board with the rest of the Pieces
	 * @return a Set of Spots that the Pawn can move to
	 */
	@Override
	public Set<Spot> validMoves(Board board) {
		Spot[][] gameBoard = board.getBoard();
		int row= getRow(); int col= getCol();

		Set<Spot> validMoves= getMoveList();
		validMoves.clear();
		Spot curSpot;

		if(forward && row > 0){
			curSpot= gameBoard[row-1][col];
			if(curSpot.getPiece() == null){
				validMoves.add(curSpot);
				if(row==6 && (gameBoard[row-2][col].getPiece()== null)){
					validMoves.add(gameBoard[row-2][col]);
					setEnPassant(true);
				}
			}
			//captures
			if(col-1>=0) {
				curSpot = gameBoard[row-1][col-1];
				if(curSpot.getPiece()!=null && curSpot.getPiece().isWhite()!=isWhite()) {
					validMoves.add(curSpot);
				}
			}
			if(col+1<=7) {
				curSpot = gameBoard[row-1][col+1];
				if(curSpot.getPiece()!=null && curSpot.getPiece().isWhite()!=isWhite()) {
					validMoves.add(curSpot);
				}
			}
			//enpassant
			if(row==3) {
				if(col-1>=0) {
					curSpot = gameBoard[row][col-1];
					if(curSpot.getPiece() instanceof Pawn && ((Pawn)curSpot.getPiece()).getEnPassant()) {
						validMoves.add(gameBoard[row-1][col-1]);
					}
				}
				if(col+1<=7) {
					curSpot = gameBoard[row][col+1];
					if(curSpot.getPiece() instanceof Pawn && ((Pawn)curSpot.getPiece()).getEnPassant()) {
						validMoves.add(gameBoard[row-1][col+1]);
					}
				}
			}

		} else if (!forward && row <7){
			curSpot= gameBoard[row+1][col];
			if(curSpot.getPiece() == null){
				validMoves.add(curSpot);
				if(row==1 && (gameBoard[row+2][col].getPiece()== null)){
					validMoves.add(gameBoard[row+2][col]);
					setEnPassant(true);
				}
			}
			//captures
			if(col-1>=0) {
				curSpot = gameBoard[row+1][col-1];
				if(curSpot.getPiece()!=null && curSpot.getPiece().isWhite()!=isWhite()) {
					validMoves.add(curSpot);
				}
			}
			if(col+1<=7) {
				curSpot = gameBoard[row+1][col+1];
				if(curSpot.getPiece()!=null && curSpot.getPiece().isWhite()!=isWhite()) {
					validMoves.add(curSpot);
				}
			}
			//enpassant
			if(row==4) {
				if(col-1>=0) {
					curSpot = gameBoard[row][col-1];
					if(curSpot.getPiece() instanceof Pawn && ((Pawn)curSpot.getPiece()).getEnPassant()) {
						validMoves.add(gameBoard[row+1][col-1]);
					}
				}
				if(col+1<=7) {
					curSpot = gameBoard[row][col+1];
					if(curSpot.getPiece() instanceof Pawn && ((Pawn)curSpot.getPiece()).getEnPassant()) {
						validMoves.add(gameBoard[row+1][col+1]);
					}
				}
			}
		}
		return super.getMoveList();
	}

	public boolean getEnPassant() {
		return this.enPassant;
	}

	public void setEnPassant(boolean b) {
		this.enPassant = b;
	}

	public boolean getForward() {
		return this.forward;
	}

}