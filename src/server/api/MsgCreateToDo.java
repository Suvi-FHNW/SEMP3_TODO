package server.api;

public class MsgCreateToDo {
    String title;
    String priority;
    String description;

    public MsgCreateToDo(String title, String priority, String description) {
        this.title = title;
        this.priority = priority;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
