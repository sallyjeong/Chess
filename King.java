import java.io.File;
import java.io.IOException;
import java.util.Set;

import javax.imageio.ImageIO;

public class King extends Piece {

	private boolean castle = false;

	public King(boolean w, boolean m, int p, char s, int r, int c) {
		super(w, m, p, s, r, c);
	}

	public boolean isCastled() {
		return this.castle;
	}

	@Override
	public void loadImage() {
		try {
			this.setImage(ImageIO.read(new File("06_classic2\\w-king2.png")), 0);
			this.setImage(ImageIO.read(new File("06_classic2\\b-king2.png")), 1);
		} catch (IOException e) {
			System.out.println("error loading sprite");
		}
	}

	@Override
	public Set<Spot> validMoves(Board b) {
		Spot[][] board = b.getBoard();
		int[] rowIncrements= {-1, 1, 0, 0, 1, 1, -1, -1};
		int[] colIncrements= {0, 0 , -1, 1, 1, -1, 1, -1};
		int row= getRow(); int col= getCol();

		Set<Spot> validMoves= getMoveList();
		validMoves.clear();
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

		canCastle(b);
		
		return super.getMoveList();

	}

	private void canCastle(Board b) {
		Spot[][] board = b.getBoard();
		if(isWhite()) {
			if(!getMoved() && board[7][7].getPiece()!=null && !board[7][7].getPiece().getMoved()) {
				if(board[7][5].getPiece()==null && !b.isThreatenedSpot(true, board[7][5]) && board[7][6].getPiece()==null && !b.isThreatenedSpot(true, board[7][6])) {
					getMoveList().add(board[7][6]);
				}
			}
			if(!getMoved() && board[7][0].getPiece()!=null && !board[7][0].getPiece().getMoved()) {
				if(board[7][1].getPiece()==null && !b.isThreatenedSpot(true, board[7][1]) && board[7][2].getPiece()==null && !b.isThreatenedSpot(true, board[7][2])
						&& board[7][3].getPiece()==null && !b.isThreatenedSpot(true, board[7][3])) {
					getMoveList().add(board[7][2]);
				}
			}
		}else {
			if(!getMoved() && board[0][7].getPiece()!=null && !board[0][7].getPiece().getMoved()) {
				if(board[0][5].getPiece()==null && !b.isThreatenedSpot(false, board[0][5]) && board[0][6].getPiece()==null && !b.isThreatenedSpot(false, board[0][6])) {
					getMoveList().add(board[0][6]);
				}
			}
			if(!getMoved() && board[0][0].getPiece()!=null && !board[0][0].getPiece().getMoved()) {
				if(board[0][1].getPiece()==null && !b.isThreatenedSpot(false, board[0][1]) && board[0][2].getPiece()==null && !b.isThreatenedSpot(false, board[0][2])
						&& board[0][3].getPiece()==null && !b.isThreatenedSpot(false, board[0][3])) {
					getMoveList().add(board[0][2]);
				}
			}
		}
	}

}
