package sugat.todos.myworks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import sugat.todos.myworks.data.TodoDBHandler;
import sugat.todos.myworks.fragments.AddOrUpdateTodoFragment;
import sugat.todos.myworks.fragments.TodoFragment;
import sugat.todos.myworks.fragments.DoneTodosFragment;
import sugat.todos.myworks.fragments.TodoInfoFragment;
import sugat.todos.myworks.models.Todo;
import sugat.todos.myworks.Receivers.WorkTimeReceiver;

public class MainActivity extends AppCompatActivity {

    private TodoDBHandler dbHandler;
    private TodoFragment todoFragment;
    private DoneTodosFragment doneTodosFragment;
    private BottomNavigationView bottomNavigationView;
    private ArrayList<Todo> doneTodoArrayList;
    private ArrayList<Todo> notDoneTodoArrayList;
    public static final String NotificationChannel = "sugat.todos.myworks.Channels.Notify";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_MyWorks);
        setContentView(R.layout.activity_main);
        createNotificationChannel();

        dbHandler = new TodoDBHandler(MainActivity.this);

        doneTodoArrayList = (ArrayList<Todo>) dbHandler.getAllDoneTodo();
        notDoneTodoArrayList = (ArrayList<Todo>) dbHandler.getNotDoneTodo();

        if (savedInstanceState == null) {

            todoFragment = new TodoFragment();
            doneTodosFragment = new DoneTodosFragment();

            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_container_view, todoFragment, "main")
                    .commit();
            
            bottomNavigationView = findViewById(R.id.bottomNavigationView);
            bottomNavigationView.setSelectedItemId(R.id.bottom_nav_home_item);
            
            bottomNavigationView.setOnItemSelectedListener(item -> {
                
                switch (item.getItemId()){
                    case R.id.bottom_nav_home_item :
                        clearBackStack();
                        getSupportFragmentManager().beginTransaction()
                                .setReorderingAllowed(true)
                                .replace(R.id.fragment_container_view, todoFragment)
                                .commit();
                        break;
                    
                    case R.id.bottom_nav_all_done_item :
                        clearBackStack();
                        getSupportFragmentManager().popBackStack();
                        getSupportFragmentManager().beginTransaction()
                                    .setReorderingAllowed(true)
                                    .replace(R.id.fragment_container_view, doneTodosFragment)
                                    .commit();
                        break;

                    case R.id.bottom_nav_add_item:
                        try {
                            clearBackStack();
                            getSupportFragmentManager().beginTransaction()
                                    .setReorderingAllowed(true)
                                    .replace(R.id.fragment_container_view, new AddOrUpdateTodoFragment(false,null))
                                    .commit();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case R.id.bottom_nav_invisible:
                        clearBackStack();
                        break;

                    default : 
                        System.out.println("Hello");
                        break;
                }
                
                return true;
            });
        }
    }

    @Override
    public void onBackPressed() {

        if (getSupportFragmentManager().getBackStackEntryCount() < 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    // Sql operations
    public void addTodo(@NonNull Todo todo) {
        dbHandler.addTodo(todo);
        bottomNavigationView.setSelectedItemId(R.id.bottom_nav_home_item);
        clearBackStack();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_view, todoFragment)
                .addToBackStack(null)
                .commit();
        notifyTodoSetChanged();

        Intent intent = new Intent(MainActivity.this, WorkTimeReceiver.class);
        intent.putExtra("title", todo.getTitle());
        intent.putExtra("desc", todo.getDesc());
        intent.putExtra("time", todo.getTimeString());

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(todo.getTimeInDate());
        
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0 , intent, 0);
        ((AlarmManager)getSystemService(Context.ALARM_SERVICE))
                .set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    public void onTodoDone(Todo todo) {
        todo.setDone(true);
        dbHandler.updateTodo(todo);
        notifyTodoSetChanged();
        try {
            Intent intent = new Intent(MainActivity.this, WorkTimeReceiver.class);
            intent.putExtra("title", todo.getTitle());
            intent.putExtra("desc", todo.getDesc());
            intent.putExtra("time", todo.getTimeString());

            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(todo.getTimeInDate());

            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
            ((AlarmManager) getSystemService(Context.ALARM_SERVICE))
                    .cancel(pendingIntent);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public ArrayList<Todo> getAllDoneTodo() {
        return doneTodoArrayList;
    }

    public ArrayList<Todo> getNotDoneTodos() {
        return notDoneTodoArrayList;
    }

    public void onTodoDelete(int id) {
        dbHandler.deleteTodoById(id);
        notifyTodoSetChanged();
    }
    
    // On Data Set (TodoArrayLists) Changed
    private void notifyTodoSetChanged() {
        notDoneTodoArrayList.clear();
        notDoneTodoArrayList.addAll(dbHandler.getNotDoneTodo());
        
        doneTodoArrayList.clear();
        doneTodoArrayList.addAll(dbHandler.getAllDoneTodo());

        todoFragment.notifyDataSetChanged();
        doneTodosFragment.notifyDataSetChanged();
    }

    public void onInfoClickListener(Todo todo){
        bottomNavigationView.setSelectedItemId(R.id.bottom_nav_invisible);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_view, new TodoInfoFragment(todo));

        if (doneTodosFragment.isVisible()) transaction.addToBackStack("doneTodoFragment").commit();
        else transaction.addToBackStack("notDoneFragment").commit();
    }
    
    public void editTodoUi(@NonNull Todo todo){
        try {
            bottomNavigationView.setSelectedItemId(R.id.bottom_nav_invisible);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_view, new AddOrUpdateTodoFragment(true,todo))
                    .addToBackStack("viewInfo")
                    .commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void editTodoSql(@NonNull Todo todo){
        Todo oldTodo = dbHandler.getTodoById(todo.getId());
        dbHandler.updateTodo(todo);
        notifyTodoSetChanged();

        try {
            Intent oldIntent = new Intent(MainActivity.this, WorkTimeReceiver.class);
            oldIntent.putExtra("title", oldTodo.getTitle());
            oldIntent.putExtra("desc" , oldTodo.getDesc());
            oldIntent.putExtra("time" , oldTodo.getTimeString());

            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(todo.getTimeInDate());

            PendingIntent oldPendingIntent = PendingIntent.getBroadcast(this, 1, oldIntent, 0);
            ((AlarmManager) getSystemService(Context.ALARM_SERVICE))
                    .cancel(oldPendingIntent);

            Intent intent = new Intent(MainActivity.this, WorkTimeReceiver.class);
            intent.putExtra("title", todo.getTitle());
            intent.putExtra("desc", todo.getDesc());
            intent.putExtra("time", WorkTimeReceiver.dateFormat.format(todo.getTimeInDate()));

            calendar = new GregorianCalendar();
            calendar.setTime(todo.getTimeInDate());

            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0 , intent, 0);
            ((AlarmManager)getSystemService(Context.ALARM_SERVICE))
                    .set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void createNotificationChannel(){

        NotificationChannel channel;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channel = new NotificationChannel(NotificationChannel,
                    "My Works notification channel", NotificationManager.IMPORTANCE_HIGH);

            channel.setDescription("This channel is to notify about work");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

    }
    
    public void clearBackStack(){
        for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
            getSupportFragmentManager().popBackStack();
        }
    }
}