package server;

import java.io.*;
import java.net.URL;
import java.util.logging.Logger;

public class UserData {
    private ServerModel serverModel;
    private Logger log = ServerMain.getLogger();

    public UserData(ServerModel serverModel) {
        this.serverModel = serverModel;
    }


    public void readData() {
        InputStream inStream = this.getClass().getClassLoader().getResourceAsStream("server/users.txt");
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
                        User u = new User();
                        u.setId(Integer.parseInt(data[0]));
                        u.setName(data[1]);
                        u.setPassword(data[2]);
                        serverModel.getUserList().add(u);
                        log.info(u.toString());
                    }
                }
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("Successfully read user data");
    }

    public void saveDataToFile() {
        File file = new File("src/server/users.txt");
        String data = "";
        try (FileWriter fileOut = new FileWriter(file)) {
            for (User u : serverModel.getUserList()) {
                data = u.getId() + "," + u.getName() + "," + u.getPassword();
                fileOut.write(data + "\n");
                fileOut.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
