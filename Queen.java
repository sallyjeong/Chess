package chessproject;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import javax.imageio.ImageIO;

public class Queen extends Piece {

	public Queen(boolean w, boolean m, int p, int s, int r, int c) {
		super(w, m, p, s, r, c);
	}

	@Override
	public void loadImage() {
		try {
			this.setImage(ImageIO.read(new File("files\\06_classic2\\w-queen2.png")), 0);
			this.setImage(ImageIO.read(new File("files\\06_classic2\\b-queen2.png")), 1);
		} catch (IOException e) {
			System.out.println("error loading sprite");
		}
	}

	@Override
	public Set<Spot> validMoves(Board b) {
		Spot[][] board = b.getBoard();
		this.getMoveList().clear();
		int row= getRow(); int col= getCol();
		checkRow(row, col, board);
		checkCol(row, col, board);
		checkDiags(row, col, board);

		return super.getMoveList();
	}

	
}
