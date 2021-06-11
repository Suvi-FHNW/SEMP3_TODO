package server;

public class ToDoTask {
    enum Priority {High, Medium, Low;
        public String toString(){
            switch(this){
                case High :
                    return "High";
                case Medium :
                    return "Medium";
                case Low :
                    return "Low";
            }
            return null;
        }}
    private String title;
    private Priority priority;
    private String description;
    private int id;
    private static int staticId = 1;
    private int userId;

    public ToDoTask(String title, String priority, String description, int userId) {
        this.title = title;
        for (Priority p : Priority.values()) {
            if (priority.equals(p.toString())) {
                this.priority = p;
            }
        }
        this.description = description;
        id = staticId;
        staticId++;
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String toString() {
        return this.getId() + "|" + this.getTitle() + "|" + this.getPriority().toString() + "|" + this.getDescription();
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
