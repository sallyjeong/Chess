package chessproject;

import java.awt.Color;
import java.awt.Graphics;

public class Board {
	private Spot[][] board;
	
	public Board() {
		this.create();
	}
	
	public void create() {
		board = new Spot[8][8];
		for(int i=0; i<8; i++) {
			for(int j=0; j<8; j++) {
				Color color = new Color(139, 69, 19);
				if((i+j)%2==0) {
					color = new Color(238, 232, 170);
				}
				board[i][j] = new Spot(i, j, "test", null, color);
			}
		}
		board[0][0] = new Spot(0, 0, "a8", new Rook(false, false, 5, 0), new Color(238, 232, 170));
	}
	
	public void draw(Graphics g) {
		for(int i=0; i<8; i++) {
			for(int j=0; j<8; j++) {
				board[i][j].draw(g);
			}
		}
	}
}
