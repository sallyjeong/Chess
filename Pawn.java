package chessproject;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import javax.imageio.ImageIO;

public class Pawn extends Piece {	

	private boolean enPassant;

	public Pawn(boolean w, boolean m, int p, char s, int r, int c) {
		super(w, m, p, s, r, c);
	}

	@Override
	public void loadImage() {
		try {
			this.setImage(ImageIO.read(new File("06_classic2\\w-pawn2.png")), 0);
			this.setImage(ImageIO.read(new File("06_classic2\\b-pawn2.png")), 1);
		} catch (IOException e) {
			System.out.println("error loading sprite");
		}

	}

	@Override
	public Set<Spot> validMoves(Board b) {
		Spot[][] board = b.getBoard();
		int row= getRow(); int col= getCol();

		Set<Spot> validMoves= getMoveList();
		validMoves.clear();
		Spot curSpot;

		if(isWhite()){
			curSpot= board[row-1][col];
			if(curSpot.getPiece() == null){
				validMoves.add(curSpot);
				if(!getMoved() && (board[row-2][col].getPiece()== null)){
					validMoves.add(board[row-2][col]);
					setEnPassant(true);
				}
			}
			//captures
			if(col-1>=0) {
				curSpot = board[row-1][col-1];
				if(curSpot.getPiece()!=null && curSpot.getPiece().isWhite()!=isWhite()) {
					validMoves.add(curSpot);
				}
			}
			if(col+1<=7) {
				curSpot = board[row-1][col+1];
				if(curSpot.getPiece()!=null && curSpot.getPiece().isWhite()!=isWhite()) {
					validMoves.add(curSpot);
				}
			}
			//enpassant
			if(row==3) {
				if(col-1>=0) {
					curSpot = board[row][col-1];
					if(curSpot.getPiece() instanceof Pawn && ((Pawn)curSpot.getPiece()).getEnPassant()) {
						validMoves.add(board[row-1][col-1]);
					}
				}
				if(col+1<=7) {
					curSpot = board[row][col+1];
					if(curSpot.getPiece() instanceof Pawn && ((Pawn)curSpot.getPiece()).getEnPassant()) {
						validMoves.add(board[row-1][col+1]);
					}
				}
			}
			
		}
		else{
			curSpot= board[row+1][col];
			if(curSpot.getPiece() == null){
				validMoves.add(curSpot);
				if(!getMoved() && (board[row+2][col].getPiece()== null)){
					validMoves.add(board[row+2][col]);
					setEnPassant(true);
				}
			}
			//captures
			if(col-1>=0) {
				curSpot = board[row+1][col-1];
				if(curSpot.getPiece()!=null && curSpot.getPiece().isWhite()!=isWhite()) {
					validMoves.add(curSpot);
				}
			}
			if(col+1<=7) {
				curSpot = board[row+1][col+1];
				if(curSpot.getPiece()!=null && curSpot.getPiece().isWhite()!=isWhite()) {
					validMoves.add(curSpot);
				}
			}
			//enpassant
			if(row==4) {
				if(col-1>=0) {
					curSpot = board[row][col-1];
					if(curSpot.getPiece() instanceof Pawn && ((Pawn)curSpot.getPiece()).getEnPassant()) {
						validMoves.add(board[row+1][col-1]);
					}
				}
				if(col+1<=7) {
					curSpot = board[row][col+1];
					if(curSpot.getPiece() instanceof Pawn && ((Pawn)curSpot.getPiece()).getEnPassant()) {
						validMoves.add(board[row+1][col+1]);
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
}
