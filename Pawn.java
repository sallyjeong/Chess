package chessproject;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Pawn extends Piece {

	public Pawn(boolean w, boolean m, int p, int s) {
		super(w, m, p, s);
	}

	@Override
	public void loadImage() {
		try {
			this.setImage(ImageIO.read(new File("files\\06_classic2\\w-pawn2.png")), 0);
			this.setImage(ImageIO.read(new File("files\\06_classic2\\b-pawn2.png")), 1);
		} catch (IOException e) {
			System.out.println("error loading sprite");
		}
		
	}

}
