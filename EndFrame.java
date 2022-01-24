package chessproject;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import java.awt.Color;

public class EndFrame extends JFrame {

    private JPanel contentPane;

    /**
     * Create the frame.
     */
    public EndFrame(GameFrame currentGameFrame) {
        currentGameFrame.dispose();
        JFrame frame = this;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel overLabel = new JLabel("GAME OVER");
        overLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
        overLabel.setBounds(164, 37, 120, 36);
        contentPane.add(overLabel);

        JLabel winnerLabel = new JLabel("New label");
        winnerLabel.setBounds(192, 96, 61, 16);
        contentPane.add(winnerLabel);

        JLabel resultLabel = new JLabel("New label");
        resultLabel.setBounds(192, 139, 61, 16);
        contentPane.add(resultLabel);

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
        exitButton.setForeground(new Color(0, 100, 0));
        exitButton.setBounds(167, 184, 117, 29);
        contentPane.add(exitButton);
        setVisible(true);
    }
}