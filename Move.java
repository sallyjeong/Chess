package chessproject;

/* [Move.java]
 * Represent a move that is made, contains functions to specify what type of move made
 * @author Robert Jin, Peter Gao
 * @version 1.0, Jan 25, 2022
 */
public class Move {
    private Spot start;
    private Spot end;
    private Piece sourcePiece, capturedPiece;
    private boolean isCheckmatingMove, isCheckMove;
    
    /* Move
     * Sole constructor
     * Creates a move object based upon start and end positions
     * @param start the Spot object that is copied to this object instance
     * @param end the Spot object that is copied to this object instance
     * */
    public Move (Spot start, Spot end) {
        this.start = start;
        this.end = end;
        this.sourcePiece = start.getPiece();
        this.capturedPiece = end.getPiece();
    }
    
    /* isCastlingMove()
     * Checks if the move in the object instance is a castling move
     * @return boolean returns true if move is a castling move
     */
    public boolean isCastlingMove() {
        if(sourcePiece instanceof King && Math.abs(end.getColumn()-start.getColumn())==2) {
            return true;
        }
        return false;
    }
    
    /* isEnPassantMove()
     * Checks if the move in the object instance is an en passant move
     * @return boolean returns true if move is an en passant move
     */
    public boolean isEnPassantMove() {
        if(sourcePiece.getRow()>0 && sourcePiece.getRow()<7){
            if(sourcePiece instanceof Pawn && Math.abs(end.getColumn()-start.getColumn())==1 && Math.abs(end.getRow()-start.getRow())==1 && capturedPiece==null) {
                return true;
            }
        }
        return false;
    }
    
    /* isPromotionMove()
     * Checks if the move in the object instance is a move that promotes a pawn
     * @return boolean returns true if move is an en passant move
     */
    public boolean isPromotionMove() {
        if(sourcePiece instanceof Pawn) {
            if(end.getRow()==0 || end.getRow()==7) {
                return true;
            }
        }
        return false;
    }
    
    /* toString()
     * Returns a string that contains chess notation for move being made
     * @return String returns string of constants
     */
    public String toString() {
        if(isCastlingMove()) {
            if(end.getColumn()==1 || end.getColumn()==6) {
                return Constants.CASTLE_1;
            } else {
                return Constants.CASTLE_2;
            }
        }
        
        String ret = "";
        if(!(sourcePiece instanceof Pawn)) {
            ret+=sourcePiece.getSymbol();
        } else if (isEnPassantMove()) {
            ret+= Constants.PAWN_INDICATOR;
        } else {
            ret+=" ";
        }
        ret+=start.getID();
        
        if(capturedPiece==null) {
            ret+=Constants.MOVE;
        }else {
            ret+=Constants.CAPTURE+capturedPiece.getSymbol();
        }
        ret+=end.getID();
        
        if(isPromotionMove()) {
            ret+=Constants.PROMOTE;
            ret+=end.getPiece().getSymbol();
        }
        
        if(isCheckmatingMove) {
            ret+=Constants.CHECKMATE;
        }else if(isCheckMove) {
            ret+=Constants.CHECK;
        }
        
        return ret;
    }
    
    /* getStart()
     * Returns starting spot
     * @return Spot returns spot object
     */
    public Spot getStart() {
        return this.start;
    }
    
    /* getEnd()
     * Returns ending spot
     * @return Spot returns spot object
     */
    public Spot getEnd() {
        return this.end;
    }
    
    /* setCheckmatingMove()
     * Sets checkmate boolean to true
     */
    public void setCheckmatingMove() {
        this.isCheckmatingMove = true;
    }
    
    /* setCheck()
     * Sets check boolean to true
     */
    public void setCheckMove() {
        this.isCheckMove = true;
    }
    
    
}
