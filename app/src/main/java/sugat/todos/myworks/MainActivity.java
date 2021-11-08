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

            todoFragment = new TodoFragment(notDoneTodoArrayList);
            doneTodosFragment = new DoneTodosFragment();

            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_container_view, todoFragment, "main")
                    .commit();
        }

        onTodoFragmentAddBtnClicked = v -> {
            if (savedInstanceState != null) {
                return;
            }

            try {
                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fragment_container_view, new AddTodoFragment(false, null), "addTodo")
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

    @Override
    public void onBackPressed() {

        if (getSupportFragmentManager().getBackStackEntryCount() < 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    public View.OnClickListener getOnTodoFragmentAddBtnClicked() {
        return onTodoFragmentAddBtnClicked;
    }

    public View.OnClickListener getOnTodoFragmentViewAllBtnClicked() {
        return onTodoFragmentViewAllBtnClicked;
    }

    // Sql operations
    public void addTodo(@NonNull Todo todo) {
        dbHandler.addTodo(todo);
        notifyTodoSetChanged();
    }

    public void onTodoDone(int id) {
        Todo todo = dbHandler.getTodoById(id);
        todo.setDone(true);
        dbHandler.updateTodo(todo);
        notifyTodoSetChanged();
    }

    public ArrayList<Todo> getAllDoneTodo() {
        return doneTodoArrayList;
    }

    public void onTodoDelete(int id) {
        dbHandler.deleteTodoById(id);
        notifyTodoSetChanged();
    }

    private void notifyTodoSetChanged() {
        notDoneTodoArrayList = (ArrayList<Todo>) dbHandler.getNotDoneTodo();
        doneTodoArrayList = (ArrayList<Todo>) dbHandler.getAllDoneTodo();
        todoFragment.notifyDataSetChanged();
        doneTodosFragment.notifyDataSetChanged();
    }

}