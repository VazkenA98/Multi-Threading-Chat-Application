package com.controller;


import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SendFileFrame extends JFrame {

    public String name;
    public String thePersonIamChattingWith;
    public Socket socketOfSender;
    public String serverHost;

    private JButton btBrowse;
    private JButton btSendFile;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel lbNote;
    private JLabel lbNote2;
    private JProgressBar progressBar;
    private JTextField tfFilePath;
    private JTextField tfReceiver;


    public SendFileFrame(String serverHost, String sender) {
        initComponents();
        this.serverHost = serverHost;
        this.name = sender;
    }

    public JTextField getTfFilePath() {
        return tfFilePath;
    }

    public JTextField getTfReceiver() {
        return tfReceiver;
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        jLabel1 = new JLabel();
        tfFilePath = new JTextField();
        btBrowse = new JButton();
        jLabel2 = new JLabel();
        tfReceiver = new JTextField();
        btSendFile = new JButton();
        lbNote = new JLabel();
        progressBar = new JProgressBar();
        lbNote2 = new JLabel();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new Font("Arial", 0, 14)); // NOI18N
        jLabel1.setText("Select a file:");

        tfFilePath.setFont(new Font("Arial", 0, 12)); // NOI18N

        btBrowse.setFont(new Font("Arial", 0, 12)); // NOI18N
        btBrowse.setText("...");
        btBrowse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btBrowseActionPerformed(evt);
            }
        });

        jLabel2.setFont(new Font("Arial", 0, 14)); // NOI18N
        jLabel2.setText("Enter reciever:");

        tfReceiver.setFont(new Font("Arial", 0, 12)); // NOI18N

        btSendFile.setFont(new Font("Arial", 0, 12)); // NOI18N
        btSendFile.setText("Send");
        btSendFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSendFileActionPerformed(evt);
            }
        });

        lbNote.setFont(new Font("Arial", 2, 11)); // NOI18N
        lbNote.setText("Note that:");

        lbNote2.setFont(new Font("Arial", 2, 11)); // NOI18N
        lbNote2.setText("    -Make sure to enter the right receiver's name");

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(22, 22, 22)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(tfReceiver, GroupLayout.PREFERRED_SIZE, 237, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(btSendFile))
                                        .addComponent(jLabel2)
                                        .addComponent(jLabel1)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(tfFilePath, GroupLayout.PREFERRED_SIZE, 237, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(btBrowse, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addComponent(progressBar, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(lbNote, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(lbNote2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap(33, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(40, 40, 40)
                                .addComponent(jLabel1)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(btBrowse, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(tfFilePath))
                                .addGap(37, 37, 37)
                                .addComponent(jLabel2)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(btSendFile, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(tfReceiver))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lbNote)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbNote2)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                                .addComponent(progressBar, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );

        pack();
    }

    private void btBrowseActionPerformed(java.awt.event.ActionEvent evt) {
        displayOpenDialog();
    }

    private void btSendFileActionPerformed(java.awt.event.ActionEvent evt) {
        String receiver = tfReceiver.getText();
        String filePath = tfFilePath.getText();
        try {
            socketOfSender = new Socket(serverHost, 9999);
            new SendingFileThread(this.name, receiver, filePath, socketOfSender, this, null).start();
        } catch (IOException ex) {
            Logger.getLogger(ClientFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void displayOpenDialog() {
        JFileChooser chooser = new JFileChooser();
        int kq = chooser.showOpenDialog(this);
        if (kq == JFileChooser.APPROVE_OPTION) {
            tfFilePath.setText(chooser.getSelectedFile().getAbsolutePath());
        } else tfFilePath.setText("");
    }


    public static void main(String args[]) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SendFileFrame("localhost", null).setVisible(true);
            }
        });
    }

}
