package chessproject;

import java.util.ArrayList;

public class Game {
	private ArrayList<Move> pastMoves;
	private Player players[] = new Player[2];
	private boolean gameOver;
	private Player turn;
	private Board board;

	public Game(boolean white, Player p1, Player p2) {
		players[0] = p1;
		players[1] = p2;
		board = new Board(white);
		
		pastMoves = new ArrayList<Move>();
		if(p1.isWhite()) {
			this.turn = p1;
		}else {
			this.turn = p2;
		}
	}
	
	public boolean playerMove(Player player, Spot start, Spot end) {
		Move move = new Move(start, end);
		return makeMove(move);
	}

	private boolean makeMove(Move move) {
	//	System.out.println(move.getStart().getRow()+" "+move.getStart().getColumn()+" "+move.getEnd().getRow()+" "+move.getEnd().getColumn());
		Piece sourcePiece = move.getStart().getPiece();
		Player player = turn;
		
		if(sourcePiece.isWhite()!=turn.isWhite()) {
			return false;
		}
		
		Piece destPiece = move.getEnd().removePiece();
		if(destPiece!=null) {
			player.getCaptured().add(destPiece);
		}
		move.getStart().getPiece().displayValidMoves(false);
		move.getEnd().addPiece(sourcePiece);
		move.getStart().removePiece();
		sourcePiece.setMoved(true);
		
		if (sourcePiece instanceof Pawn) {
			if(((Pawn) sourcePiece).getForward()) {
				if(sourcePiece.getRow()==0) {
					System.out.println("yo");
					PromotionFrame p= new PromotionFrame();
					int choice = p.getChoice();
					if(choice == 1){
						move.getEnd().addPiece(new Queen(sourcePiece.isWhite(), true, 900, 'Q', 0, sourcePiece.getCol()));
					}
				}
			}else {
				if(sourcePiece.getRow()==7) {
					PromotionFrame p= new PromotionFrame();
					int choice = p.getChoice();
					if(choice == 1){
						move.getEnd().addPiece(new Queen(sourcePiece.isWhite(), true, 900, 'Q', 7, sourcePiece.getCol()));
					}
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
			rook.setMoved(true);
			((King)move.getEnd().getPiece()).setCastled();
		}else if(move.isEnPassantMove()) {
			Spot above = board.getBoard()[move.getEnd().getRow()-1][move.getEnd().getColumn()];
			if(above.getPiece() instanceof Pawn && ((Pawn)above.getPiece()).getEnPassant()) {
				player.getCaptured().add(above.removePiece());
			}else {
				player.getCaptured().add(board.getBoard()[move.getEnd().getRow()+1][move.getEnd().getColumn()].removePiece());
			}
		}
		
		board.setEnPassant(!player.isWhite());
		board.getPseudoLegal();
		
		if(this.turn==players[0]) {
			this.turn = players[1];
		}else {
			this.turn = players[0];
		}
	
		if(board.isCheckmateOrStalemate(turn.isWhite())==1) {
			System.out.println("Checkmate");
			move.setCheckmatingMove();
			if(turn.isWhite()) {
				new EndFrame("Black wins", "0 - 1");
			}else {
				new EndFrame("White wins", "1 - 0");
			}
			return true;
		}else if(board.isCheckmateOrStalemate(turn.isWhite())==2 || board.isInsufficientMat()) {
			System.out.println("Stalemate");
			new EndFrame("Draw", "1/2 - 1/2");
			return true;
		}else if(board.kingInCheck(turn.isWhite())) {
			move.setCheckMove();
		}
		pastMoves.add(move);
		
		if(turn instanceof ComputerPlayer) {
			Move m = ((ComputerPlayer) turn).makeMove(board);
			makeMove(m);
		}
		
		
		
		return true;
		
	}
	
	
	
	public Board getBoard() {
		return this.board;
	}


	public Player getTurn() {
		return turn;
	}
}
