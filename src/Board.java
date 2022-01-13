import java.awt.Color;
import java.awt.Graphics;
import java.util.Set;

public class Board implements Drawable {
	private Spot[][] board;

	public Board() {
		this.create();
	}

	public void create() {
		Color whiteSquare = new Color(238, 232, 170), blackSquare = new Color(139, 69, 19);
		board = new Spot[8][8];
		for(int i=0; i<8; i++) {
			for(int j=0; j<8; j++) {
				String tempId = (char)('a'+j) + ((8-i)+"");
				if((i+j)%2==0) {
					board[i][j] = new Spot(i, j, tempId, null, whiteSquare);
				}else {
					board[i][j] = new Spot(i, j, tempId, null, blackSquare);
				}
			}
		}

		board[0][0].addPiece(new Rook(false, false, 5, 0, 0, 0));
		board[0][1].addPiece(new Knight(false, false, 3, 0, 0, 1));
		board[0][2].addPiece(new Bishop(false, false, 3, 0, 0, 2));
		board[0][3].addPiece(new Queen(false, false, 9, 0, 0, 3));
		board[0][4].addPiece(new King(false, false, 1000, 0, 0, 4));
		board[0][5].addPiece(new Bishop(false, false, 3, 0, 0, 5));
		board[0][6].addPiece(new Knight(false, false, 3, 0, 0, 6));
		board[0][7].addPiece(new Rook(false, false, 5, 0, 0, 7));
		for(int i=0; i<8; i++) {
			board[1][i].addPiece(new Pawn(false, false, 1, 0, 1, i));
		}
		for(int i=0; i<8; i++) {
			board[6][i].addPiece(new Pawn(true, false, 1, 0, 6, i));
		}
		board[7][0].addPiece(new Rook(true, false, 5, 0, 7, 0));
		board[7][1].addPiece(new Knight(true, false, 3, 0, 7, 1));
		board[7][2].addPiece(new Bishop(true, false, 3, 0, 7, 2));
		board[7][3].addPiece(new Queen(true, false, 9, 0, 7, 3));
		board[7][4].addPiece(new King(true, false, 1000, 0, 7, 4));
		board[7][5].addPiece(new Bishop(true, false, 3, 0, 7, 5));
		board[7][6].addPiece(new Knight(true, false, 3, 0, 7, 6));
		board[7][7].addPiece(new Rook(true, false, 5, 0, 7, 7));
		
		board[3][4].addPiece(new Queen(true, false, 9, 0, 3, 4));
		Set<Spot> validM= board[3][4].getPiece().validMoves(board);
		board[3][4].setClicked(true);

		for(Spot s: validM){
			s.setHighlight(true);
			System.out.print(s.getID() + '\t');
		}
		
	}

	@Override
	public void draw(Graphics g) {
		for(int i=0; i<8; i++) {
			for(int j=0; j<8; j++) {
				board[i][j].draw(g);
			}
		}
	}
	
	public Spot[][] getBoard() {
		return this.board;
	}
}
