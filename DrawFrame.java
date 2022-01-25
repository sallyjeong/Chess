package chessproject;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.Color;

public class DrawFrame extends JFrame {

    private JPanel contentPane;
    private JButton agreeButton;
    private String result = "";

    /**
     * Create the frame.
     */
    public DrawFrame() {
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel drawLabel = new JLabel("Opponent proposes to draw");
        drawLabel.setBounds(135, 86, 175, 16);
        contentPane.add(drawLabel);

        agreeButton = new JButton("Agree");
        agreeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                result = "confirmed";
            }
        });
        agreeButton.setForeground(new Color(0, 100, 0));
        agreeButton.setBounds(75, 136, 146, 29);
        contentPane.add(agreeButton);

        JButton rejectButton = new JButton("Reject");
        rejectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                result = "denied";
            }
        });
        rejectButton.setForeground(new Color(178, 34, 34));
        rejectButton.setBounds(229, 136, 146, 29);
        contentPane.add(rejectButton);
        setVisible(true);
    }

    public String getResult() {
        return result;
    }

}