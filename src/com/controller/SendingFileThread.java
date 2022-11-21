package com.controller;


import com.server.ServerCommands;

import javax.swing.*;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class SendingFileThread extends Thread {

    public String sender;
    public String receiver;
    public String filePath;
    public Socket socketOfSender;
    public BufferedWriter bufferedWriter;
    public BufferedReader bufferedReader;
    public JProgressBar progressBar;
    public SendFileFrame frameToDisplayDialog;

    public SendingFileThread(String sender, String receiver, String filePath, Socket socket, SendFileFrame frameToDisplayDialog, JProgressBar progressBar) {
        this.sender = sender;
        this.receiver = receiver;
        this.filePath = filePath;
        this.socketOfSender = socket;
        this.frameToDisplayDialog = frameToDisplayDialog;
        this.progressBar = progressBar;

        try {
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socketOfSender.getOutputStream()));
            bufferedReader = new BufferedReader(new InputStreamReader(socketOfSender.getInputStream()));
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
            JOptionPane.showMessageDialog(null, "Server is close, can't send message!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public void run() {
        FileInputStream fileInputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        try {
            File file = new File(filePath);
            this.sendToServer(ServerCommands.SEND_FILE_TO_SERVER+"|"+sender+"|"+receiver+"|"+file.getName()+"|"+(int) file.length());
            fileInputStream = new FileInputStream(file);
            bufferedOutputStream = new BufferedOutputStream(socketOfSender.getOutputStream());

            byte []buffer = new byte[1024];
            int count=0;
            while((count = fileInputStream.read(buffer)) > 0) {
                bufferedOutputStream.write(buffer, 0, count);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if(fileInputStream != null) fileInputStream.close();
                if(bufferedOutputStream != null) bufferedOutputStream.close();
                socketOfSender.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        JOptionPane.showMessageDialog(frameToDisplayDialog, "File successfully sent!", "Notice!", JOptionPane.INFORMATION_MESSAGE);
    }

}
