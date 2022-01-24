package chessproject;//package chessproject;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class PromotionFrame extends JFrame{

    private JPanel contentPane;
    private int choice = 0;
    private Board board;
    private Piece piece;
    private Piece endPiece;
    private Move move;
    private King enemyKing;
    private JFrame mainFrame;
    private Player curPlayer;

    /**
     * Launch the application.
     */

    /**
     * Create the frame.
     */
    public PromotionFrame(Piece piece, Piece endPiece, Board board, Move move, JFrame main, Game g) {
        this.board = board;
        this.piece = piece;
        this.endPiece = endPiece;
        this.move = move;
        boolean isWhite= piece.isWhite();
        mainFrame= main;
        mainFrame.setEnabled(false);
        Player prevTurn= g.getTurn();
        if(isWhite){
            enemyKing= board.getBlackKing();
        }
        else{
            enemyKing= board.getWhiteKing();
        }
        int prevRow= piece.getRow();
        int prevCol= piece.getCol();

        JFrame frame= this;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 443, 503);
        setVisible(true);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new GridLayout(0, 2, 0, 0));

        JLabel lblNewLabel_4 = new JLabel("Promote y");
        lblNewLabel_4.setOpaque(true);
        lblNewLabel_4.setBackground(new Color(0 ,100 ,0));
        lblNewLabel_4.setForeground(new Color(245, 245, 220));
        lblNewLabel_4.setFont(new Font("Trebuchet MS", Font.BOLD , 30));
        lblNewLabel_4.setHorizontalAlignment(SwingConstants.RIGHT);
        contentPane.add(lblNewLabel_4);

        JLabel lblNewLabel_5 = new JLabel("our Pawn!");
        lblNewLabel_5.setForeground(new Color(245, 245, 220));
        lblNewLabel_5.setHorizontalAlignment(SwingConstants.LEFT);
        lblNewLabel_5.setBackground(new Color(0 ,100 ,0));
        lblNewLabel_5.setFont(new Font("Trebuchet MS", Font.BOLD , 30));
        lblNewLabel_5.setOpaque(true);
        contentPane.add(lblNewLabel_5);


        JButton btnNewButton = new JButton(new ImageIcon(new ImageIcon("06_classic2/w-queen2.png").getImage().getScaledInstance(90, 80, Image.SCALE_DEFAULT)));
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                move.getEnd().addPiece(new Queen(isWhite, false, 9, 'Q', piece.getRow(), piece.getCol()));
                board.getPseudoLegal();
                mainFrame.setEnabled(true);
                frame.dispose();
            }
        });
        btnNewButton.setBackground(new Color(245, 245, 220));
        btnNewButton.setOpaque(true);
        contentPane.add(btnNewButton);

        JButton btnNewButton_1 = new JButton(new ImageIcon(new ImageIcon("06_classic2/w-rook2.png").getImage().getScaledInstance(90, 80, Image.SCALE_DEFAULT)));
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                move.getEnd().addPiece(new Rook(isWhite, false, 5 , 'R', piece.getRow(), piece.getCol()));
                board.getPseudoLegal();
                mainFrame.setEnabled(true);
                frame.dispose();
            }
        });
        btnNewButton_1.setBackground(new Color(245, 245, 220));
        btnNewButton_1.setOpaque(true);
        contentPane.add(btnNewButton_1);


        JButton btnNewButton_1_1 = new JButton(new ImageIcon(new ImageIcon("06_classic2/w-bishop2.png").getImage().getScaledInstance(90, 80, Image.SCALE_DEFAULT)));
        btnNewButton_1_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                move.getEnd().addPiece(new Bishop(isWhite, false, 3, 'B', piece.getRow(), piece.getCol()));
                board.getPseudoLegal();
                mainFrame.setEnabled(true);
                frame.dispose();
            }
        });
        btnNewButton_1_1.setBackground(new Color(245, 245, 220));
        btnNewButton_1_1.setOpaque(true);
        contentPane.add(btnNewButton_1_1);


        JButton btnNewButton_1_1_1 = new JButton(new ImageIcon(new ImageIcon("06_classic2/w-knight2.png").getImage().getScaledInstance(90 , 80, Image.SCALE_DEFAULT)));
        btnNewButton_1_1_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                move.getEnd().addPiece(new Knight(isWhite, false, 3, 'N', piece.getRow(), piece.getCol()));
                board.getPseudoLegal();
                mainFrame.setEnabled(true);
                frame.dispose();
                
            }
        });
        btnNewButton_1_1_1.setBackground(new Color(245, 245, 220));
        btnNewButton_1_1_1.setOpaque(true);
        contentPane.add(btnNewButton_1_1_1);


    }
}
