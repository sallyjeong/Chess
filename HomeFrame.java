package chessproject;

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
import java.awt.SystemColor;
import javax.swing.JScrollPane;

import java.util.ArrayList;

public class HomeFrame extends JFrame {

    private JPanel contentPane;
    private Client thisClient;
    public static ArrayList<String> roomNames = new ArrayList<>();
    public static JList list;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    HomeFrame frame = new HomeFrame();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public HomeFrame() {
        thisClient = new Client();

        //setting up the frame
        JFrame frame = this;
        setTitle("HomeFrame");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setBounds(100, 100, 1313, 715);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        //main panel with instructions and settings
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(255, 255, 255));
        mainPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
        mainPanel.setBounds(14, 10, 395, 669);
        contentPane.add(mainPanel);
        mainPanel.setLayout(null);

        //chess label
        JLabel chessLabel = new JLabel("Chess");
        chessLabel.setBounds(170, 6, 40, 16);
        chessLabel.setFont(new Font("Apple Braille", Font.PLAIN, 13));
        mainPanel.add(chessLabel);

        //quit button
        JButton quitButton = new JButton("Quit");
        quitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                thisClient.quitGame(true);
            }
        });
        quitButton.setBounds(26, 65, 343, 80);
        mainPanel.add(quitButton);

        //lobby panel
        JPanel lobbyPanel = new JPanel();
        lobbyPanel.setBackground(new Color(255, 255, 255));
        lobbyPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
        lobbyPanel.setBounds(426, 9, 876, 669);
        contentPane.add(lobbyPanel);
        lobbyPanel.setLayout(null);

        //lobby label
        JLabel lobbyLabel = new JLabel("Lobbies");
        lobbyLabel.setBounds(24, 7, 57, 16);
        lobbyPanel.add(lobbyLabel);

        JSeparator separator = new JSeparator();
        separator.setBounds(24, 31, 839, 12);
        lobbyPanel.add(separator);

        //public label
        JLabel publicLabel = new JLabel("Public ");
        publicLabel.setBounds(22, 52, 61, 16);
        lobbyPanel.add(publicLabel);

        JSeparator separator1 = new JSeparator();
        separator1.setBounds(22, 326, 839, 12);
        lobbyPanel.add(separator1);

        JSeparator separator2 = new JSeparator();
        separator2.setBounds(22, 479, 839, 12);
        lobbyPanel.add(separator2);

        JButton playButton = new JButton("Enter matchmaking");
        playButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        thisClient.getUsernameInput();
                        thisClient.quickMatch();
                    }
                }).start();

            }
        });
        playButton.setBackground(SystemColor.windowBorder);
        playButton.setBounds(21, 229, 825, 80);
        lobbyPanel.add(playButton);

        //private label
        JLabel privateLabel = new JLabel("Private");
        privateLabel.setBounds(23, 352, 61, 16);
        lobbyPanel.add(privateLabel);

        //enter room code button (for private room)
        JButton enterCodeButton = new JButton("Join room");
        enterCodeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        thisClient.getUsernameInput();
                        thisClient.getRoomInput();
                    }
                }).start();
            }
        });
        enterCodeButton.setBounds(22, 384, 407, 80);
        lobbyPanel.add(enterCodeButton);

        // creating private room
        JButton createRoomLabel = new JButton("Create room");
        createRoomLabel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        thisClient.getUsernameInput();
                        thisClient.createRoom();
                    }
                }).start();
            }
        });

        createRoomLabel.setBounds(469, 384, 373, 80);
        lobbyPanel.add(createRoomLabel);

        // playing computer label
        JLabel computerLabel = new JLabel("Computer");
        computerLabel.setBounds(23, 505, 96, 16);
        lobbyPanel.add(computerLabel);

        //play computer button
        JButton playComputerButton = new JButton("Play computer");
        playComputerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new PlayComputerFrame(frame);
            }
        });
        playComputerButton.setBackground(SystemColor.windowBorder);
        playComputerButton.setBounds(23, 539, 825, 80);
        lobbyPanel.add(playComputerButton);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setVisible(true);
        scrollPane.setBounds(22, 82, 510, 129);
        lobbyPanel.add(scrollPane);
        scrollPane.setBackground(new Color(143, 188, 143));

        roomNames = thisClient.getRoomNames();

        list = new JList(roomNames.toArray());
        scrollPane.setViewportView(list);

        JLabel lblNewLabel = new JLabel("Spectate public lobbies");
        scrollPane.setColumnHeaderView(lblNewLabel);

        JButton confirmButton = new JButton("Spectate");
        confirmButton.setForeground(new Color(143, 188, 143));

        confirmButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String roomName = (String) list.getSelectedValue();
                if (roomName != null) {
                    frame.dispose();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            thisClient.getUsernameInput();
                            thisClient.spectate(roomName);

                        }
                    }).start();
                }
            }
        });

        confirmButton.setBounds(559, 82, 278, 57);
        lobbyPanel.add(confirmButton);

        JButton reloadButton = new JButton("Reload");
        reloadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                roomNames = thisClient.getRoomNames();
                System.out.println(roomNames);
                list.setListData(roomNames.toArray());

            }
        });
        reloadButton.setBounds(559, 151, 278, 57);
        lobbyPanel.add(reloadButton);

        setVisible(true);
    }
}
