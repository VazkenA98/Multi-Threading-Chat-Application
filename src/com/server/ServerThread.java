package com.server;


import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;

public class ServerThread extends Thread {

    public Socket socketOfServer;
    public BufferedWriter bufferedWriter;
    public BufferedReader bufferedReader;
    public String clientName;
    public String clientRoom;
    public static Hashtable<String, ServerThread> listUser = new Hashtable<>();
    public static final String NICKNAME_EXIST = "This nickname is already exist";
    public static final String NICKNAME_VALID = "This nickname is okay";
    public JTextArea taServer;
    public SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
    public StringTokenizer tokenizer;
    public static boolean isBusy = false;

    public ServerThread(Socket socketOfServer) {
        this.socketOfServer = socketOfServer;
        this.bufferedWriter = null;
        this.bufferedReader = null;
        clientName = "";
        clientRoom = "";
    }

    public void appendMessage(String message) {
        taServer.append(message);
        taServer.setCaretPosition(taServer.getText().length() - 1);
    }

    public String receiveFromClient() {
        try {
            return bufferedReader.readLine();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public void sendToClient(String response) {
        try {
            bufferedWriter.write(response);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void sendToSpecificClient(ServerThread socketOfClient, String response) {
        try {
            BufferedWriter writer = socketOfClient.bufferedWriter;
            writer.write(response);
            writer.newLine();
            writer.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void notifyToAllUsers(String message) {

        Enumeration<ServerThread> clients = listUser.elements();
        ServerThread serverThread;
        BufferedWriter bufferedWriter;

        while (clients.hasMoreElements()) {
            serverThread = clients.nextElement();
            bufferedWriter = serverThread.bufferedWriter;
            try {
                bufferedWriter.write(message);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void notifyToUsersInRoom(String message) {
        Enumeration<ServerThread> clients = listUser.elements();
        ServerThread serverThread;
        BufferedWriter bufferedWriter;

        while (clients.hasMoreElements()) {
            serverThread = clients.nextElement();
            if (serverThread.clientRoom.equals(this.clientRoom)) {
                bufferedWriter = serverThread.bufferedWriter;

                try {
                    bufferedWriter.write(message);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public void notifyToUsersInRoom(String room, String message) {
        Enumeration<ServerThread> clients = listUser.elements();
        ServerThread serverThread;
        BufferedWriter bufferedWriter;

        while (clients.hasMoreElements()) {
            serverThread = clients.nextElement();
            if (serverThread.clientRoom.equals(room)) {
                bufferedWriter = serverThread.bufferedWriter;
                try {
                    bufferedWriter.write(message);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public String getAllUsers() {
        StringBuffer stringBuffer = new StringBuffer();
        String temp = null;

        Enumeration<String> keys = listUser.keys();
        if (keys.hasMoreElements()) {
            String str = keys.nextElement();
            stringBuffer.append(str);
        }

        while (keys.hasMoreElements()) {
            temp = keys.nextElement();
            stringBuffer.append("|").append(temp);
        }
        return stringBuffer.toString();
    }

    public String getUsersThisRoom() {
        StringBuffer stringBuffer = new StringBuffer();
        String temp = null;
        ServerThread serverThread;
        Enumeration<String> keys = listUser.keys();

        while (keys.hasMoreElements()) {
            temp = keys.nextElement();
            serverThread = listUser.get(temp);
            if (serverThread.clientRoom.equals(this.clientRoom)) {
                stringBuffer.append("|").append(temp);
            }
        }

        if (stringBuffer.equals(""))
            return "|";
        return stringBuffer.toString();
    }

    public String getUsersAtRoom(String room) {
        StringBuffer stringBuffer = new StringBuffer();
        String temp = null;
        ServerThread serverThread;
        Enumeration<String> keys = listUser.keys();

        while (keys.hasMoreElements()) {
            temp = keys.nextElement();
            serverThread = listUser.get(temp);
            if (serverThread.clientRoom.equals(room)) {
                stringBuffer.append("|").append(temp);
            }
        }

        if (stringBuffer.equals(""))
            return "|";
        return stringBuffer.toString();
    }

    public void clientQuit() {
        if (clientName != null) {
            this.appendMessage("\n[" + simpleDateFormat.format(new Date()) + "] Client \"" + clientName + "\" is disconnected");
            listUser.remove(clientName);
            if (listUser.isEmpty())
                this.appendMessage("\n[" + simpleDateFormat.format(new Date()) + "] There is no one in server\n");
            notifyToAllUsers(ServerCommands.ONLINE_USERS + "|" + getAllUsers());
            notifyToUsersInRoom(ServerCommands.ONLINE_USERS_IN_ROOM + getUsersThisRoom());
            notifyToUsersInRoom(clientName + " has disconnected");
        }
    }

    public void changeUserRoom() {
        ServerThread serverThread = listUser.get(this.clientName);
        serverThread.clientRoom = this.clientRoom;
        listUser.put(this.clientName, serverThread);
    }

    public void removeUserRoom() {
        ServerThread serverThread = listUser.get(this.clientName);
        serverThread.clientRoom = this.clientRoom;
        listUser.put(this.clientName, serverThread);
    }

    @Override
    public void run() {
        try {
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socketOfServer.getOutputStream()));
            bufferedReader = new BufferedReader(new InputStreamReader(socketOfServer.getInputStream()));
            boolean isUserExist;
            String message;
            String sender;
            String receiver;
            String fileName;
            StringBuffer stringBuffer;
            String serverCommand;
            while (true) {
                try {
                    message = receiveFromClient();
                    tokenizer = new StringTokenizer(message, "|");
                    serverCommand = tokenizer.nextToken();

                    switch (serverCommand) {
                        case ServerCommands.CHAT_COMMAND:
                            stringBuffer = new StringBuffer(message);
                            stringBuffer = stringBuffer.delete(0, 9);
                            notifyToUsersInRoom(ServerCommands.CHAT_COMMAND + "|" + this.clientName + "|" + stringBuffer.toString());
                            break;

                        case ServerCommands.PRIVATE_CHAT:
                            String privateSender = tokenizer.nextToken();
                            String privateReceiver = tokenizer.nextToken();
                            String messageContent = message.substring(serverCommand.length() + privateSender.length() + privateReceiver.length() + 3, message.length());
                            ServerThread st_receiver = listUser.get(privateReceiver);
                            sendToSpecificClient(st_receiver, ServerCommands.PRIVATE_CHAT + "|" + privateSender + "|" + messageContent);
                            break;

                        case ServerCommands.ENTER_ROOM:
                            clientRoom = tokenizer.nextToken();
                            changeUserRoom();
                            notifyToAllUsers(ServerCommands.ONLINE_USERS + "|" + getAllUsers());
                            notifyToUsersInRoom(ServerCommands.ONLINE_USERS_IN_ROOM + getUsersThisRoom());
                            notifyToUsersInRoom(clientName + " has just entered!");
                            break;

                        case ServerCommands.LEAVE_ROOM_COMMAND:
                            String room = clientRoom;
                            clientRoom = "";
                            removeUserRoom();
                            this.appendMessage("\n[" + simpleDateFormat.format(new Date()) + "] User \"" + clientName + "\" left the room:\n" + room);
                            notifyToUsersInRoom(room, ServerCommands.ONLINE_USERS_IN_ROOM + getUsersAtRoom(room));
                            notifyToUsersInRoom(room, clientName + " left this room!");
                            break;

                        case ServerCommands.CHECK_USER_USERNAME:
                            clientName = tokenizer.nextToken();
                            isUserExist = listUser.containsKey(clientName);

                            if (isUserExist) {
                                sendToClient(NICKNAME_EXIST);
                            } else {
                                sendToClient(NICKNAME_VALID);
                                this.appendMessage("\n[" + simpleDateFormat.format(new Date()) + "] User \"" + clientName + "\" joined the server");
                                listUser.put(clientName, this);
                            }
                            break;

                        case ServerCommands.ONLINE_USERS:
                            sendToClient(ServerCommands.ONLINE_USERS + "|" + getAllUsers());
                            notifyToUsersInRoom(ServerCommands.ONLINE_USERS_IN_ROOM + getUsersThisRoom());
                            break;

                        case ServerCommands.SEND_FILE_TO_SERVER:
                            sender = tokenizer.nextToken();
                            receiver = tokenizer.nextToken();
                            fileName = tokenizer.nextToken();

                            String path = System.getProperty("user.dir") + "\\sendfile\\" + fileName;

                            BufferedInputStream bufferedInputStream = new BufferedInputStream(socketOfServer.getInputStream());
                            FileOutputStream fileOutputStream = new FileOutputStream(path);

                            byte[] buffer = new byte[1024];
                            int count = -1;
                            while ((count = bufferedInputStream.read(buffer)) > 0) {
                                fileOutputStream.write(buffer, 0, count);
                            }

                            Thread.sleep(300);
                            bufferedInputStream.close();
                            fileOutputStream.close();
                            socketOfServer.close();

                            ServerThread serverThreadSender = listUser.get(sender);
                            ServerThread serverThreadReceiver = listUser.get(receiver);
                            sendToSpecificClient(serverThreadSender, ServerCommands.AVAILABLE_FILE + "|" + fileName + "|" + receiver + "|" + sender);
                            sendToSpecificClient(serverThreadReceiver, ServerCommands.AVAILABLE_FILE + "|" + fileName + "|" + sender + "|" + sender);

                            isBusy = false;
                            break;

                        case ServerCommands.DOWNLOAD_FILE:
                            fileName = tokenizer.nextToken();
                            path = System.getProperty("user.dir") + "\\sendfile\\" + fileName;
                            FileInputStream fileInputStream = new FileInputStream(path);
                            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(socketOfServer.getOutputStream());

                            byte[] buffer2 = new byte[1024];
                            int count2 = 0;

                            while ((count2 = fileInputStream.read(buffer2)) > 0) {
                                bufferedOutputStream.write(buffer2, 0, count2);
                            }
                            bufferedOutputStream.close();
                            fileInputStream.close();
                            socketOfServer.close();
                            break;

                        default:
                            notifyToAllUsers(message);
                            break;
                    }

                } catch (Exception e) {
                    clientQuit();
                    break;
                }
            }
        } catch (IOException ex) {
            clientQuit();
            ex.printStackTrace();
        }
    }
}