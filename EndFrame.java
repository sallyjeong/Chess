package chessproject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;

/** [EndFrame.java]
 * The JFrame that opens when the game ends
 * @author Rachel Liu, Katherine Liu
 * @version 1.0 Jan 25, 2022
 */
public class EndFrame extends JFrame {

    private JPanel contentPane;

    /**
     * EndFrame
     * This constructor creates an EndFrame
     * @param currentGameFrame is the game frame that the client is currently playing on
     * @param winner represents who has won the game
     * @param score displays the outcome of the game (black win, white win, or draw)
     */
    public EndFrame(JFrame currentGameFrame, String winner, String score) {
        JFrame frame = this;
        frame.setVisible(true);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel overLabel = new JLabel("GAME OVER");
        overLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
        overLabel.setBounds(100, 37, 250, 36);
        overLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(overLabel);

        JLabel winnerLabel = new JLabel(winner);
        winnerLabel.setBounds(100, 96, 250, 16);
        winnerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(winnerLabel);

        JLabel resultLabel = new JLabel(score);
        resultLabel.setBounds(100, 139, 250, 16);
        resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(resultLabel);

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                currentGameFrame.dispose();
            }
        });
        exitButton.setForeground(new Color(0, 100, 0));
        exitButton.setBounds(167, 184, 117, 29);
        contentPane.add(exitButton);
        setVisible(true);
        frame.toFront();
    }
}
