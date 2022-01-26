package chessproject;

// imports
import java.io.File;
import java.io.IOException;
import java.util.Set;
import javax.imageio.ImageIO;

/** [Knight.java]
 * Represents each Knight on the board
 * @author Peter Gao, Stanley Wang
 * @version 1.0 Jan 25, 2022
 */
public class Knight extends Piece {

	/**
	 * Knight
	 * This constructor creates a Knight by passing the necessary variables into the super class
	 * Sets moved and castled to false to allow castling
	 * @param white is true when the Knight is white
	 * @param points is the number of points gained when capturing the Knight (used for AI)
	 * @param symbol is the character that represents the Knight (used for displaying Move)
	 * @param row is an int representing the row the piece is in a Spot[row][col] 2d array
	 * @param col is an int representing the column the piece is in a Spot[row][col] 2d array
	 */
	public Knight(boolean white, int points, char symbol, int row, int col) {
		super(white, points, symbol, row, col);
	}

	/**
	 * loadImage
	 * Loads the sprite for the white or black Knight
	 */
	@Override
	public void loadImage() {
		try {
			this.setImage(ImageIO.read(new File("06_classic2/w-knight2.png")), 0);
			this.setImage(ImageIO.read(new File("06_classic2/b-knight2.png")), 1);
		} catch (IOException e) {
			System.out.println("error loading sprite");
		}
	}

	/**
	 * validMoves
	 * Calculates the legal moves for the Knight
	 * @param board is the in-game Board with the rest of the Pieces
	 * @return a Set of Spots that the Knight can move to
	 */
	@Override
	public Set<Spot> validMoves(Board board) {
		Spot[][] gameBoard = board.getBoard();
		int[] rowIncrements= {-2, -1, -2, -1, 1, 2, 1, 2};
		int[] colIncrements= {-1, -2 , 1, 2, -2, -1, 2, 1};
		int row= getRow(); int col= getCol();

		Set<Spot> validMoves= getMoveList();
		validMoves.clear();
		Spot curSpot;
		for(int i=0; i< 8; i++){
			int r= row+ rowIncrements[i]; int c= col+ colIncrements[i];
			if((r>=0 && r <8) && (c >=0 && c <8)){
				curSpot= gameBoard[r][c];
				if(curSpot.getPiece()== null || curSpot.getPiece().isWhite()!= isWhite()){
					validMoves.add(curSpot);
				}
			}
		}

		return super.getMoveList();
	}

}
