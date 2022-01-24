package chessproject;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;

public class Board implements Drawable {
	final int LENGTH = 72;

	private Spot[][] board;
	private King whiteKing, blackKing;
	private boolean white;
	private Client player;

	public Board(Client player) {
		this.player = player;
		this.white = player.isWhite();
		this.create();
	}

	public void create() {
		Color whiteSquare = new Color(238, 232, 170), blackSquare = new Color(139, 69, 19);
		board = new Spot[8][8];
		String tempId;
		int whiteRow, blackRow;
		int kingCol, queenCol;

		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (white) {
					tempId = (char) ('a' + j) + ((8 - i) + "");
				} else {
					tempId = (char) ('a' + (7 - j)) + ((i + 1) + "");
				}
				if ((i + j) % 2 == 0) {
					board[i][j] = new Spot(i, j, tempId, null, whiteSquare, LENGTH);
				} else {
					board[i][j] = new Spot(i, j, tempId, null, blackSquare, LENGTH);
				}
			}
		}

		if (player.getIsPlayer()) {
			if (white) {
				whiteRow = 7;
				blackRow = 0;
				kingCol = 4;
				queenCol = 3;
				for (int i = 0; i < 8; i++) {
					// white pawns
					board[whiteRow - 1][i].addPiece(new Pawn(true, false, 1, '\u0000', whiteRow - 1, i, white));
					// black pawns
					board[blackRow + 1][i].addPiece(new Pawn(false, false, 1, '\u0000', blackRow + 1, i, !white));
				}
			} else {
				whiteRow = 0;
				blackRow = 7;
				kingCol = 3;
				queenCol = 4;
				for (int i = 0; i < 8; i++) {
					// white pawns
					board[whiteRow + 1][i].addPiece(new Pawn(true, false, 1, '\u0000', whiteRow + 1, i, white));
					// black pawns
					board[blackRow - 1][i].addPiece(new Pawn(false, false, 1, '\u0000', blackRow - 1, i, !white));
				}
			}

			// white pieces
			board[whiteRow][0].addPiece(new Rook(true, false, 5, 'R', whiteRow, 0));
			board[whiteRow][1].addPiece(new Knight(true, false, 3, 'N', whiteRow, 1));
			board[whiteRow][2].addPiece(new Bishop(true, false, 3, 'B', whiteRow, 2));
			board[whiteRow][queenCol].addPiece(new Queen(true, false, 9, 'Q', whiteRow, queenCol));
			whiteKing = new King(true, false, 1000, 'K', whiteRow, kingCol);
			board[whiteRow][kingCol].addPiece(whiteKing);
			board[whiteRow][5].addPiece(new Bishop(true, false, 3, 'B', whiteRow, 5));
			board[whiteRow][6].addPiece(new Knight(true, false, 3, 'N', whiteRow, 6));
			board[whiteRow][7].addPiece(new Rook(true, false, 5, 'R', whiteRow, 7));

			// black pieces
			board[blackRow][0].addPiece(new Rook(false, false, 5, 'R', blackRow, 0));
			board[blackRow][1].addPiece(new Knight(false, false, 3, 'N', blackRow, 1));
			board[blackRow][2].addPiece(new Bishop(false, false, 3, 'B', blackRow, 2));
			board[blackRow][queenCol].addPiece(new Queen(false, false, 9, 'Q', blackRow, queenCol));
			blackKing = new King(false, false, 1000, 'K', blackRow, kingCol);
			board[blackRow][kingCol].addPiece(blackKing);
			board[blackRow][5].addPiece(new Bishop(false, false, 3, 'B', blackRow, 5));
			board[blackRow][6].addPiece(new Knight(false, false, 3, 'N', blackRow, 6));
			board[blackRow][7].addPiece(new Rook(false, false, 5, 'R', blackRow, 7));

			getPseudoLegal();

		} else {
			// request for copying a player's current board
			player.sendData(Constants.BOARD_DATA + "!request");
		}
	}

	public void getPseudoLegal() {
		Set<Spot> validM;
		for(int i=0; i<8; i++) {
			for(int j=0; j<8; j++) {
				if(board[i][j].getPiece()!=null) {
					validM = board[i][j].getPiece().validMoves(this);
					filterPseudoLegalMoves(board[i][j].getPiece().isWhite(), validM, board[i][j]);
				}
			}
		}
	}

	private void filterPseudoLegalMoves(boolean w, Set<Spot> validMoves, Spot originalS){
		Iterator<Spot> itr= validMoves.iterator();

		while(itr.hasNext()){
			Spot s= itr.next();
			Piece curPiece= originalS.removePiece();
			if(curPiece instanceof King) {
				curPiece.setRow(s.getRow()); curPiece.setCol(s.getColumn());
			}
			Piece newPiece= s.removePiece();
			s.addPiece(curPiece);
			if(kingInCheck(w)) itr.remove();
			//undo
			s.addPiece(newPiece);
			originalS.addPiece(curPiece);
			if(curPiece instanceof King) {
				curPiece.setRow(originalS.getRow()); curPiece.setCol(originalS.getColumn());
			}
		}

	}

	/*0 is not stalemate or checkmate, 1 is checkmate, 2 is stalemate.
	 * Function is created as such because checkmate and stalemate checks are the same.
	 */
	public int isCheckmateOrStalemate(boolean w) {
		Set<Spot> completeSet = new HashSet<Spot>();
		for(int i=0; i<8; i++) {
			for(int j=0; j<8; j++) {
				if(board[i][j].getPiece()!=null && board[i][j].getPiece().isWhite()== w) {
					Set<Spot> s = board[i][j].getPiece().getMoveList();
					completeSet.addAll(s);
				}
			}
		}
		if (kingInCheck(w) == true) {
			if (completeSet == null) {
				return 1;
			}
		} else {
			if (completeSet == null) {
				return 2;
			}
		}
		return 0;
	}

	public boolean kingInCheck(boolean w){
		if(w){
			return isThreatenedSpot(w, board[whiteKing.getRow()][whiteKing.getCol()]);
		}
		else{
			return isThreatenedSpot(w, board[blackKing.getRow()][blackKing.getCol()]);
		}
	}

	public void setEnPassant(boolean white) {
		for(int i=0; i<8; i++) {
			for(int j=0; j<8; j++) {
				if(board[i][j].getPiece() instanceof Pawn && board[i][j].getPiece().isWhite()==white) {
					((Pawn)board[i][j].getPiece()).setEnPassant(false);
				}
			}
		}
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