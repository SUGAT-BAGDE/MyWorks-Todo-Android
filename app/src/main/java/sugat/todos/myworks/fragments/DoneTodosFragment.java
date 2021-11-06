package sugat.todos.myworks.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sugat.todos.myworks.Adapters.RecyclerAdapter.DoneTodoListAdapter;
import sugat.todos.myworks.Adapters.RecyclerAdapter.TodoListAdapter;
import sugat.todos.myworks.MainActivity;
import sugat.todos.myworks.R;

public class DoneTodosFragment extends Fragment {

    private DoneTodoListAdapter todoRecyclerViewAdapter;

    public DoneTodosFragment() {
        this.todoRecyclerViewAdapter = new DoneTodoListAdapter(((MainActivity) requireActivity()).getAllDoneTodo(),
                p -> ((MainActivity)requireActivity()).onTodoDelete(p)) ;;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_todo_done, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.doneTodoRecyclerView);
        recyclerView.setAdapter(todoRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void notifyDataSetChanged(){
        todoRecyclerViewAdapter.notifyDataSetChanged();
    }
}