package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClientMain extends Thread{
    private BufferedReader socketIn;
    Scanner in = new Scanner(System.in);
    private int port = 55555;
    private String ipAddress = "127.0.0.1";
    private String msg = "";


    public static void main (String[] args) {
        ClientMain client = new ClientMain();
        client.readFromConsole();
    }

    private ClientMain() {
        super("Client");
    }

    private void readFromConsole() {
        try (Socket socket = new Socket(ipAddress, port)) {
            OutputStreamWriter socketOut = new OutputStreamWriter(socket.getOutputStream());
            socketIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.start();
            while (!msg.equals("stop")) {
                System.out.println("Enter Message:");
                msg = in.nextLine();
                socketOut.write(msg + "\n");
                socketOut.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        in.close();
    }

    @Override
    public void run() {
        try {
            String message = "";
            while (message != null) {
                message = socketIn.readLine();
                System.out.println(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
