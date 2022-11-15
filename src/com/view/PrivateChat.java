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

    public String sender, receiver;
    public String serverHost;
    public BufferedWriter bw;
    public BufferedReader br;
    HTMLEditorKit htmlKit;
    HTMLDocument htmlDoc;

    private JButton btFile_pc;
    private JButton btSend_pc;
    private JPanel jPanel1;
    private JScrollPane jScrollPane1;
    private JLabel lbReceiver;
    private JTextField tfInput_pc;
    private JTextPane tpMessage_pc;


    public PrivateChat() {
        initComponents();
        htmlKit = new HTMLEditorKit();
        htmlDoc = new HTMLDocument();
        tpMessage_pc.setEditorKit(htmlKit);
        tpMessage_pc.setDocument(htmlDoc);
    }

    public PrivateChat(String sender, String receiver, String serverHost, BufferedWriter bw, BufferedReader br) {
        initComponents();
        this.sender = sender;
        this.receiver = receiver;
        this.serverHost = serverHost;
        this.bw = bw;
        this.br = br;

        htmlKit = new HTMLEditorKit();
        htmlDoc = new HTMLDocument();
        tpMessage_pc.setEditorKit(htmlKit);
        tpMessage_pc.setDocument(htmlDoc);
    }

    public JLabel getLbReceiver() {
        return lbReceiver;
    }

    public void sendToServer(String line) {
        try {
            this.bw.write(line);
            this.bw.newLine();
            this.bw.flush();
        } catch (java.net.SocketException e) {
            JOptionPane.showMessageDialog(this, "Server is closed, can't send message!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (java.lang.NullPointerException e) {
            System.out.println("[sendToServer()] Server is not open yet, or already closed!");
        } catch (IOException ex) {
            Logger.getLogger(PrivateChat.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void appendMessage_Left(String msg1, String msg2) {
        try {
            htmlKit.insertHTML(htmlDoc, htmlDoc.getLength(), "<p style=\"color:black; padding: 3px; margin-top: 4px; margin-right:35px; text-align:left; font:normal 12px Tahoma;\"><span style=\"background-color:#f3f3f3;\"><b>" + msg1 + "</b><span style=\"color:black;\">" + msg2 + "</span></span></p>", 0, 0, null);
        } catch (BadLocationException | IOException ex) {
            Logger.getLogger(PrivateChat.class.getName()).log(Level.SEVERE, null, ex);
        }
        tpMessage_pc.setCaretPosition(tpMessage_pc.getDocument().getLength());
    }

    public void appendMessage_Left(String msg1, String msg2, String color1, String color2) {
        try {
            htmlKit.insertHTML(htmlDoc, htmlDoc.getLength(), "<p style=\"color:" + color1 + "; padding: 3px; margin-top: 4px; margin-right:35px; text-align:left; font:normal 12px Tahoma;\"><span><b>" + msg1 + "</b><span style=\"color:" + color2 + ";\">" + msg2 + "</span></span></p><br/>", 0, 0, null);
        } catch (BadLocationException | IOException ex) {
            Logger.getLogger(PrivateChat.class.getName()).log(Level.SEVERE, null, ex);
        }
        tpMessage_pc.setCaretPosition(tpMessage_pc.getDocument().getLength());
    }

    public void appendMessage_Right(String msg1) {
        try {
            htmlKit.insertHTML(htmlDoc, htmlDoc.getLength(), "<p style=\"color:white; padding: 3px; margin-top: 4px; margin-left:35px; text-align:right; font:normal 12px Tahoma;\"><span style=\"background-color: #889eff; -webkit-border-radius: 10px;\">" + msg1 + "</span></p>", 0, 0, null);
        } catch (BadLocationException | IOException ex) {
            Logger.getLogger(PrivateChat.class.getName()).log(Level.SEVERE, null, ex);
        }
        tpMessage_pc.setCaretPosition(tpMessage_pc.getDocument().getLength());
    }

    public void insertButton(String sender, String fileName) {
        JButton bt = new JButton(fileName);
        bt.setName(fileName);
        bt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                downloadFile(fileName);
            }
        });
        appendMessage_Left(sender, " sends a file ", "#00dddd", "#00ee11");
        tpMessage_pc.setCaretPosition(tpMessage_pc.getDocument().getLength() - 1);
        tpMessage_pc.insertComponent(bt);
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
            JOptionPane.showMessageDialog(this, "The default folder to save file is in D:\\", "Notice", JOptionPane.INFORMATION_MESSAGE);
        }

        try {
            Socket socketOfReceiver = new Socket(serverHost, 9999);
            new ReceivingFileThread(socketOfReceiver, myDownloadFolder, buttonName, this).start();
            System.out.println("start receiving file");
        } catch (IOException ex) {
            Logger.getLogger(PrivateChat.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    @SuppressWarnings("unchecked")
    private void initComponents() {

        jPanel1 = new JPanel();
        lbReceiver = new JLabel();
        jScrollPane1 = new JScrollPane();
        tpMessage_pc = new JTextPane();
        tfInput_pc = new JTextField();
        btSend_pc = new JButton();
        btFile_pc = new JButton();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        lbReceiver.setFont(new Font("Arial", 0, 16)); // NOI18N
        lbReceiver.setText("Receiver");

        tpMessage_pc.setEditable(false);
        jScrollPane1.setViewportView(tpMessage_pc);

        tfInput_pc.setFont(new Font("Arial", 0, 14)); // NOI18N
        tfInput_pc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfInput_pcActionPerformed(evt);
            }
        });

        btSend_pc.setFont(new Font("Arial", 0, 14)); // NOI18N
        btSend_pc.setText("Send");
        btSend_pc.setToolTipText("send a message");
        btSend_pc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSend_pcActionPerformed(evt);
            }
        });

        btFile_pc.setFont(new Font("Arial", 0, 14)); // NOI18N
        btFile_pc.setText("File");
        btFile_pc.setToolTipText("send a file");
        btFile_pc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btFile_pcActionPerformed(evt);
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
                                        .addComponent(tfInput_pc)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addComponent(lbReceiver)
                                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addComponent(btSend_pc)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(btFile_pc)))
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
                                .addComponent(tfInput_pc, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(btSend_pc, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btFile_pc, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

    private void tfInput_pcActionPerformed(java.awt.event.ActionEvent evt) {
        sendMessage();
    }

    private void btSend_pcActionPerformed(java.awt.event.ActionEvent evt) {
        sendMessage();
    }

    private void btFile_pcActionPerformed(java.awt.event.ActionEvent evt) {
        openSendFileFrame();
    }

    private void sendMessage() {
        String msg = tfInput_pc.getText();
        if (msg.equals("")) return;
        appendMessage_Right(msg);
        sendToServer(ServerCommands.PRIVATE_CHAT +"|" + this.sender + "|" + this.receiver + "|" + msg);
        tfInput_pc.setText("");
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
