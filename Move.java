package chessproject;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;
public class Move {
    private Spot start;
    private Spot end;
    private Piece piece;
    private Piece capturedPiece;
    private Player player;
    private Spot[][] board;
    private Spot spot;
    
    public Move (Player player, Spot start, Spot end) {
        this.player = player;
        this.spot = spot;
        this.end = end;
        this.piece = start.getPiece();
    }
    
        
    public void movePiece(Player player, Spot start, Spot end, Board currentBoard) {
        this.piece = start.getPiece();
        this.end = end;
        this.board = currentBoard.getBoard();
        Set<Spot> spot = board[start.getColumn()][start.getRow()].getPiece().ValidMoves(board);
        Iterator<Spot> spotIterator = spot.iterator();
        while (spotIterator.hasNext()) {
            Spot s = spotIterator.next();
            if (end == s) {
                if (end.getPiece() != null) {
                    capturedPiece = end.getPiece();
                    board[end.getColumn()][end.getRow()].removePiece();
                    board[end.getColumn()][end.getRow()].addPiece(piece);
                }
            }
        }
    }
    /*
    public boolean isCastlingMove(Player player, Spot start, Spot end, Board currentBoard) {
        if (player.isWhite() == false) {
            if (start.getRow() == 0 && end.getRow() == 0 && start.getColumn() == 4 & (end.getColumn() == 6 || end.getColumn() == 1)) {
                if (currentBoard[4][0].getPiece() instanceof King) {
                    if (currentBoard[4][0].getPiece().getIsMoved() == false) {
                        if (currentBoard[7][0].getPiece() instanceof Rook && end.getColumn() == 6) {
                            if (currentBoard[7][0].getPiece().getIsMoved() == false) {
                                if (currentBoard[5][0].getPiece() == null && currentBoard[6][0].getPiece() == null) {
                                    
                                }
                        } else if (currentBoard[0][0].getPiece() instanceof Rook && end.getColumn() == 1) {
                            if (currentBoard[7][0].getPiece().getIsMoved() == false) {
                                if (currentBoard[3][0].getPiece() == null && currentBoard[2][0].getPiece() == null && currentBoard[1][0].getPiece() == null) {
                                    
                                }
                        }
                    }
                        
                }
            }
        }
    }
    */
    public void storeMove() {
        
    }
    
    
}
