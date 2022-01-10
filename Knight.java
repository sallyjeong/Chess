package chessproject;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Knight extends Piece {

	public Knight(boolean w, boolean m, int p, int s) {
		super(w, m, p, s);
	}

	@Override
	public void loadImage() {
		try {
			this.setImage(ImageIO.read(new File("files\\06_classic2\\w-knight2.png")), 0);
			this.setImage(ImageIO.read(new File("files\\06_classic2\\b-knight2.png")), 1);
		} catch (IOException e) {
			System.out.println("error loading sprite");
		}
	}


}
