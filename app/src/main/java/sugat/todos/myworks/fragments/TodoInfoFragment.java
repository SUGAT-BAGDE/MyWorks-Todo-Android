package sugat.todos.myworks.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

        return inflater.inflate(R.layout.fragment_todo_info, container, false);
    }
}