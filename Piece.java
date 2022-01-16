package chessproject;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;

public abstract class Piece {
	private boolean white;
	private boolean moved;
	private int points;
	private HashSet<Spot> possibleMoves;
	private int symbol;
	private BufferedImage image[] = new BufferedImage[2];
	private boolean captured = false;
	private int row, col;

	public Piece(boolean w, boolean m, int p, int s, int r, int c) {
		this.white = w;
		this.moved = m;
		this.points = p;
		this.symbol = s;
		this.row = r;
		this.col = c;
		possibleMoves = new HashSet<Spot>();

		loadImage();
	}

	public abstract void loadImage();

	public abstract Set<Spot> validMoves(Board board);

	public BufferedImage[] getImage() {
		return this.image;
	}

	public void setImage(BufferedImage img, int ind) {
		this.image[ind] = img;
	}

	public boolean isWhite() {
		return this.white;
	}

	public HashSet<Spot> getMoveList() {
		return this.possibleMoves;
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
	
	public boolean getMoved() {
		return this.moved;
	}

	public void setMoved(boolean b) {
		this.moved = b;
	}

	public void addMove(Spot s) {
		this.possibleMoves.add(s);
	}
	
	public void checkCol(int row, int col, Spot[][] board){
		boolean blockedFront= false, blockedBack= false;
		Spot curSpot; Piece curPiece;
		for(int r= row-1; r>=0 && !blockedFront; r--){
			curSpot= board[r][col]; curPiece= curSpot.getPiece();
			if(curPiece!=null){
				if(curPiece.isWhite() !=  this.white) possibleMoves.add(curSpot);
				blockedFront= true;
			}
			else{
				possibleMoves.add(curSpot);
			}
		}

		for(int r= row+1; r<8 && !blockedBack; r++){
			curSpot= board[r][col]; curPiece= curSpot.getPiece();
			if(curPiece!=null){
				if(curPiece.isWhite() !=  this.white) possibleMoves.add(curSpot);
				blockedBack= true;
			}
			else{
				possibleMoves.add(curSpot);
			}
		}
	}

	public void checkRow(int row, int col, Spot[][] board){
		boolean blockedFront= false, blockedBack= false;
		Spot curSpot; Piece curPiece;
		for(int c= col-1; c>=0 && !blockedFront; c--){
			curSpot= board[row][c]; curPiece= curSpot.getPiece();
			if(curPiece!=null){
				if(curPiece.isWhite() !=  this.white) possibleMoves.add(curSpot);
				blockedFront= true;
			}
			else{
				possibleMoves.add(curSpot);
			}
		}

		for(int c= col+1; c<8 && !blockedBack; c++){
			curSpot= board[row][c]; curPiece= curSpot.getPiece();
			if(curPiece!=null){
				if(curPiece.isWhite() !=  this.white) possibleMoves.add(curSpot);
				blockedBack= true;
			}
			else{
				possibleMoves.add(curSpot);
			}
		}
	}

	public void checkDiags(int row, int col, Spot[][] board){
		checkMajorDiag(row, col, board);
		checkMinorDiag(row, col, board);
	}

	private void checkMajorDiag(int row, int col, Spot[][] board){
		boolean blockedFront= false, blockedBack= false;
		Spot curSpot; Piece curPiece;

		int r= row-1; int c= col-1;
		while(r >=0 && c >=0 && !blockedFront){
			curSpot= board[r][c]; curPiece= curSpot.getPiece();
			if(curPiece!=null){
				if(curPiece.isWhite() !=  this.white) possibleMoves.add(curSpot);
				blockedFront= true;
			}
			else{
				possibleMoves.add(curSpot);
			}
			r--;
			c--;
		}
		r= row+1; c= col+1;
		while(r < 8 && c  < 8 && !blockedBack) {
			curSpot= board[r][c]; curPiece= curSpot.getPiece();
			if(curPiece!=null){
				if(curPiece.isWhite() !=  this.white) possibleMoves.add(curSpot);
				blockedBack= true;
			}
			else{
				possibleMoves.add(curSpot);
			}
			r++;
			c++;
		}
	}

	private void checkMinorDiag(int row, int col, Spot[][] board){
		boolean blockedFront= false, blockedBack= false;
		Spot curSpot; Piece curPiece;
		int r= row-1; int c= col+1;
		while(r >=0 && c <8 && !blockedFront){
			curSpot= board[r][c]; curPiece= curSpot.getPiece();
			if(curPiece!=null){
				if(curPiece.isWhite() !=  this.white) possibleMoves.add(curSpot);
				blockedFront= true;
			}
			else{
				possibleMoves.add(curSpot);
			}
			r--;
			c++;
		}

		r= row+1; c= col-1;
		while(c >=0 && r<8 && !blockedBack){
			curSpot= board[r][c]; curPiece= curSpot.getPiece();
			if(curPiece!=null){
				if(curPiece.isWhite() !=  this.white) possibleMoves.add(curSpot);
				blockedBack= true;
			}
			else{
				possibleMoves.add(curSpot);
			}
			r++;
			c--;
		}
	}
	
	public void displayValidMoves(boolean b) {
		for(Spot s: possibleMoves){
			s.setHighlight(b);
		}
	}



}
