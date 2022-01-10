package chessproject;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

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
		try {
			this.setImage(ImageIO.read(new File("files\\06_classic2\\w-king2.png")), 0);
			this.setImage(ImageIO.read(new File("files\\06_classic2\\b-king2.png")), 1);
		} catch (IOException e) {
			System.out.println("error loading sprite");
		}
	}

}
