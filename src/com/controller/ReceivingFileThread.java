package com.controller;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import com.server.ServerCommands;
import com.view.PrivateChat;

import javax.swing.*;

public class ReceivingFileThread extends Thread {

    public BufferedWriter bufferedWriter;
    public BufferedReader bufferedReader;
    public Socket socketOfReceiver;
    public String myDownloadFolder;
    public String fileName;
    public PrivateChat frameToDisplayDialog;

    public ReceivingFileThread(Socket socketOfReceiver, String myDownloadFolder, String fileName, PrivateChat pc) {
        this.socketOfReceiver = socketOfReceiver;
        this.myDownloadFolder = myDownloadFolder;
        this.fileName = fileName;
        this.frameToDisplayDialog = pc;

        try {
            bufferedReader = new BufferedReader(new InputStreamReader(socketOfReceiver.getInputStream()));
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socketOfReceiver.getOutputStream()));
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
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        try {
            this.sendToServer(ServerCommands.DOWNLOAD_FILE+"|"+fileName);
            bis = new BufferedInputStream(socketOfReceiver.getInputStream());
            fos = new FileOutputStream(myDownloadFolder + "\\" + fileName);

            byte[] buffer = new byte[1024];
            int count;
            while((count = bis.read(buffer)) > 0) {
                fos.write(buffer, 0, count);
            }

            JOptionPane.showMessageDialog(frameToDisplayDialog, "File downloaded to\n"+myDownloadFolder + "\\" + fileName, "Info", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
           ex.printStackTrace();
        } finally {
            try {
                if(bis != null) bis.close();
                if(fos != null) fos.close();
                socketOfReceiver.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}