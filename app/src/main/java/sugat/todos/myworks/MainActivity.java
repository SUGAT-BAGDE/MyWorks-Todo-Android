package sugat.todos.myworks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import sugat.todos.myworks.JobServices.NotificationJobService;
import sugat.todos.myworks.Params.Params;
import sugat.todos.myworks.Receivers.WorkTimeReceiver;
import sugat.todos.myworks.data.TodoDBHandler;
import sugat.todos.myworks.fragments.AddOrUpdateTodoFragment;
import sugat.todos.myworks.fragments.TodoFragment;
import sugat.todos.myworks.fragments.DoneTodosFragment;
import sugat.todos.myworks.fragments.TodoInfoFragment;
import sugat.todos.myworks.models.Todo;

public class MainActivity extends AppCompatActivity {

    private TodoDBHandler dbHandler;
    private TodoFragment todoFragment;
    private DoneTodosFragment doneTodosFragment;
    private BottomNavigationView bottomNavigationView;
    private ArrayList<Todo> doneTodoArrayList;
    private ArrayList<Todo> notDoneTodoArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_MyWorks);
        setContentView(R.layout.activity_main);

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
            
            bottomNavigationView.setOnItemSelectedListener(this::changeFragmentByBottomNav);
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
        long id = dbHandler.addTodo(todo);
        bottomNavigationView.setSelectedItemId(R.id.bottom_nav_home_item);
        clearBackStack();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_view, todoFragment)
                .addToBackStack(null)
                .commit();
        notifyTodoSetChanged();

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(todo.getTimeInDate());

        /*
        Intent intent = new Intent(MainActivity.this, WorkTimeReceiver.class);
        intent.putExtra(Params.todo_title, todo.getTitle());
        intent.putExtra(Params.todo_desc, todo.getDesc());
        intent.putExtra("time", todo.getTimeString());
        
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 1 , intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        ((AlarmManager)getSystemService(Context.ALARM_SERVICE))
                .setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
         */
        
        scheduleTodoWork(todo);

    }

    public void onTodoDone(Todo todo) {
        todo.setDone(true);
        dbHandler.updateTodo(todo);
        notifyTodoSetChanged();
        try {
            /*
            Intent intent = new Intent(MainActivity.this, WorkTimeReceiver.class);
            intent.putExtra(Params.todo_title, todo.getTitle());
            intent.putExtra(Params.todo_desc, todo.getDesc());
            intent.putExtra("time", todo.getTimeString());                       

            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(todo.getTimeInDate());

            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 1, intent, 0);
            ((AlarmManager) getSystemService(Context.ALARM_SERVICE))
                    .cancel(pendingIntent);
                    
             */

            cancelTodoWork(todo.getId());
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
        dbHandler.updateTodo(todo);
        notifyTodoSetChanged();

        if (todo.isDone()) {
            try {
                /*
            Intent oldIntent = new Intent(MainActivity.this, WorkTimeReceiver.class);
            oldIntent.putExtra(Params.todo_title, oldTodo.getTitle());
            oldIntent.putExtra(Params.todo_desc , oldTodo.getDesc());
            oldIntent.putExtra("time" , oldTodo.getTimeString());

            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(todo.getTimeInDate());

            PendingIntent oldPendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 1, oldIntent, 0);
            ((AlarmManager) getSystemService(Context.ALARM_SERVICE))
                    .cancel(oldPendingIntent);

            Intent intent = new Intent(MainActivity.this, WorkTimeReceiver.class);
            intent.putExtra(Params.todo_title, todo.getTitle());
            intent.putExtra(Params.todo_desc, todo.getDesc());

            calendar = new GregorianCalendar();
            calendar.setTime(todo.getTimeInDate());

            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0 , intent, 0);
            ((AlarmManager)getSystemService(Context.ALARM_SERVICE))
                    .setExact(AlarmManager.RTC_WAKEUP,
                            calendar.getTimeInMillis(),
                            pendingIntent);

             */

                cancelTodoWork(todo.getId());
                scheduleTodoWork(todo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void scheduleTodoWork(Todo todo){

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(todo.getTimeInDate());


        JobScheduler jobScheduler = getSystemService(JobScheduler.class);

        PersistableBundle bundle = new PersistableBundle();
        bundle.putString(Params.todo_title, todo.getTitle());
        bundle.putString(Params.todo_desc, todo.getDesc());

        long def = calendar.getTimeInMillis() - Calendar.getInstance().getTimeInMillis();
        JobInfo.Builder builder = new JobInfo.Builder(todo.getId(), new ComponentName(getApplicationContext(), NotificationJobService.class))
                    .setMinimumLatency(def)
                    .setOverrideDeadline(def + 2000)
                    .setExtras(bundle)
                    .setPersisted(true);

        jobScheduler.schedule(builder.build());
    }

    private void cancelTodoWork(int id){
        getSystemService(JobScheduler.class).cancel(id);
    }
    
    private void clearBackStack(){
        for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
            getSupportFragmentManager().popBackStack();
        }
    }

    @SuppressLint("NonConstantResourceId")
    private boolean changeFragmentByBottomNav(MenuItem item){

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
    }
}