package chessproject;

import java.util.ArrayList;

public class Game {
	private ArrayList<Move> pastMoves;
	private Player players[] = new Player[2];
	//private CheckStatus checkStatus;
	private boolean gameOver;
	private Player turn;
	private Board board;
	private int moveRule = 0;

	public Game(Player p1, Player p2) {
		players[0] = p1;
		players[1] = p2;
		board = new Board();
		pastMoves = new ArrayList<Move>();
		if(p1.isWhite()) {
			this.turn = p1;
		}else {
			this.turn = p2;
		}
	}
	
	public boolean playerMove(Player player, Spot start, Spot end) {
		Move move = new Move(player, start, end);
		return makeMove(move);
	}

	private boolean makeMove(Move move) {
		Piece sourcePiece = move.getStart().getPiece();
		Player player = move.getPlayer();
		
		if(player!=turn) {
			return false;
		}
		
		Piece destPiece = move.getEnd().getPiece();
		if(destPiece!=null) {
			move.getEnd().removePiece();
		}
		move.getStart().getPiece().displayValidMoves(false);
		move.getEnd().addPiece(sourcePiece);
		move.getStart().removePiece();
		sourcePiece.setMoved(true);
		sourcePiece.setRow(move.getEnd().getRow()); sourcePiece.setCol(move.getEnd().getColumn());
		
		if(move.isCastlingMove()) {
			Piece movingRook;
			Spot movingTo;
			if(move.getEnd().getRow()==0) {
				if(move.getEnd().getColumn()==2) {
					movingRook = board.getBoard()[0][0].getPiece();
					board.getBoard()[0][0].removePiece();
					movingTo = board.getBoard()[0][3];
				}else {
					movingRook = board.getBoard()[0][7].getPiece();
					board.getBoard()[0][7].removePiece();
					movingTo = board.getBoard()[0][5];
				}
			}else {
				if(move.getEnd().getColumn()==2) {
					movingRook = board.getBoard()[7][0].getPiece();
					board.getBoard()[7][0].removePiece();
					movingTo = board.getBoard()[7][3];
				}else {
					movingRook = board.getBoard()[7][7].getPiece();
					board.getBoard()[7][7].removePiece();
					movingTo = board.getBoard()[7][5];
				}
			}
			movingTo.addPiece(movingRook);
			movingRook.setMoved(true);
			movingRook.setRow(movingTo.getRow()); movingRook.setCol(movingTo.getColumn());
		}else if(move.isEnPassantMove()) {
			Spot above = board.getBoard()[move.getEnd().getRow()-1][move.getEnd().getColumn()];
			if(above.getPiece() instanceof Pawn && ((Pawn)above.getPiece()).getEnPassant()) {
				above.removePiece();
			}else {
				board.getBoard()[move.getEnd().getRow()+1][move.getEnd().getColumn()].removePiece();
			}
		}
		
		
		pastMoves.add(move);
		board.getPseudoLegal(!player.isWhite());
		
		if(this.turn==players[0]) {
			this.turn = players[1];
		}else {
			this.turn = players[0];
		}
		
		if (board.kingInCheck(player.isWhite()) == true) {
			this.moveRule = 0;
		} else {
			this.moveRule++;
		}
		if (moveRule == 100) {
		      //gameState is changed to DRAW;
		}
		
		if (board.isCheckmateOrStalemate(player.isWhite()) == 1) {
			//gameState is changed to CHECKMATE
		} else if (board.isCheckmateOrStalemate(player.isWhite()) == 2) {
			//gameState is changed to DRAW
		}
		
		printMoveList();
		
		return true;
		
	}
	
	public void printMoveList() {
		for(int i=0; i<pastMoves.size(); i++) {
			System.out.println(pastMoves.get(i).toString());
		}
	}
	
	public Board getBoard() {
		return this.board;
	}
	
}
