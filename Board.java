package chessproject;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Iterator;
import java.util.Set;

public class Board implements Drawable {

	final int LENGTH = 50;

	private Spot[][] board;
	private King whiteKing, blackKing;

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
					board[i][j] = new Spot(i, j, tempId, null, whiteSquare, LENGTH);
				}else {
					board[i][j] = new Spot(i, j, tempId, null, blackSquare, LENGTH);
				}
			}
		}

		board[0][0].addPiece(new Rook(false, false, 5, 0, 0, 0));
		board[0][1].addPiece(new Knight(false, false, 3, 0, 0, 1));
		board[0][2].addPiece(new Bishop(false, false, 3, 0, 0, 2));
		board[0][3].addPiece(new Queen(false, false, 9, 0, 0, 3));
		blackKing = new King(false, false, 1000, 0, 0, 4);
		board[0][4].addPiece(blackKing);
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
		whiteKing = new King(true, false, 1000, 0, 7, 4);
		board[7][4].addPiece(whiteKing);
		board[7][5].addPiece(new Bishop(true, false, 3, 0, 7, 5));
		board[7][6].addPiece(new Knight(true, false, 3, 0, 7, 6));
		board[7][7].addPiece(new Rook(true, false, 5, 0, 7, 7));
		
		getPseudoLegal(true);
		
	}

	public void getPseudoLegal(boolean white) {
		long startTime = System.currentTimeMillis();
		Set<Spot> validM;
		for(int i=0; i<8; i++) {
			for(int j=0; j<8; j++) {
				if(board[i][j].getPiece()!=null && board[i][j].getPiece().isWhite()==white) {
					validM = board[i][j].getPiece().validMoves(this);
					filterPseudoLegalMoves(board[i][j].getPiece().isWhite(), validM, board[i][j]);
				}
			}
		}
		long endTime = System.currentTimeMillis();
		System.out.println((endTime-startTime));
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



	public boolean kingInCheck(boolean w){
		if(w){
			return isThreatenedSpot(w, board[whiteKing.getRow()][whiteKing.getCol()]);
		}
		else{
			return isThreatenedSpot(w, board[blackKing.getRow()][blackKing.getCol()]);
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
