package chessproject;

// imports
import java.io.File;
import java.io.IOException;
import java.util.Set;
import javax.imageio.ImageIO;

/** [King.java]
 * Represents each King on the board
 * @author
 * @version 1.0 Jan 25, 2022
 */
public class King extends Piece {
	private boolean castled;
	private boolean moved;

	/**
	 * King
	 * This constructor creates a King by passing the necessary variables into the super class
	 * Sets moved and castled to false to allow castling
	 * @param white is true when the King is white
	 * @param points is the number of points gained when capturing the King (used for AI)
	 * @param symbol is the character that represents the King (used for displaying Move)
	 * @param row is an int representing the row the piece is in a Spot[row][col] 2d array
	 * @param col is an int representing the column the piece is in a Spot[row][col] 2d array
	 */
	public King(boolean white, int points, char symbol, int row, int col) {
		super(white, points, symbol, row, col);
		this.moved = false;
		this.castled = false;
	}

	/**
	 * loadImage
	 * Loads the sprite for the white or black King
	 */
	@Override
	public void loadImage() {
		try {
			this.setImage(ImageIO.read(new File("06_classic2/w-king2.png")), 0);
			this.setImage(ImageIO.read(new File("06_classic2/b-king2.png")), 1);
		} catch (IOException e) {
			System.out.println("error loading sprite");
		}
	}

	/**
	 * validMoves
	 * Calculates the legal moves for the King
	 * @param board is the in-game Board with the rest of the Pieces
	 * @return a Set of Spots that the King can move to
	 */
	@Override
	public Set<Spot> validMoves(Board board) {
		Spot[][] gameBoard = board.getBoard();
		int[] rowIncrements= {-1, 1, 0, 0, 1, 1, -1, -1};
		int[] colIncrements= {0, 0 , -1, 1, 1, -1, 1, -1};
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

		canCastle(board);

		return super.getMoveList();

	}

	/**
	 * canCastle
	 * Adds the castling move to list of legal moves if applicable
	 * @param board is the in-game Board with the rest of the Pieces
	 */
	private void canCastle(Board board) {
		Spot[][] gameBoard = board.getBoard();
		int row = getRow();
		if(!board.kingInCheck(isWhite())) {
			if(getCol()==4) {
				if(!moved && gameBoard[row][7].getPiece()!=null && gameBoard[row][7].getPiece() instanceof Rook) {
					if(gameBoard[row][5].getPiece()==null
							&& !board.isThreatenedSpot(isWhite(), gameBoard[row][5])
							&& gameBoard[row][6].getPiece()==null
							&& !board.isThreatenedSpot(isWhite(), gameBoard[row][6])) {
						getMoveList().add(gameBoard[row][6]);
					}
				}
				if(!moved && gameBoard[row][0].getPiece()!=null && gameBoard[row][0].getPiece() instanceof Rook) {
					if(gameBoard[row][1].getPiece()==null
							&& !board.isThreatenedSpot(isWhite(), gameBoard[row][1])
							&& gameBoard[row][2].getPiece()==null
							&& !board.isThreatenedSpot(isWhite(), gameBoard[row][2])
							&& gameBoard[row][3].getPiece()==null
							&& !board.isThreatenedSpot(isWhite(), gameBoard[row][3])) {
						getMoveList().add(gameBoard[row][2]);
					}
				}
			}else if(getCol()==3) {
				if(!moved && gameBoard[row][0].getPiece()!=null && gameBoard[row][0].getPiece() instanceof Rook) {
					if(gameBoard[row][1].getPiece()==null
							&& !board.isThreatenedSpot(isWhite(), gameBoard[row][1])
							&& gameBoard[row][2].getPiece()==null
							&& !board.isThreatenedSpot(isWhite(), gameBoard[row][2])) {
						getMoveList().add(gameBoard[row][1]);
					}
				}
				if(!moved && gameBoard[row][7].getPiece()!=null && gameBoard[row][7].getPiece() instanceof Rook) {
					if(gameBoard[row][4].getPiece()==null
							&& !board.isThreatenedSpot(isWhite(), gameBoard[row][4])
							&& gameBoard[row][5].getPiece()==null
							&& !board.isThreatenedSpot(isWhite(), gameBoard[row][5])
							&& gameBoard[row][6].getPiece()==null
							&& !board.isThreatenedSpot(isWhite(), gameBoard[row][6])) {
						getMoveList().add(gameBoard[row][5]);
					}
				}
			}
		}
	}

	public void setCastled(boolean b) {
		this.castled = b;
	}

	public boolean isCastled() {
		return this.castled;
	}

	public void setMoved() {
		this.moved = true;
	}
}
