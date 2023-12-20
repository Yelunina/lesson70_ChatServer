package ait.chat.server.task;

import ait.mediation.BlkQueue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ChatServerReceiver implements Runnable {
    private Socket socket;
    private BlkQueue<String> messageBox;

    public ChatServerReceiver(Socket socket, BlkQueue<String> messageBox) {
        this.socket = socket;
        this.messageBox = messageBox;
    }

    @Override
    public void run() {
        try (Socket socket = this.socket) {
            BufferedReader socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (true) {
                String message = socketReader.readLine();
                messageBox.push(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}