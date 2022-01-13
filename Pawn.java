package chessproject;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import javax.imageio.ImageIO;

public class Pawn extends Piece {	

	private boolean onFirst;
	
	public Pawn(boolean w, boolean m, int p, int s, int r, int c) {
		super(w, m, p, s, r, c);
		this.onFirst = true;
	}

	@Override
	public void loadImage() {
		try {
			this.setImage(ImageIO.read(new File("files\\06_classic2\\w-pawn2.png")), 0);
			this.setImage(ImageIO.read(new File("files\\06_classic2\\b-pawn2.png")), 1);
		} catch (IOException e) {
			System.out.println("error loading sprite");
		}
		
	}
	
	@Override
	public Set<Spot> validMoves(Spot[][] board) {
		int row= getRow(); int col= getCol();

		Set<Spot> validMoves= getMoveList();
		Spot curSpot;

		if(isWhite() && row-1 >=0){
			curSpot= board[row-1][col];
			if(curSpot.getPiece() == null){
				validMoves.add(curSpot);
				if(onFirst && board[row-2][col].getPiece()== null){
					validMoves.add(board[row-2][col]);
					onFirst= false;
				}
			}
		}
		else{
			curSpot= board[row+1][col];
			if(curSpot.getPiece() == null){
				validMoves.add(curSpot);
				if(onFirst && board[row+2][col].getPiece()== null){
					validMoves.add(board[row+2][col]);
					onFirst= false;
				}
			}
		}

		return super.getMoveList();
	}

}
