package chessproject;

// imports
import java.awt.Graphics;
import java.util.ArrayList;

/** [Player.java]
 * Represents a player in a chess game, can be human or computer
 * @author 
 * @version 1.0 Jan 25, 2022
 */
public class Player {
    final int LENGTH = 35;
    private boolean white;
    private ArrayList<Piece> captured;

    /**
     * Player
     * This constructor is for the human and computer player in a computer game
     * @param white is true if player is playing the white pieces
     */
    public Player(boolean white){
        this.white = white;
        this.captured = new ArrayList<Piece>();
    }

    /**
     * Player
     * This constructor is used for the players of type Client to initialize their captured pieces
     */
    public Player() {
        this.captured = new ArrayList<Piece>();
    }

    /**
     * displayCaptured
     * This allows the pieces the player has captured to show up on the screen
     * @param g is the Graphics object which allows the pieces to be displayed
     */
    public void displayCaptured(Graphics g) {
        for(int i=0; i<captured.size(); i++) {
            Piece p = captured.get(i);
            if(p.isWhite()) {
                g.drawImage(p.getImage()[0], i*LENGTH, 535, LENGTH, LENGTH, null);
            }else {
                g.drawImage(p.getImage()[1], i*LENGTH, 535, LENGTH, LENGTH, null);
            }

        }
    }
    
    /*
    GETTERS AND SETTERS
     */
    public boolean isWhite() {
        return white;
    }

    public ArrayList<Piece> getCaptured() {
        return this.captured;
    }

}
