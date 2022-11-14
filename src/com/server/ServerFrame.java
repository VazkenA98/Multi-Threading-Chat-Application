package com.server;


import com.view.RoomPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ServerFrame extends JFrame implements Runnable {

    public JButton buttonStart = new JButton("Start server");
    public JButton buttonStop = new JButton("Stop server");
    public JTextArea textArea = new JTextArea();
    private ServerSocket serverSocket;
    public SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
    private ServerThread serverThread;
    public JMenuBar menuBar = new JMenuBar();
    public JMenu menuSettings = new JMenu();
    public JMenuItem itemAddRoom = new JMenuItem();
    public JMenuItem itemRemoveRoom = new JMenuItem();
    public JPanel p1 = new JPanel();
    public JPanel p2 = new JPanel();
    public JPanel panel = new JPanel(new BorderLayout());
    public JPanel panelButton = new JPanel();
    public JLabel labelStateServer = new JLabel("        Server logs");


    public ServerFrame() {
        menuSettings.setText("Settings");
        menuSettings.setEnabled(false);
        itemAddRoom.setText("Add room");
        itemRemoveRoom.setText("Remove room");
        menuSettings.add(itemAddRoom);
        menuSettings.add(itemRemoveRoom);
        menuBar.add(menuSettings);
        setJMenuBar(menuBar);

        textArea.setEditable(false);
        textArea.setBackground(new Color(0, 0, 0));
        textArea.setForeground(new Color(47, 132, 47));
        JScrollPane scroll = new JScrollPane();
        scroll.setViewportView(textArea);
        scroll.setPreferredSize(new Dimension(400, 400));

        buttonStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                buttonStartEvent();
            }
        });

        buttonStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                buttonStopEvent();
            }
        });
        buttonStart.setEnabled(true);
        buttonStop.setEnabled(false);

        panelButton.add(buttonStart);
        panelButton.add(buttonStop);

        p1.setPreferredSize(new Dimension(30, 30));
        p2.setPreferredSize(new Dimension(30, 30));

        panel.add(labelStateServer, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(panelButton, BorderLayout.SOUTH);
        panel.add(p1, BorderLayout.WEST);
        panel.add(p2, BorderLayout.EAST);

        this.add(panel);
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        itemAddRoom.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                String addedRoomName = JOptionPane.showInputDialog(ServerFrame.this, "Enter room name to add");
                if (addedRoomName != null) {
                    String msg = RoomPanel.addRoom(addedRoomName);
                    JOptionPane.showMessageDialog(ServerFrame.this, msg);
                }
            }
        });

        itemRemoveRoom.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                String deletedRoomName = JOptionPane.showInputDialog(ServerFrame.this, "Enter room name to remove");
                if (deletedRoomName != null) {
                    String msg = RoomPanel.deleteRoom(deletedRoomName);
                    JOptionPane.showMessageDialog(ServerFrame.this, msg);
                }
            }
        });
    }

    public void appendMessage(String message) {
        textArea.append(message);
        textArea.setCaretPosition(textArea.getText().length() - 1);
    }

    private void startServer() {
        try {
            serverSocket = new ServerSocket(9999);
            appendMessage("[" + simpleDateFormat.format(new Date()) + "] Server is running on host [" + InetAddress.getLocalHost().getHostAddress() + "]");
            appendMessage("\n[" + simpleDateFormat.format(new Date()) + "] Now there's no one is connecting to server\n");

            while (true) {
                Socket socketOfServer = serverSocket.accept();
                serverThread = new ServerThread(socketOfServer);
                serverThread.taServer = this.textArea;
                serverThread.start();
            }

        } catch (java.net.SocketException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            this.setVisible(false);
            System.exit(0);
        }
    }

    private void buttonStartEvent() {
        new Thread(this).start();
        this.buttonStart.setEnabled(false);
        this.buttonStop.setEnabled(true);
        this.menuSettings.setEnabled(true);
    }

    private void buttonStopEvent() {
        int ans = JOptionPane.showConfirmDialog(this, "Are you sure to stop server ?", "Stop the server", JOptionPane.YES_NO_OPTION);
        if (ans == JOptionPane.YES_OPTION) {
            try {
                if (serverThread != null) {
                    serverThread.notifyToAllUsers("Warning: Server has been closed!");
                }
                serverSocket.close();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public void run() {
        this.startServer();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
        ServerFrame serverFrame = new ServerFrame();
        serverFrame.setVisible(true);

    }

}

