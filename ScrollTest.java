package chessproject;

/* TextDemo.java requires no other files. */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

//
//public class ScrollTest{
//    private JFrame f; //Main frame
//    private JTextArea ta; // Text area
//    private JScrollPane sbrText; // Scroll pane for text area
//    private JButton btnQuit; // Quit Program
//
//    public ScrollTest(){ //Constructor
//        // Create Frame
//        f = new JFrame("Swing Demo");
//        f.getContentPane().setLayout(new FlowLayout());
//
//        // Create Scrolling Text Area in Swing
//        ta = new JTextArea("", 5, 50);
//        ta.setLineWrap(true);
//        sbrText = new JScrollPane(ta);
//        sbrText.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
//
//        // Create Quit Button
//        btnQuit = new JButton("Quit");
//        btnQuit.addActionListener(
//                new ActionListener(){
//                    public void actionPerformed(ActionEvent e){
//                        System.exit(0);
//                    }
//                }
//        );
//
//    }
//
//    public void launchFrame(){ // Create Layout
//        // Add text area and button to frame
//        f.getContentPane().add(sbrText);
//        f.getContentPane().add(btnQuit);
//
//        // Close when the close button is clicked
//        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        //Display Frame
//        f.pack(); // Adjusts frame to size of components
//        f.setVisible(true);
//    }
//
//    public static void main(String args[]){
//        ScrollTest gui = new ScrollTest();
//        gui.launchFrame();
//    }
//}

public class ScrollTest extends JPanel implements ActionListener {
    protected JTextField textField;
    protected JTextArea textArea;
    private final static String newline = "\n";

    public ScrollTest() {
        super(new GridBagLayout());

        textField = new JTextField(20);
        textField.addActionListener(this);

        textArea = new JTextArea(5, 20);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        //Add Components to this panel.
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;

        c.fill = GridBagConstraints.HORIZONTAL;
        add(textField, c);

        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        add(scrollPane, c);
    }

    public void actionPerformed(ActionEvent evt) {
        String text = textField.getText();
        textArea.append(text + newline);
        textField.selectAll();

        //Make sure the new text is visible, even if there
        //was a selection in the text area.
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("TextDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add contents to the window.
        frame.add(new ScrollTest());

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}

//package chessproject;
//
//import java.awt.BorderLayout;
//import java.awt.Color;
//import java.awt.EventQueue;
//
//import javax.swing.*;
//import javax.swing.border.EmptyBorder;
//
//public class ScrollTest extends JFrame {
//
//    private JPanel contentPane;
//
//    /**
//     * Launch the application.
//     */
//    public static void main(String[] args) {
//        EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                try {
//                    ScrollTest frame = new ScrollTest();
//                    frame.setVisible(true);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }
//
//    /**
//     * Create the frame.
//     */
//    public ScrollTest() {
//        JFrame frame = this;
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setBounds(100, 100, 450, 300);
//        contentPane = new JPanel();
//        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
//        setContentPane(contentPane);
//        contentPane.setLayout(null);
//
//        JPanel panel = new JPanel();
//        panel.setBounds(6, 6, 438, 260);
//        contentPane.add(panel);
//
//        JTextArea textArea = new JTextArea("TEST");
//        textArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
//        textArea.setSize(400,400);
//
//        textArea.setLineWrap(true);
//        textArea.setEditable(false);
//        textArea.setVisible(true);
//
//        JScrollPane scroll = new JScrollPane (textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
////        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
////        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
//
//        scroll.setViewportView(textArea);
//        scroll.setVisible(true);
//        scroll.setBounds(6,6,438,260);
//        panel.add(scroll);
//        panel.setVisible(true);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        textArea.setText("getContentPane().setLayout(\n"
//                + "                new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));\n"
//                + "        setResizable(false);\n"
//                + "\n"
//                + "        JTextArea window1 = new JTextArea(\"text\");\n"
//                + "        window1.setEditable(false);\n"
//                + "        window1.setBorder(BorderFactory.createLineBorder(Color.BLACK));\n"
//                + "        window1.setLineWrap(true);\n"
//                + "\n"
//                + "\n"
//                + "        JScrollPane scroll1 = new JScrollPane(window1);\n"
//                + "        scroll1.setPreferredSize(new Dimension(200, 250));\n"
//                + "        scroll1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);\n"
//                + "        scroll1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);\n"
//                + "        add(scroll1);\n"
//                + "\n"
//                + "        JTextArea window2 = new JTextArea();\n"
//                + "        window2.setEditable(true);\n"
//                + "        window2.setBorder(BorderFactory.createLineBorder(Color.BLACK));\n"
//                + "        window2.setLineWrap(true);\n"
//                + "        add(window2);\n"
//                + "\n"
//                + "        JScrollPane scroll2 = new JScrollPane(window2);\n"
//                + "        scroll2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);\n"
//                + "        scroll2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);\n"
//                + "        scroll2.setPreferredSize(new Dimension(100, 50));\n"
//                + "        add(scroll2);\n"
//                + "\n"
//                + "        setDefaultCloseOperation(EXIT_ON_CLOSE);");
//
//
//    }
//}