package sugat.todos.myworks.models;

public class Todo {

    private int id;
    private String title;
    private String desc;
    private boolean done = false;

    public Todo(){}

    private Todo(int id, String title, String desc){
        this.id = id;
        this.title = title;
        this.desc = desc;
    }

    public Todo(String title, String desc){
        this.title = title;
        this.desc = desc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isDone() {
        return done;
    }

    public int isDoneInt() {
        if (isDone()) return 1;
        else return 0;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
