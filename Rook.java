
import java.io.File;
import java.io.IOException;
import java.util.Set;

import javax.imageio.ImageIO;

public class Rook extends Piece {

	public Rook(boolean w, boolean m, int p, int s, int r, int c) {
		super(w, m, p, s ,r , c);
	}

	@Override
	public void loadImage() {
		try {
			this.setImage(ImageIO.read(new File("C:\\Users\\tosta\\IdeaProjects\\Chess\\w-rook2.png")), 0);
			this.setImage(ImageIO.read(new File("C:\\Users\\tosta\\IdeaProjects\\Chess\\b-rook2.png")), 1);
		} catch (IOException e) {
			System.out.println("error loading sprite");
		}
		
	}

	@Override
	public Set<Spot> validMoves(Spot[][] board) {
		int row= getRow(); int col= getCol();

		checkRow(row, col, board);
		checkCol(row, col, board);

		return super.getMoveList();
	}

}
