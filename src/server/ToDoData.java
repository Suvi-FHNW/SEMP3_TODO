package server;

import java.io.*;
import java.net.URL;
import java.util.logging.Logger;

public class ToDoData {
    private ServerModel model;
    private Logger log = ServerMain.getLogger();

    public ToDoData (ServerModel model) {
        this.model = model;
    }

    public void readData() {
        InputStream inStream = this.getClass().getClassLoader().getResourceAsStream("server/todo.txt");
        String dataString = "";
        String data[];
        boolean run = true;
        try (BufferedReader fileIn = new BufferedReader(new InputStreamReader(inStream))) {
            while (run) {
                dataString = fileIn.readLine();
                if (dataString == null || dataString.equals("")) {
                    run = false;
                } else {
                    data = dataString.split(",");
                    int userId = Integer.parseInt(data[3]);
                    ToDoTask t = new ToDoTask(data[0], data[1], data[2], userId);
                    for (User u : model.getUserList()) {
                        if (u.getId() == userId) {
                            u.getToDoList().add(t);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveDataToFile() {
        File file = new File("src/server/todo.txt");
        String data = "";
        try (FileWriter fileOut = new FileWriter(file)) {
            for (User u : model.getUserList()) {
                for (ToDoTask t : u.getToDoList()) {
                    data = t.getTitle() + "," + t.getPriority() + "," + t.getDescription() + "," + t.getUserId();
                    fileOut.write(data + "\n");
                    fileOut.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
