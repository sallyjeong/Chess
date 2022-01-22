//package chessproject;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class AItester {

    static Game game;
    static Player curPlayer;
    static Player p1, p2;

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        p1 = new Player(true); p2 = new ComputerPlayer();
        curPlayer = p1;
        game = new Game(true, p1, p2);

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GamePanel v = new GamePanel();
        frame.setSize(1000, 1000);
        frame.setVisible(true);
        frame.getContentPane().add(v);
    }


    public static class GamePanel extends JPanel implements MouseListener {

        private Spot source = null;

        public GamePanel() {
            setPreferredSize(new Dimension(1000, 1000));
            addMouseListener(this);
            setFocusable(true);
            requestFocusInWindow();
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            game.getBoard().draw(g);
            repaint();
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if(e.getX()<8*game.getBoard().LENGTH && e.getY()<8*game.getBoard().LENGTH) {
                Spot spot = game.getBoard().getBoard()[e.getY()/game.getBoard().LENGTH][e.getX()/game.getBoard().LENGTH];
                if(source==null) {
                    if(spot.getPiece()==null) {
                        return;
                    }else {
                        if (game.getTurn().isWhite() == spot.getPiece().isWhite()) {
                            spot.setClicked(true);
                            spot.getPiece().displayValidMoves(true);
                            source = spot;
                        }
                    }
                }else {
                    if(spot.equals(source)) {
                        source.setClicked(false);
                        spot.getPiece().displayValidMoves(false);
                        source = null;
                    }else if(source.getPiece().getMoveList().contains(spot)) {
                        if(source.getPiece().isWhite()) {
                            game.playerMove(p1, source, spot);
                        }else {
                            game.playerMove(p2, source, spot);
                        }
                        source.setClicked(false);
                        if(source.getPiece()!=null) {
                            source.getPiece().displayValidMoves(false);
                        }
                        source = null;
                    }
                }
            }

            Spot[] s= ((ComputerPlayer)p2).selectSpot();
            game.playerMove(p2, s[0], s[1]);


        }



        @Override
        public void mousePressed(MouseEvent e) {
        }


        @Override
        public void mouseReleased(MouseEvent e) {
        }


        @Override
        public void mouseEntered(MouseEvent e) {
        }


        @Override
        public void mouseExited(MouseEvent e) {
        }

    }

}
