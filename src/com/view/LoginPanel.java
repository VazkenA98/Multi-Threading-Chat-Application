package com.view;


import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {


    public JButton btOK = new JButton();
    public JLabel jLabel1 = new JLabel();
    public JLabel jLabel2 = new JLabel();
    public JLabel jLabel3 = new JLabel();
    public JLabel lbBackLogin = new JLabel();
    public JTextField tfHost = new JTextField();
    public JTextField tfNickname = new JTextField();


    public LoginPanel() {
        initComponents();
    }

    public JButton getBtOK() {
        return btOK;
    }

    public JTextField getTfHost() {
        return tfHost;
    }

    public JTextField getTfNickname() {
        return tfNickname;
    }

    public JLabel getLbBackLogin() {
        return lbBackLogin;
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        jLabel1.setFont(new Font("Arial", 1, 36));
        jLabel1.setText("Login");
        jLabel2.setFont(new Font("Arial", 0, 18));
        jLabel2.setText("Nickname");
        tfNickname.setFont(new Font("Arial", 0, 18));
        tfNickname.setToolTipText("Nickname must be unique");
        jLabel3.setFont(new Font("Arial", 0, 18));
        jLabel3.setText("IP Address");
        tfHost.setFont(new Font("Arial", 0, 18));
        tfHost.setText("127.0.0.1");
        btOK.setFont(new Font("Arial", 1, 18));
        btOK.setForeground(new Color(0, 0, 0));
        btOK.setText("Join");

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(lbBackLogin)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel1)
                                .addGap(214, 214, 214))
                        .addGroup(layout.createSequentialGroup()
                                .addGap(85, 85, 85)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel2, GroupLayout.PREFERRED_SIZE, 79, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel3))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(tfNickname, GroupLayout.Alignment.LEADING)
                                        .addComponent(tfHost, GroupLayout.Alignment.LEADING))
                                .addContainerGap(111, Short.MAX_VALUE))
                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btOK, GroupLayout.PREFERRED_SIZE, 83, GroupLayout.PREFERRED_SIZE)
                                .addGap(224, 224, 224))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(lbBackLogin))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(30, 30, 30)
                                                .addComponent(jLabel1)))
                                .addGap(40, 40, 40)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel3, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(tfHost, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel2, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(tfNickname, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE))
                                .addGap(41, 41, 41)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 53, Short.MAX_VALUE)
                                .addComponent(btOK)
                                .addGap(47, 47, 47))
        );
    }

}
