import java.util.ArrayList;

public class Game {
	private ArrayList<Move> pastMoves;
	private Player players[] = new Player[2];
	//private CheckStatus checkStatus;
	private boolean gameOver;
	private Player turn;
	private Board board; 

	public boolean isThreatenedSpot(boolean whiteThreatened, Spot threatenedSpot, Board board) {
		
		int knightDirections[] = {2,1,2,-1,-2,1,-2,-1,1,2,1,-2,-1,2,-1,-2};
		//8 directions, -1, -1 is northwest diag, -1, 0 north, -1, 1 northeast, 0, -1 west
		int[] rowDirections = {-1, -1, -1, 0, 0, 1, 1, 1};
		int[] colDirections = {-1, 0, 1, -1, 1, -1, 0, 1};
		boolean bishopThreats[] = {true, false, true, false, false, true, false, true};
		boolean rookThreats[] = {false, true, false, true, true, false, true, false};
		boolean queenThreats[] = {true, true, true, true, true, true, true, true};
		boolean kingThreats[] = {true, true, true, true, true, true, true, true};
		boolean pawnKill = whiteThreatened;
		boolean pawnThreats[] = {pawnKill, false, pawnKill, false, false, !pawnKill, false, !pawnKill};
		
		int row = threatenedSpot.getRow(), column = threatenedSpot.getColumn();
		
		for(int i=0; i<16; i+=2) {
			int nrow = row+knightDirections[i], ncol = column+knightDirections[i+1];
			if(isRCValid(nrow, ncol)) {
				Piece piece = board.getBoard()[nrow][ncol].getPiece();
				if(piece!=null && whiteThreatened!=piece.isWhite()) {
					if(piece instanceof Knight) {
						System.out.print("n");
						return true;
					}
				}
			}
		}
		for(int direction=0; direction<8; direction++) {
			row = threatenedSpot.getRow(); column = threatenedSpot.getColumn();
			int rowIncrement = rowDirections[direction], columnIncrement = colDirections[direction];
			row+=rowIncrement; column+=columnIncrement;
			boolean closestStep = true;
			Piece piece = null;
			while(isRCValid(row, column) && piece==null) {
				piece = board.getBoard()[row][column].getPiece();
				if(piece!=null && whiteThreatened!=piece.isWhite()) {
					if(piece instanceof Bishop && bishopThreats[direction]) {
						System.out.print("b");
						return true;
					}else if(piece instanceof Rook && rookThreats[direction]) {
						System.out.print("r");
						return true;
					}else if(piece instanceof Queen && queenThreats[direction]) {
						System.out.print("q");
						return true;
					}else if(closestStep) {
						if(piece instanceof King && kingThreats[direction]) {
							System.out.print("k");
							return true;
						}else if(piece instanceof Pawn && pawnThreats[direction]) {
							System.out.print("p");
							return true;
						}
					}
				}
				closestStep=false;
				row+=rowIncrement; column+=columnIncrement;
			}
		}
		return false;
	}

	private boolean isRCValid(int row, int col) {
		if(row<0 || row>7 || col<0 || col>7) {
			return false;
		}
		return true;
	}

}
