package chessproject;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.Color;

public class PlayComputerFrame extends JFrame {

    private JPanel contentPane;
    private JComboBox colourChoices, difficulty;

    /**
     * Create the frame.
     */
    public PlayComputerFrame(JFrame homeFrame) {
        JFrame frame = this;
        setTitle("Play Computer");

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setBounds(100, 100, 292, 200);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel colourLabel = new JLabel("Colour:");
        colourLabel.setBounds(63, 34, 55, 16);
        contentPane.add(colourLabel);

        JLabel levelLabel = new JLabel("Difficulty:");
        levelLabel.setBounds(47, 79, 71, 16);
        contentPane.add(levelLabel);

        String[] colours = {"black", "white"};
        colourChoices = new JComboBox<>(colours);//use this one for drop down list
        //JComboBox colourChoices = new JComboBox();
        colourChoices.setBounds(130, 30, 125, 27);
        contentPane.add(colourChoices);

        String[] levels = {"easy", "medium"};
        difficulty = new JComboBox<>(levels);//use this one for drop down list
        //JComboBox difficulty = new JComboBox();
        difficulty.setBounds(130, 75, 125, 27);
        contentPane.add(difficulty);

        JButton doneButton = new JButton("Done");
        doneButton.setForeground(new Color(0, 100, 0));
        doneButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new GameFrame((String)difficulty.getSelectedItem(), getWhite());
            }
        });
        doneButton.setBounds(47, 122, 98, 29);
        contentPane.add(doneButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                homeFrame.setVisible(true);            }
        });
        cancelButton.setForeground(new Color(178, 34, 34));
        cancelButton.setBounds(157, 122, 98, 29);
        contentPane.add(cancelButton);

        setVisible(true);
    }

    public boolean getWhite() {
        if (colourChoices.getSelectedItem().equals("white")) {
            return true;
        } else {
            return false;
        }
    }
}