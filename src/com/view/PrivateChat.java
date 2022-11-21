package com.view;


import com.controller.ReceivingFileThread;
import com.controller.SendFileFrame;
import com.server.ServerCommands;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PrivateChat extends JFrame {

    public String sender;
    public String receiver;
    public String serverHost;
    public BufferedWriter bufferedWriter;
    public BufferedReader bufferedReader;
    public HTMLEditorKit htmlKit;
    public HTMLDocument htmlDoc;

    public JButton buttonFile = new JButton();
    public JButton buttonSend = new JButton();
    public JPanel jPanel1 = new JPanel();
    public JScrollPane jScrollPane1 = new JScrollPane();
    public JLabel lbReceiver = new JLabel();
    public JTextField tfInput = new JTextField();
    public JTextPane tpMessage = new JTextPane();


    public PrivateChat() {
        initComponents();
        htmlKit = new HTMLEditorKit();
        htmlDoc = new HTMLDocument();
        tpMessage.setEditorKit(htmlKit);
        tpMessage.setDocument(htmlDoc);
    }

    public PrivateChat(String sender, String receiver, String serverHost, BufferedWriter bufferedWriter, BufferedReader bufferedReader) {
        initComponents();
        this.sender = sender;
        this.receiver = receiver;
        this.serverHost = serverHost;
        this.bufferedWriter = bufferedWriter;
        this.bufferedReader = bufferedReader;

        htmlKit = new HTMLEditorKit();
        htmlDoc = new HTMLDocument();
        tpMessage.setEditorKit(htmlKit);
        tpMessage.setDocument(htmlDoc);
    }

    public JLabel getLbReceiver() {
        return lbReceiver;
    }

    public void sendToServer(String line) {
        try {
            this.bufferedWriter.write(line);
            this.bufferedWriter.newLine();
            this.bufferedWriter.flush();
        } catch (java.net.SocketException e) {
            JOptionPane.showMessageDialog(this, "Server is closed, can't send message!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NullPointerException | IOException e) {
            e.printStackTrace();
        }
    }

    public void appendMessageLeft(String msg1, String msg2) {
        try {
            htmlKit.insertHTML(htmlDoc, htmlDoc.getLength(), "<p style=\"color:black; padding: 3px; margin-top: 4px; margin-right:35px; text-align:left; font:normal 12px Tahoma;\"><span style=\"background-color:#f3f3f3;\"><b>" + msg1 + "</b><span style=\"color:black;\">" + msg2 + "</span></span></p>", 0, 0, null);
        } catch (BadLocationException | IOException ex) {
            Logger.getLogger(PrivateChat.class.getName()).log(Level.SEVERE, null, ex);
        }
        tpMessage.setCaretPosition(tpMessage.getDocument().getLength());
    }

    public void appendMessageLeft(String msg1, String msg2, String color1, String color2) {
        try {
            htmlKit.insertHTML(htmlDoc, htmlDoc.getLength(), "<p style=\"color:" + color1 + "; padding: 3px; margin-top: 4px; margin-right:35px; text-align:left; font:normal 12px Tahoma;\"><span><b>" + msg1 + "</b><span style=\"color:" + color2 + ";\">" + msg2 + "</span></span></p><br/>", 0, 0, null);
        } catch (BadLocationException | IOException ex) {
            Logger.getLogger(PrivateChat.class.getName()).log(Level.SEVERE, null, ex);
        }
        tpMessage.setCaretPosition(tpMessage.getDocument().getLength());
    }

    public void appendMessageRight(String msg1) {
        try {
            htmlKit.insertHTML(htmlDoc, htmlDoc.getLength(), "<p style=\"color:white; padding: 3px; margin-top: 4px; margin-left:35px; text-align:right; font:normal 12px Tahoma;\"><span style=\"background-color: #889eff; -webkit-border-radius: 10px;\">" + msg1 + "</span></p>", 0, 0, null);
        } catch (BadLocationException | IOException ex) {
            Logger.getLogger(PrivateChat.class.getName()).log(Level.SEVERE, null, ex);
        }
        tpMessage.setCaretPosition(tpMessage.getDocument().getLength());
    }

    public void insertButton(String sender, String fileName) {
        JButton button = new JButton(fileName);
        button.setName(fileName);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                downloadFile(fileName);
            }
        });
        appendMessageLeft(sender, " send a file ", "#00dddd", "#00ee11");
        tpMessage.setCaretPosition(tpMessage.getDocument().getLength() - 1);
        tpMessage.insertComponent(button);
    }

    private void downloadFile(String buttonName) {
        String myDownloadFolder;
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int kq = chooser.showSaveDialog(this);
        if (kq == JFileChooser.APPROVE_OPTION) {
            myDownloadFolder = chooser.getSelectedFile().getAbsolutePath();
        } else {
            myDownloadFolder = "D:";
        }

        try {
            Socket socketOfReceiver = new Socket(serverHost, 9999);
            new ReceivingFileThread(socketOfReceiver, myDownloadFolder, buttonName, this).start();
        } catch (IOException ex) {
            Logger.getLogger(PrivateChat.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    @SuppressWarnings("unchecked")
    private void initComponents() {

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        lbReceiver.setFont(new Font("Arial", 0, 16));
        lbReceiver.setText("Receiver");

        tpMessage.setEditable(false);
        jScrollPane1.setViewportView(tpMessage);

        tfInput.setFont(new Font("Arial", 0, 14));
        tfInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfInputActionPerformed(evt);
            }
        });

        buttonSend.setFont(new Font("Arial", 0, 14));
        buttonSend.setText("Send");
        buttonSend.setToolTipText("send a message");
        buttonSend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSendActionPerformed(evt);
            }
        });

        buttonFile.setFont(new Font("Arial", 0, 14));
        buttonFile.setText("File");
        buttonFile.setToolTipText("send a file");
        buttonFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btFileActionPerformed(evt);
            }
        });

        GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane1)
                                        .addComponent(tfInput)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addComponent(lbReceiver)
                                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addComponent(buttonSend)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(buttonFile)))
                                                .addGap(0, 156, Short.MAX_VALUE)))
                                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(lbReceiver)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 213, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(tfInput, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(buttonSend, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(buttonFile, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }

    private void tfInputActionPerformed(java.awt.event.ActionEvent evt) {
        sendMessage();
    }

    private void btSendActionPerformed(java.awt.event.ActionEvent evt) {
        sendMessage();
    }

    private void btFileActionPerformed(java.awt.event.ActionEvent evt) {
        openSendFileFrame();
    }

    private void sendMessage() {
        String msg = tfInput.getText();
        if (msg.equals("")) return;
        appendMessageRight(msg);
        sendToServer(ServerCommands.PRIVATE_CHAT + "|" + this.sender + "|" + this.receiver + "|" + msg);
        tfInput.setText("");
    }

    private void openSendFileFrame() {
        SendFileFrame sendFileFrame = new SendFileFrame(serverHost, sender);

        sendFileFrame.thePersonIamChattingWith = receiver;
        sendFileFrame.getTfReceiver().setText(receiver);
        sendFileFrame.setVisible(true);
        sendFileFrame.setLocation(450, 250);
        sendFileFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new PrivateChat().setVisible(true);
            }
        });
    }

}
