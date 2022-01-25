package chessproject;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;

public class Board implements Drawable {
	final int LENGTH = 65;

	private Spot[][] board;
	private King whiteKing, blackKing;
	private boolean white;
	private Client player;

	private int qEval[][] = {{-20,-10,-10, -5, -5,-10,-10,-20},
			{-10,  0,  0,  0,  0,  0,  0,-10},
			{-10,  0,  5,  5,  5,  5,  0,-10},
			{-5,  0,  5,  5,  5,  5,  0, -5},
			{0,  0,  5,  5,  5,  5,  0, -5},
			{-10,  5,  5,  5,  5,  5,  0,-10},
			{-10,  0,  5,  0,  0,  0,  0,-10},
			{-20,-10,-10, -5, -5,-10,-10,-20}};
	private int kEvalMid[][] = {{-30,-40,-40,-50,-50,-40,-40,-30},
			{-30,-40,-40,-50,-50,-40,-40,-30},
			{-30,-40,-40,-50,-50,-40,-40,-30},
			{-30,-40,-40,-50,-50,-40,-40,-30},
			{-20,-30,-30,-40,-40,-30,-30,-20},
			{-10,-20,-20,-20,-20,-20,-20,-10},
			{20, 20,  0,  0,  0,  0, 20, 20},
			{20, 30, 10,  0,  0, 10, 30, 20}};
	private int kEvalEnd[][] = {{-50,-40,-30,-20,-20,-30,-40,-50},
			{-30,-20,-10,  0,  0,-10,-20,-30},
			{-30,-10, 20, 30, 30, 20,-10,-30},
			{-30,-10, 30, 40, 40, 30,-10,-30},
			{-30,-10, 30, 40, 40, 30,-10,-30},
			{-30,-10, 20, 30, 30, 20,-10,-30},
			{-30,-30,  0,  0,  0,  0,-30,-30},
			{-50,-30,-30,-30,-30,-30,-30,-50}};
	private int nEval[][] = {{-50,-40,-30,-30,-30,-30,-40,-50},
			{-40,-20,  0,  0,  0,  0,-20,-40},
			{-30,  0, 10, 15, 15, 10,  0,-30},
			{-30,  5, 15, 20, 20, 15,  5,-30},
			{-30,  0, 15, 20, 20, 15,  0,-30},
			{-30,  5, 10, 15, 15, 10,  5,-30},
			{-40,-20,  0,  5,  5,  0,-20,-40},
			{-50,-30,-30,-30,-30,-30,-30,-50}};
	private int pEval[][] = {{0,  0,  0,  0,  0,  0,  0,  0},
			{50, 50, 50, 50, 50, 50, 50, 50},
			{10, 10, 20, 30, 30, 20, 10, 10},
			{5,  5, 10, 25, 25, 10,  5,  5},
			{0,  0,  0, 20, 20,  0,  0,  0},
			{5, -5,-10,  0,  0,-10, -5,  5},
			{5, 10, 10,-20,-20, 10, 10,  5},
			{0,  0,  0,  0,  0,  0,  0,  0}};
	private int bEval[][] = {{-20,-10,-10,-10,-10,-10,-10,-20},
			{-10,  0,  0,  0,  0,  0,  0,-10},
			{-10,  0,  5, 10, 10,  5,  0,-10},
			{-10,  5,  5, 10, 10,  5,  5,-10},
			{-10,  0, 10, 10, 10, 10,  0,-10},
			{-10, 10, 10, 10, 10, 10, 10,-10},
			{-10,  5,  0,  0,  0,  0,  5,-10},
			{-20,-10,-10,-10,-10,-10,-10,-20}};
	private int rEval[][] = {{0,  0,  0,  0,  0,  0,  0,  0},
			{5, 10, 10, 10, 10, 10, 10,  5},
			{-5,  0,  0,  0,  0,  0,  0, -5},
			{-5,  0,  0,  0,  0,  0,  0, -5},
			{-5,  0,  0,  0,  0,  0,  0, -5},
			{-5,  0,  0,  0,  0,  0,  0, -5},
			{-5,  0,  0,  0,  0,  0,  0, -5},
			{0,  0,  0,  5,  5,  0,  0,  0}};
	private final int CHECKMATE_BONUS = 10000;
	private final int DEPTH_BONUS = 100;
	private final int CASTLE_BONUS = 60;
	private final int MOBILITY_BONUS = 2;

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

		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
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
				for (int i = 0; i < board.length; i++) {
					// white pawns
					board[whiteRow - 1][i].addPiece(new Pawn(true,  1, '\u0000', whiteRow - 1, i, white));
					// black pawns
					board[blackRow + 1][i].addPiece(new Pawn(false, 1, '\u0000', blackRow + 1, i, !white));
				}
			} else {
				whiteRow = 0;
				blackRow = 7;
				kingCol = 3;
				queenCol = 4;
				for (int i = 0; i < board.length; i++) {
					// white pawns
					board[whiteRow + 1][i].addPiece(new Pawn(true, 1, '\u0000', whiteRow + 1, i, white));
					// black pawns
					board[blackRow - 1][i].addPiece(new Pawn(false, 1, '\u0000', blackRow - 1, i, !white));
				}
			}

			// white pieces
			board[whiteRow][0].addPiece(new Rook(true, 5, 'R', whiteRow, 0));
			board[whiteRow][1].addPiece(new Knight(true, 3, 'N', whiteRow, 1));
			board[whiteRow][2].addPiece(new Bishop(true, 3, 'B', whiteRow, 2));
			board[whiteRow][queenCol].addPiece(new Queen(true, 9, 'Q', whiteRow, queenCol));
			whiteKing = new King(true, 1000, 'K', whiteRow, kingCol);
			board[whiteRow][kingCol].addPiece(whiteKing);
			board[whiteRow][5].addPiece(new Bishop(true, 3, 'B', whiteRow, 5));
			board[whiteRow][6].addPiece(new Knight(true, 3, 'N', whiteRow, 6));
			board[whiteRow][7].addPiece(new Rook(true, 5, 'R', whiteRow, 7));

			// black pieces
			board[blackRow][0].addPiece(new Rook(false, 5, 'R', blackRow, 0));
			board[blackRow][1].addPiece(new Knight(false, 3, 'N', blackRow, 1));
			board[blackRow][2].addPiece(new Bishop(false, 3, 'B', blackRow, 2));
			board[blackRow][queenCol].addPiece(new Queen(false, 9, 'Q', blackRow, queenCol));
			blackKing = new King(false, 1000, 'K', blackRow, kingCol);
			board[blackRow][kingCol].addPiece(blackKing);
			board[blackRow][5].addPiece(new Bishop(false, 3, 'B', blackRow, 5));
			board[blackRow][6].addPiece(new Knight(false, 3, 'N', blackRow, 6));
			board[blackRow][7].addPiece(new Rook(false, 5, 'R', blackRow, 7));

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

	public Set<Move> getCompleteMoveSet(boolean w) {
		Set<Move> completeSet = new HashSet<Move>();
		for(int i=0; i<8; i++) {
			for(int j=0; j<8; j++) {
				if(board[i][j].getPiece()!=null && board[i][j].getPiece().isWhite()== w) {
					Set<Spot> s = board[i][j].getPiece().getMoveList();
					for(Spot spot: s) {
						Move m = new Move(board[i][j], spot);
						completeSet.add(m);
					}
				}
			}
		}
		return completeSet;
	}

	public boolean isInsufficientMat() { //method doesn't check if the bishops are on the same colour squares or not: 2 bishops of same colored diagonals cannot checkmate
		int wKnights = 0, wBishops = 0, bBishops = 0, bKnights = 0;
		for(int i=0; i<8; i++) {
			for(int j=0; j<8; j++) {
				Piece p = board[i][j].getPiece();
				if(p!=null) {
					if(p instanceof Knight) {
						if(p.isWhite()) {
							wKnights++;
						}else {
							bKnights++;
						}
					} else if(p instanceof Bishop) {
						if(p.isWhite()) {
							wBishops++;
						}else {
							bBishops++;
						}
					} else if(!(p instanceof King)) {
						return false;
					}
				}
			}
		}
		if((bKnights<=2 && bBishops == 0) || (wKnights <= 2 && wBishops ==0)) {
			return true;
		}
		return false;
	}

	/*0 is not stalemate or checkmate, 1 is checkmate, 2 is stalemate.
	 * Function is created as such because checkmate and stalemate checks are the same.
	 */
	public int isCheckmateOrStalemate(boolean w) {
		for(int i=0; i<8; i++) {
			for(int j=0; j<8; j++) {
				Piece piece = board[i][j].getPiece();
				if(piece!=null && piece.isWhite()==w && !piece.getMoveList().isEmpty()) {
					return 0;
				}
			}
		}
		if (kingInCheck(w)) {
			return 1;
		} else {
			return 2;
		}
	}

	public boolean isGameOver() {
		return this.isCheckmateOrStalemate(true)>0 || this.isCheckmateOrStalemate(false)>0;
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

	public int evaluateWhite(int depth) {
		int cnt = 0;
		for(int i=0; i<8; i++) {
			for(int j=0; j<8; j++) {
				Piece piece = board[i][j].getPiece();
				if(piece!=null && piece.isWhite()) {
					cnt+=piece.getPoints();
					cnt+=piece.getMoveList().size()*MOBILITY_BONUS;
					cnt+=positionEval(piece, i, j);
				}
			}
		}
		if(whiteKing.isCastled()) {
			cnt+=CASTLE_BONUS;
		}
		if(isCheckmateOrStalemate(false)==1) {
			cnt+=CHECKMATE_BONUS + depth*DEPTH_BONUS;
		}
		return cnt;
	}

	public int evaluateBlack(int depth) {
		int cnt = 0;
		for(int i=0; i<8; i++) {
			for(int j=0; j<8; j++) {
				Piece piece = board[i][j].getPiece();
				if(piece!=null && !piece.isWhite()) {
					cnt+=piece.getPoints();
					cnt+=piece.getMoveList().size()*MOBILITY_BONUS;
					cnt+=positionEval(piece, i, j);
				}
			}
		}
		if(blackKing.isCastled()) {
			cnt+=CASTLE_BONUS;
		}
		if(isCheckmateOrStalemate(true)==1) {
			cnt+=CHECKMATE_BONUS + depth*DEPTH_BONUS;
		}
		return cnt;
	}

	private int positionEval(Piece piece, int i, int j) {
		int cnt = 0;
		//forward pieces
		if(piece.isWhite()==white) {
			if(piece instanceof Pawn) {
				cnt+=pEval[i][j];
			}else if(piece instanceof Knight) {
				cnt+=nEval[i][j];
			}else if(piece instanceof Bishop) {
				cnt+=bEval[i][j];
			}else if(piece instanceof Rook) {
				cnt+=rEval[i][j];
			}else if(piece instanceof Queen) {
				cnt+=qEval[i][j];
			}else {
				if(inEndGame()) {
					cnt+=kEvalEnd[i][j];
				}else {
					cnt+=kEvalMid[i][j];
				}
			}
			//backward pieces
		}else {
			if(piece instanceof Pawn) {
				cnt+=flipEval(pEval)[i][j];
			}else if(piece instanceof Knight) {
				cnt+=flipEval(nEval)[i][j];
			}else if(piece instanceof Bishop) {
				cnt+=flipEval(bEval)[i][j];
			}else if(piece instanceof Rook) {
				cnt+=flipEval(rEval)[i][j];
			}else if(piece instanceof Queen) {
				cnt+=flipEval(qEval)[i][j];
			}else {
				if(inEndGame()) {
					cnt+=flipEval(kEvalEnd)[i][j];
				}else {
					cnt+=flipEval(kEvalMid)[i][j];
				}
			}
		}
		return cnt;
	}

	private boolean inEndGame() {
		int qcount = 0, minorPieceCount = 0;
		for(int i=0; i<8; i++) {
			for(int j=0; j<8; j++) {
				Piece piece = board[i][j].getPiece();
				if(piece!=null && piece instanceof Queen) {
					qcount++;
				}else if(piece!=null && (piece instanceof Knight || piece instanceof Bishop)) {
					minorPieceCount++;
				}
			}
		}
		return qcount==0 || minorPieceCount<3;
	}

	private int[][] flipEval(int eval[][]) {
		int flipped[][] = new int[8][8];
		for(int i=0; i<8; i++) {
			for(int j=0; j<8; j++) {
				flipped[i][j] = eval[7-i][j];
			}
		}
		return flipped;
	}

	public Spot[][] getBoard() {
		return this.board;
	}

	public void setWhiteKingChecked(boolean b) {
		board[whiteKing.getRow()][whiteKing.getCol()].setChecked(b);
	}

	public void setBlackKingChecked(boolean b) {
		board[blackKing.getRow()][blackKing.getCol()].setChecked(b);
	}

}
