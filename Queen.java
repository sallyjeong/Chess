package chessproject;

// imports
import java.io.File;
import java.io.IOException;
import java.util.Set;
import javax.imageio.ImageIO;

/** [Queen.java]
 * Represents each Queen on the gameBoard
 * @author
 * @version 1.0 Jan 25, 2022
 */
public class Queen extends Piece {

	/**
	 * Queen
	 * This constructor creates a Queen by passing the necessary variables into the super class
	 * Sets moved and castled to false to allow castling
	 * @param white is true when the Queen is white
	 * @param points is the number of points gained when capturing the Queen (used for AI)
	 * @param symbol is the character that represents the Queen (used for displaying Move)
	 * @param row is an int representing the row the piece is in a Spot[row][col] 2d array
	 * @param col is an int representing the column the piece is in a Spot[row][col] 2d array
	 */
	public Queen(boolean white, int points, char symbol, int row, int col) {
		super(white, points, symbol, row, col);
	}

	/**
	 * loadImage
	 * Loads the sprite for the white or black Queen
	 */
	@Override
	public void loadImage() {
		try {
			this.setImage(ImageIO.read(new File("06_classic2/w-queen2.png")), 0);
			this.setImage(ImageIO.read(new File("06_classic2/b-queen2.png")), 1);
		} catch (IOException e) {
			System.out.println("error loading sprite");
		}
	}

	/**
	 * validMoves
	 * Calculates the legal moves for the Queen
	 * @param board is the in-game Board with the rest of the Pieces
	 * @return a Set of Spots that the Queen can move to
	 */
	@Override
	public Set<Spot> validMoves(Board board) {
		Spot[][] gameBoard = board.getBoard();
		this.getMoveList().clear();
		checkRow(gameBoard);
		checkCol(gameBoard);
		checkDiags(gameBoard);

		return super.getMoveList();
	}

	
}
