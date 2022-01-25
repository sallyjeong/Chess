package chessproject;

import java.util.ArrayList;

public class Game {
	private ArrayList<Move> pastMoves;
	private Board board;
	private Client player;

	public Game(Client player) {
		this.player = player;
		board = new Board(player);
		player.setGame(this);
		pastMoves = new ArrayList<Move>();
	}

	public boolean playerMove(Client player, Spot start, Spot end) {
		Move move = new Move(start, end);
		return makeMove(move);
	}

	private boolean makeMove(Move move) {
		Piece sourcePiece = move.getStart().getPiece();

		//revoke castling rights
		if(sourcePiece instanceof King) {
			((King) sourcePiece).setMoved();
		}

		Piece destPiece = move.getEnd().removePiece();
		if(destPiece!=null) {
			player.getCaptured().add(destPiece);
		}
		move.getStart().getPiece().displayValidMoves(false);
		move.getEnd().addPiece(sourcePiece);
		move.getStart().removePiece();
		if(player.isWhite()) {
			board.setWhiteKingChecked(false);
		}else {
			board.setBlackKingChecked(false);
		}

		if (sourcePiece instanceof Pawn) {
			if(((Pawn) sourcePiece).getForward()) {
				if(sourcePiece.getRow()==0) {
					System.out.println("yo");
					PromotionFrame p= new PromotionFrame(sourcePiece,board,move,player.getGameFrame());
				}
			}else {
				if(sourcePiece.getRow()==7) {
					PromotionFrame p= new PromotionFrame(sourcePiece,board,move,player.getGameFrame());
				}
			}
		}

		if(move.isCastlingMove()) {
			Spot movingRook;
			int row = move.getEnd().getPiece().getRow();
			int col;
			if(move.getEnd().getColumn()==2) {
				col = 3;
				movingRook = board.getBoard()[row][0];
			}else if(move.getEnd().getColumn()==5) {
				col = 4;
				movingRook = board.getBoard()[row][7];
			}else if(move.getEnd().getColumn()==6) {
				col = 5;
				movingRook = board.getBoard()[row][7];
			}else {
				col = 2;
				movingRook = board.getBoard()[row][0];
			}
			Piece rook = movingRook.removePiece();
			board.getBoard()[row][col].addPiece(rook);
			((King)move.getEnd().getPiece()).setCastled(true);
		}else if(move.isEnPassantMove()) {
			Spot above = board.getBoard()[move.getEnd().getRow()-1][move.getEnd().getColumn()];
			if(above.getPiece() instanceof Pawn && ((Pawn)above.getPiece()).getEnPassant()) {
				player.getCaptured().add(above.removePiece());
			}else {
				player.getCaptured().add(board.getBoard()[move.getEnd().getRow()+1][move.getEnd().getColumn()].removePiece());
			}
		}
		pastMoves.add(move);

		//checking
		if(board.kingInCheck(!player.isWhite())) {
			if(player.isWhite()) {
				board.setBlackKingChecked(true);
			}else {
				board.setWhiteKingChecked(true);
			}
			move.setCheckMove();
		}

		Spot erase = player.getOpponentStart();
		if (erase != null) {
			erase.setLeft(false);
		}

		if (!move.isPromotionMove()) {
			player.sendData(Constants.MOVE_DATA + move.toString());
		}
		player.setTurn(false);
		return true;

	}
	public Board getBoard() {
		return this.board;
	}
	public ArrayList<Move> getPastMoves() {
		return pastMoves;
	}
	// get Turn?
//	public Player getTurn() {
//		return turn;
//	}
}