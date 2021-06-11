package server;

import server.api.MsgChangePassword;
import server.api.MsgCreateLogin;
import server.api.MsgLogin;

import java.util.ArrayList;
import java.util.logging.Logger;

public class ServerModel {
    private ArrayList<User> userList = new ArrayList<>();
    private Logger log = ServerMain.getLogger();
    private int minPasswordLength = 5;
    private UserData userData;
    private ToDoData todoData;

    public ServerModel() {
        log.info("ServerModel created");
        userData = new UserData(this);
        userData.readData();
        todoData = new ToDoData(this);
        todoData.readData();
    }

    public String handleMessage(String message) {
        String returnString = "";
        String[] msgParts = message.split("\\|");
        long token;
        ToDoTask task;
        User user;
        // log.info(msgParts[0]);
        if (validateMessage()) {
            switch (msgParts[0]) {
                case "Ping":
                    returnString = "Result|True";
                    break;
                case "CreateLogin":
                    MsgCreateLogin msgCreateLogin = new MsgCreateLogin(msgParts[1], msgParts[2]);
                    if (userAlreadyExists(msgCreateLogin)) {
                        returnString = buildMsgFailure();
                    } else {
                        user = new User(msgCreateLogin.getUserName(), msgCreateLogin.getPassword());
                        log.info("New user created: " + user.toString());
                        userList.add(user);
                        returnString = buildMsgSuccess();
                    }
                    break;
                case "Login":
                    MsgLogin msgLogin = new MsgLogin(msgParts[1], msgParts[2]);
                    if (matchingCredentials(msgLogin)) {
                        user = getUserFromList(msgLogin);
                        user.createToken();
                        returnString = buildMsgSuccess() + "|" + user.getToken();
                    } else {
                        returnString = buildMsgFailure();
                    }
                    break;
                case "ChangePassword":
                    token = Long.parseLong(msgParts[1]);
                    MsgChangePassword msgChangePassword = new MsgChangePassword(token, msgParts[2]);
                    if (checkToken(token)) {
                        if (changePasswordFailed(msgChangePassword)) {
                            returnString = buildMsgFailure();
                        } else {
                            user = getUserByToken(token);
                            user.setPassword(msgChangePassword.getPassword());
                            log.info(user.toString());
                            returnString = buildMsgSuccess();
                        }
                    } else {
                        returnString = buildMsgFailure();
                    }
                    break;
                case "Logout":
                    // TODO log out correct client
                    returnString = buildMsgSuccess();
                    break;
                case "CreateToDo":
                    token = Long.parseLong(msgParts[1]);
                    if (checkToken(token)) {
                        // TODO check data for validity
                        user = getUserByToken(token);
                        task = new ToDoTask(msgParts[2], msgParts[3], msgParts[4], user.getId());
                        user.getToDoList().add(task);
                        returnString = buildMsgSuccess() + "|" + task.getId();

                        for (User u : userList) {
                            for (ToDoTask t : u.getToDoList()) {
                                System.out.println(t);
                            }
                        }
                    } else {
                        returnString = buildMsgFailure();
                    }
                    break;
                case "GetToDo":
                    token = Long.parseLong(msgParts[1]);
                    task = null;
                    if (checkToken(token)) {
                        try {
                            int taskId = Integer.parseInt(msgParts[2]);
                            user = getUserByToken(token);
                            for (ToDoTask t : user.getToDoList()) {
                                if (taskId == t.getId()) {
                                    task = t;
                                }
                            }
                            returnString = buildMsgSuccess() + "|" + task.toString();
                        } catch (NumberFormatException e) {
                            returnString = buildMsgFailure();
                        }
                    }
                    break;
                case "DeleteToDo":
                    token = Long.parseLong(msgParts[1]);
                    task = null;
                    if (checkToken(token)) {
                        try {
                            int taskId = Integer.parseInt(msgParts[2]);
                            user = getUserByToken(token);
                            for (ToDoTask t : user.getToDoList()) {
                                if (taskId == t.getId()) {
                                    task = t;
                                }
                            }
                            user.getToDoList().remove(task);
                            returnString = buildMsgSuccess();
                        } catch (NumberFormatException e){
                            returnString = buildMsgFailure();
                        }
                    }
                    break;
                case "ListToDos":
                    token = Long.parseLong(msgParts[1]);
                    if (checkToken(token)) {
                        returnString = buildMsgSuccess();
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(returnString);
                        user = getUserByToken(token);
                        for (ToDoTask t : user.getToDoList()) {
                            stringBuilder.append("|" + t.getId());
                        }
                        returnString = stringBuilder.toString();
                    } else {
                        returnString = buildMsgFailure();
                    }
            }
        } else {
            returnString = buildMsgFailure();
        }
        return returnString;
    }

    private boolean validateMessage() {
        return true;
    }

    private boolean userAlreadyExists(MsgCreateLogin msg) {
        boolean alreadyExists = false;
        if (userList.size() != 0) {
            for (User u : userList) {
                if (u.getName().equals(msg.getUserName())) {
                    alreadyExists = true;
                }
            }
        }
        return alreadyExists;
    }

    private boolean matchingCredentials(MsgLogin msg) {
        boolean matchingCredentials = false;
        if (userList.size() != 0) {
            for (User u : userList) {
                if (msg.getUserName().equals(u.getName()) && msg.getPassword().equals(u.getPassword())) {
                    matchingCredentials = true;
                }
            }
        } else {
            log.info("No existing user in UserList");

        }
        return matchingCredentials;
    }

    private boolean changePasswordFailed(MsgChangePassword msg) {
        boolean changePwFailed = false;
        if (msg.getPassword().length() <= minPasswordLength) {
            changePwFailed = true;
        }
        return changePwFailed;
    }

    private String buildMsgSuccess(){
        return "Result|True";
    }

    private String buildMsgFailure(){
        return "Result|False";
    }

    private User getUserFromList(MsgLogin msg) {
        User userToReturn = null;
        for (User u : userList) {
            if (msg.getUserName().equals(u.getName()) && msg.getPassword().equals(u.getPassword())) {
                userToReturn = u;
            }
        }
        return userToReturn;
    }

    private User getUserByToken(long token) {
        for (User u : userList) {
            if (u.getToken() == token) {
                return u;
            }
        }
        return null;
    }

    private boolean checkToken(long token) {
        boolean valid = false;
        for (User u : userList) {
            if (u.getToken() == token) {
                valid = true;
                log.info("Check Token - Current user: " + u.toString());
                break;
            }
        }
        return valid;
    }

    public ArrayList<User> getUserList() {
        return userList;
    }

    public void setUserList(ArrayList<User> userList) {
        this.userList = userList;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public ToDoData getTodoData() {
        return todoData;
    }

    public void setTodoData(ToDoData todoData) {
        this.todoData = todoData;
    }
}
