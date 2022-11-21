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
    public static final String NICKNAME_EXIST = "This nickname is already login in another place! Please using another nickname";
    public static final String NICKNAME_INVALID = "Nickname or password is incorrect";
    private String name;
    private String room;
    private Socket socketOfClient;
    private String serverHost;
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;
    public Thread clientThread;
    boolean isRunning;
    boolean isConnectToServer;
    public JMenuBar menuBar = new JMenuBar();
    public JMenu menuShareFile = new JMenu();
    public JMenuItem itemSendFile = new JMenuItem();
    public JMenu menuAccount = new JMenu();
    public JMenuItem itemLeaveRoom =  new JMenuItem();
    public JPanel mainPanel = new JPanel();
    public LoginPanel loginPanel = new LoginPanel();
    public ClientPanel clientPanel = new ClientPanel();
    public RoomPanel roomPanel = new RoomPanel();
    public StringTokenizer tokenizer;
    public DefaultListModel<String> listModel = new DefaultListModel<>();
    public DefaultListModel<String> listModelThisRoom = new DefaultListModel<>();
    public DefaultListModel<String> listModelRoomPeople= new DefaultListModel<>();
    public int timeClicked = 0;
    public Hashtable<String, PrivateChat> listReceiver = new Hashtable<>();

    public ClientFrame(String name) {
        this.name = name;
        socketOfClient = null;
        bufferedWriter = null;
        bufferedReader = null;
        isRunning = true;
        isConnectToServer = false;
        roomPanel.setVisible(false);
        clientPanel.setVisible(false);
        mainPanel.add(loginPanel);
        mainPanel.add(roomPanel);
        mainPanel.add(clientPanel);
        menuAccount.setText("Account");
        itemLeaveRoom.setText("Leave room");
        menuAccount.add(itemLeaveRoom);
        menuShareFile.setText("File");
        itemSendFile.setText("Send file");
        menuShareFile.add(itemSendFile);
        menuBar.add(menuAccount);
        menuBar.add(menuShareFile);

        addEventsForLoginPanel();
        addEventsForClientPanel();
        addEventsForRoomPanel();

        itemLeaveRoom.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                int ans = JOptionPane.showConfirmDialog(ClientFrame.this, "Are you sure to leave this room ?", "Notice", JOptionPane.YES_NO_OPTION);
                if(ans == JOptionPane.YES_OPTION) {
                    leaveRoom();
                }
            }
        });

        itemSendFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                JOptionPane.showMessageDialog(ClientFrame.this, "Go to private chat to send a file", "Notice", JOptionPane.INFORMATION_MESSAGE);
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
                if(ke.getKeyCode() == KeyEvent.VK_ENTER) buttonOkEvent();
            }

        });
        loginPanel.getBtOK().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                buttonOkEvent();
            }
        });
        loginPanel.getLbBackLogin().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                loginPanel.setVisible(false);
                clientPanel.setVisible(false);
                roomPanel.setVisible(false);
            }
        });
    }


    private void addEventsForClientPanel() {
        clientPanel.getButtonSend().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                buttonSendEvent();
            }
        });
        clientPanel.getButtonExit().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                buttonExitEvent();
            }
        });
        clientPanel.getTaInput().addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent ke) {
                if(ke.getKeyCode() == KeyEvent.VK_ENTER) {
                    buttonSendEvent();
                    buttonClearEvent();
                }
            }
        });
        clientPanel.getOnlineList().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                openPrivateChatInsideRoom();
            }
        });
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

        roomPanel.getOnlineListRoomPeople().addMouseListener(new MouseAdapter() {
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

        if(timeClicked == 2) {
            String nameClicked = clientPanel.getOnlineList().getSelectedValue();

            if(nameClicked.equals(ClientFrame.this.name)) {
                JOptionPane.showMessageDialog(ClientFrame.this, "Can't send a message to yourself!", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            if(!listReceiver.containsKey(nameClicked)) {
                PrivateChat privateChat = new PrivateChat(name, nameClicked, serverHost, bufferedWriter, bufferedReader);

                privateChat.getLbReceiver().setText("Private chat with \""+privateChat.receiver+"\"");
                privateChat.setTitle(privateChat.receiver);
                privateChat.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                privateChat.setVisible(true);

                listReceiver.put(nameClicked, privateChat);
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
            String privateReceiver = roomPanel.getOnlineListRoomPeople().getSelectedValue();
            PrivateChat privateChat = listReceiver.get(privateReceiver);
            if(privateChat == null) {
                privateChat = new PrivateChat(name, privateReceiver, serverHost, bufferedWriter, bufferedReader);

                privateChat.getLbReceiver().setText("Private chat with \""+privateChat.receiver+"\"");
                privateChat.setTitle(privateChat.receiver);
                privateChat.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                privateChat.setVisible(true);

                listReceiver.put(privateReceiver, privateChat);
            } else {
                privateChat.setVisible(true);
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
        this.sendToServer(ServerCommands.ENTER_ROOM+"|"+this.room);
        try {
            Thread.sleep(200);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
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
           ex.printStackTrace();
        }
        this.roomPanel.setVisible(true);
        this.clientPanel.setVisible(false);
        clientPanel.getTpMessage().setText("");
        this.setTitle("\""+this.name+"\"");
    }

    private void buttonOkEvent() {
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
        this.sendToServer(ServerCommands.CHECK_USER_USERNAME+"|" +this.name);


        String response = this.receiveFromServer();
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
                this.sendToServer(ServerCommands.ENTER_ROOM+"|"+this.room);

            }
        }
    }

    private void buttonSendEvent() {
        String message = clientPanel.getTaInput().getText().trim();
        if(message.equals("")) clientPanel.getTaInput().setText("");
        else {
            this.sendToServer(ServerCommands.CHAT_COMMAND+"|" + message);
            this.buttonClearEvent();
        }

    }

    private void buttonClearEvent() {
        clientPanel.getTaInput().setText("");
    }

    private void buttonExitEvent() {
        try {
            isRunning = false;
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void connectToServer(String hostAddress) {
        try {
            socketOfClient = new Socket(hostAddress, 9999);
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socketOfClient.getOutputStream()));
            bufferedReader = new BufferedReader(new InputStreamReader(socketOfClient.getInputStream()));

        } catch (java.net.UnknownHostException e) {
            JOptionPane.showMessageDialog(this, "Host IP is not correct.\nPlease try again!", "Failed to connect to server", JOptionPane.ERROR_MESSAGE);
        } catch (java.net.ConnectException e) {
            JOptionPane.showMessageDialog(this, "Server is unreachable, maybe server is not open yet, or can't find this host.\nPlease try again!", "Failed to connect to server", JOptionPane.ERROR_MESSAGE);
        } catch(java.net.NoRouteToHostException e) {
            JOptionPane.showMessageDialog(this, "Can't find this host!\nPlease try again!", "Failed to connect to server", JOptionPane.ERROR_MESSAGE);
        } catch (IOException ex) {
            ex.printStackTrace();

        }
    }

    public void sendToServer(String line) {
        try {
            this.bufferedWriter.write(line);
            this.bufferedWriter.newLine();
            this.bufferedWriter.flush();
        } catch (java.net.SocketException e) {
            JOptionPane.showMessageDialog(this, "Server is closed you can't send message!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (java.lang.NullPointerException e) {
           e.printStackTrace();
        } catch (IOException ex) {
            Logger.getLogger(ClientFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String receiveFromServer() {
        try {
            return this.bufferedReader.readLine();
        } catch (NullPointerException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void run() {
        String response;
        String sender;
        String receiver;
        String fileName;
        String thePersonIamChattingWith;
        String thePersonSendFile;
        String msg;
        String serverCommand;
        PrivateChat privateChat;

        while(isRunning) {
            response = this.receiveFromServer();
            tokenizer = new StringTokenizer(response, "|");
            serverCommand = tokenizer.nextToken();
            switch (serverCommand) {

                case ServerCommands.CHAT_COMMAND:
                    sender = tokenizer.nextToken();
                    msg = response.substring(serverCommand.length()+sender.length()+2, response.length());

                    if(sender.equals(this.name)) this.clientPanel.appendMessage(sender+": ", msg, Color.BLACK, new Color(0, 0, 0));
                    else this.clientPanel.appendMessage(sender+": ", msg, Color.MAGENTA, new Color(0, 0, 0));

                    break;

                case ServerCommands.PRIVATE_CHAT:

                    sender = tokenizer.nextToken();
                    msg = response.substring(serverCommand.length()+sender.length()+2, response.length());
                    privateChat = listReceiver.get(sender);

                    if(privateChat == null) {
                        privateChat = new PrivateChat(name, sender, serverHost, bufferedWriter, bufferedReader);
                        privateChat.sender = name;
                        privateChat.receiver = sender;
                        privateChat.serverHost = this.serverHost;
                        privateChat.bufferedWriter = ClientFrame.this.bufferedWriter;
                        privateChat.bufferedReader = ClientFrame.this.bufferedReader;

                        privateChat.getLbReceiver().setText("Private chat with \""+privateChat.receiver+"\"");
                        privateChat.setTitle(privateChat.receiver);
                        privateChat.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        privateChat.setVisible(true);

                        listReceiver.put(sender, privateChat);
                    } else {
                        privateChat.setVisible(true);
                    }
                    privateChat.appendMessageLeft(sender+": ", msg);
                    break;

                case ServerCommands.ONLINE_USERS:
                    listModel.clear();
                    listModelRoomPeople.clear();
                    while(tokenizer.hasMoreTokens()) {
                        serverCommand = tokenizer.nextToken();
                        listModel.addElement(serverCommand);
                        listModelRoomPeople.addElement(serverCommand);
                    }
                    clientPanel.getOnlineList().setModel(listModel);

                    listModelRoomPeople.removeElement(this.name);
                    roomPanel.getOnlineListRoomPeople().setModel(listModelRoomPeople);
                    break;

                case ServerCommands.ONLINE_USERS_IN_ROOM:
                    listModelThisRoom.clear();
                    while(tokenizer.hasMoreTokens()) {
                        serverCommand = tokenizer.nextToken();
                        listModelThisRoom.addElement(serverCommand);
                    }
                    clientPanel.getOnlineListThisRoom().setModel(listModelThisRoom);
                    break;


                case ServerCommands.AVAILABLE_FILE:
                    fileName = tokenizer.nextToken();
                    thePersonIamChattingWith = tokenizer.nextToken();
                    thePersonSendFile = tokenizer.nextToken();

                    privateChat = listReceiver.get(thePersonIamChattingWith);

                    if(privateChat == null) {
                        sender = this.name;
                        receiver = thePersonIamChattingWith;
                        privateChat = new PrivateChat(sender, receiver, serverHost, bufferedWriter, bufferedReader);

                        privateChat.getLbReceiver().setText("Private chat with \""+privateChat.receiver+"\"");
                        privateChat.setTitle(privateChat.receiver);
                        privateChat.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                        listReceiver.put(receiver, privateChat);
                    }

                    privateChat.setVisible(true);
                    privateChat.insertButton(thePersonSendFile, fileName);
                    break;

                default:
                    if(!response.startsWith("SERVER_")) {
                        if(response.equals("Warning: Server has been closed!")) {
                            this.clientPanel.appendMessage(response, Color.RED);
                        }
                        else this.clientPanel.appendMessage(response, new Color(153, 153, 153));
                    }

            }
        }
        System.out.println("Disconnected to server!");
    }

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
        ClientFrame client = new ClientFrame(null);
        client.setVisible(true);
    }

}
