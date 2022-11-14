package com.view;


import com.server.ApplicationProperties;
import com.server.ServerCommands;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;


public class RoomPanel extends JPanel {

    public RoomPanel() {
        initComponents();
    }

    public JLabel getLbRoom1() {
        return lbRoom1;
    }

    public JLabel getLbRoom2() {
        return lbRoom2;
    }

    public JLabel getLbRoom3() {
        return lbRoom3;
    }

    public JLabel getLbRoom4() {
        return lbRoom4;
    }

    public JLabel getLbRoom5() {
        return lbRoom5;
    }

    public JLabel getLbRoom6() {
        return lbRoom6;
    }

    public JLabel getLbRoom7() {
        return lbRoom7;
    }

    public JLabel getLbRoom8() {
        return lbRoom8;
    }

    public JList<String> getOnlineList_rp() {
        return onlineList_rp;
    }
    
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JScrollPane jScrollPane1;
    private JLabel lbRoom1;
    private JLabel lbRoom2;
    private JLabel lbRoom3;
    private JLabel lbRoom4;
    private JLabel lbRoom5 = new JLabel();
    private JLabel lbRoom6 = new JLabel();
    private JLabel lbRoom7 = new JLabel();
    private JLabel lbRoom8 = new JLabel();
    private JList<String> onlineList_rp;
    private List<JLabel> customRooms =  new ArrayList<>(Arrays.asList(lbRoom5, lbRoom6, lbRoom7, lbRoom8));


    private void initComponents() {

        lbRoom1 = new JLabel();
        jLabel2 = new JLabel();
        lbRoom3 = new JLabel();
        lbRoom2 = new JLabel();
        lbRoom4 = new JLabel();
        jScrollPane1 = new JScrollPane();
        onlineList_rp = new JList<>();
        jLabel1 = new JLabel();


        lbRoom1.setBackground(new Color(179, 177, 177, 255));
        lbRoom1.setFont(new Font("Arial", 0, 18)); // NOI18N
        lbRoom1.setHorizontalAlignment(SwingConstants.CENTER);
        lbRoom1.setText("Room 1");
        lbRoom1.setOpaque(true);

        jLabel2.setFont(new Font("Arial", 1, 24)); // NOI18N
        jLabel2.setText("Choose room");

        lbRoom3.setBackground(new Color(179, 177, 177, 255));
        lbRoom3.setFont(new Font("Arial", 0, 18)); // NOI18N
        lbRoom3.setHorizontalAlignment(SwingConstants.CENTER);
        lbRoom3.setText("Room 3");
        lbRoom3.setOpaque(true);

        lbRoom2.setBackground(new Color(179, 177, 177, 255));
        lbRoom2.setFont(new Font("Arial", 0, 18)); // NOI18N;
        lbRoom2.setHorizontalAlignment(SwingConstants.CENTER);
        lbRoom2.setText("Room 2");
        lbRoom2.setOpaque(true);

        lbRoom4.setBackground(new Color(179, 177, 177, 255));
        lbRoom4.setFont(new Font("Arial", 0, 18)); // NOI18N
        lbRoom4.setHorizontalAlignment(SwingConstants.CENTER);
        lbRoom4.setText("Room 4");
        lbRoom4.setOpaque(true);

        onlineList_rp.setFont(new Font("Arial", 0, 14)); // NOI18N
        onlineList_rp.setToolTipText("double-click to send a message");
        jScrollPane1.setViewportView(onlineList_rp);

        jLabel1.setFont(new Font("Arial", 0, 24)); // NOI18N
        jLabel1.setText("Online");
        jLabel1.setToolTipText("");

        int listOrder = 0;
        for (Map.Entry<String,Boolean> entry : ApplicationProperties.getRoomsMap().entrySet()){
            customRooms.get(listOrder).setBackground(new Color(179, 177, 177, 255));
            customRooms.get(listOrder).setFont(new Font("Arial", 0, 18)); // NOI18N
            customRooms.get(listOrder).setHorizontalAlignment(SwingConstants.CENTER);
            customRooms.get(listOrder).setText(entry.getKey());
            customRooms.get(listOrder).setOpaque(true);
            customRooms.get(listOrder).setVisible(entry.getValue());
            customRooms.get(listOrder).setEnabled(entry.getValue());
            listOrder++;
        }




        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(55, 55, 55)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(lbRoom7, GroupLayout.PREFERRED_SIZE, 83, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lbRoom3, GroupLayout.PREFERRED_SIZE, 83, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lbRoom1, GroupLayout.PREFERRED_SIZE, 83, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lbRoom5, GroupLayout.PREFERRED_SIZE, 83, GroupLayout.PREFERRED_SIZE))
                                .addGap(49, 49, 49)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(lbRoom4, GroupLayout.PREFERRED_SIZE, 83, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lbRoom2, GroupLayout.PREFERRED_SIZE, 83, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lbRoom6, GroupLayout.PREFERRED_SIZE, 83, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lbRoom8, GroupLayout.PREFERRED_SIZE, 83, GroupLayout.PREFERRED_SIZE))
                                .addGap(105, 105, 105)
                                .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(layout.createSequentialGroup()
                                .addGap(92, 92, 92)
                                .addComponent(jLabel2)
                                .addGap(164, 164, 164)
                                .addComponent(jLabel1)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(48, 48, 48)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel2)
                                        .addComponent(jLabel1))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(lbRoom1, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(lbRoom2, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE))
                                                .addGap(18, 18, 18)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addComponent(lbRoom3, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(lbRoom4, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE))
                                                .addGap(21, 21, 21)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addComponent(lbRoom6, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(lbRoom5, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE))
                                                .addGap(19, 19, 19)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(lbRoom8, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(lbRoom7, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE)))
                                        .addComponent(jScrollPane1))
                                .addContainerGap(43, Short.MAX_VALUE))
        );
    }

    public static String addRoom(String name){
        String msg = "You reached rooms size limit";
        Map<String, Boolean> map = ApplicationProperties.getRoomsMap();
        if(map.size() < 4){
            ApplicationProperties.addRoomNameAndFlag(name);
            msg = "Done";
        }
        return msg;
    }

    public static String deleteRoom(String name){
        String msg = "There's no such a room with name: "+name;

        for (Map.Entry<String,Boolean> entry : ApplicationProperties.getRoomsMap().entrySet()){
            if(entry.getKey().equals(name)){
                ApplicationProperties.removeRoom(name);
                msg = "Done";
            }
        }
        return msg;
    }

}