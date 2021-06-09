package server;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ServerMain extends Thread {

    private ServerSocket listener;
    private ArrayList<Socket> clientSockets = new ArrayList<>();
    private final int port = 55555;
    // private final String ip = "147.86.8.31";


    public static void main (String[] args) {
        ServerMain server = new ServerMain();
        server.start();
        server.waitForStop();
    }

    private void waitForStop() {
        System.out.println("Enter 'stop' to end the server");
        try (Scanner in = new Scanner(System.in)) {
            String cmd = in.nextLine();
            while (!cmd.equals("stop")) {
                cmd = in.nextLine();
            }
        }

        closeIgnoreExceptions(listener);
        for (Socket s : clientSockets) {
            closeIgnoreExceptions(s);
        }
    }

    private void closeIgnoreExceptions(Closeable a) {
        try {
            a.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            listener = new ServerSocket(port);
            while (true) {
                Socket clientSocket = listener.accept();
                clientSockets.add(clientSocket);
                ClientThread client = new ClientThread(this, clientSocket.getInputStream());
                client.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void broadcast(String message) {
        for (Socket s : clientSockets) {
            try {
                OutputStreamWriter out = new OutputStreamWriter(s.getOutputStream());
                out.write(message + "\n");
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }




}