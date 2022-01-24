package chessproject;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;
import javax.swing.JButton;

public class CreatePrivateRoomFrame extends JFrame {
    public static HashSet<String> roomCodes = new HashSet<String>(); //possibly make it private and add getters/setters
    private JComboBox colourChoices;
    private boolean closed = false;
    private String colourChosen;
    private String code = CodeGenerator.generateCode();
    private JPanel contentPane;

    /**
     * Create the frame.
     */
    public CreatePrivateRoomFrame(Client client) {
        JFrame frame = this;

        setTitle("Creating private room");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setBounds(100, 100, 292, 200);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(238, 238, 238));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel roomCodeLabel = new JLabel("Room code:");
        roomCodeLabel.setBounds(33, 33, 79, 16);
        contentPane.add(roomCodeLabel);

        JLabel codeValue = new JLabel(code);
        codeValue.setHorizontalAlignment(SwingConstants.CENTER);
        codeValue.setBounds(124, 33, 131, 16);
        contentPane.add(codeValue);

        JLabel colourLabel = new JLabel("Colour:");
        colourLabel.setBounds(60, 70, 52, 16);
        contentPane.add(colourLabel);

        String[] colours = {"white", "black", "random"};
        colourChoices = new JComboBox<>(colours);//use this one for drop down list
        //JComboBox colourChoices = new JComboBox();
        colourChoices.setBounds(134, 66, 121, 27);
        contentPane.add(colourChoices);

        JButton doneButton = new JButton("Done");
        doneButton.setForeground(new Color(0, 100, 0));
        doneButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                colourChosen = (String) (colourChoices.getSelectedItem());
                closed = true;
                frame.dispose();
            }
        });
        doneButton.setBounds(33, 112, 106, 29);
        contentPane.add(doneButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                client.quitGame(false);
                new HomeFrame();
            }
        });
        cancelButton.setForeground(new Color(178, 34, 34));
        cancelButton.setBounds(143, 112, 106, 29);
        contentPane.add(cancelButton);
        setVisible(true);
    }

    public boolean isClosed() {
        return closed;
    }
    public String getColourChosen() {
        if (colourChosen == null) {
            colourChosen = (String) (colourChoices.getSelectedItem());
        }
        return colourChosen;
    }

    public String getCode () {
        return code;
    }

}