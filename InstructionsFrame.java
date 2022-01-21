package chessproject;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class InstructionsFrame extends JFrame {

    private JPanel contentPane;

    /**
     * Launch the application.
     */
//    public static void main(String[] args) {
////        EventQueue.invokeLater(new Runnable() {
////            public void run() {
////                try {
////                    InstructionsFrame frame = new InstructionsFrame();
////                    frame.setVisible(true);
////                } catch (Exception e) {
////                    e.printStackTrace();
////                }
////            }
////        });
//    }

    /**
     * Create the frame.
     */
    public InstructionsFrame() {
        setTitle("Instructions");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setBounds(100, 100, 1313, 715);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);
        setVisible(true);
    }

}