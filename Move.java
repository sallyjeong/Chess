package chessproject;

public class Move {
	private Spot start;
	private Spot end;
	private Piece sourcePiece, capturedPiece;
	private boolean isCheckmatingMove, isCheckMove;

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

	public Spot getStart() {
		return this.start;
	}

	public Spot getEnd() {
		return this.end;
	}

	public void setCheckmatingMove() {
		this.isCheckmatingMove = true;
	}


	public void setCheckMove() {
		this.isCheckMove = true;
	}


}