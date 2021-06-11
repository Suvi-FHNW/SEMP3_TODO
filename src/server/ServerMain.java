package server;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Logger;

public class ServerMain extends Thread {
    private ServerModel model;
    private ServerSocket listener;
    private ArrayList<Socket> clientSockets = new ArrayList<>();
    private final int port = 50002;
    private static Logger log;
    // private final String ip = "147.86.8.31";

    public ServerMain() {
        model = new ServerModel();
    }

    public static void main (String[] args) {
        log = Logger.getLogger("main");
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
        model.getUserData().saveDataToFile();
        model.getTodoData().saveDataToFile();
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
                ClientThread client = new ClientThread(this, clientSocket.getInputStream(), clientSocket.getOutputStream());
                client.start();
                log.info("Connection to client established, ClientID: " +client.getClientId());
            }
        } catch (Exception e) {
            log.info("Server is closed - no connection possible");
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

    public void sendMessage(ClientThread ct, String message) {
        try {
            OutputStreamWriter out = new OutputStreamWriter(ct.getOutStream());
            out.write(message+"\n");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Logger getLogger() {
        return log;
    }

    public ServerModel getModel() {
        return model;
    }

    public void setModel(ServerModel model) {
        this.model = model;
    }
}