package sugat.todos.myworks.models;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import sugat.todos.myworks.R;

public class Todo {
    private int id;
    private String title;
    private String desc;
    private Date time;
    private boolean done = false;
    @SuppressLint("SimpleDateFormat")
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("dd LLL, yyyy KK:mm:ss aaa");

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
    
    public Todo(String title, String desc, Date time){
        this(title,desc);
        this.time = time;
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

    public Date getTimeInDate() {
        return time;
    }

    public String getTimeString(){
        return dateFormat.format(time);
    }

    public void setTimeDate(@NonNull Date time) {
        this.time = time;
    }

    public int isDoneInt() {
        if (isDone()) return 1;
        else return 0;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
