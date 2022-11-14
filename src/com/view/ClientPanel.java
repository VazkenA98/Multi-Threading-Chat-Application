package com.view;



import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientPanel extends JPanel {

    JFileChooser chooser;

    private JButton btExit;
    private JButton btSend;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel4;
    private JScrollPane jScrollPane2;
    private JScrollPane jScrollPane3;
    private JScrollPane jScrollPane4;
    private JScrollPane jScrollPane5;
    private JLabel lbCry;
    private JLabel lbDislike;
    private JLabel lbGrin;
    private JLabel lbLike;
    private JLabel lbPacman;
    private JLabel lbRoom;
    private JLabel lbSmile;
    private JList<String> onlineList;
    private JList<String> onlineListThisRoom;
    private JTextArea taInput;
    private JTextPane tpMessage;

    public ClientPanel() {
        initComponents();
        chooser = new JFileChooser();

    }

    public JButton getBtExit() {
        return btExit;
    }

    public JButton getBtSend() {
        return btSend;
    }

    public JTextArea getTaInput() {
        return taInput;
    }

    public JTextPane getTpMessage() {
        return tpMessage;
    }


    public JList<String> getOnlineList() {
        return onlineList;
    }

    public JList<String> getOnlineListThisRoom() {
        return onlineListThisRoom;
    }

    public JLabel getLbRoom() {
        return lbRoom;
    }


    public void appendMessage(String msg1, String msg2, Color c1, Color c2) {

        int len = tpMessage.getDocument().getLength();
        StyledDocument doc = (StyledDocument) tpMessage.getDocument();

        SimpleAttributeSet sas = new SimpleAttributeSet();
        StyleConstants.setFontFamily(sas, "Arial");
        StyleConstants.setBold(sas, true);
        StyleConstants.setFontSize(sas, 14);
        StyleConstants.setForeground(sas, c1);

        try {
            doc.insertString(len, msg1, sas);
        } catch (BadLocationException ex) {
            Logger.getLogger(ClientPanel.class.getName()).log(Level.SEVERE, null, ex);
        }

        doc = (StyledDocument) tpMessage.getDocument();
        len = len + msg1.length();

        sas = new SimpleAttributeSet();
        StyleConstants.setFontFamily(sas, "Arial");
        StyleConstants.setFontSize(sas, 14);
        StyleConstants.setForeground(sas, c2);

        try {
            doc.insertString(len, msg2 + "\n", sas);
        } catch (BadLocationException ex) {
            Logger.getLogger(ClientPanel.class.getName()).log(Level.SEVERE, null, ex);
        }

        tpMessage.setCaretPosition(len + msg2.length());
    }

    public void appendMessage(String message, Color color) {
        int len = tpMessage.getDocument().getLength();
        StyledDocument doc = (StyledDocument) tpMessage.getDocument();

        SimpleAttributeSet sas = new SimpleAttributeSet();
        StyleConstants.setFontFamily(sas, "Arial");
        StyleConstants.setItalic(sas, true);
        StyleConstants.setFontSize(sas, 14);
        StyleConstants.setForeground(sas, color);

        try {
            doc.insertString(len, message + "\n", sas);
        } catch (BadLocationException ex) {
            Logger.getLogger(ClientPanel.class.getName()).log(Level.SEVERE, null, ex);
        }

        tpMessage.setCaretPosition(len + message.length());
    }

    private void initComponents() {

        jLabel1 = new JLabel();
        btExit = new JButton();
        btSend = new JButton();
        jScrollPane3 = new JScrollPane();
        taInput = new JTextArea();
        jLabel2 = new JLabel();
        jScrollPane4 = new JScrollPane();
        onlineList = new JList<>();
        jScrollPane2 = new JScrollPane();
        tpMessage = new JTextPane();
        lbRoom = new JLabel();
        jLabel4 = new JLabel();
        jScrollPane5 = new JScrollPane();
        onlineListThisRoom = new JList<>();
        lbLike = new JLabel();
        lbDislike = new JLabel();
        lbPacman = new JLabel();
        lbSmile = new JLabel();
        lbGrin = new JLabel();
        lbCry = new JLabel();

        jLabel1.setFont(new Font("Arial", 0, 14)); // NOI18N
        jLabel1.setText("Online");

        btExit.setFont(new Font("Arial", 1, 16)); // NOI18N
        btExit.setForeground(new Color(255, 102, 102));
        btExit.setText("Exit");

        btSend.setFont(new Font("Arial", 1, 18)); // NOI18N
        btSend.setForeground(new Color(102, 102, 255));
        btSend.setText("Send");

        taInput.setColumns(20);
        taInput.setRows(5);
        jScrollPane3.setViewportView(taInput);

        jLabel2.setFont(new Font("Arial", 0, 14)); // NOI18N
        jLabel2.setText("Chat");

        onlineList.setFont(new Font("Arial", 0, 14)); // NOI18N
        onlineList.setForeground(new Color(51, 51, 255));
        jScrollPane4.setViewportView(onlineList);

        tpMessage.setEditable(false);
        tpMessage.setFont(new Font("Arial", 0, 14)); // NOI18N
        jScrollPane2.setViewportView(tpMessage);

        lbRoom.setFont(new Font("Arial", 0, 18)); // NOI18N
        lbRoom.setForeground(new Color(0, 0, 255));
        lbRoom.setText("Room ?");

        jLabel4.setFont(new Font("Arial", 0, 14)); // NOI18N
        jLabel4.setText("Online in this room");

        onlineListThisRoom.setFont(new Font("Arial", 0, 14)); // NOI18N
        onlineListThisRoom.setForeground(new Color(51, 51, 255));
        jScrollPane5.setViewportView(onlineListThisRoom);


        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(93, 93, 93)
                                                .addComponent(jLabel2, GroupLayout.PREFERRED_SIZE, 123, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                .addComponent(jScrollPane2)
                                                .addGroup(layout.createSequentialGroup()
                                                        .addComponent(btSend)
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(lbLike)
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(lbDislike)
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(lbPacman)
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(lbSmile)
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(lbGrin)
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(lbCry)
                                                        .addGap(0, 0, Short.MAX_VALUE))
                                                .addComponent(jScrollPane3)))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(btExit, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 92, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jScrollPane4, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE)
                                                        .addComponent(jScrollPane5, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                                .addComponent(jLabel4)
                                                                .addGap(19, 19, 19)))
                                                .addGap(21, 21, 21))
                                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(jLabel1)
                                                .addGap(87, 87, 87))))
                        .addGroup(layout.createSequentialGroup()
                                .addGap(243, 243, 243)
                                .addComponent(lbRoom)
                                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(lbRoom)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel2)
                                        .addComponent(jLabel1))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jScrollPane4, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(jLabel4)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jScrollPane5, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                                        .addComponent(jScrollPane2, GroupLayout.PREFERRED_SIZE, 242, GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane3, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(lbGrin, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btExit, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(lbLike, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(lbDislike, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(lbPacman, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(lbSmile, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(lbCry, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btSend, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap(26, Short.MAX_VALUE))
        );

        lbLike.getAccessibleContext().setAccessibleDescription("");
    }
}