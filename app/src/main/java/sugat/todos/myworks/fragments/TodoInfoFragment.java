package sugat.todos.myworks.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import sugat.todos.myworks.MainActivity;
import sugat.todos.myworks.R;
import sugat.todos.myworks.models.Todo;

public class TodoInfoFragment extends Fragment {
    
    private final Todo todo;

    public TodoInfoFragment(Todo todo) {
        this.todo = todo;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_todo_info, container, false);

        ((TextView)view.findViewById(R.id.infoTodoTitleTextView)).setText(todo.getTitle());
        ((TextView)view.findViewById(R.id.infoTodoDescTextView)).setText(todo.getDesc());
        ((TextView)view.findViewById(R.id.infoTodoTimeTextView)).setText(todo.getTimeString());
        view.findViewById(R.id.todoEditBtn).setOnClickListener(v -> ((MainActivity)requireActivity()).editTodoUi(todo));

        return view;
    }
}