package sugat.todos.myworks.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import sugat.todos.myworks.Adapters.RecyclerAdapter.TodoListAdapter;
import sugat.todos.myworks.MainActivity;
import sugat.todos.myworks.R;
import sugat.todos.myworks.models.Todo;

public class TodoFragment extends Fragment {

    private TodoListAdapter todoRecyclerViewAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.todoRecycerView);

        todoRecyclerViewAdapter = new TodoListAdapter(
                ((MainActivity)requireActivity()).getNotDoneTodos(),
                ((MainActivity)requireActivity())::onTodoDone,
                ((MainActivity)requireActivity())::onInfoClickListener);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(todoRecyclerViewAdapter);
        
        return view;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void notifyDataSetChanged(){
        if (todoRecyclerViewAdapter!=null) todoRecyclerViewAdapter.notifyDataSetChanged();
    }
}