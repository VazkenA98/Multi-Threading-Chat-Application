package com.controller;


import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.Socket;

public class SendFileFrame extends JFrame {

    public String name;
    public String thePersonIamChattingWith;
    public Socket socketOfSender;
    public String serverHost;
    public JButton btBrowse = new JButton();
    public JButton btSendFile = new JButton();
    public JLabel jLabel1 = new JLabel();
    public JLabel jLabel2 = new JLabel();
    public JProgressBar progressBar = new JProgressBar();
    public JTextField textFilePath = new JTextField();;
    public JTextField textReceiver = new JTextField();



    public SendFileFrame(String serverHost, String sender) {
        initComponents();
        this.serverHost = serverHost;
        this.name = sender;
    }

    public JTextField getTfReceiver() {
        return textReceiver;
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jLabel1.setFont(new Font("Arial", 0, 14));
        jLabel1.setText("Select a file:");
        textFilePath.setFont(new Font("Arial", 0, 12));
        btBrowse.setFont(new Font("Arial", 0, 12));
        btBrowse.setText("...");
        btBrowse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonBrowseActionPerformed(evt);
            }
        });

        jLabel2.setFont(new Font("Arial", 0, 14));
        jLabel2.setText("Enter user:");

        textReceiver.setFont(new Font("Arial", 0, 12));

        btSendFile.setFont(new Font("Arial", 0, 12));
        btSendFile.setText("Send");
        btSendFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSendFileActionPerformed(evt);
            }
        });

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(22, 22, 22)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(textReceiver, GroupLayout.PREFERRED_SIZE, 237, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(btSendFile))
                                        .addComponent(jLabel2)
                                        .addComponent(jLabel1)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(textFilePath, GroupLayout.PREFERRED_SIZE, 237, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(btBrowse, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addComponent(progressBar, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                                        .addComponent(textFilePath))
                                .addGap(37, 37, 37)
                                .addComponent(jLabel2)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(btSendFile, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(textReceiver))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                                .addComponent(progressBar, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );

        pack();
    }

    private void buttonBrowseActionPerformed(java.awt.event.ActionEvent evt) {
        displayOpenDialog();
    }

    private void buttonSendFileActionPerformed(java.awt.event.ActionEvent evt) {
        String receiver = textReceiver.getText();
        String filePath = textFilePath.getText();
        try {
            socketOfSender = new Socket(serverHost, 9999);
            new SendingFileThread(this.name, receiver, filePath, socketOfSender, this, null).start();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void displayOpenDialog() {
        JFileChooser chooser = new JFileChooser();
        int ans = chooser.showOpenDialog(this);
        if (ans == JFileChooser.APPROVE_OPTION) {
            textFilePath.setText(chooser.getSelectedFile().getAbsolutePath());
        } else textFilePath.setText("");
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
