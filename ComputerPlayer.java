import java.sql.Array;
import java.util.ArrayList;
import java.util.Set;

public class ComputerPlayer extends Player{
    private Board board;
    private ArrayList<Piece> blackPieces;
    public ComputerPlayer() {
        super(false);
        blackPieces= new ArrayList<Piece>();
    }

    public Spot[] selectSpot(){
        Spot[] move= new Spot[2];
        Spot src= null;

        int highestPriority= -1;
        Spot bestSpot = null;
        for(int i= blackPieces.size()-1; i>=0; i--){
            Piece p= blackPieces.get(i);
            Spot r= board.getBoard()[p.getRow()][p.getCol()];
            if(p.isCaptured()){ //Remove pieces when they get captured by white
                blackPieces.remove(i);
                System.out.println("captured");

            }
            else{
                Set<Spot> validMoves= p.validMoves(board);
                for(Spot s: validMoves){
                    int priority=0;
                    if(s.getPiece() != null){
                        priority= s.getPiece().getPoints();
                    }
                    if(priority > highestPriority){
                        highestPriority= priority;
                        bestSpot= s;
                        src= r;
                    }
                }
            }
        }

        move[0]= src;
        move[1]= bestSpot;

        return move;

    }

    void receiveBoard(Board b){
        board= b;

        for(int i=0; i<=1; i++){
            for(int j=0; j< 8; j++){
                Piece blackPiece= board.getBoard()[i][j].getPiece();
                blackPieces.add(blackPiece);

            }
        }


    }





}
