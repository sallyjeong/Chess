package chessproject;

import java.util.ArrayList;

public class Game {
	private ArrayList<Move> pastMoves;
	private Board board;
	private Client player;
	private Player players[] = new Player[2];
	private GameFrame gameFrame;
	private Player turn;
	private boolean computerGame;
	private boolean whiteFirstMove = false;

	// for playing someone online
	public Game(Client player) {
		this.player = player;
		board = new Board(player);
		player.setGame(this);
		pastMoves = new ArrayList<Move>();
		computerGame = false;
		gameFrame = player.getGameFrame();
	}

	// for playing a computer game (p1 = computer)
	public Game(boolean white, Player p1, Player p2, GameFrame gameFrame) {
		this.gameFrame = gameFrame;

		board = new Board(white);
		pastMoves = new ArrayList<Move>();
		computerGame = true;

		if (p1.isWhite()) {
			players[0] = p1;
			players[1] = p2;
			this.turn = p1;
			whiteFirstMove = true;
			Move m = ((ComputerPlayer) p1).makeMove(board, gameFrame.determineDepth());
			playerMove(p1, m.getStart(), m.getEnd());

		} else {
			players[0] = p2;
			players[1] = p1;
			this.turn = p2;
		}
	}

	public boolean playerMove(Client player, Spot start, Spot end) {
		Move move = new Move(start, end);
		return makeMove(move);
	}

	public boolean playerMove(Player player, Spot start, Spot end) {
		Move move = new Move(start, end);
		if(player!=turn) {
			return false;
		}
		return makeMove(move);
	}

	private boolean makeMove(Move move) {
		Player temp;
		if (!computerGame) {
			temp = player;
		} else {
			temp = turn;
		}

		Piece sourcePiece = move.getStart().getPiece();

		//revoke castling rights
		if(sourcePiece instanceof King) {
			((King) sourcePiece).setMoved();
		}

		Piece destPiece = move.getEnd().removePiece();
		if(destPiece!=null) {
			temp.getCaptured().add(destPiece);
		}
		move.getStart().getPiece().displayValidMoves(false);
		move.getEnd().addPiece(sourcePiece);
		move.getStart().removePiece();

		if(temp.isWhite()) {
			board.setWhiteKingChecked(false);
		}else {
			board.setBlackKingChecked(false);
		}

		if (computerGame || (player.getIsPlayer())) {
			if (sourcePiece instanceof Pawn) {
				if (((Pawn) sourcePiece).getForward()) {
					if (sourcePiece.getRow() == 0) {
						PromotionFrame p = new PromotionFrame(sourcePiece, board, move, gameFrame);
					}
				} else {
					if (sourcePiece.getRow() == 7) {
						PromotionFrame p = new PromotionFrame(sourcePiece, board, move, gameFrame);
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
			((King)move.getEnd().getPiece()).setCastled(true);
		}else if(move.isEnPassantMove()) {
			Spot above = board.getBoard()[move.getEnd().getRow()-1][move.getEnd().getColumn()];
			if(above.getPiece() instanceof Pawn && ((Pawn)above.getPiece()).getEnPassant()) {
				temp.getCaptured().add(above.removePiece());
			}else {
				temp.getCaptured().add(board.getBoard()[move.getEnd().getRow()+1][move.getEnd().getColumn()].removePiece());
			}
		}

		if (temp instanceof Client) {
			pastMoves.add(move);

			//checking
			if (board.kingInCheck(!player.isWhite())) {
				if (player.isWhite()) {
					board.setBlackKingChecked(true);
				} else {
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

		} else {
			//reset all pawns as not be able to be captured by enpassant
			board.setEnPassant(!turn.isWhite());
			board.getPseudoLegal();

			if(this.turn==players[0]) {
				this.turn = players[1];
			}else {
				this.turn = players[0];
			}

			//checkmate
			if(board.isCheckmateOrStalemate(turn.isWhite())==1) {
				move.setCheckmatingMove();
				pastMoves.add(move);
				if(turn.isWhite()) {
					new EndFrame(gameFrame, "Black wins", "0 - 1");
					new HomeFrame();
				}else {
					new EndFrame(gameFrame, "White wins", "1 - 0");
					new HomeFrame();
				}
				return true;
				//stalemate
			}else if(board.isCheckmateOrStalemate(turn.isWhite())==2 || board.isInsufficientMat()) {
				pastMoves.add(move);
				new EndFrame(gameFrame, "Draw", "1/2 - 1/2");
				new HomeFrame();
				return true;
				//checkingmove
			}else if(board.kingInCheck(turn.isWhite())) {
				if(turn.isWhite()) {
					board.setWhiteKingChecked(true);
				}else {
					board.setBlackKingChecked(true);
				}
				move.setCheckMove();
			}
			pastMoves.add(move);

			if (whiteFirstMove) {
				whiteFirstMove = false;
			} else {
				gameFrame.addMove(move.toString());
			}
		}
		return true;

	}
	public Board getBoard() {
		return this.board;
	}
	public ArrayList<Move> getPastMoves() {
		return pastMoves;
	}
}