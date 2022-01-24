//package chessproject;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JLabel;
import javax.swing.SpringLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class PromotionFrame extends JFrame{

    private JPanel contentPane;
    private int choice = 0;
    private Board board;
    private Piece piece;
    private Piece endPiece;
    private Move move;

    /**
     * Launch the application.
     */

    /**
     * Create the frame.
     */
    public PromotionFrame(Piece piece, Piece endPiece, Board board, Move move) {
        this.board = board;
        this.piece = piece;
        this.endPiece = endPiece;
        this.move = move;
        boolean isWhite= piece.isWhite();
        int prevRow= piece.getRow();
        int prevCol= piece.getCol();
        
        JFrame frame = this;
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setVisible(true);
        requestFocusInWindow();
        setBounds(100, 100, 261, 446);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        SpringLayout sl_contentPane = new SpringLayout();
        contentPane.setLayout(sl_contentPane);

        JLabel lblNewLabel = new JLabel(new ImageIcon(new ImageIcon("06_classic2/w-queen2.png").getImage().getScaledInstance(90, 80, Image.SCALE_DEFAULT)));
        sl_contentPane.putConstraint(SpringLayout.NORTH, lblNewLabel, 10, SpringLayout.NORTH, contentPane);
        sl_contentPane.putConstraint(SpringLayout.WEST, lblNewLabel, 10, SpringLayout.WEST, contentPane);
        sl_contentPane.putConstraint(SpringLayout.SOUTH, lblNewLabel, 92, SpringLayout.NORTH, contentPane);
        contentPane.add(lblNewLabel);

        JLabel lblNewLabel_1 = new JLabel(new ImageIcon(new ImageIcon("06_classic2/w-rook2.png").getImage().getScaledInstance(90, 80, Image.SCALE_DEFAULT)));
        sl_contentPane.putConstraint(SpringLayout.NORTH, lblNewLabel_1, 19, SpringLayout.SOUTH, lblNewLabel);
        sl_contentPane.putConstraint(SpringLayout.WEST, lblNewLabel_1, 10, SpringLayout.WEST, contentPane);
        sl_contentPane.putConstraint(SpringLayout.EAST, lblNewLabel_1, -135, SpringLayout.EAST, contentPane);
        contentPane.add(lblNewLabel_1);

        JLabel lblNewLabel_1_1 = new JLabel(new ImageIcon(new ImageIcon("06_classic2/w-bishop2.png").getImage().getScaledInstance(90, 80, Image.SCALE_DEFAULT)));
        sl_contentPane.putConstraint(SpringLayout.SOUTH, lblNewLabel_1, -17, SpringLayout.NORTH, lblNewLabel_1_1);
        sl_contentPane.putConstraint(SpringLayout.WEST, lblNewLabel_1_1, 10, SpringLayout.WEST, contentPane);
        sl_contentPane.putConstraint(SpringLayout.EAST, lblNewLabel_1_1, -135, SpringLayout.EAST, contentPane);
        sl_contentPane.putConstraint(SpringLayout.NORTH, lblNewLabel_1_1, 210, SpringLayout.NORTH, contentPane);
        contentPane.add(lblNewLabel_1_1);

        JLabel lblNewLabel_1_1_1 = new JLabel(new ImageIcon(new ImageIcon("06_classic2/w-knight2.png").getImage().getScaledInstance(90 , 80, Image.SCALE_DEFAULT)));
        sl_contentPane.putConstraint(SpringLayout.WEST, lblNewLabel_1_1_1, 10, SpringLayout.WEST, contentPane);
        sl_contentPane.putConstraint(SpringLayout.EAST, lblNewLabel_1_1_1, -135, SpringLayout.EAST, contentPane);
        sl_contentPane.putConstraint(SpringLayout.SOUTH, lblNewLabel_1_1, -17, SpringLayout.NORTH, lblNewLabel_1_1_1);
        sl_contentPane.putConstraint(SpringLayout.NORTH, lblNewLabel_1_1_1, 309, SpringLayout.NORTH, contentPane);
        sl_contentPane.putConstraint(SpringLayout.SOUTH, lblNewLabel_1_1_1, -10, SpringLayout.SOUTH, contentPane);
        contentPane.add(lblNewLabel_1_1_1);

        JButton btnNewButton = new JButton("Queen");
        sl_contentPane.putConstraint(SpringLayout.WEST, btnNewButton, 128, SpringLayout.WEST, contentPane);
        sl_contentPane.putConstraint(SpringLayout.EAST, lblNewLabel, -28, SpringLayout.WEST, btnNewButton);
        sl_contentPane.putConstraint(SpringLayout.NORTH, btnNewButton, 11, SpringLayout.NORTH, lblNewLabel);
        sl_contentPane.putConstraint(SpringLayout.SOUTH, btnNewButton, -11, SpringLayout.SOUTH, lblNewLabel);
        sl_contentPane.putConstraint(SpringLayout.EAST, btnNewButton, -17, SpringLayout.EAST, contentPane);
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                move.getEnd().addPiece(new Queen(isWhite, false, 9, 'Q', piece.getRow(), piece.getCol()));
                frame.dispose();
            }
        });
        contentPane.add(btnNewButton);

        JButton btnNewButton_1 = new JButton("Rook");
        sl_contentPane.putConstraint(SpringLayout.NORTH, btnNewButton_1, 40, SpringLayout.SOUTH, btnNewButton);
        sl_contentPane.putConstraint(SpringLayout.WEST, btnNewButton_1, 0, SpringLayout.WEST, btnNewButton);
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                move.getEnd().addPiece(new Rook(isWhite, false, 9, 'R', piece.getRow(), piece.getCol()));
                frame.dispose();
            }
        });
        contentPane.add(btnNewButton_1);

        JButton btnNewButton_1_1 = new JButton("Bishop");
        sl_contentPane.putConstraint(SpringLayout.NORTH, btnNewButton_1_1, 219, SpringLayout.NORTH, contentPane);
        sl_contentPane.putConstraint(SpringLayout.SOUTH, btnNewButton_1, -35, SpringLayout.NORTH, btnNewButton_1_1);
        sl_contentPane.putConstraint(SpringLayout.WEST, btnNewButton_1_1, 0, SpringLayout.WEST, btnNewButton);
        sl_contentPane.putConstraint(SpringLayout.EAST, btnNewButton_1_1, -18, SpringLayout.EAST, contentPane);
        btnNewButton_1_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                move.getEnd().addPiece(new Bishop(isWhite, false, 9, 'B', piece.getRow(), piece.getCol()));
                frame.dispose();
            }
        });
        contentPane.add(btnNewButton_1_1);

        JButton btnNewButton_1_1_1 = new JButton("Knight");
        sl_contentPane.putConstraint(SpringLayout.SOUTH, btnNewButton_1_1, -36, SpringLayout.NORTH, btnNewButton_1_1_1);
        sl_contentPane.putConstraint(SpringLayout.NORTH, btnNewButton_1_1_1, 11, SpringLayout.NORTH, lblNewLabel_1_1_1);
        sl_contentPane.putConstraint(SpringLayout.WEST, btnNewButton_1_1_1, 0, SpringLayout.WEST, btnNewButton);
        sl_contentPane.putConstraint(SpringLayout.SOUTH, btnNewButton_1_1_1, -20, SpringLayout.SOUTH, contentPane);
        sl_contentPane.putConstraint(SpringLayout.EAST, btnNewButton_1_1_1, -18, SpringLayout.EAST, contentPane);
        btnNewButton_1_1_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                move.getEnd().addPiece(new Knight(isWhite, false, 9, 'N', piece.getRow(), piece.getCol()));
                
            }
        });
        contentPane.add(btnNewButton_1_1_1);

        JButton btnNewButton_2 = new JButton("BACK");
        contentPane.add(btnNewButton_2);
        btnNewButton_2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                move.getEnd().addPiece(endPiece);
                move.getStart().addPiece(piece);
                piece.setRow(prevRow); piece.setCol(prevCol);
                frame.dispose();
            }
        });

    }
}
