package chessproject;

public class Player {
    private boolean white;
    private boolean humanPlayer;
    
    public Player(boolean w, boolean h){
        this.white = w;
        this.humanPlayer = h;
    }
    
    public boolean isWhite() {
        return white;
    }
}