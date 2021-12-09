package sugat.todos.myworks.Params;

public interface Params {
    String Key_id = "todo_id";
    String Key_title = "todo_title";
    String Key_desc = "todo_desc";
    String Key_time = "todo_time";
    String Key_done = "todo_done";

    int db_Version = 1;
    String db_Name = "db_todos";
    String db_MainTable = "db_main_table";

    String todo_title = "sugat.todos.myworks.Models.Todos.Title";
    String todo_desc = "sugat.todos.myworks.Models.Todos.desc";

    String NotificationChannel = "sugat.todos.myworks.Channels.Notify";
}
