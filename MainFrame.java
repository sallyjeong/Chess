package chessproject;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JList;

import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import javax.swing.border.EtchedBorder;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import java.awt.SystemColor;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

public class MainFrame extends JFrame {

    private JPanel contentPane;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MainFrame frame = new MainFrame();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * Create the frame.
     */
    public MainFrame() {
        //setting up the frame
        JFrame frame = this;
        setTitle("HomeFrame");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1313, 715);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        //main panel with instructions and settings
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(255, 255, 255));
        mainPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
        mainPanel.setBounds(14, 10, 184, 669);
        contentPane.add(mainPanel);
        mainPanel.setLayout(null);

        //chess label
        JLabel chessLabel = new JLabel("Chess");
        chessLabel.setBounds(71, 10, 40, 16);
        chessLabel.setFont(new Font("Apple Braille", Font.PLAIN, 13));
        mainPanel.add(chessLabel);

        //instructions button
        JButton instructionsButton = new JButton("Instructions");
        instructionsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new InstructionsFrame();
            }
        });
        instructionsButton.setBounds(27, 133, 134, 45);
        mainPanel.add(instructionsButton);

        //settings button
        JButton settingsButton = new JButton("Settings");
        settingsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new SettingsFrame();
            }
        });
        settingsButton.setBounds(26, 65, 134, 45);
        mainPanel.add(settingsButton);

        //lobby panel
        JPanel lobbyPanel = new JPanel();
        lobbyPanel.setBackground(new Color(255, 255, 255));
        lobbyPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
        lobbyPanel.setBounds(210, 10, 1095, 669);
        contentPane.add(lobbyPanel);
        lobbyPanel.setLayout(null);

        //lobby label
        JLabel lobbyLabel = new JLabel("Lobbies");
        lobbyLabel.setBounds(175, 10, 57, 16);
        lobbyPanel.add(lobbyLabel);

        JSeparator separator = new JSeparator();
        separator.setBounds(3, 30, 1086, 12);
        lobbyPanel.add(separator);

        //public label
        JLabel publicLabel = new JLabel("Public ");
        publicLabel.setBounds(15, 43, 61, 16);
        lobbyPanel.add(publicLabel);

        JSeparator separator1 = new JSeparator();
        separator1.setBounds(3, 203, 1086, 12);
        lobbyPanel.add(separator1);

        JButton enterRoomButton = new JButton("put variable for open server");
        enterRoomButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                String roomCode = "123"; // set the variable String roomCode based on the open server
                // create a Client with existing roomCode variable

                // new EnterUsernameFrame(roomCode);
            }
        });
        enterRoomButton.setHorizontalAlignment(SwingConstants.LEFT);
        enterRoomButton.setBounds(7, 69, 437, 23);
        lobbyPanel.add(enterRoomButton);

        JSeparator separator2 = new JSeparator();
        separator2.setBounds(3, 276, 1086, 12);
        lobbyPanel.add(separator2);

        JButton enterRoomButton2 = new JButton("put variable for open server");
        enterRoomButton2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                // new ();    new frame here
            }
        });

        enterRoomButton2.setHorizontalAlignment(SwingConstants.LEFT);
        enterRoomButton2.setBounds(7, 100, 437, 23);
        lobbyPanel.add(enterRoomButton2);

        JButton enterRoomButton3 = new JButton("put variable for open server");
        enterRoomButton3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                // new ();
            }
        });
        enterRoomButton3.setHorizontalAlignment(SwingConstants.LEFT);
        enterRoomButton3.setBounds(7, 130, 437, 23);
        lobbyPanel.add(enterRoomButton3);

        JLabel statusLabel = new JLabel("STATUS");
        statusLabel.setBounds(456, 43, 61, 16);
        lobbyPanel.add(statusLabel);

        JLabel status = new JLabel("var");
        status.setForeground(Color.RED);
        status.setHorizontalAlignment(SwingConstants.CENTER);
        status.setBounds(456, 72, 61, 16);
        lobbyPanel.add(status);

        JLabel status1 = new JLabel("var");
        status1.setHorizontalAlignment(SwingConstants.CENTER);
        status1.setForeground(new Color(50, 205, 50));
        status1.setBounds(456, 102, 61, 16);
        lobbyPanel.add(status1);

        JLabel status2 = new JLabel("var");
        status2.setHorizontalAlignment(SwingConstants.CENTER);
        status2.setForeground(new Color(50, 205, 50));
        status2.setBounds(456, 132, 61, 16);
        lobbyPanel.add(status2);

        JButton playButton = new JButton("Enter matchmaking");
        playButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new FindingRoomFrame();
            }
        });
        playButton.setBackground(SystemColor.windowBorder);
        playButton.setBounds(13, 160, 1062, 40);
        lobbyPanel.add(playButton);

        //private label
        JLabel privateLabel = new JLabel("Private");
        privateLabel.setBounds(19, 220, 61, 16);
        lobbyPanel.add(privateLabel);

        //enter room code button (for private room)
        JButton enterCodeButton = new JButton("Enter code");
        enterCodeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                // create Client with NO roomCode
                // create new PrivateRoomCodeFrame() in Client to get and store the variable;
            }
        });
        enterCodeButton.setBounds(11, 241, 510, 29);
        lobbyPanel.add(enterCodeButton);

        JButton createRoomLabel = new JButton("Create room");
        createRoomLabel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new CreatePrivateRoomFrame();
            }
        });
        createRoomLabel.setBounds(565, 241, 510, 29);
        lobbyPanel.add(createRoomLabel);

        // playing computer label
        JLabel computerLabel = new JLabel("Computer");
        computerLabel.setBounds(19, 290, 96, 16);
        lobbyPanel.add(computerLabel);

        //play computer button
        JButton playComputerButton = new JButton("Play computer");
        playComputerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new PlayComputerFrame();
            }
        });
        playComputerButton.setBackground(SystemColor.windowBorder);
        playComputerButton.setBounds(15, 315, 1060, 24);
        lobbyPanel.add(playComputerButton);

        JButton enterRoomButton4 = new JButton("put variable for open server");
        enterRoomButton4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                // new ();
            }
        });
        enterRoomButton4.setHorizontalAlignment(SwingConstants.LEFT);
        enterRoomButton4.setBounds(565, 69, 437, 23);
        lobbyPanel.add(enterRoomButton4);

        JButton enterRoomButton5 = new JButton("put variable for open server");
        enterRoomButton5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                // new ();
            }
        });
        enterRoomButton5.setHorizontalAlignment(SwingConstants.LEFT);
        enterRoomButton5.setBounds(565, 100, 437, 23);
        lobbyPanel.add(enterRoomButton5);

        JButton enterRoomButton6 = new JButton("put variable for open server");
        enterRoomButton6.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                // new ();
            }
        });
        enterRoomButton6.setHorizontalAlignment(SwingConstants.LEFT);
        enterRoomButton6.setBounds(565, 130, 437, 23);
        lobbyPanel.add(enterRoomButton6);

        JLabel status3 = new JLabel("STATUS");
        status3.setBounds(1014, 43, 61, 16);
        lobbyPanel.add(status3);

        JLabel status4 = new JLabel("var");
        status4.setHorizontalAlignment(SwingConstants.CENTER);
        status4.setForeground(Color.RED);
        status4.setBounds(1014, 72, 61, 16);
        lobbyPanel.add(status4);

        JLabel status5 = new JLabel("var");
        status5.setHorizontalAlignment(SwingConstants.CENTER);
        status5.setForeground(new Color(50, 205, 50));
        status5.setBounds(1014, 102, 61, 16);
        lobbyPanel.add(status5);

        JLabel status6 = new JLabel("var");
        status6.setHorizontalAlignment(SwingConstants.CENTER);
        status6.setForeground(new Color(50, 205, 50));
        status6.setBounds(1014, 132, 61, 16);
        lobbyPanel.add(status6);

        JSeparator separator3 = new JSeparator();
        separator3.setOrientation(SwingConstants.VERTICAL);
        separator3.setBounds(529, 69, 12, 84);
        lobbyPanel.add(separator3);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setVisible(true);
        scrollPane.setBounds(77, 408, 383, 200);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        lobbyPanel.add(scrollPane);
        scrollPane.setBackground(Color.PINK);

        JScrollBar scrollBar = new JScrollBar();
        scrollPane.setRowHeaderView(scrollBar);

        String categories[] = { "Household", "Office", "Extended Family",
                "Company (US)", "Company (World)", "Team", "Will",
                "Birthday Card List", "High School", "Country", "Continent",
                "Planet" };
        JList list = new JList(categories);
        scrollPane.setViewportView(list);

        JLabel lblNewLabel = new JLabel("Spectate public lobbies");
        scrollPane.setColumnHeaderView(lblNewLabel);
    }
}
