package chessproject;

//imports
import java.io.File;
import java.io.IOException;
import java.util.Set;
import javax.imageio.ImageIO;

/** [Bishop.java]
 * Represents each Bishop on the board
 * @author
 * @version 1.0 Jan 25, 2021
 */
public class Bishop extends Piece {

	/**
	 * Bishop
	 * This constructor creates a Bishop by passing the necessary variables into the super class
	 * @param white is true when the Bishop is white
	 * @param points is the number of points gained when capturing the Bishop (used for AI)
	 * @param symbol is the character that represents the Bishop (used for displaying Move)
	 * @param row is an int representing the row the piece is in a Spot[row][col] 2d array
	 * @param col is an int representing the column the piece is in a Spot[row][col] 2d array
	 */
	public Bishop(boolean white, int points, char symbol, int row, int col) {
		super(white, points, symbol, row, col);
	}

	/**
	 * loadImage
	 * Loads the sprite for the white or black Bishop
	 */
	@Override
	public void loadImage() {
		try {
			this.setImage(ImageIO.read(new File("06_classic2/w-bishop2.png")), 0);
			this.setImage(ImageIO.read(new File("06_classic2/b-bishop2.png")), 1);
		} catch (IOException e) {
			System.out.println("error loading sprite");
		}
	}

	/**
	 * validMoves
	 * Calculates the legal moves for the Bishop
	 * @param board is the in-game Board with the rest of the Pieces
	 * @return a Set of Spots that the Bishop can move to
	 */
	@Override
	public Set<Spot> validMoves(Board board) {
		Spot[][] gameBoard = board.getBoard();
		this.getMoveList().clear();
		checkDiags(gameBoard);

		return super.getMoveList();
	}

}
