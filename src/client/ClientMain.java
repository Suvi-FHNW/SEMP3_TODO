package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Logger;

public class ClientMain extends Thread{
    private BufferedReader socketIn;
    Scanner in = new Scanner(System.in);
    private int port = 50002;
    private String ipAddress = "127.0.0.1";
    private String msg = "";
    public static Logger log;


    public static void main (String[] args) {
        log = Logger.getLogger("client");
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
            log.info("Connection to server established on port 50002");
            System.out.println("Enter Message:");
            while (!msg.equals("stop")) {

                msg = in.nextLine();
                socketOut.write(msg + "\n");
                socketOut.flush();
                log.info("messge sent to server");
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
                System.out.println("Enter Message:");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
