package chessproject;

import java.util.ArrayList;
import java.util.Set;
import java.util.Stack;

public class ComputerPlayer extends Player {
	private boolean white;
	//private ArrayList<Piece> blackPieces;
	
	public ComputerPlayer(boolean white) {
		super(white);
		//blackPieces= new ArrayList<Piece>();
	}

//	public Spot[] selectSpot(){
//		Spot[] move= new Spot[2];
//		Spot src= null;
//
//		int highestPriority= -1;
//		Spot bestSpot = null;
//		for(int i= blackPieces.size()-1; i>=0; i--){
//			Piece p= blackPieces.get(i);
//			Spot r= board.getBoard()[p.getRow()][p.getCol()];
//			if(p.isCaptured()){ //Remove pieces when they get captured by white
//				blackPieces.remove(i);
//				System.out.println("captured");
//			}
//			else{
//				Set<Spot> validMoves= p.validMoves(board);
//				for(Spot s: validMoves){
//					int priority=0;
//					if(s.getPiece() != null){
//						priority= s.getPiece().getPoints();
//					}
//					if(priority > highestPriority){
//						highestPriority= priority;
//						bestSpot= s;
//						src= r;
//					}
//				}
//			}
//		}
//
//		move[0]= src;
//		move[1]= bestSpot;
//
//		return move;
//
//	}

	public Move makeMove(Board board) {
		Move bestMove = minimax(board, 1, Integer.MIN_VALUE, Integer.MAX_VALUE, true).move;
		if(bestMove==null) {
			System.out.println("Game over");
		}
		return bestMove;
	}
	

	public int evaluate(Board board) {
		if(white) {
			return board.evaluateWhite()-board.evaluateBlack();
		}else {
			return board.evaluateBlack()-board.evaluateWhite();
		}
	}

	public MoveAndEval minimax(Board board, int depth, int alpha, int beta, boolean maximizing) {
		if(depth==0 || board.isGameOver()) {
			return new MoveAndEval(null, evaluate(board));
		}
		Set<Move> allMoves;
		if(maximizing) {
			allMoves = board.getCompleteMoveSet(white);
		}else {
			allMoves = board.getCompleteMoveSet(!white);
		}
		Move bestMove = null;
		if(maximizing) {
			int maxEval = Integer.MIN_VALUE;
			for(Move m: allMoves) {
				Piece captured = makeTempMove(m, board);
				int currentEval = minimax(board, depth-1, alpha, beta, false).eval;
				unmakeMove(m, board, captured);
				if(currentEval>maxEval) {
					maxEval = currentEval;
					bestMove = m;
				}
				alpha = Math.max(alpha, currentEval);
				if(beta<=alpha) {
					break;
				}
			}
			return new MoveAndEval(bestMove, maxEval);
		}else {
			int minEval = Integer.MAX_VALUE;
			for(Move m: allMoves) {
				Piece captured = makeTempMove(m, board);
				int currentEval = minimax(board, depth-1, alpha, beta, true).eval;
				unmakeMove(m, board, captured);
				if(currentEval<minEval) {
					minEval = currentEval;
					bestMove = m;
				}
				beta = Math.min(beta, currentEval);
				if(beta<=alpha) {
					break;
				}
			}
			return new MoveAndEval(bestMove, minEval);
		}
	}
	
	static class MoveAndEval {
		Move move;
		int eval; 
		
		MoveAndEval(Move m, int e) {
			this.move = m;
			this.eval = e;
		}
	}
	
//	private int max(int depth) {
//		if(depth==0) {
//			return evaluate();
//		}
//		int maxEval = Integer.MIN_VALUE;
//		Set<Move> allMoves = board.getCompleteMoveSet(true);
//		if(allMoves.size()>0) {
//			for(Move m: allMoves) {
//				Piece capturedPiece = makeTempMove(m);
//				int currentEval = -min(depth-1);
//				if(currentEval>maxEval) {
//					maxEval = currentEval;
//					if(isWhite()) {
//						bestMove = m;
//					}
//				}
//				unmakeMove(m, capturedPiece);
//			}
//		}
//		
//		return maxEval;
//	}
//	
//	private int min(int depth) {
//		if(depth==0) {
//			return evaluate();
//		}
//		int maxEval = Integer.MAX_VALUE;
//		Set<Move> allMoves = board.getCompleteMoveSet(false);
//		if(allMoves.size()>0) {
//			for(Move m: allMoves) {
//				Piece capturedPiece = makeTempMove(m);
//				int currentEval = -min(depth-1);
//				if(currentEval>maxEval) {
//					maxEval = currentEval;
//					if(isWhite()) {
//						bestMove = m;
//					}
//				}
//				unmakeMove(m, capturedPiece);
//			}
//		}
//		
//		return maxEval;
//	}
	
	public Piece makeTempMove(Move m, Board b) {
		Piece capturedPiece = null;
		Spot start = m.getStart(), end = m.getEnd();
		Piece movingPiece = start.removePiece();
		if(end.getPiece()!=null) {
			capturedPiece = end.removePiece();
		}
		end.addPiece(movingPiece);
		return capturedPiece;
	}
	
	public void unmakeMove(Move m, Board b, Piece captured) {
		Spot start = m.getStart(), end = m.getEnd();
		Piece movingPiece = end.removePiece();
		start.addPiece(movingPiece);
		if(captured!=null) {
			end.addPiece(captured);
		}
	}
	

}
