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

	public Move (Player player, Spot start, Spot end) {
		this.player = player;
		this.start = start;
		this.end = end;
	}

	public boolean isCastlingMove() {
		Piece sourcePiece = end.getPiece();
		if(sourcePiece instanceof King && Math.abs(end.getColumn()-start.getColumn())==2) {
			return true;
		}
		return false;
	}
	
	public boolean isEnPassantMove() {
		Piece sourcePiece = end.getPiece();
		if(sourcePiece.getRow()>0 && sourcePiece.getRow()<7){
			if(sourcePiece instanceof Pawn && Math.abs(end.getColumn()-start.getColumn())==1 && Math.abs(end.getRow()-start.getRow())==1) {
				return true;
			}
		}

		return false;
	}
	
	public Player getPlayer() {
		return this.player;
	}
	
	public Spot getStart() {
		return this.start;
	}
	
	public Spot getEnd() {
		return this.end;
	}
	

}