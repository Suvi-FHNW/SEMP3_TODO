package server;


import java.io.*;
import java.util.ArrayList;
import java.util.logging.Logger;

public class ClientThread extends Thread{
    private static int staticClientID = 1;
    private int clientId;
    private ServerMain server;
    private BufferedReader in;
    private Logger log;
    private ServerModel model;
    private ArrayList<Long> tokenList;
    private OutputStream outStream;

    public ClientThread(ServerMain server, InputStream in, OutputStream out) {
        super("nameToChange");
        clientId = staticClientID;
        this.setName("Client"+clientId);
        staticClientID++;
        this.server = server;
        this.in = new BufferedReader(new InputStreamReader(in));
        log = ServerMain.getLogger();
        model = server.getModel();
        tokenList = new ArrayList<>();
        this.outStream = out;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String message = in.readLine();
                // long token = addTokenToList(message);
                // System.out.println("Message from client: "+ message);
                log.info("Client  message received: " + message);
                String answer = model.handleMessage(message);

                server.sendMessage(this, answer);
                log.info("Message sent to client"+this.getClientId()+": " +answer);
            }
        } catch (IOException e) {
            log.info("Server is closed");
        }
        System.out.println(this.getName() + " terminated");
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public OutputStream getOutStream() {
        return outStream;
    }

    public void setOutStream(OutputStream outStream) {
        this.outStream = outStream;
    }
}