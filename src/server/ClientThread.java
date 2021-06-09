package server;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ClientThread extends Thread{
    private static int clientID = 1;
    private ServerMain server;
    private BufferedReader in;

    public ClientThread(ServerMain server, InputStream in) {
        super("Client "+ clientID++);
        this.server = server;
        this.in = new BufferedReader(new InputStreamReader(in));
    }

    @Override
    public void run() {
        try {
            while (true) {
                String message = in.readLine();
                System.out.println("Message from client: "+ message);
                String messageToClient = "Hi, I'm the server";
                server.broadcast(messageToClient);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(this.getName() + " terminated");
    }
}