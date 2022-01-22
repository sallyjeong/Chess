

import java.awt.Graphics;
import java.util.ArrayList;

public class Player {
	final int LENGTH = 35;
	
    private boolean white;
    private ArrayList<Piece> captured;
    
    public Player(boolean w){
        this.white = w;
        this.captured = new ArrayList<Piece>();
    }
    
    public boolean isWhite() {
        return white;
    }
    
    public ArrayList<Piece> getCaptured() {
    	return this.captured;
    }
    
    public void displayCaptured(Graphics g) {
    	for(int i=0; i<captured.size(); i++) {
    		Piece p = captured.get(i);
    		if(p.isWhite()) {
    			g.drawImage(p.getImage()[0], 576+i*LENGTH, 0, LENGTH, LENGTH, null);
    		}else {
    			g.drawImage(p.getImage()[1], 576+i*LENGTH, 520, LENGTH, LENGTH, null);
    		}
    		
    	}
    }
    
}
