package chessproject;

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

public class PromotionFrame extends JFrame implements Runnable{

    private JPanel contentPane;
    private int choice;
    private JFrame frame;



    /**
     * Launch the application.
     */

    /**
     * Create the frame.
     */
    public PromotionFrame() {
        frame= this;
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setBounds(100, 100, 261, 446);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        SpringLayout sl_contentPane = new SpringLayout();
        contentPane.setLayout(sl_contentPane);

        JLabel lblNewLabel = new JLabel(new ImageIcon(new ImageIcon("06_classic2\\w-queen2.png").getImage().getScaledInstance(90, 80, Image.SCALE_DEFAULT)));
        sl_contentPane.putConstraint(SpringLayout.NORTH, lblNewLabel, 10, SpringLayout.NORTH, contentPane);
        sl_contentPane.putConstraint(SpringLayout.WEST, lblNewLabel, 10, SpringLayout.WEST, contentPane);
        sl_contentPane.putConstraint(SpringLayout.SOUTH, lblNewLabel, 92, SpringLayout.NORTH, contentPane);
        contentPane.add(lblNewLabel);

        JLabel lblNewLabel_1 = new JLabel(new ImageIcon(new ImageIcon("06_classic2\\w-rook2.png").getImage().getScaledInstance(90, 80, Image.SCALE_DEFAULT)));
        sl_contentPane.putConstraint(SpringLayout.NORTH, lblNewLabel_1, 19, SpringLayout.SOUTH, lblNewLabel);
        sl_contentPane.putConstraint(SpringLayout.WEST, lblNewLabel_1, 10, SpringLayout.WEST, contentPane);
        sl_contentPane.putConstraint(SpringLayout.EAST, lblNewLabel_1, -135, SpringLayout.EAST, contentPane);
        contentPane.add(lblNewLabel_1);

        JLabel lblNewLabel_1_1 = new JLabel(new ImageIcon(new ImageIcon("06_classic2\\w-bishop2.png").getImage().getScaledInstance(90, 80, Image.SCALE_DEFAULT)));
        sl_contentPane.putConstraint(SpringLayout.SOUTH, lblNewLabel_1, -17, SpringLayout.NORTH, lblNewLabel_1_1);
        sl_contentPane.putConstraint(SpringLayout.WEST, lblNewLabel_1_1, 10, SpringLayout.WEST, contentPane);
        sl_contentPane.putConstraint(SpringLayout.EAST, lblNewLabel_1_1, -135, SpringLayout.EAST, contentPane);
        sl_contentPane.putConstraint(SpringLayout.NORTH, lblNewLabel_1_1, 210, SpringLayout.NORTH, contentPane);
        contentPane.add(lblNewLabel_1_1);

        JLabel lblNewLabel_1_1_1 = new JLabel(new ImageIcon(new ImageIcon("06_classic2\\w-knight2.png").getImage().getScaledInstance(90	, 80, Image.SCALE_DEFAULT)));
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
                choice= 1;
                frame.dispose();
            }
        });
        contentPane.add(btnNewButton);

        JButton btnNewButton_1 = new JButton("Rook");
        sl_contentPane.putConstraint(SpringLayout.NORTH, btnNewButton_1, 40, SpringLayout.SOUTH, btnNewButton);
        sl_contentPane.putConstraint(SpringLayout.WEST, btnNewButton_1, 0, SpringLayout.WEST, btnNewButton);
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                choice= 2;
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
                choice= 3;
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
                choice= 4;
                frame.dispose();
            }
        });
        contentPane.add(btnNewButton_1_1_1);

        JButton btnNewButton_2 = new JButton("BACK");
        contentPane.add(btnNewButton_2);
        btnNewButton_2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                choice= 0;
                frame.dispose();
            }
        });

    }

    public int getChoice() {
        return choice;
    }

    @Override
    public void run() {
        frame.setVisible(true);

    }
}
