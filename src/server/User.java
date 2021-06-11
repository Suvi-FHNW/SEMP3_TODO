package server;

import java.util.ArrayList;
import java.util.Random;

public class User {
    private String name;
    private String password;
    private long token; // only needed for runtime
    private static int staticUserId = 1;
    private int userId;
    private int clientId;
    private ArrayList<ToDoTask> toDoList = new ArrayList<>();
    private Random rand = new Random();

    public User(String name, String password) {
        this.name = name;
        this.password = password;
        userId = staticUserId;
        staticUserId++;
    }

    public User() {
        staticUserId++;
    }

    public void createToken() {
        token = Math.abs(rand.nextLong());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return userId;
    }

    public void setId(int id) {
        this.userId = id;
    }

    public ArrayList<ToDoTask> getToDoList() {
        return toDoList;
    }

    public void setToDoList(ArrayList<ToDoTask> toDoList) {
        this.toDoList = toDoList;
    }

    public long getToken() {
        return token;
    }

    public void setToken(long token) {
        this.token = token;
    }

    public String toString() {
        return "ID: "+this.getId() + ", Name: "+this.getName()+", Password: "+this.getPassword();
    }
}
