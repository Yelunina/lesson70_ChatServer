package ait.chat.server;

import ait.chat.server.task.ChatServerReceiver;
import ait.chat.server.task.ChatServerSender;
import ait.mediation.BlkQueue;
import ait.mediation.BlkQueueImpl;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ChatServerAppl {
    public static void main(String[] args) {
        int port = 9000;
        BlkQueue<String> messageBox = new BlkQueueImpl<>(10);
        ChatServerSender sender = new ChatServerSender(messageBox);
        Thread senderThread =  new Thread(sender);
        senderThread.start();
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            try {
                while (true) {
                    System.out.println("server wait....");
                    Socket socket = serverSocket.accept();
                    System.out.println("Connection established");
                    System.out.println("Client host: " + socket.getInetAddress() + ":" + socket.getPort());
                    sender.addClient(socket);
                    ChatServerReceiver receiver = new ChatServerReceiver(socket,messageBox);
                    executorService.execute(receiver);
                }
            } finally {
                executorService.shutdown();
                executorService.awaitTermination(1, TimeUnit.MINUTES);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        }
    }

