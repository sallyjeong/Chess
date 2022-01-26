package chessproject;

import java.util.ArrayList;

/** [Game.java]
 * Represents the Game being played, holds player(s) and chess board
 * @author Peter Gao, Katherine Liu, Robert Jin, Stanley Wang
 * @version 1.0 Jan 25, 2022
 */
public class Game {
	private ArrayList<Move> pastMoves;
	private Board board;
	private Client player;
	private Player players[] = new Player[2];
	private GameFrame gameFrame;
	private Player turn;
	private boolean computerGame;
	private boolean whiteFirstMove = false;

	/**
	 * Game
	 * This constructor is used for users playing an online game
	 * Alters the necessary variables and creates the right board
	 * @param player is the Client that has connected to the server
	 */
	public Game(Client player) {
		this.player = player;
		gameFrame = player.getGameFrame();
		board = new Board(player);
		player.setGame(this);
		pastMoves = new ArrayList<Move>();
		computerGame = false;
	}


	/**
	 * Game
	 * This constructor is used for users playing a game against the AI
	 * Alters the necessary variables and creates the proper board
	 * @param white is true if p2 (the human player) is playing white, false for black
	 * @param p1 is the ComputerPlayer
	 * @param p2 is the human Player
	 * @param gameFrame is the GameFrame the frame the game is played on/board is displayed
	 */
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

	/**
	 * playerMove
	 * This method is used for users playing an online game
	 * @param start the origin Spot of the moved piece
	 * @param end the ending Spot of the moved piece
	 * @return true once the move was successfully made
	 */
	public boolean playerMove(Client client, Spot start, Spot end) {
		Move move = new Move(start, end);
		return makeMove(move);
	}

	/**
	 * playerMove
	 * This method is used for users playing an online game
	 * @param player the Client that is making a mode
	 * @param start the origin Spot of the moved piece
	 * @param end the ending Spot of the moved piece
	 * @return true once the move was successfully made
	 */
	public boolean playerMove(Player player, Spot start, Spot end) {
		Move move = new Move(start, end);
		if (player!=turn) {
			return false;
		}
		return makeMove(move);
	}

	/**
	 * makeMove
	 * This method is used to make the move and edit the board accordingly
	 * @return true once the move was successfully made
	 */
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

		// resetting check
		if(temp.isWhite()) {
			board.setWhiteKingChecked(false);
		}else {
			board.setBlackKingChecked(false);
		}

		// checking for promotion
		if (computerGame || (player.getIsPlayer())) {
			if(move.isPromotionMove()) {
				if(!computerGame) {
					PromotionFrame p = new PromotionFrame((Pawn)sourcePiece, board, move, gameFrame);
				}else {
					move.getEnd().addPiece(new Queen(move.getEnd().getPiece().isWhite(), 900, 'Q', 0, 0));
				}
			}
		}

		// checking castling
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
		
		// checking en passant	
		}else if(move.isEnPassantMove()) {
			Spot above = board.getBoard()[move.getEnd().getRow()-1][move.getEnd().getColumn()];
			if(above.getPiece() instanceof Pawn && ((Pawn)above.getPiece()).getEnPassant()) {
				temp.getCaptured().add(above.removePiece());
			}else {
				temp.getCaptured().add(board.getBoard()[move.getEnd().getRow()+1][move.getEnd().getColumn()].removePiece());
			}
		}

		// for online games, send move data over to users in the room 
		if (temp instanceof Client) {
			pastMoves.add(move);

			// checking check
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
			
			if(move.isCastlingMove()) {
				if(player.isWhite()) {
					player.sendData(Constants.MOVE_DATA + "w" + move.toString());
				}else {
					player.sendData(Constants.MOVE_DATA + "y" + move.toString());
				}
			}else if (!move.isPromotionMove()) {
				player.sendData(Constants.MOVE_DATA + move.toString());
			}
			player.setTurn(false);

		} else {
			//reset all pawns as not be able to be captured by enpassant
			board.setEnPassant(!turn.isWhite());
			board.getLegal();

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
				gameFrame.addMove(move.toString()); // display the move in moves
			}
		}
		return true;

	}
	
	/*
	GETTERS AND SETTERS
	 */
	public Board getBoard() {
		return this.board;
	}
	public ArrayList<Move> getPastMoves() {
		return pastMoves;
	}
}
