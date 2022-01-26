package chessproject;

import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;

/**
 * [Piece.java]
 * Abstract class that represents each piece on the board.
 *  @author Stanley Wang
 *  @version 1.0 Jan 25, 2022
 */
public abstract class Piece {
	private boolean isWhite; //boolean representing if the current piece is white
	private int points; //points value of the current piece
	private HashSet<Spot> pseudoLegalMoves; //stores all pseudo-legal moves (moves that follow the basic move rules)
	private char symbol; //the symbol of the current piece
	private BufferedImage image[] = new BufferedImage[2]; //the possible sprites of the current piece
	private int row, col; //the row and column #s of the current piece

	/**
	 * constructs the Piece
	 * @param white: the color of the piece
	 * @param pts: the number of points its worth
	 * @param symbol: the piece's symbol
	 * @param r: the piece's row
	 * @param c: the piece's col
	 */
	public Piece(boolean white, int pts, char symbol, int r, int c) {
		this.isWhite = white;
		this.points = pts;
		this.symbol = symbol;
		this.row = r;
		this.col = c;
		pseudoLegalMoves = new HashSet<Spot>();
		loadImage();
	}

	/**
	 * abstract method that loads our current sprite.
	 */
	public abstract void loadImage();

	/**
	 * abstract method that determines all valid psuedolegal moves
	 * @param board
	 * @return
	 */
	public abstract Set<Spot> validMoves(Board board);

	/**
	 * Method to be used by subclasses, determines the pseudo legal moves along the current column.
	 * @param board: the current board
	 */
	public void checkCol( Spot[][] board){
		boolean blockedFront= false, blockedBack= false;
		Spot curSpot; Piece curPiece;

		//checks in all spots in front of the current one for valid spots
		for(int r= row-1; r>=0 && !blockedFront; r--){
			curSpot= board[r][col]; curPiece= curSpot.getPiece();

			if(curPiece!=null){ //once we hit a piece,
				if(curPiece.isWhite() !=  this.isWhite){ //if it's an opponent's piece we can take it,
					pseudoLegalMoves.add(curSpot);
				}
				blockedFront= true; //the piece acts as a wall and we cannot move beyond it.
			}
			else{
				pseudoLegalMoves.add(curSpot);
			}
		}

		//same process for checking all spots behind the current one.
		for(int r= row+1; r<Constants.BOARD_SIZE && !blockedBack; r++){
			curSpot= board[r][col]; curPiece= curSpot.getPiece();
			if(curPiece!=null){
				if(curPiece.isWhite() !=  this.isWhite) pseudoLegalMoves.add(curSpot);
				blockedBack= true;
			}
			else{
				pseudoLegalMoves.add(curSpot);
			}
		}
	}

	/**
	 * Method to be used by subclasses, determines the pseudo legal moves along the current row.
	 * @param board: the current board
	 */
	public void checkRow(Spot[][] board){
		boolean blockedFront= false, blockedBack= false;
		Spot curSpot; Piece curPiece;

		//checks in all spots to the left of the current one for valid spots
		for(int c= col-1; c>=0 && !blockedFront; c--){
			curSpot= board[row][c]; curPiece= curSpot.getPiece();
			if(curPiece!=null){ //once we hit a piece,
				if(curPiece.isWhite() !=  this.isWhite){ //if it's an opponent's piece we can take it,
					pseudoLegalMoves.add(curSpot);
				}
				blockedFront= true; //the piece acts as a wall and we cannot move beyond it.
			}
			else{
				pseudoLegalMoves.add(curSpot);
			}
		}

		//Same process but for spots to the right of the current one.
		for(int c= col+1; c<Constants.BOARD_SIZE && !blockedBack; c++){
			curSpot= board[row][c]; curPiece= curSpot.getPiece();
			if(curPiece!=null){
				if(curPiece.isWhite() !=  this.isWhite) pseudoLegalMoves.add(curSpot);
				blockedBack= true;
			}
			else{
				pseudoLegalMoves.add(curSpot);
			}
		}
	}

	/**
	 * Method to be used by subclasses, determines the pseudo legal moves along both diagonals.
	 * @param board: the current board
	 */
	public void checkDiags(Spot[][] board){
		checkMajorDiag(board);
		checkMinorDiag(board);
	}

	/**
	 * Helper method that determines the pseudo legal moves along the major diagonal.
	 * @param board: the current board
	 */
	private void checkMajorDiag(Spot[][] board){
		boolean blockedFront= false, blockedBack= false;
		Spot curSpot; Piece curPiece;

		int r= row-1; int c= col-1;
		//same checking process as for row/cols but for the diagonal part to the left of the current pos
		while(r >=0 && c >=0 && !blockedFront){
			curSpot= board[r][c]; curPiece= curSpot.getPiece();
			if(curPiece!=null){
				if(curPiece.isWhite() !=  this.isWhite) pseudoLegalMoves.add(curSpot);
				blockedFront= true;
			}
			else{
				pseudoLegalMoves.add(curSpot);
			}
			r--;
			c--;
		}


		r= row+1; c= col+1;
		//same checking process as for row/cols but for the diagonal part to the right of the current pos
		while(r < Constants.BOARD_SIZE && c  < Constants.BOARD_SIZE && !blockedBack) {
			curSpot= board[r][c]; curPiece= curSpot.getPiece();
			if(curPiece!=null){
				if(curPiece.isWhite() !=  this.isWhite) pseudoLegalMoves.add(curSpot);
				blockedBack= true;
			}
			else{
				pseudoLegalMoves.add(curSpot);
			}
			r++;
			c++;
		}
	}

	/**
	 * Helper method that determines the pseudo legal moves along the minor diagonal.
	 * @param board: the current board
	 */
	private void checkMinorDiag(Spot[][] board){
		boolean blockedFront= false, blockedBack= false;
		Spot curSpot; Piece curPiece;

		int r= row-1; int c= col+1;
		//same checking process as for row/cols but for the diagonal part to the right of the current pos
		while(r >=0 && c <Constants.BOARD_SIZE && !blockedFront){
			curSpot= board[r][c]; curPiece= curSpot.getPiece();
			if(curPiece!=null){
				if(curPiece.isWhite() !=  this.isWhite) pseudoLegalMoves.add(curSpot);
				blockedFront= true;
			}
			else{
				pseudoLegalMoves.add(curSpot);
			}
			r--;
			c++;
		}

		r= row+1; c= col-1;
		//same checking process as for row/cols but for the diagonal part to the left of the current pos
		while(c >=0 && r<Constants.BOARD_SIZE && !blockedBack){
			curSpot= board[r][c]; curPiece= curSpot.getPiece();
			if(curPiece!=null){
				if(curPiece.isWhite() !=  this.isWhite) pseudoLegalMoves.add(curSpot);
				blockedBack= true;
			}
			else{
				pseudoLegalMoves.add(curSpot);
			}
			r++;
			c--;
		}
	}

	/**
	 * highlights all pseudo legal moves
	 * @param highlighted:
	 */
	public void displayValidMoves(boolean highlighted) {
		for(Spot s: pseudoLegalMoves){
			s.setHighlight(highlighted);
		}
	}

	/** Getters and Setters **/

	public BufferedImage[] getImage() {
		return this.image;
	}

	public void setImage(BufferedImage img, int ind) {
		this.image[ind] = img;
	}

	public boolean isWhite() {
		return this.isWhite;
	}

	public HashSet<Spot> getMoveList() {
		return this.pseudoLegalMoves;
	}

	public int getPoints(){
		return points;
	}

	public int getRow() {
		return this.row;
	}

	public int getCol() {
		return this.col;
	}

	public void setRow(int r) {
		this.row = r;
	}

	public void setCol(int c) {
		this.col = c;
	}

	public char getSymbol() {
		return this.symbol;
	}



}
