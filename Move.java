package chessproject;

public class Move {
	private Spot start;
	private Spot end;
	private Piece sourcePiece, capturedPiece;

	public Move (Spot start, Spot end) {
		this.start = start;
		this.end = end;
		this.sourcePiece = start.getPiece();
		this.capturedPiece = end.getPiece();
	}

	public boolean isCastlingMove() {
		if(sourcePiece instanceof King && Math.abs(end.getColumn()-start.getColumn())==2) {
			return true;
		}
		return false;
	}

	public boolean isEnPassantMove() {
		if(sourcePiece.getRow()>0 && sourcePiece.getRow()<7){
			if(sourcePiece instanceof Pawn && Math.abs(end.getColumn()-start.getColumn())==1 && Math.abs(end.getRow()-start.getRow())==1 && capturedPiece==null) {
				return true;
			}
		}
		return false;
	}

	public boolean isPromotionMove() {
		if(sourcePiece instanceof Pawn) {
			if(end.getRow()==0 || end.getRow()==7) {
				return true;
			}
		}
		return false;
	}
	
	public String toString() {
		if(isCastlingMove()) {
			if(end.getColumn()==6 || end.getColumn()==1) {
				return "O-O";
			}else {
				return "O-O-O";
			}
		}

		String ret = "";
		if(!(sourcePiece instanceof Pawn)) {
			ret+=sourcePiece.getSymbol();
		}
		ret+=start.getID();
		if(capturedPiece==null) {
			ret+="-";
		}else if(capturedPiece instanceof Pawn) {
			ret+="x";
		}else {
			ret+="x"+capturedPiece.getSymbol();
		}
		ret+=end.getID();

		if(isPromotionMove()) {
			ret+="=";
			ret+=end.getPiece().getSymbol();
		}
		
		return ret;
	}

	public Spot getStart() {
		return this.start;
	}

	public Spot getEnd() {
		return this.end;
	}


}