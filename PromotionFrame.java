

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



    /**
     * Launch the application.
     */


    /**
     * Create the frame.
     */
    public PromotionFrame() {

        JFrame frame= this;
        this.setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 418, 462);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        SpringLayout sl_contentPane = new SpringLayout();
        contentPane.setLayout(sl_contentPane);

        JLabel lblNewLabel = new JLabel(new ImageIcon(new ImageIcon("06_classic2\\w-queen2.png").getImage().getScaledInstance(80, 80, Image.SCALE_DEFAULT)));
        sl_contentPane.putConstraint(SpringLayout.NORTH, lblNewLabel, 10, SpringLayout.NORTH, contentPane);
        sl_contentPane.putConstraint(SpringLayout.WEST, lblNewLabel, 29, SpringLayout.WEST, contentPane);
        sl_contentPane.putConstraint(SpringLayout.EAST, lblNewLabel, -283, SpringLayout.EAST, contentPane);
        contentPane.add(lblNewLabel);

        JLabel lblNewLabel_1 = new JLabel(new ImageIcon(new ImageIcon("06_classic2\\w-rook2.png").getImage().getScaledInstance(80, 80, Image.SCALE_DEFAULT)));
        sl_contentPane.putConstraint(SpringLayout.WEST, lblNewLabel_1, 29, SpringLayout.WEST, contentPane);
        sl_contentPane.putConstraint(SpringLayout.SOUTH, lblNewLabel, -26, SpringLayout.NORTH, lblNewLabel_1);
        sl_contentPane.putConstraint(SpringLayout.NORTH, lblNewLabel_1, 117, SpringLayout.NORTH, contentPane);
        contentPane.add(lblNewLabel_1);

        JLabel lblNewLabel_1_1 = new JLabel(new ImageIcon(new ImageIcon("06_classic2\\w-bishop2.png").getImage().getScaledInstance(80, 80, Image.SCALE_DEFAULT)));
        sl_contentPane.putConstraint(SpringLayout.WEST, lblNewLabel_1_1, 24, SpringLayout.WEST, contentPane);
        sl_contentPane.putConstraint(SpringLayout.SOUTH, lblNewLabel_1, -11, SpringLayout.NORTH, lblNewLabel_1_1);
        sl_contentPane.putConstraint(SpringLayout.NORTH, lblNewLabel_1_1, 209, SpringLayout.NORTH, contentPane);
        contentPane.add(lblNewLabel_1_1);

        JLabel lblNewLabel_1_2 = new JLabel(new ImageIcon(new ImageIcon("06_classic2\\w-knight2.png").getImage().getScaledInstance(80, 80, Image.SCALE_DEFAULT)));

        sl_contentPane.putConstraint(SpringLayout.SOUTH, lblNewLabel_1_1, -26, SpringLayout.NORTH, lblNewLabel_1_2);
        sl_contentPane.putConstraint(SpringLayout.NORTH, lblNewLabel_1_2, 311, SpringLayout.NORTH, contentPane);
        sl_contentPane.putConstraint(SpringLayout.WEST, lblNewLabel_1_2, 0, SpringLayout.WEST, lblNewLabel);
        sl_contentPane.putConstraint(SpringLayout.SOUTH, lblNewLabel_1_2, -21, SpringLayout.SOUTH, contentPane);
        sl_contentPane.putConstraint(SpringLayout.EAST, lblNewLabel_1_2, -283, SpringLayout.EAST, contentPane);
        contentPane.add(lblNewLabel_1_2);

        JButton btnNewButton = new JButton("Queen");
        sl_contentPane.putConstraint(SpringLayout.WEST, btnNewButton, 0, SpringLayout.WEST, btnNewButton);
        sl_contentPane.putConstraint(SpringLayout.NORTH, btnNewButton, 21, SpringLayout.NORTH, contentPane);
        sl_contentPane.putConstraint(SpringLayout.WEST, btnNewButton, 63, SpringLayout.EAST, lblNewLabel);
        sl_contentPane.putConstraint(SpringLayout.SOUTH, btnNewButton, 0, SpringLayout.SOUTH, lblNewLabel);
        sl_contentPane.putConstraint(SpringLayout.EAST, btnNewButton, -112, SpringLayout.EAST, contentPane);
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                choice= 1;
                frame.dispose();
            }
        });
        contentPane.add(btnNewButton);

        JButton btnNewButton_2 = new JButton("New button");
        sl_contentPane.putConstraint(SpringLayout.EAST, btnNewButton_2, 0, SpringLayout.EAST, btnNewButton_2);
        sl_contentPane.putConstraint(SpringLayout.WEST, btnNewButton_2, 172, SpringLayout.WEST, contentPane);
        sl_contentPane.putConstraint(SpringLayout.EAST, btnNewButton_2, -96, SpringLayout.EAST, contentPane);
        sl_contentPane.putConstraint(SpringLayout.EAST, lblNewLabel_1, -68, SpringLayout.WEST, btnNewButton_2);
        sl_contentPane.putConstraint(SpringLayout.SOUTH, btnNewButton_2, 0, SpringLayout.SOUTH, lblNewLabel_1);
        sl_contentPane.putConstraint(SpringLayout.NORTH, btnNewButton_2, 50, SpringLayout.SOUTH, btnNewButton);
        btnNewButton_2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                choice= 2;
            }
        });
        contentPane.add(btnNewButton_2);

        JButton btnNewButton_3 = new JButton("New button");
        sl_contentPane.putConstraint(SpringLayout.NORTH, btnNewButton_3, 40, SpringLayout.SOUTH, btnNewButton_3);
        sl_contentPane.putConstraint(SpringLayout.EAST, lblNewLabel_1_1, -68, SpringLayout.WEST, btnNewButton_3);
        sl_contentPane.putConstraint(SpringLayout.SOUTH, btnNewButton_3, 102, SpringLayout.SOUTH, btnNewButton_2);
        sl_contentPane.putConstraint(SpringLayout.EAST, btnNewButton_3, 0, SpringLayout.EAST, btnNewButton_2);
        sl_contentPane.putConstraint(SpringLayout.NORTH, btnNewButton_3, 45, SpringLayout.SOUTH, btnNewButton_2);
        sl_contentPane.putConstraint(SpringLayout.WEST, btnNewButton_3, 0, SpringLayout.WEST, btnNewButton);
        btnNewButton_3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                choice= 3;
            }
        });
        contentPane.add(btnNewButton_3);

        JButton btnNewButton_4 = new JButton("New button");
        sl_contentPane.putConstraint(SpringLayout.NORTH, btnNewButton_3, -40, SpringLayout.SOUTH, btnNewButton_4);
        sl_contentPane.putConstraint(SpringLayout.EAST, lblNewLabel_1_1, -68, SpringLayout.WEST, btnNewButton_4);
        sl_contentPane.putConstraint(SpringLayout.SOUTH, btnNewButton_3, 102, SpringLayout.SOUTH, btnNewButton_2);
        sl_contentPane.putConstraint(SpringLayout.EAST, btnNewButton_3, 0, SpringLayout.EAST, btnNewButton_2);
        sl_contentPane.putConstraint(SpringLayout.NORTH, btnNewButton_3, 45, SpringLayout.SOUTH, btnNewButton_2);
        sl_contentPane.putConstraint(SpringLayout.WEST, btnNewButton_3, 0, SpringLayout.WEST, btnNewButton);
        btnNewButton_3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                choice= 4;
            }
        });
        contentPane.add(btnNewButton_4);
    }

    public int getChoice() {
        return choice;
    }

    @Override
    public void run() {

    }
}
