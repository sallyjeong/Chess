package chessproject;

import java.util.ArrayList;

public class Game {
	private ArrayList<Move> pastMoves;
	//private Player players[] = new Player[2];
	//private CheckStatus checkStatus;
	private boolean gameOver;
	private Player turn;
	private Board board;

	public Game(Client player) {
//		players[0] = p1;
//		players[1] = p2;
		board = new Board(player.isWhite());
		player.setBoard(board);
		pastMoves = new ArrayList<Move>();
//		if (player.isWhite()) {
//			player.setTurn(true);
//		}
	}

	public boolean playerMove(Client player, Spot start, Spot end) {
		Move move = new Move(player, start, end);
		return makeMove(move);
	}

	private boolean makeMove(Move move) {
		//	System.out.println(move.getStart().getRow()+" "+move.getStart().getColumn()+" "+move.getEnd().getRow()+" "+move.getEnd().getColumn());
		Piece sourcePiece = move.getStart().getPiece();
		Client player = move.getPlayer();

		if(player.getTurn() == false) {
			return false;
		}

		Piece destPiece = move.getEnd().getPiece();
		if(destPiece!=null) {
			move.getEnd().removePiece();
			player.getCaptured().add(destPiece);
		} else if(board.kingInCheck(player.isWhite())) {
			move.getStart().setChecked(false);
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
			rook.setRow(row);
			rook.setCol(col);
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

		Spot erase = player.getOpponentStart();
		if (erase != null) {
			erase.setLeft(false);
		}
		player.sendData(Constants.MOVE_DATA + move.toString());
		player.setTurn(false);

		//System.out.println(pastMoves);
		// for testing board.print();

		return true;

	}



	public Board getBoard() {
		return this.board;
	}


	public Player getTurn() {
		return turn;
	}
}