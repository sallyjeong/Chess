package chessproject;

import java.util.ArrayList;

public class Game {
	private ArrayList<Move> pastMoves;
	private Player players[] = new Player[2];
	//private CheckStatus checkStatus;
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
		Move move = new Move(player, start, end);
		return makeMove(move);
	}

	private boolean makeMove(Move move) {
	//	System.out.println(move.getStart().getRow()+" "+move.getStart().getColumn()+" "+move.getEnd().getRow()+" "+move.getEnd().getColumn());
		Piece sourcePiece = move.getStart().getPiece();
		Player player = move.getPlayer();
		
		if(player!=turn) {
			return false;
		}
		
		Piece destPiece = move.getEnd().getPiece();
		if(destPiece!=null) {
			player.getCaptured().add(move.getEnd().removePiece());
		}
		move.getStart().getPiece().displayValidMoves(false);
		move.getEnd().addPiece(sourcePiece);
		move.getStart().removePiece();
		sourcePiece.setMoved(true);
		sourcePiece.setRow(move.getEnd().getRow()); sourcePiece.setCol(move.getEnd().getColumn());

		if (sourcePiece instanceof Pawn) {
			boolean isWhite= sourcePiece.isWhite();
			int lastRow=0;
			if(!isWhite){
				lastRow+= 7;
			}
			if(sourcePiece.getRow() == lastRow){
				PromotionFrame p= new PromotionFrame();
				int choice= p.getChoice();
					if(choice == 1){
						move.getEnd().addPiece(new Queen(isWhite, false, 9, 'Q', sourcePiece.getRow(), sourcePiece.getCol()));
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
			rook.setRow(row); rook.setCol(col);
			System.out.println(board.getBoard()[row][col].getPiece().getRow()+ " "+board.getBoard()[row][col].getPiece().getCol());
		}else if(move.isEnPassantMove()) {
			Spot above = board.getBoard()[move.getEnd().getRow()-1][move.getEnd().getColumn()];
			if(above.getPiece() instanceof Pawn && ((Pawn)above.getPiece()).getEnPassant()) {
				player.getCaptured().add(above.removePiece());
			}else {
				player.getCaptured().add(board.getBoard()[move.getEnd().getRow()+1][move.getEnd().getColumn()].removePiece());
			}
		}
		
		
		pastMoves.add(move);
		board.setEnPassant(!player.isWhite());
		board.getPseudoLegal();
		
		if(this.turn==players[0]) {
			this.turn = players[1];
		}else {
			this.turn = players[0];
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
