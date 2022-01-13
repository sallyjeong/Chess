package chessproject;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import javax.imageio.ImageIO;

public class Knight extends Piece {

	public Knight(boolean w, boolean m, int p, int s, int r, int c) {
		super(w, m, p, s, r, c);
	}

	@Override
	public void loadImage() {
		try {
			this.setImage(ImageIO.read(new File("files\\06_classic2\\w-knight2.png")), 0);
			this.setImage(ImageIO.read(new File("files\\06_classic2\\b-knight2.png")), 1);
		} catch (IOException e) {
			System.out.println("error loading sprite");
		}
	}

	@Override
	public Set<Spot> validMoves(Spot[][] board) {
		int[] rowIncrements= {-2, -1, -2, -1, 1, 2, 1, 2};
		int[] colIncrements= {-1, -2 , 1, 2, -2, -1, 2, 1};
		int row= getRow(); int col= getCol();

		Set<Spot> validMoves= getMoveList();
		Spot curSpot;
		for(int i=0; i< 8; i++){
			int r= row+ rowIncrements[i]; int c= col+ colIncrements[i];
			if((r>=0 && r <8) && (c >=0 && c <8)){
				curSpot= board[r][c];
				if(curSpot.getPiece()== null || curSpot.getPiece().isWhite()!= isWhite()){
					validMoves.add(curSpot);
				}
			}
		}

		return super.getMoveList();
	}

}
