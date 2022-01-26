package chessproject;

// imports
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.Color;

/** [DrawFrame.java]
 * The JFrame that opens when the client receives opponent's draw request
 * @author Katherine Liu, Rachel Liu
 * @version 1.0 Jan 25, 2022
 */
public class DrawFrame extends JFrame {

    private JPanel contentPane;
    private JButton agreeButton;
    private String result = "";

    /**
     * DrawFrame
     * This constructor creates a DrawFrame
     * @param gameFrame is the GameFrame the game is on
     */
    public DrawFrame(GameFrame gameFrame) {
        gameFrame.setEnabled(false); // user must respond to request before continuing gam
        
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
            	System.out.println("yo");
                gameFrame.setEnabled(true);
                result = "confirmed";
            }
        });
        agreeButton.setForeground(new Color(0, 100, 0));
        agreeButton.setBounds(75, 136, 146, 29);
        contentPane.add(agreeButton);

        JButton rejectButton = new JButton("Reject");
        rejectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	System.out.println("yoo");
                gameFrame.setEnabled(true);
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
