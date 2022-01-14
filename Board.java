package chessproject;

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
		
				board[4][4].addPiece(new Bishop(true, false, 9, 0, 4, 4));
				Set<Spot> validM= board[4][4].getPiece().validMoves(this);
				board[4][4].setClicked(true);
				System.out.println(validM.size());
		
				for(Spot s: validM){
					s.setHighlight(true);
					System.out.print(s.getID() + '\t');
				}




		board[1][4].addPiece(new King(true, true, 1000, 0, 1, 4));
		board[4][4].addPiece(new Rook(false, true, 1000, 0, 4, 4));
		System.out.println(isThreatenedSpot(true, board[4][4]));
	}

	public boolean isThreatenedSpot(boolean whiteThreatened, Spot threatenedSpot) {

		int knightDirections[] = {2,1,2,-1,-2,1,-2,-1,1,2,1,-2,-1,2,-1,-2};
		//8 directions, -1, -1 is northwest diag, -1, 0 north, -1, 1 northeast, 0, -1 west
		int[] rowDirections = {-1, -1, -1, 0, 0, 1, 1, 1};
		int[] colDirections = {-1, 0, 1, -1, 1, -1, 0, 1};
		boolean bishopThreats[] = {true, false, true, false, false, true, false, true};
		boolean rookThreats[] = {false, true, false, true, true, false, true, false};
		boolean queenThreats[] = {true, true, true, true, true, true, true, true};
		boolean kingThreats[] = {true, true, true, true, true, true, true, true};
		boolean pawnKill = whiteThreatened;
		boolean pawnThreats[] = {pawnKill, false, pawnKill, false, false, !pawnKill, false, !pawnKill};

		int row = threatenedSpot.getRow(), column = threatenedSpot.getColumn();
		for(int i=0; i<16; i+=2) {
			int nrow = row+knightDirections[i], ncol = column+knightDirections[i+1];
			if(isRCValid(nrow, ncol)) {
				Piece piece = board[nrow][ncol].getPiece();
				if(piece!=null && whiteThreatened!=piece.isWhite()) {
					if(piece instanceof Knight) {
						return true;
					}
				}
			}
		}

		for(int direction=0; direction<8; direction++) {
			int nrow = row+rowDirections[direction], ncol = column+colDirections[direction];
			if(isRCValid(nrow, ncol)) {
				Piece piece = board[nrow][ncol].getPiece();
				if(piece!=null && whiteThreatened!=piece.isWhite()) {
					if(piece instanceof King && kingThreats[direction]) {
						return true;
					}else if(piece instanceof Pawn && pawnThreats[direction]) {
						return true;
					}
				}
			}
		}

		for(int direction=0; direction<8; direction++) {
			row = threatenedSpot.getRow(); column = threatenedSpot.getColumn();
			int rowIncrement = rowDirections[direction], columnIncrement = colDirections[direction];
			row+=rowIncrement; column+=columnIncrement;
			Piece piece = null;
			while(isRCValid(row, column) && piece==null) {
				piece = board[row][column].getPiece();
				if(piece!=null && whiteThreatened!=piece.isWhite()) {
					if(piece instanceof Bishop && bishopThreats[direction]) {
						return true;
					}else if(piece instanceof Rook && rookThreats[direction]) {
						return true;
					}else if(piece instanceof Queen && queenThreats[direction]) {
						return true;
					}
				}
				row+=rowIncrement; column+=columnIncrement;
			}
		}
		return false;
	}

	private boolean isRCValid(int row, int col) {
		if(row<0 || row>7 || col<0 || col>7) {
			return false;
		}
		return true;
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
