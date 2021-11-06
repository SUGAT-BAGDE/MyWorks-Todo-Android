package sugat.todos.myworks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

import sugat.todos.myworks.data.TodoDBHandler;
import sugat.todos.myworks.fragments.AddTodoFragment;
import sugat.todos.myworks.fragments.TodoFragment;
import sugat.todos.myworks.fragments.DoneTodosFragment;
import sugat.todos.myworks.models.Todo;

public class MainActivity extends AppCompatActivity {

    private TodoDBHandler dbHandler;
    private TodoFragment todoFragment;
    private DoneTodosFragment doneTodosFragment;
    private View.OnClickListener onTodoFragmentAddBtnClicked;
    private View.OnClickListener onTodoFragmentViewAllBtnClicked;
    private ArrayList<Todo> todoArrayList;
    private ArrayList<Todo> doneTodoArrayList;
    private ArrayList<Todo> notDoneTodoArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_MyWorks);
        setContentView(R.layout.activity_main);

        dbHandler = new TodoDBHandler(MainActivity.this);

        todoArrayList = (ArrayList<Todo>) dbHandler.getAllTodos();
        doneTodoArrayList = (ArrayList<Todo>) dbHandler.getAllDoneTodo();
        notDoneTodoArrayList = (ArrayList<Todo>) dbHandler.getNotDoneTodo();

        if (savedInstanceState == null){

            todoFragment = new TodoFragment();
            doneTodosFragment = new DoneTodosFragment();

            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_container_view, todoFragment,"main")
                    .commit();
        }

        onTodoFragmentAddBtnClicked = v -> {
            if (savedInstanceState != null) {
                return;
            }

            try {
                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fragment_container_view, new AddTodoFragment(false,null), "addTodo")
                        .addToBackStack("main")
                        .commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        
        onTodoFragmentViewAllBtnClicked = v -> {
            if (savedInstanceState != null) {
                return;
            }
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragment_container_view, doneTodosFragment, "doneTodo")
                    .addToBackStack("main")
                    .commit();
        };
    }

    public void addTodo(@NonNull Todo todo){
        dbHandler.addTodo(todo);
        todoArrayList = (ArrayList<Todo>) dbHandler.getAllTodos();
        if (doneTodosFragment.isVisible()) notifyDoneTodoSetChanged();
        else notifyNotDoneTodoSetChanged();
    }

    @Override
    public void onBackPressed() {

        if (getSupportFragmentManager().getBackStackEntryCount()<0){
            getSupportFragmentManager().popBackStack();
        }
        else{
            super.onBackPressed();
        }
    }

    public View.OnClickListener getOnTodoFragmentAddBtnClicked() {
        return onTodoFragmentAddBtnClicked;
    }

    public View.OnClickListener getOnTodoFragmentViewAllBtnClicked() {
        return onTodoFragmentViewAllBtnClicked;
    }

    public ArrayList<Todo> getAllTodo(){
        return notDoneTodoArrayList;
    }

    public void onTodoDone(int position){
        Todo todo = todoArrayList.get(position);
        todo.setDone(true);
        dbHandler.updateTodo(todo);
        if (doneTodosFragment.isVisible()) notifyDoneTodoSetChanged();
        else notifyNotDoneTodoSetChanged();
    }

    public ArrayList<Todo> getAllDoneTodo() {
        return doneTodoArrayList;
    }

    public void onTodoDelete(int position){
        dbHandler.deleteTodoById(position);
        if (doneTodosFragment.isVisible()) notifyDoneTodoSetChanged();
        else notifyNotDoneTodoSetChanged();
    }

    private void notifyDoneTodoSetChanged(){
        doneTodoArrayList = (ArrayList<Todo>) dbHandler.getAllDoneTodo();
        todoArrayList = (ArrayList<Todo>) dbHandler.getAllTodos();
        doneTodosFragment.notifyDataSetChanged();
    }

    private void notifyNotDoneTodoSetChanged(){
        notDoneTodoArrayList = (ArrayList<Todo>) dbHandler.getNotDoneTodo();
        todoArrayList = (ArrayList<Todo>) dbHandler.getAllTodos();
        todoFragment.notifyDataSetChanged();
    }
}