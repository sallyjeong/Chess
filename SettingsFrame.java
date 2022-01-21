package chessproject;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class SettingsFrame extends JFrame {

    private JPanel contentPane;

    /**
     * Launch the application.
     */
//    public static void main(String[] args) {
//        SettingsFrame frame = new SettingsFrame();
////        EventQueue.invokeLater(new Runnable() {
////            public void run() {
////                try {
////                    SettingsFrame frame = new SettingsFrame();
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
    public SettingsFrame() {
        setTitle("Settings");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setBounds(100, 100, 1313, 715);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));

        setVisible(true);
    }

}