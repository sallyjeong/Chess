package chessproject;
//imports
import java.awt.Color;
import java.awt.Graphics;
import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;
/**
 * [Board.java]
 * This class represents a chess board
 * Used to keep track of pieces, find valid moves, and evaluate positions
 * @author Peter Gao, Katherine Liu, Robert Jin, Stanley Wang
 * @version 1.0 Jan 25, 2022
 */
public class Board implements Drawable {

	final int LENGTH = 72;

	private Spot[][] board;
	//to keep track of both kings at all times
	private King whiteKing, blackKing;
	private boolean white;
	private Client player;

	//piece square table values
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
	private int[][] centerManhattanDistance = {
			{6, 5, 4, 3, 3, 4, 5, 6},
			{5, 4, 3, 2, 2, 3, 4, 5},
			{4, 3, 2, 1, 1, 2, 3, 4},
			{3, 2, 1, 0, 0, 1, 2, 3},
			{3, 2, 1, 0, 0, 1, 2, 3},
			{4, 3, 2, 1, 1, 2, 3, 4},
			{5, 4, 3, 2, 2, 3, 4, 5},
			{6, 5, 4, 3, 3, 4, 5, 6}};

	//constants for evaluation purposes
	private final int CHECKMATE_BONUS = 10000;
	private final int DEPTH_BONUS = 100;
	private final int CASTLE_BONUS = 60;
	private final int MOBILITY_BONUS = 2;
	private final int PASSEDPAWN_BONUS = 10;
	private final int KINGPOSITIONAL_BONUS = 30;

	/**
	 * constructs the board for a person v person match
	 * @param player: the current player
	 */
	public Board(Client player) {
		this.player = player;
		this.white = player.isWhite();
		this.create(false);
	}

	/**
	 * constructs the board for a person v computer match
	 * @param white: true if board is in white's pov, false if black's
	 */
	public Board(boolean white) {
		this.white = white;
		this.create(true);
	}

	/**
	 * create
	 * method called in the constructor to create the board and place pieces on their starting spots
	 * @param computerGame: true if it's a player vs computer match
	 */
	private void create(boolean computerGame) {
		Color whiteSquare = new Color(238, 232, 170), blackSquare = new Color(139, 69, 19);
		board = new Spot[Constants.BOARD_SIZE][Constants.BOARD_SIZE];
		String tempId;
		//values to be able to build the board for both povs
		int whiteRow, blackRow;
		int kingCol, queenCol;
		//create spots
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				if (white) {
					tempId = (char) ('a' + j) + ((Constants.BOARD_SIZE - i) + "");
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

		if ((!computerGame && player.getIsPlayer()) || (computerGame)) {
			//white pov pawns
			if (white) {
				whiteRow = 7;
				blackRow = 0;
				kingCol = 4;
				queenCol = 3;
				for (int i = 0; i < board.length; i++) {
					// white pawns
					board[whiteRow - 1][i].addPiece(new Pawn(true,  100, '\u0000', whiteRow - 1, i, white));
					// black pawns
					board[blackRow + 1][i].addPiece(new Pawn(false, 100, '\u0000', blackRow + 1, i, !white));
				}
				//black pov pawns
			} else {
				whiteRow = 0;
				blackRow = 7;
				kingCol = 3;
				queenCol = 4;
				for (int i = 0; i < board.length; i++) {
					// white pawns
					board[whiteRow + 1][i].addPiece(new Pawn(true, 100, '\u0000', whiteRow + 1, i, white));
					// black pawns
					board[blackRow - 1][i].addPiece(new Pawn(false, 100, '\u0000', blackRow - 1, i, !white));
				}
			}

			// white pieces
			board[whiteRow][0].addPiece(new Rook(true, 500, 'R', whiteRow, 0));
			board[whiteRow][1].addPiece(new Knight(true, 320, 'N', whiteRow, 1));
			board[whiteRow][2].addPiece(new Bishop(true, 330, 'B', whiteRow, 2));
			board[whiteRow][queenCol].addPiece(new Queen(true, 900, 'Q', whiteRow, queenCol));
			whiteKing = new King(true, 20000, 'K', whiteRow, kingCol);
			board[whiteRow][kingCol].addPiece(whiteKing);
			board[whiteRow][5].addPiece(new Bishop(true, 330, 'B', whiteRow, 5));
			board[whiteRow][6].addPiece(new Knight(true, 320, 'N', whiteRow, 6));
			board[whiteRow][7].addPiece(new Rook(true, 500, 'R', whiteRow, 7));

			// black pieces
			board[blackRow][0].addPiece(new Rook(false, 500, 'R', blackRow, 0));
			board[blackRow][1].addPiece(new Knight(false, 320, 'N', blackRow, 1));
			board[blackRow][2].addPiece(new Bishop(false, 330, 'B', blackRow, 2));
			board[blackRow][queenCol].addPiece(new Queen(false, 900, 'Q', blackRow, queenCol));
			blackKing = new King(false, 20000, 'K', blackRow, kingCol);
			board[blackRow][kingCol].addPiece(blackKing);
			board[blackRow][5].addPiece(new Bishop(false, 330, 'B', blackRow, 5));
			board[blackRow][6].addPiece(new Knight(false, 320, 'N', blackRow, 6));
			board[blackRow][7].addPiece(new Rook(false, 500, 'R', blackRow, 7));

			//for all pieces in the board, find legal moves
			getLegal();

		} else {
			// request for copying a player's current board
			player.sendData(Constants.BOARD_DATA + Constants.REQUEST);
		}
	}

	/**
	 * create
	 * method that loops through every piece on the board and updates its move set
	 */
	public void getLegal() {
		Set<Spot> validM;
		for(int i=0; i<Constants.BOARD_SIZE; i++) {
			for(int j=0; j<Constants.BOARD_SIZE; j++) {
				if(board[i][j].getPiece()!=null) {
					//get all pseudolegal moves
					validM = board[i][j].getPiece().validMoves(this);
					//filter out illegal moves
					filterPseudoLegalMoves(board[i][j].getPiece().isWhite(), validM, board[i][j]);
				}
			}
		}
	}

	/**
	 * filterPseudoLegalMoves
	 * method that filters out illegal moves by temporarily making the move, seeing if it leaves own king is in check, then unmaking it if true
	 * @param w: if the current piece is white
	 * @param validMoves: the set of moves being filtered
	 * @param originalS: the original spot of the piece
	 */
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

	/**
	 * getCompleteMoveSet
	 * method that combines the movesets of all pieces on one side
	 * @param w: true if white's moveset is being returned, false if black
	 * @return Set<Move> the complete move set
	 */
	public Set<Move> getCompleteMoveSet(boolean w) {
		Set<Move> completeSet = new HashSet<Move>();
		for(int i=0; i<Constants.BOARD_SIZE; i++) {
			for(int j=0; j<Constants.BOARD_SIZE; j++) {
				if(board[i][j].getPiece()!=null && board[i][j].getPiece().isWhite()== w) {
					Set<Spot> s = board[i][j].getPiece().getMoveList();
					for(Spot spot: s) {
						//convert from spot to move
						Move m = new Move(board[i][j], spot);
						completeSet.add(m);
					}
				}
			}
		}
		return completeSet;
	}

	/**
	 * isInsufficientMat
	 * method that detects if the game will be a draw due to insufficient material
	 * @return true if there's insufficient material
	 */
	public boolean isInsufficientMat() { //method doesn't check if the bishops are on the same colour squares or not: 2 bishops of same colored diagonals cannot checkmate
		int wKnights = 0, wBishops = 0, bBishops = 0, bKnights = 0;
		for(int i=0; i<Constants.BOARD_SIZE; i++) {
			for(int j=0; j<Constants.BOARD_SIZE; j++) {
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

	/**
	 * isCheckmateOrStalemate
	 * method that detects if one side is in checkmate or stalemate
	 * @param w: true if the white side is being checked, false if black
	 * @return 0 if not checkmate or stalemate, 1 if checkmate, 2 if stalemate
	 */
	public int isCheckmateOrStalemate(boolean w) {
		for(int i=0; i<Constants.BOARD_SIZE; i++) {
			for(int j=0; j<Constants.BOARD_SIZE; j++) {
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

	/**
	 * isGameOver
	 * method that detects if the game is over due to checkmate or stalemate
	 * @return true if checkmate or stalemate
	 */
	public boolean isGameOver() {
		return this.isCheckmateOrStalemate(true)>0 || this.isCheckmateOrStalemate(false)>0;
	}

	/**
	 * kingInCheck
	 * method that detects if a king of a certain color is in check
	 * @param w: true if the white king, false if the black king
	 * @return true if the king is in check, false if not
	 */
	public boolean kingInCheck(boolean w){
		if(w){
			return isThreatenedSpot(w, board[whiteKing.getRow()][whiteKing.getCol()]);
		}
		else{
			return isThreatenedSpot(w, board[blackKing.getRow()][blackKing.getCol()]);
		}
	}

	/**
	 * setEnPassant
	 * method that sets all pawns of one side to not be eligible for enpassant capture
	 * opponents have 1 chance to capture enpassant, if they don't do it that next turn the pawns aren't eligible anymore
	 * @param white: true if the white king, false if the black king
	 * @return true if the king is in check, false if not
	 */
	public void setEnPassant(boolean white) {
		for(int i=0; i<Constants.BOARD_SIZE; i++) {
			for(int j=0; j<Constants.BOARD_SIZE; j++) {
				if(board[i][j].getPiece() instanceof Pawn && board[i][j].getPiece().isWhite()==white) {
					((Pawn)board[i][j].getPiece()).setEnPassant(false);
				}
			}
		}
	}

	/** isThreatenedSpot()
	 * Checks if a spot is threatened
	 * @param whiteThreatened A boolean that contains which player's turn it is
	 * @param threatenedSpot A Spot object that contains the spot to be checked
	 * @return boolean returns true if the spot is threatened by any opponent's piece
	 */
	public boolean isThreatenedSpot(boolean whiteThreatened, Spot threatenedSpot) {

		int knightDirections[] = {2,1,2,-1,-2,1,-2,-1,1,2,1,-2,-1,2,-1,-2};
		//Constants.BOARD_SIZE directions, -1, -1 is northwest diag, -1, 0 north, -1, 1 northeast, 0, -1 west
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

		for(int direction=0; direction<Constants.BOARD_SIZE; direction++) {
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

		for(int direction=0; direction<Constants.BOARD_SIZE; direction++) {
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

	/* isRCValid()
	 * Checks if a Row, Column exists
	 * @param row An integer that contains the row
	 * @param col An integer that contains the column
	 * @return boolean returns true if the row, column pair exists within the chessboard
	 */
	private boolean isRCValid(int row, int col) {
		if(row<0 || row>7 || col<0 || col>7) {
			return false;
		}
		return true;
	}

	/* draw()
	 * Draws the board
	 * @param g A Graphics object
	 */
	@Override
	public void draw(Graphics g) {
		for(int i=0; i<Constants.BOARD_SIZE; i++) {
			for(int j=0; j<Constants.BOARD_SIZE; j++) {
				board[i][j].draw(g);
			}
		}
	}

	/* evaluateWhite()
	 * Evaluates the position for white pieces
	 * The value of a position depends on the value of each piece, which is variable depending on each position
	 * @param depth A integer that checks how far checkmate is
	 * @return int returns a value for the current position
	 */
	public int evaluateWhite(int depth) {
		int cnt = 0;
		for(int i=0; i<Constants.BOARD_SIZE; i++) {
			for(int j=0; j<Constants.BOARD_SIZE; j++) {
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

	/* evaluateBlack()
	 * Evaluates the position for black pieces
	 * The value of a position depends on the value of each piece, which is variable depending on each position
	 * @param depth A integer that checks how far checkmate is
	 * @return int returns a value for the current position
	 */
	public int evaluateBlack(int depth) {
		int cnt = 0;
		for(int i=0; i<Constants.BOARD_SIZE; i++) {
			for(int j=0; j<Constants.BOARD_SIZE; j++) {
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

	/* positionEval()
	 * Returns a numerical evaluation for a piece
	 * The value of a position depends on the value of each piece, which is variable depending on each position
	 * @param piece A Piece object that contains the pawn to be checked
	 * @param i An integer that contains the location the piece is in, in the 2d array (piece square tables)
	 * @param j An integer that contains the location the piece is in, in the 2d array (piece square tables)
	 * @return int returns a value for how good a piece is
	 */
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
				if(inEndGame()){
					if(inRKEndgame(white)) {
						if (white) {
							cnt += kingRookPositionEval(whiteKing, blackKing);
						} else {
							cnt += kingRookPositionEval(blackKing, whiteKing);
						}
					}
				}
				else{
					cnt+=(rEval)[i][j];
				}

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
							cnt += kingRookPositionEval(blackKing, whiteKing);
						} else {
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

	/* passedPawn()
	 * Checks if a pawn is considered a passed pawn and returns a value
	 * A Passed Pawn is defined as a pawn that has no opposing pawns in the same column as it and not in front of it in the two columns next to it
	 * @param pawn A Pawn object that contains the pawn to be checked
	 * @return int returns a value for how passed a pawn is
	 */
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
			passed += PASSEDPAWN_BONUS * (7-pawn.getRow()+1);
		} else {
			passed += PASSEDPAWN_BONUS * (pawn.getRow()+1);
		}
		return passed;
	}
	/* inEndGame()
	 * Checks if currentposition is in end game
	 * An End Game is defined as both sides not having a total of more than 4 minor pieces or queen(s) and 2 minor pieces
	 * @return boolean returns true if the position is in end game
	 */
	private boolean inEndGame() {
		int qcount = 0, minorPieceCount = 0;
		for(int i=0; i<Constants.BOARD_SIZE; i++) {
			for(int j=0; j<Constants.BOARD_SIZE; j++) {
				Piece piece = board[i][j].getPiece();
				if(piece!=null && piece instanceof Queen) {
					qcount++;
				}else if(piece!=null && (piece instanceof Knight || piece instanceof Bishop || piece instanceof Rook)) {
					minorPieceCount++;
				}
			}
		}
		return (qcount==0 && minorPieceCount < 5) || (qcount <= 2 && minorPieceCount<3);
	}

	/* kingRookPositionEval()
	 * Moves the winning king towards losing king
	 * @param winningKing A King object that contains the king that does not have a rook
	 * @param losingKing A King object that contains the king that has the rook
	 * @return int returns a value for the current position
	 */
	private int kingRookPositionEval(King winningKing, King losingKing){
		int lR= losingKing.getRow(); int lC= losingKing.getCol();
		int wR= winningKing.getRow(); int wC= winningKing.getCol();

		int CMD= centerManhattanDistance[lR][lC];
		int MD= Math.abs(wR- lR)+ Math.abs(wC- lC);

		return (int)(4.7 * CMD + 1.6 * (14 - MD));

	}

	/* kingPawnEndgame()
	 * Evaluates the position to check if it is a king and pawn endgame
	 * A King and Pawn Endgame is defined as one side having a King and pawns
	 * @param currentP A Piece object that contains a piece to be checked for its color
	 * @return boolean returns true if it is a king and pawn endgame
	 */
	private boolean kingPawnEndgame(Piece currentP) {
		for(int i=0; i<Constants.BOARD_SIZE; i++) {
			for(int j=0; j<Constants.BOARD_SIZE; j++) {
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

	/* kingPositionKPEnding()
	 * Evaluates a king's positioning in a king and pawn endgame
	 * A good King positioning in a King and Pawn Endgame is defined as being able to control key squares in front of passing pawns (2 rows in front or 1 row in front)
	 * @param king A Piece object that contains a piece to be checked
	 * @return int returns an integer value that represents how good a king's position is in a king and pawn endgame
	 */
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
					if (passedPawn (board[king.getRow() - 2][king.getCol()+1].getPiece()) > 30) {
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

	/* inKQEndgame()
	 * Checks if the position is in a queen and king endgame
	 * A King and Queen ending is defined as one side having a King and Queen and the other side only having a King
	 * @param white A boolean that contains what color is being evaluated for
	 * @return boolean returns true if the position is a queen and king endgame
	 */
	public boolean inKQEndGame(boolean white) {
		int qCount = 0;
		for(int i=0; i<Constants.BOARD_SIZE; i++) {
			for(int j=0; j<Constants.BOARD_SIZE; j++) {
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

	/* inRKEndgame()
	 * Checks if the position is in a rook and king endgame
	 * A King and Rook ending is defined as one side having a King and Rook and the other side only having a King
	 * @param white A boolean that contains what color is being evaluated for
	 * @return boolean returns true if the position is a rook and king endgame
	 */
	private boolean inRKEndgame(boolean white) {

		int rCount = 0;
		for(int i=0; i<Constants.BOARD_SIZE; i++) {
			for(int j=0; j<Constants.BOARD_SIZE; j++) {
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

	/* flipEval()
	 * Flips a 2d arraylist of integers around so as to make the the first index in the first array the last index of the second array
	 * @return int[][] A 2d array of the flipped values
	 */
	private int[][] flipEval(int eval[][]) {
		int flipped[][] = new int[Constants.BOARD_SIZE][Constants.BOARD_SIZE];
		for(int i=0; i<Constants.BOARD_SIZE; i++) {
			for(int j=0; j<Constants.BOARD_SIZE; j++) {
				flipped[i][j] = eval[7-i][j];
			}
		}
		return flipped;
	}

	/* getBoard()
	 * Returns a 2-d array of Spot objects
	 * @return Spot[][] A 2d array of Spot objects
	 */
	public Spot[][] getBoard() {
		return this.board;
	}

	/* setWhiteKingChecked()
	 * Sets the boolean in the whiteKing object
	 * @param b A Boolean value
	 */
	public void setWhiteKingChecked(boolean b) {
		board[whiteKing.getRow()][whiteKing.getCol()].setChecked(b);
	}

	/* setBlackKingChecked()
	 * Sets the boolean in the blackKing object
	 * @param b A Boolean value
	 */
	public void setBlackKingChecked(boolean b) {
		board[blackKing.getRow()][blackKing.getCol()].setChecked(b);
	}

	/* setWhiteKing()
	 * Sets the whiteKing object
	 * @param whiteKing A King object
	 */
	public void setWhiteKing(King whiteKing) {
		this.whiteKing = whiteKing;
	}

	/* setBlackKing()
	 * Sets the blackKing object
	 * @param blackKing A King object
	 */
	public void setBlackKing(King blackKing) {
		this.blackKing = blackKing;
	}

}
