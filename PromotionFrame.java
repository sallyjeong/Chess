package chessproject;//package chessproject;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class PromotionFrame extends JFrame{

    private JPanel contentPane;
    private Board board;
    private Piece piece;
    private GameFrame mainFrame;

    /**
     * Launch the application.
     */

    /**
     * Create the frame.
     */
    public PromotionFrame(Piece piece, Board board, Move move, GameFrame main) {
        this.board = board;
        this.piece = piece;
        boolean isWhite= piece.isWhite();
        mainFrame= main;
        mainFrame.setEnabled(false);

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
                move.getEnd().addPiece(new Queen(isWhite, 9, 'Q', piece.getRow(), piece.getCol()));
                choosePiece(move);
            }
        });
        btnNewButton.setBackground(new Color(245, 245, 220));
        btnNewButton.setOpaque(true);
        contentPane.add(btnNewButton);

        JButton btnNewButton_1 = new JButton(new ImageIcon(new ImageIcon("06_classic2/w-rook2.png").getImage().getScaledInstance(90, 80, Image.SCALE_DEFAULT)));
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                move.getEnd().addPiece(new Rook(isWhite, 5 , 'R', piece.getRow(), piece.getCol()));
                choosePiece(move);
            }
        });
        btnNewButton_1.setBackground(new Color(245, 245, 220));
        btnNewButton_1.setOpaque(true);
        contentPane.add(btnNewButton_1);


        JButton btnNewButton_1_1 = new JButton(new ImageIcon(new ImageIcon("06_classic2/w-bishop2.png").getImage().getScaledInstance(90, 80, Image.SCALE_DEFAULT)));
        btnNewButton_1_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                move.getEnd().addPiece(new Bishop(isWhite, 3, 'B', piece.getRow(), piece.getCol()));
                choosePiece(move);
            }
        });
        btnNewButton_1_1.setBackground(new Color(245, 245, 220));
        btnNewButton_1_1.setOpaque(true);
        contentPane.add(btnNewButton_1_1);


        JButton btnNewButton_1_1_1 = new JButton(new ImageIcon(new ImageIcon("06_classic2/w-knight2.png").getImage().getScaledInstance(90 , 80, Image.SCALE_DEFAULT)));
        btnNewButton_1_1_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                move.getEnd().addPiece(new Knight(isWhite, 3, 'N', piece.getRow(), piece.getCol()));
                choosePiece(move);
            }
        });
        btnNewButton_1_1_1.setBackground(new Color(245, 245, 220));
        btnNewButton_1_1_1.setOpaque(true);
        contentPane.add(btnNewButton_1_1_1);


    }

    public void choosePiece(Move move) {
        board.getPseudoLegal();
        mainFrame.setEnabled(true);
        //mainFrame.getClient().sendData(Constants.PROMOTION_DATA + "1"); // allow the opponent to move
        dispose();
        mainFrame.getClient().sendData(Constants.MOVE_DATA + move.toString());
    }
}