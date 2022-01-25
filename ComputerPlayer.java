package chessproject;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Set;

public class ComputerPlayer extends Player {

    private boolean thinking;

    public ComputerPlayer(boolean white) {
        super(white);
    }

    public Move makeMove(Board board, int depth) {
        Move bestMove;
        if(board.inKQEndGame(isWhite())) {
            bestMove = minimax(board, 7, Integer.MIN_VALUE, Integer.MAX_VALUE, true).move;
        }else {
            bestMove = minimax(board, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, true).move;
        }
        return bestMove;
    }


    public int evaluate(Board board, int depth) {
        if(isWhite()) {
            return board.evaluateWhite(depth)-board.evaluateBlack(depth);
        }else {
            return board.evaluateBlack(depth)-board.evaluateWhite(depth);
        }
    }

    public MoveAndEval minimax(Board board, int depth, int alpha, int beta, boolean maximizing) {
        if(depth==0 || board.isGameOver()) {
            return new MoveAndEval(null, evaluate(board, depth));
        }

        Set<Move> allMoves;
        if(maximizing) {
            allMoves = board.getCompleteMoveSet(isWhite());
        }else {
            allMoves = board.getCompleteMoveSet(!isWhite());
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

    public Piece makeTempMove(Move m, Board b) {
        Piece capturedPiece = null;
        Spot start = m.getStart(), end = m.getEnd();
        Piece movingPiece = start.removePiece();
        if(end.getPiece()!=null) {
            capturedPiece = end.removePiece();
        }
        if(m.isCastlingMove()) {
            ((King)movingPiece).setCastled(true);
        }
        if(m.isPromotionMove()) {
            end.addPiece(new Queen(movingPiece.isWhite(), 900, 'Q', end.getRow(), end.getColumn()));
        }else {
            end.addPiece(movingPiece);
        }

        b.getPseudoLegal();
        return capturedPiece;
    }

    public void unmakeMove(Move m, Board b, Piece captured) {
        Spot start = m.getStart(), end = m.getEnd();
        Piece movingPiece = end.removePiece();
        if(m.isCastlingMove()) {
            ((King)movingPiece).setCastled(false);
        }
        if(m.isPromotionMove()) {
            boolean f;
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
