package ait.chat.server.task;

import ait.mediation.BlkQueueImpl;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class ChatServerSender implements Runnable {
    private BlkQueueImpl<String> messageBox;
    private Set<PrintWriter> clients;

    public ChatServerSender(BlkQueueImpl<String> messageBox, Set<Socket> clients) {
        this.messageBox = messageBox;
        clients = new HashSet<>();
    }

    public boolean addClient(Socket socket) throws IOException {
        return clients.add(new PrintWriter(socket.getOutputStream(),true));
    }

    @Override
    public void run() {
        while (true) {
            String message = messageBox.pop();
            clients.forEach(c -> c.println(message));
        }
    }
}
