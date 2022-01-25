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
	private int[][] arrCenterManhattanDistance = {
			{6, 5, 4, 3, 3, 4, 5, 6},
			{5, 4, 3, 2, 2, 3, 4, 5},
			{4, 3, 2, 1, 1, 2, 3, 4},
			{3, 2, 1, 0, 0, 1, 2, 3},
			{3, 2, 1, 0, 0, 1, 2, 3},
			{4, 3, 2, 1, 1, 2, 3, 4},
			{5, 4, 3, 2, 2, 3, 4, 5},
			{6, 5, 4, 3, 3, 4, 5, 6}};


	private final int CHECKMATE_BONUS = 10000;
	private final int DEPTH_BONUS = 100;
	private final int CASTLE_BONUS = 60;
	private final int MOBILITY_BONUS = 2;
	private final int PASSEDPAWN_BONUS = 10;
	private final int KINGPOSITIONAL_BONUS = 30;

	public Board(boolean white) {
		this.white = white;
		this.create();
	}

	private void create() {
		Color whiteSquare = new Color(238, 232, 170), blackSquare = new Color(139, 69, 19);
		board = new Spot[8][8];
		String tempId;
		int whiteRow, blackRow;
		int kingCol, queenCol;

		for(int i=0; i<8; i++) {
			for(int j=0; j<8; j++) {
				if (white) {
					tempId = (char) ('a' + j) + ((8 - i) + "");
				} else {
					tempId = (char) ('a'+ (7 - j)) + ((i + 1) +"");
				}
				if((i+j)%2==0) {
					board[i][j] = new Spot(i, j, tempId, null, whiteSquare, LENGTH);
				}else {
					board[i][j] = new Spot(i, j, tempId, null, blackSquare, LENGTH);
				}
			}
		}

		if (white) {
			whiteRow = 7;
			blackRow = 0;
			kingCol = 4;
			queenCol = 3;

		} else {
			whiteRow = 0;
			blackRow = 7;
			kingCol = 3;
			queenCol = 4;

		}


		whiteKing = new King(true, 20000, 'K', whiteRow, kingCol);
		board[0][0].addPiece(whiteKing);

		board[0][4].addPiece(new Rook(true, 500, 'R', whiteRow, 7));


		blackKing = new King(false, 20000, 'K', blackRow, kingCol);
		board[2][3].addPiece(blackKing);


		//		whiteKing = new King(true, 200000, 'K', 0, 0);
		//		board[3][6].addPiece(whiteKing);
		//		blackKing = new King(false, 20000, 'K', 0, 0);
		//		board[4][5].addPiece(blackKing);
		//		board[0][0].addPiece(new Rook(true, 900, 'Q', 0, 0));
		//		board[7][0].addPiece(new Rook(true, 900, 'Q', 0, 0));
		getPseudoLegal();

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
			Piece newPiece= s.removePiece();
			s.addPiece(curPiece);
			if(kingInCheck(w)) itr.remove();
			//undo
			s.addPiece(newPiece);
			originalS.addPiece(curPiece);
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
		//pawn captures for both povs
		boolean pawnKill = whiteThreatened;
		if(!white) {
			pawnKill = !whiteThreatened;
		}
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
		}else if(isCheckmateOrStalemate(false)==2) {
			return evaluateBlack(depth);
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
		}else if(isCheckmateOrStalemate(true)==2) {
			return evaluateWhite(depth);
		}
		return cnt;
	}

	private int positionEval(Piece piece, int i, int j) {
		int cnt = 0;
		//forward pieces
		if(piece.isWhite()==white) {
			if(piece instanceof Pawn) {
				cnt+=pEval[i][j];
				if (inEndGame()) {
					cnt += passedPawn(piece);
				}
			}else if(piece instanceof Knight) {
				cnt+=nEval[i][j];
			}else if(piece instanceof Bishop) {
				cnt+=bEval[i][j];
			}else if(piece instanceof Rook) {
				cnt+=rEval[i][j];

			}else if(piece instanceof Queen) {
				cnt+=qEval[i][j];
			}else if (piece instanceof King) {
				if(inEndGame()) {
					if (kingPawnEndgame(piece)) {
						cnt += kingPositionKPEnding(piece);
					}
					cnt+=kEvalEnd[i][j];
				}else {
					cnt+=kEvalMid[i][j];
				}
			}
			//backward pieces
		}else {
			if(piece instanceof Pawn) {
				cnt+=flipEval(pEval)[i][j];
				if (inEndGame()) {
					cnt += passedPawn(piece);
				}
			}else if(piece instanceof Knight) {
				cnt+=flipEval(nEval)[i][j];
			}else if(piece instanceof Bishop) {
				cnt+=flipEval(bEval)[i][j];
			}else if(piece instanceof Rook) {
				if(inEndGame()){
					if(inRKEndgame(!white)) {
						if (white) {
							System.out.println(2.2);
							cnt += kingRookPositionEval(blackKing, whiteKing);
						} else {
							System.out.println(2.3);
							cnt += kingRookPositionEval(whiteKing, blackKing);
						}
					}
				}
				else{
					cnt+=flipEval(rEval)[i][j];
				}

			}else if(piece instanceof Queen) {
				cnt+=flipEval(qEval)[i][j];
			}else if (piece instanceof King) {
				if(inEndGame()) {
					if (kingPawnEndgame(piece)) {
						cnt += kingPositionKPEnding(piece);
					}
					cnt+=flipEval(kEvalEnd)[i][j];
				}else {
					cnt+=flipEval(kEvalMid)[i][j];
				}
			}
		}
		return cnt;
	}

	private int passedPawn(Piece pawn) {
		int passed = 0;
		for (int i = 0; i < board.length; i++) {
			if (board[i][pawn.getCol()].getPiece() != null) {
				if (board[i][pawn.getCol()].getPiece() instanceof Pawn && board[i][pawn.getCol()].getPiece().isWhite() != pawn.isWhite()) {
					return passed;
				}
			}
		}
		if (pawn.isWhite()) {
			for (int i = board.length - pawn.getRow(); i < board.length; i++) {
				if(pawn.getCol()+1<=7) {
					if (board[i][pawn.getCol()+1].getPiece() != null) {
						if (board[i][pawn.getCol()+1].getPiece() instanceof Pawn && board[i][pawn.getCol()+1].getPiece().isWhite() != pawn.isWhite()) {
							return passed;
						}
					}
				}
				if(pawn.getCol()-1>=0) {
					if (board[i][pawn.getCol()-1].getPiece() != null) {
						if (board[i][pawn.getCol()-1].getPiece() instanceof Pawn && board[i][pawn.getCol()-1].getPiece().isWhite() != pawn.isWhite()) {
							return passed;
						}
					}
				}
			}
		} else {
			for (int i = board.length - pawn.getRow(); i > board.length; i--) {
				if(pawn.getCol()+1<=7) {
					if (board[i][pawn.getCol()+1].getPiece() != null) {
						if (board[i][pawn.getCol()+1].getPiece() instanceof Pawn && board[i][pawn.getCol()+1].getPiece().isWhite() != pawn.isWhite()) {
							return passed;
						}
					}
				}
				if(pawn.getCol()-1>=0) {
					if (board[i][pawn.getCol()-1].getPiece() != null) {
						if (board[i][pawn.getCol()-1].getPiece() instanceof Pawn && board[i][pawn.getCol()-1].getPiece().isWhite() != pawn.isWhite()) {
							return passed;
						}
					}
				}
			}
		}
		if (pawn.isWhite()==white) {
			passed += 2 * (7-pawn.getRow()+1);
		} else {
			passed += 2 * (pawn.getRow()+1);
		}
		return passed;
	}

	private boolean inEndGame() {
		int qcount = 0, minorPieceCount = 0;
		for(int i=0; i<8; i++) {
			for(int j=0; j<8; j++) {
				Piece piece = board[i][j].getPiece();
				if(piece!=null && piece instanceof Queen) {
					qcount++;
				}else if(piece!=null && (piece instanceof Knight || piece instanceof Bishop || piece instanceof Rook)) {
					minorPieceCount++;
				}
			}
		}
		return (qcount==0 && minorPieceCount < 3) || (qcount == 1 && minorPieceCount<2);
	}

	private int kingRookPositionEval(King winningKing, King losingKing){
		int lR= losingKing.getRow(); int lC= losingKing.getCol();
		int wR= winningKing.getRow(); int wC= winningKing.getCol();

		int CMD= arrCenterManhattanDistance[lR][lC];
		int MD= Math.abs(wR- lR)+ Math.abs(wC- lC);

		return (int)(4.7 * CMD + 1.6 * (14 - MD));

	}



	private boolean kingPawnEndgame(Piece currentP) {
		for(int i=0; i<8; i++) {
			for(int j=0; j<8; j++) {
				Piece piece = board[i][j].getPiece();
				if(piece != null) {
					if (currentP.isWhite() == piece.isWhite()) {
						if (piece instanceof Knight || piece instanceof Bishop || piece instanceof Rook || piece instanceof Queen) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}

	private int kingPositionKPEnding(Piece king) {
		int positioned = 0;
		if (king.isWhite()) { 
			if (isRCValid(king.getRow()-2, king.getCol())) {
				if (board[king.getRow() - 2][king.getCol()].getPiece() instanceof Pawn) {
					if (passedPawn(board[king.getRow() - 2][king.getCol()].getPiece()) > 30) {
						positioned += KINGPOSITIONAL_BONUS;
					}
				}
			}
			if (isRCValid(king.getRow()-2, king.getCol()-1)) {
				if (board[king.getRow() - 2][king.getCol()-1].getPiece() instanceof Pawn) {
					if (passedPawn(board[king.getRow() - 2][king.getCol()-1].getPiece()) > 30) {
						positioned += KINGPOSITIONAL_BONUS;
					}
				}
			}
			if (isRCValid(king.getRow()-2, king.getCol()+1)) {
				if (board[king.getRow() - 2][king.getCol()+1].getPiece() instanceof Pawn) {
					if (passedPawn(board[king.getRow() - 2][king.getCol()+1].getPiece()) > 30) {
						positioned += KINGPOSITIONAL_BONUS;
					}
				}

			}
			if (isRCValid(king.getRow()-1, king.getCol())) {
				if (board[king.getRow() - 1][king.getCol()].getPiece() instanceof Pawn) {
					if (passedPawn(board[king.getRow() - 1][king.getCol()].getPiece()) > 30) {
						positioned += KINGPOSITIONAL_BONUS;
					}
				}
			}
		} else {
			if (isRCValid(king.getRow()+2, king.getCol())) {
				if (board[king.getRow() + 2][king.getCol()].getPiece() instanceof Pawn) {
					if (passedPawn (board[king.getRow() + 2][king.getCol()].getPiece()) > 30) {
						positioned += KINGPOSITIONAL_BONUS;
					}
				}
			}
			if (isRCValid(king.getRow()+2, king.getCol()-1)) {
				if (board[king.getRow() + 2][king.getCol()-1].getPiece() instanceof Pawn){
					if (passedPawn (board[king.getRow() + 2][king.getCol()-1].getPiece()) > 30) {
						positioned += KINGPOSITIONAL_BONUS;
					}
				}
			}
			if (isRCValid(king.getRow()-2, king.getCol()+1)) {
				if (board[king.getRow() - 2][king.getCol()+1].getPiece() instanceof Pawn) {
					if (passedPawn (board[king.getRow() + 2][king.getCol()+1].getPiece()) > 30) {
						positioned += KINGPOSITIONAL_BONUS;
					}
				}
			}
			if (isRCValid(king.getRow()+1, king.getCol())) {
				if (board[king.getRow() +1][king.getCol()].getPiece() instanceof Pawn) {
					if (passedPawn (board[king.getRow() + 1][king.getCol()].getPiece()) > 30) {
						positioned += KINGPOSITIONAL_BONUS;
					}
				}
			}
		}
		return positioned;
	}

	public boolean inKQEndGame(boolean white) {
		int qCount = 0;
		for(int i=0; i<8; i++) {
			for(int j=0; j<8; j++) {
				Piece p = board[i][j].getPiece();
				if(p!=null && p.isWhite()==white) {
					if(p instanceof Queen) {
						qCount++;
					}else if(!(p instanceof King)) {
						return false;
					}
				}else if(p!=null && !(p instanceof King)) {
					return false;
				}
			}
		}
		if(qCount==1 ) {
			return true;
		}
		return false;
	}

	private boolean inRKEndgame(boolean white) {

		int rCount = 0;
		for(int i=0; i<8; i++) {
			for(int j=0; j<8; j++) {
				Piece p = board[i][j].getPiece();
				if(p!=null && p.isWhite()==white) {
					if(p instanceof Rook) {
						rCount++;
					}else if(!(p instanceof King)) {
						return false;
					}
				}else if(p!=null && !(p instanceof King)) {
					return false;
				}
			}
		}
		if(rCount==1 ) {
			return true;
		}
		return false;
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