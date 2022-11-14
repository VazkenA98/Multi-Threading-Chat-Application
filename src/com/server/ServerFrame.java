package com.server;


import com.view.RoomPanel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.*;

public class ServerFrame extends JFrame implements Runnable {

    public JButton btStart, btStop;
    public JTextArea taInfo;
    private ServerSocket serverSocket;
    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    private ServerThread serverThread;
    JMenuBar menuBar;
    JMenu menuSettings;
    JMenuItem itemAddRoom;JMenuItem itemRemoveRoom;



    public ServerFrame() {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel lbStateServer = new JLabel("        Server logs");

        menuBar = new JMenuBar();
        menuSettings = new JMenu();
        itemRemoveRoom = new JMenuItem();
        itemAddRoom = new JMenuItem();
        menuSettings.setText("Settings");
        itemAddRoom.setText("Add room");
        itemRemoveRoom.setText("Remove room");
        menuSettings.add(itemAddRoom);
        menuSettings.add(itemRemoveRoom);
        menuBar.add(menuSettings);
        setJMenuBar(menuBar);

        taInfo = new JTextArea();
        taInfo.setEditable(false);
        taInfo.setBackground(new Color(0, 0, 0));
        taInfo.setForeground(new Color(47, 132, 47));
        JScrollPane scroll = new JScrollPane();
        scroll.setViewportView(taInfo);
        scroll.setPreferredSize(new Dimension(400, 400));

        btStart = new JButton("Start server");
        btStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                btStartEvent(ae);
            }
        });

        btStop = new JButton("Stop server");
        btStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                btStopEvent(ae);
            }
        });
        btStart.setEnabled(true);
        btStop.setEnabled(false);

        JPanel panelBtn = new JPanel();
        panelBtn.add(btStart);
        panelBtn.add(btStop);

        JPanel p1 = new JPanel();
        p1.setPreferredSize(new Dimension(30, 30));
        JPanel p2 = new JPanel();
        p2.setPreferredSize(new Dimension(30, 30));

        panel.add(lbStateServer, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(panelBtn, BorderLayout.SOUTH);
        panel.add(p1, BorderLayout.WEST);
        panel.add(p2, BorderLayout.EAST);


        this.add(panel);
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        itemAddRoom.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                String addedRoomName = JOptionPane.showInputDialog(ServerFrame.this, "Enter room name to add");
                if(addedRoomName != null) {
                    String msg = RoomPanel.addRoom(addedRoomName);
                    JOptionPane.showMessageDialog(ServerFrame.this, msg);
                }
            }
        });

        itemRemoveRoom.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                String deletedRoomName = JOptionPane.showInputDialog(ServerFrame.this, "Enter room name to remove");
                if(deletedRoomName != null) {
                    String msg = RoomPanel.deleteRoom(deletedRoomName);
                    JOptionPane.showMessageDialog(ServerFrame.this, msg);
                }
            }
        });
    }

    public void appendMessage(String message) {
        taInfo.append(message);
        taInfo.setCaretPosition(taInfo.getText().length() - 1);
    }

    private void startServer() {
        try {
            serverSocket = new ServerSocket(9999);
            appendMessage("["+sdf.format(new Date())+"] Server is running on host ["+ InetAddress.getLocalHost().getHostAddress()+"]");
            appendMessage("\n["+sdf.format(new Date())+"] Now there's no one is connecting to server\n");

            while(true) {
                Socket socketOfServer = serverSocket.accept();
                serverThread = new ServerThread(socketOfServer);
                serverThread.taServer = this.taInfo;
                serverThread.start();
            }

        } catch (java.net.SocketException e) {
            System.exit(0);
        } catch (IOException ex) {
            Logger.getLogger(ServerFrame.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            this.setVisible(false);
            System.exit(0);
        }
    }

    private void btStartEvent(ActionEvent ae) {

        new Thread(this).start();
        //startServer();
        this.btStart.setEnabled(false);
        this.btStop.setEnabled(true);
    }

    private void btStopEvent(ActionEvent ae) {
        int kq = JOptionPane.showConfirmDialog(this, "Are you sure to close server?", "Close the server", JOptionPane.YES_NO_OPTION);
        if(kq == JOptionPane.YES_OPTION) {
            try {

                if(serverThread != null) {
                    serverThread.notifyToAllUsers("Warnning: Server has been closed!");
                }

                serverSocket.close();
            } catch (IOException ex) {
                Logger.getLogger(ServerFrame.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(ServerFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        ServerFrame serverFrame = new ServerFrame();
        serverFrame.setVisible(true);

    }

}

