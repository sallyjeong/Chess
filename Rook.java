package chessproject;

// imports
import java.io.File;
import java.io.IOException;
import java.util.Set;
import javax.imageio.ImageIO;

/** [Rook.java]
 * Represents each Rook on the gameBoard
 * @author
 * @version 1.0 Jan 25, 2022
 */
public class Rook extends Piece {

	/**
	 * Rook
	 * This constructor creates a Rook by passing the necessary variables into the super class
	 * Sets moved and castled to false to allow castling
	 * @param white is true when the Rook is white
	 * @param points is the number of points gained when capturing the Rook (used for AI)
	 * @param symbol is the character that represents the Rook (used for displaying Move)
	 * @param row is an int representing the row the piece is in a Spot[row][col] 2d array
	 * @param col is an int representing the column the piece is in a Spot[row][col] 2d array
	 */
	public Rook(boolean white, int points, char symbol, int row, int col) {
		super(white, points, symbol, row, col);
	}

	/**
	 * loadImage
	 * Loads the sprite for the white or black Rook
	 */
	@Override
	public void loadImage() {
		try {
			this.setImage(ImageIO.read(new File("06_classic2/w-rook2.png")), 0);
			this.setImage(ImageIO.read(new File("06_classic2/b-rook2.png")), 1);
		} catch (IOException e) {
			System.out.println("error loading sprite");
		}
		
	}

	/**
	 * validMoves
	 * Calculates the legal moves for the Rook
	 * @param board is the in-game Board with the rest of the Pieces
	 * @return a Set of Spots that the Rook can move to
	 */
	@Override
	public Set<Spot> validMoves(Board board) {
		Spot[][] gameBoard = board.getBoard();
		this.getMoveList().clear();
		checkRow(gameBoard);
		checkCol(gameBoard);

		return super.getMoveList();
	}
	
}
