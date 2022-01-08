package chessproject;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class King extends Piece {

	private boolean castle = false;
	
	public King(boolean w, boolean m, int p, int s) {
		super(w, m, p, s);
	}
	
	public boolean isCastled() {
		return this.castle;
	}

	@Override
	public void loadImage() {
		// TODO Auto-generated method stub
		
	}

}
