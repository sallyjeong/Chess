package chessproject;

//imports
import java.awt.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;


/**
 * [PromotionFrame.java]
 * Creates the frame that user uses to promote the current pawn once it reaches the farthest rank from its original square.
 *  @author Stanley Wang
 *  @version 1.0 Jan 25, 2022
 */
public class PromotionFrame extends JFrame{

    private JPanel contentPane;
    private Board board;
    private Piece piece;
    private GameFrame mainFrame;
    private Color DARK_GREEN= new Color(0 ,100 ,0);
    private Color BEIGE= new Color(245, 245, 220);


    /**
     * creates the PromotionFrame.
     * @param piece: the pawn piece being promoted
     * @param board: the board our game is being played on
     * @param move: the current frame being made
     * @param mainFrame: the current GameFrame we're playing on
     */
    public PromotionFrame(Pawn piece, Board board, Move move, GameFrame mainFrame) {
        this.board = board;
        this.piece = piece;
        this.mainFrame= mainFrame;
        boolean isWhite= piece.isWhite();

        mainFrame.setEnabled(false); //freezes the GameFrame until the user has finished with promotion.

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 443, 503);
        setVisible(true);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new GridLayout(0, 2, 0, 0));

        //text that instructs users to promote
        JLabel promoText1 = new JLabel("Promote y");
        promoText1.setOpaque(true);
        promoText1.setBackground(DARK_GREEN);
        promoText1.setForeground(BEIGE);
        promoText1.setFont(new Font("Trebuchet MS", Font.BOLD , 30));
        promoText1.setHorizontalAlignment(SwingConstants.RIGHT);
        contentPane.add(promoText1);
        JLabel promoText2 = new JLabel("our Pawn!");
        promoText2.setForeground(BEIGE);
        promoText2.setHorizontalAlignment(SwingConstants.LEFT);
        promoText2.setBackground(DARK_GREEN);
        promoText2.setFont(new Font("Trebuchet MS", Font.BOLD , 30));
        promoText2.setOpaque(true);
        contentPane.add(promoText2);

        //queen promotion button
        JButton queenPromoBtn = new JButton(new ImageIcon(new ImageIcon("06_classic2/w-queen2.png").getImage().getScaledInstance(90, 80, Image.SCALE_DEFAULT)));
        queenPromoBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                move.getEnd().addPiece(new Queen(isWhite, 9, 'Q', piece.getRow(), piece.getCol())); //promotes the pawn to a queen
                choosePiece(move);
            }
        });
        queenPromoBtn.setBackground(new Color(245, 245, 220));
        queenPromoBtn.setOpaque(true);
        contentPane.add(queenPromoBtn);

        //rook promotion button
        JButton rookPromoBtn = new JButton(new ImageIcon(new ImageIcon("06_classic2/w-rook2.png").getImage().getScaledInstance(90, 80, Image.SCALE_DEFAULT)));
        rookPromoBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                move.getEnd().addPiece(new Rook(isWhite, 5 , 'R', piece.getRow(), piece.getCol())); //promotes the pawn to a rook
                choosePiece(move);
            }
        });
        rookPromoBtn.setBackground(new Color(245, 245, 220));
        rookPromoBtn.setOpaque(true);
        contentPane.add(rookPromoBtn);

        //bishop promotion button
        JButton bishopPromoBtn = new JButton(new ImageIcon(new ImageIcon("06_classic2/w-bishop2.png").getImage().getScaledInstance(90, 80, Image.SCALE_DEFAULT)));
        bishopPromoBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                move.getEnd().addPiece(new Bishop(isWhite, 3, 'B', piece.getRow(), piece.getCol())); //promotes the pawn to a bishop
                choosePiece(move);
            }
        });
        bishopPromoBtn.setBackground(new Color(245, 245, 220));
        bishopPromoBtn.setOpaque(true);
        contentPane.add(bishopPromoBtn);

        //knight promotion button
        JButton knightPromoBtn = new JButton(new ImageIcon(new ImageIcon("06_classic2/w-knight2.png").getImage().getScaledInstance(90 , 80, Image.SCALE_DEFAULT)));
        knightPromoBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                move.getEnd().addPiece(new Knight(isWhite, 3, 'N', piece.getRow(), piece.getCol())); //promotes the pawn to a knight
                choosePiece(move);
            }
        });
        knightPromoBtn.setBackground(new Color(245, 245, 220));
        knightPromoBtn.setOpaque(true);
        contentPane.add(knightPromoBtn);

    }

    /**
     * ChoosePiece
     * method that performs the necessary actions required after the user has chosen the promotion piece.
     * @param move: our current move
     */
    public void choosePiece(Move move) {
        board.getLegal(); //reevaluate all legal moves with the new promoted piece
        mainFrame.setEnabled(true); //unfreeze GameFrame to continue the game
        dispose(); //closes the current frame
        mainFrame.getClient().sendData(Constants.MOVE_DATA + move.toString()); //sends relevant data to the client
    }
}
