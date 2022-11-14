package com.controller;


import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.server.ServerCommands;
import com.view.ClientPanel;
import com.view.LoginPanel;
import com.view.PrivateChat;
import com.view.RoomPanel;

import javax.swing.*;


public class ClientFrame extends JFrame implements Runnable {
    String serverHost;
    public static final String NICKNAME_EXIST = "This nickname is already login in another place! Please using another nickname";
    public static final String NICKNAME_INVALID = "Nickname or password is incorrect";


    String name;
    String room;
    Socket socketOfClient;
    BufferedWriter bw;
    BufferedReader br;

    JPanel mainPanel;
    LoginPanel loginPanel;
    ClientPanel clientPanel;
    RoomPanel roomPanel;

    Thread clientThread;
    boolean isRunning;

    JMenuBar menuBar;
    JMenu menuShareFile;
    JMenuItem itemSendFile;
    JMenu menuAccount;
    JMenuItem itemLeaveRoom;


    StringTokenizer tokenizer;

    DefaultListModel<String> listModel, listModelThisRoom, listModel_rp;

    boolean isConnectToServer;

    int timeClicked = 0;

    Hashtable<String, PrivateChat> listReceiver;

    public ClientFrame(String name) {
        this.name = name;
        socketOfClient = null;
        bw = null;
        br = null;
        isRunning = true;
        listModel = new DefaultListModel<>();
        listModelThisRoom = new DefaultListModel<>();
        listModel_rp = new DefaultListModel<>();
        isConnectToServer = false;
        listReceiver = new Hashtable<>();

        mainPanel = new JPanel();
        loginPanel = new LoginPanel();
        clientPanel = new ClientPanel();
        roomPanel = new RoomPanel();


        loginPanel.setVisible(true);
        roomPanel.setVisible(false);
        clientPanel.setVisible(false);

        mainPanel.add(loginPanel);
        mainPanel.add(roomPanel);
        mainPanel.add(clientPanel);

        addEventsForLoginPanel();
        addEventsForClientPanel();
        addEventsForRoomPanel();

        menuBar = new JMenuBar();
        menuShareFile = new JMenu();
        menuAccount = new JMenu();
        itemLeaveRoom = new JMenuItem();
        itemSendFile = new JMenuItem();

        menuAccount.setText("Account");
        itemLeaveRoom.setText("Leave room");
        menuAccount.add(itemLeaveRoom);

        menuShareFile.setText("File sharing");
        itemSendFile.setText("Send a file");
        menuShareFile.add(itemSendFile);

        menuBar.add(menuAccount);
        menuBar.add(menuShareFile);

        itemLeaveRoom.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                int kq = JOptionPane.showConfirmDialog(ClientFrame.this, "Are you sure to leave this room?", "Notice", JOptionPane.YES_NO_OPTION);
                if(kq == JOptionPane.YES_OPTION) {
                    leaveRoom();
                }
            }
        });

        itemSendFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                JOptionPane.showMessageDialog(ClientFrame.this, "This function has changed! Go to private chat to send a file", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        menuBar.setVisible(false);

        setJMenuBar(menuBar);
        pack();

        add(mainPanel);
        setSize(570, 520);
        setLocation(400, 100);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle(name);
    }

    private void addEventsForLoginPanel() {
        loginPanel.getTfNickname().addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent ke) {
                if(ke.getKeyCode() == KeyEvent.VK_ENTER) btOkEvent();
            }

        });
        loginPanel.getBtOK().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                btOkEvent();
            }
        });
        loginPanel.getLbBack_login().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                loginPanel.setVisible(false);
                clientPanel.setVisible(false);
                roomPanel.setVisible(false);
            }
        });
    }


    private void addEventsForClientPanel() {
        clientPanel.getBtSend().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                btSendEvent();
            }
        });
        clientPanel.getBtExit().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                btExitEvent();
            }
        });
        clientPanel.getTaInput().addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent ke) {
                if(ke.getKeyCode() == KeyEvent.VK_ENTER) {
                    btSendEvent();
                    btClearEvent();
                }
            }
        });
/*        //events for emotion icons:
        clientPanel.getLbLike().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                sendToServer("CMD_ICON|LIKE");
            }
        });
        clientPanel.getLbDislike().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                sendToServer("CMD_ICON|DISLIKE");
            }
        });
        clientPanel.getLbPacMan().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                sendToServer("CMD_ICON|PAC_MAN");
            }
        });
        clientPanel.getLbCry().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                sendToServer("CMD_ICON|CRY");
            }
        });
        clientPanel.getLbGrin().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                sendToServer("CMD_ICON|GRIN");
            }
        });
        clientPanel.getLbSmile().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                sendToServer("CMD_ICON|SMILE");
            }
        });

        clientPanel.getOnlineList().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                openPrivateChatInsideRoom();
            }
        });*/
    }

    private void addEventsForRoomPanel() {
        roomPanel.getLbRoom1().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                ClientFrame.this.room = roomPanel.getLbRoom1().getText();
                labelRoomEvent();
            }
        });
        roomPanel.getLbRoom2().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                ClientFrame.this.room = roomPanel.getLbRoom2().getText();
                labelRoomEvent();
            }
        });
        roomPanel.getLbRoom3().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                ClientFrame.this.room = roomPanel.getLbRoom3().getText();
                labelRoomEvent();
            }
        });
        roomPanel.getLbRoom4().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                ClientFrame.this.room = roomPanel.getLbRoom4().getText();
                labelRoomEvent();
            }
        });
        roomPanel.getLbRoom5().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                ClientFrame.this.room = roomPanel.getLbRoom5().getText();
                labelRoomEvent();
            }
        });
        roomPanel.getLbRoom6().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                ClientFrame.this.room = roomPanel.getLbRoom6().getText();
                labelRoomEvent();
            }
        });
        roomPanel.getLbRoom7().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                ClientFrame.this.room = roomPanel.getLbRoom7().getText();
                labelRoomEvent();
            }
        });
        roomPanel.getLbRoom8().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                ClientFrame.this.room = roomPanel.getLbRoom8().getText();
                labelRoomEvent();
            }
        });

        roomPanel.getOnlineList_rp().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                openPrivateChatOutsideRoom();
            }
        });
    }

    private void openPrivateChatInsideRoom() {
        timeClicked++;
        if(timeClicked == 1) {
            Thread countingTo500ms = new Thread(counting);
            countingTo500ms.start();
        }

        if(timeClicked == 2) {  //nếu như countingTo500ms chưa thực hiện xong, tức là timeClicked vẫn = 2:
            String nameClicked = clientPanel.getOnlineList().getSelectedValue();
            if(nameClicked.equals(ClientFrame.this.name)) {
                JOptionPane.showMessageDialog(ClientFrame.this, "Can't send a message to yourself!", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            if(!listReceiver.containsKey(nameClicked)) {    //nếu đây là lần đầu tiên nhắn tin với người mà ta vừa double-click vào
                PrivateChat pc = new PrivateChat(name, nameClicked, serverHost, bw, br);

                pc.getLbReceiver().setText("Private chat with \""+pc.receiver+"\"");
                pc.setTitle(pc.receiver);
                pc.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                pc.setVisible(true);

                listReceiver.put(nameClicked, pc);
            } else {
                PrivateChat pc = listReceiver.get(nameClicked);
                pc.setVisible(true);
            }
        }
    }

    private void openPrivateChatOutsideRoom() {
        timeClicked++;
        if(timeClicked == 1) {
            Thread countingTo500ms = new Thread(counting);
            countingTo500ms.start();
        }

        if(timeClicked == 2) {
            String privateReceiver = roomPanel.getOnlineList_rp().getSelectedValue();
            PrivateChat pc = listReceiver.get(privateReceiver);
            if(pc == null) {
                pc = new PrivateChat(name, privateReceiver, serverHost, bw, br);

                pc.getLbReceiver().setText("Private chat with \""+pc.receiver+"\"");
                pc.setTitle(pc.receiver);
                pc.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                pc.setVisible(true);

                listReceiver.put(privateReceiver, pc);
            } else {
                pc.setVisible(true);
            }
        }
    }


    Runnable counting = new Runnable() {
        @Override
        public void run() {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(ClientFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            timeClicked = 0;
        }
    };

    private void labelRoomEvent() {
        this.clientPanel.getTpMessage().setText("");
        this.sendToServer("CMD_ROOM|"+this.room);
        try {
            Thread.sleep(200);      //chờ tý cho nó đỡ lỗi!
        } catch (InterruptedException ex) {
            Logger.getLogger(ClientFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.roomPanel.setVisible(false);
        this.clientPanel.setVisible(true);
        this.setTitle("\""+this.name+"\" - "+this.room);
        clientPanel.getLbRoom().setText(this.room);
    }

    private void leaveRoom() {
        this.sendToServer(ServerCommands.LEAVE_ROOM_COMMAND +"|"+this.room);
        try {
            Thread.sleep(200);
        } catch (InterruptedException ex) {
            Logger.getLogger(ClientFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.roomPanel.setVisible(true);
        this.clientPanel.setVisible(false);
        clientPanel.getTpMessage().setText("");
        this.setTitle("\""+this.name+"\"");
    }


    ////////////////////////Events////////////////////////////
    private void btOkEvent() {
        String hostname = loginPanel.getTfHost().getText().trim();
        String nickname = loginPanel.getTfNickname().getText().trim();

        this.serverHost = hostname;
        this.name = nickname;

        if(hostname.equals("") || nickname.equals("")) {
            JOptionPane.showMessageDialog(this, "Please fill up all fields", "Notice!", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if(!isConnectToServer) {
            isConnectToServer = true;
            this.connectToServer(hostname);
        }
        this.sendToServer("CMD_CHECK_NAME|" +this.name);


        String response = this.recieveFromServer();
        if(response != null) {
            if (response.equals(NICKNAME_EXIST) || response.equals(NICKNAME_INVALID)) {
                JOptionPane.showMessageDialog(this, response, "Error", JOptionPane.ERROR_MESSAGE);

            } else {

                loginPanel.setVisible(false);
                roomPanel.setVisible(true);
                clientPanel.setVisible(false);
                this.setTitle("\""+name+"\"");

                menuBar.setVisible(true);

                clientThread = new Thread(this);
                clientThread.start();
                this.sendToServer("CMD_ROOM|"+this.room);

            }
        }
    }

    private void btSendEvent() {
        String message = clientPanel.getTaInput().getText().trim();
        if(message.equals("")) clientPanel.getTaInput().setText("");
        else {
            this.sendToServer(ServerCommands.CHAT_COMMAND+"|" + message);
            this.btClearEvent();
        }

    }

    private void btClearEvent() {
        clientPanel.getTaInput().setText("");
    }

    private void btExitEvent() {
        try {
            isRunning = false;
            System.exit(0);
        } catch (Exception e) {
        }
    }

    ////////////////////////End of Events////////////////////////////

    public void connectToServer(String hostAddress) {
        try {
            socketOfClient = new Socket(hostAddress, 9999);
            bw = new BufferedWriter(new OutputStreamWriter(socketOfClient.getOutputStream()));
            br = new BufferedReader(new InputStreamReader(socketOfClient.getInputStream()));

        } catch (java.net.UnknownHostException e) {
            JOptionPane.showMessageDialog(this, "Host IP is not correct.\nPlease try again!", "Failed to connect to server", JOptionPane.ERROR_MESSAGE);
        } catch (java.net.ConnectException e) {
            JOptionPane.showMessageDialog(this, "Server is unreachable, maybe server is not open yet, or can't find this host.\nPlease try again!", "Failed to connect to server", JOptionPane.ERROR_MESSAGE);
        } catch(java.net.NoRouteToHostException e) {
            JOptionPane.showMessageDialog(this, "Can't find this host!\nPlease try again!", "Failed to connect to server", JOptionPane.ERROR_MESSAGE);
        } catch (IOException ex) {
            Logger.getLogger(ClientFrame.class.getName()).log(Level.SEVERE, null, ex);

        }
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
            Logger.getLogger(ClientFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String recieveFromServer() {
        try {
            return this.br.readLine();
        } catch (java.lang.NullPointerException e) {
            System.out.println("[recieveFromServer()] Server is not open yet, or already closed!");
        } catch (IOException ex) {
            Logger.getLogger(ClientFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void disconnect() {
        try {
            if(br!=null) this.br.close();
            if(bw!=null) this.bw.close();
            if(socketOfClient!=null) this.socketOfClient.close();
        } catch (IOException ex) {
            Logger.getLogger(ClientFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(ClientFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        ClientFrame client = new ClientFrame(null);
        client.setVisible(true);
    }

    @Override
    public void run() {
        String response;
        String sender, receiver, fileName, thePersonIamChattingWith, thePersonSendFile;
        String msg;
        String cmd;
        PrivateChat pc;

        while(isRunning) {
            response = this.recieveFromServer();   //nhận phản hồi từ server sau khi đã gửi data ở trên
            tokenizer = new StringTokenizer(response, "|");
            cmd = tokenizer.nextToken();
            switch (cmd) {
                case ServerCommands.CHAT_COMMAND:
                    sender = tokenizer.nextToken();
                    msg = response.substring(cmd.length()+sender.length()+2, response.length());

                    if(sender.equals(this.name)) this.clientPanel.appendMessage(sender+": ", msg, Color.BLACK, new Color(0, 102, 204));
                    else this.clientPanel.appendMessage(sender+": ", msg, Color.MAGENTA, new Color(56, 224, 0));

                    break;

                case "CMD_PRIVATECHAT":

                    sender = tokenizer.nextToken();
                    msg = response.substring(cmd.length()+sender.length()+2, response.length());

                    pc = listReceiver.get(sender);

                    if(pc == null) {
                        pc = new PrivateChat(name, sender, serverHost, bw, br);
                        pc.sender = name;
                        pc.receiver = sender;
                        pc.serverHost = this.serverHost;
                        pc.bw = ClientFrame.this.bw;
                        pc.br = ClientFrame.this.br;

                        pc.getLbReceiver().setText("Private chat with \""+pc.receiver+"\"");
                        pc.setTitle(pc.receiver);
                        pc.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        pc.setVisible(true);

                        listReceiver.put(sender, pc);
                    } else {
                        pc.setVisible(true);
                    }
                    pc.appendMessage_Left(sender+": ", msg);
                    break;

                case "CMD_ONLINE_USERS":
                    listModel.clear();
                    listModel_rp.clear();
                    while(tokenizer.hasMoreTokens()) {
                        cmd = tokenizer.nextToken();
                        listModel.addElement(cmd);
                        listModel_rp.addElement(cmd);
                    }
                    clientPanel.getOnlineList().setModel(listModel);

                    listModel_rp.removeElement(this.name);
                    roomPanel.getOnlineList_rp().setModel(listModel_rp);
                    break;

                case "CMD_ONLINE_THIS_ROOM":
                    listModelThisRoom.clear();
                    while(tokenizer.hasMoreTokens()) {
                        cmd = tokenizer.nextToken();
                        listModelThisRoom.addElement(cmd);
                    }
                    clientPanel.getOnlineListThisRoom().setModel(listModelThisRoom);
                    break;


                case "CMD_FILEAVAILABLE":
                    fileName = tokenizer.nextToken();
                    thePersonIamChattingWith = tokenizer.nextToken();
                    thePersonSendFile = tokenizer.nextToken();

                    pc = listReceiver.get(thePersonIamChattingWith);

                    if(pc == null) {
                        sender = this.name;
                        receiver = thePersonIamChattingWith;
                        pc = new PrivateChat(sender, receiver, serverHost, bw, br);

                        pc.getLbReceiver().setText("Private chat with \""+pc.receiver+"\"");
                        pc.setTitle(pc.receiver);
                        pc.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                        listReceiver.put(receiver, pc);
                    }

                    pc.setVisible(true);
                    pc.insertButton(thePersonSendFile, fileName);
                    break;

                default:
                    if(!response.startsWith("CMD_")) {
                        if(response.equals("Warning: Server has been closed!")) {
                            this.clientPanel.appendMessage(response, Color.RED);
                        }
                        else this.clientPanel.appendMessage(response, new Color(153, 153, 153));
                    }

            }
        }
        System.out.println("Disconnected to server!");
    }


}
