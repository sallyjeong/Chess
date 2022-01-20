final JFrame frame = new JFrame();
frame.setLayout(new FlowLayout());
JButton next = new JButton("Next Step");
 frame.add(next);
 
 print.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.pack();
