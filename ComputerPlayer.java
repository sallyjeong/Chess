package chessproject;

import java.util.Set;
/**
 * [ComputerPlayer.java]
 * This class represents a computer that plays chess
 * @author Peter Gao, Stanley Wang
 * @version 1.0 Jan 25, 2022
 */
public class ComputerPlayer extends Player {

    /**
     * constructs the Computer player
     * @param white: if the computer is playing white or black
     */
    public ComputerPlayer(boolean white) {
        super(white);
    }

    /**
     * makeMove
     * method called to perform search of best move
     * @param board: the current board
     * @param depth: an integer representing how deep the minimax search will go
     * @return the best move that can be made
     */
    public Move makeMove(Board board, int depth) {
        Move bestMove;
        //for King Queen King endgame, we push the engine to search for a checkmate with a higher depth level
        if(board.inKQEndGame(isWhite())) {
            bestMove = minimax(board, depth*2-1, Integer.MIN_VALUE, Integer.MAX_VALUE, true).move;
        }else {
            bestMove = minimax(board, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, true).move;
        }
        return bestMove;
    }

    /**
     * [MoveAndEval.java]
     * This private inner class represents a pair of a move and the int evaluation for it
     */
    private static class MoveAndEval {
        Move move;
        int eval;

        MoveAndEval(Move m, int e) {
            this.move = m;
            this.eval = e;
        }
    }

    /**
     * minimax
     * recursive decision making algorithm to find the next move in a turn based, zero-sum game.
     * uses alpha-beta pruning to optimize the search
     * @param board: the current board
     * @param depth: the current depth level
     * @param alpha: current alpha value
     * @param beta: current beta value
     * @param maximizing: true if the current player is maximizing, false if current player is minimizing
     * @return a move and evaluation
     */
    public MoveAndEval minimax(Board board, int depth, int alpha, int beta, boolean maximizing) {
        if(depth==0 || board.isGameOver()) {
            return new MoveAndEval(null, evaluate(board, depth));
        }

        Set<Move> allMoves;
        //maximizing side will always be the computer
        if(maximizing) {
            allMoves = board.getCompleteMoveSet(isWhite());
        }else {
            allMoves = board.getCompleteMoveSet(!isWhite());
        }
        Move bestMove = null;
        //minimax algorithm
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
                //alpha beta pruning
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
                //alpha beta pruning
                beta = Math.min(beta, currentEval);
                if(beta<=alpha) {
                    break;
                }
            }
            return new MoveAndEval(bestMove, minEval);
        }
    }

    /**
     * evaluate
     * private method used by the minimax search to evaluate how good a position is for the computer
     * @param board: the current board
     * @param depth: the current depth level
     * @return integer representing how good the position is for the computer, positive if good, 0 if neutral, negative if bad
     */
    private int evaluate(Board board, int depth) {
        if(isWhite()) {
            return board.evaluateWhite(depth)-board.evaluateBlack(depth);
        }else {
            return board.evaluateBlack(depth)-board.evaluateWhite(depth);
        }
    }

    /**
     * makeTempMove
     * private method used by the minimax search to temporarily make a move
     * @param m: the move to be made
     * @param board: the current board
     * @return a Piece in case a piece is captured, used to unmake move later
     */
    private Piece makeTempMove(Move m, Board board) {
        Piece capturedPiece = null;
        Spot start = m.getStart(), end = m.getEnd();
        Piece movingPiece = start.removePiece();
        if(end.getPiece()!=null) {
            capturedPiece = end.removePiece();
        }
        if(m.isCastlingMove()) {
            ((King)movingPiece).setCastled(true);
        }
        //automatically promote to queen
        if(m.isPromotionMove()) {
            end.addPiece(new Queen(movingPiece.isWhite(), 900, 'Q', end.getRow(), end.getColumn()));
        }else {
            end.addPiece(movingPiece);
        }

        board.getLegal();
        return capturedPiece;
    }

    /**
     * unmakeMove
     * private method used by the minimax search to unmake a temporarily made move
     * @param m: the move to be unmade
     * @param board: the current board
     * @param captured: the piece that was captured
     */
    private void unmakeMove(Move m, Board board, Piece captured) {
        Spot start = m.getStart(), end = m.getEnd();
        Piece movingPiece = end.removePiece();
        if(m.isCastlingMove()) {
            ((King)movingPiece).setCastled(false);
        }
        if(m.isPromotionMove()) {
            boolean f;
            //place the pawn down facing the correct way
            if(end.getRow()==7) {
                f = false;
            }else {
                f = true;
            }
            start.addPiece(new Pawn(movingPiece.isWhite(), 100, '\u0000', start.getRow(), start.getColumn(), f));
        }else {
            start.addPiece(movingPiece);
        }
        if(captured!=null) {
            end.addPiece(captured);
        }
    }


}
