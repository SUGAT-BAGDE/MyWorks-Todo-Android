package sugat.todos.myworks.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sugat.todos.myworks.Adapters.RecyclerAdapter.TodoListAdapter;
import sugat.todos.myworks.MainActivity;
import sugat.todos.myworks.R;

public class TodoFragment extends Fragment {
    
    static public String tag = "TodoFragment";

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

        todoRecyclerViewAdapter = new TodoListAdapter(((MainActivity) getActivity()).getAllTodo(), p -> {
            ((MainActivity)getActivity()).onTodoDone(p);
        }) ;

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(todoRecyclerViewAdapter);
        
        view.findViewById(R.id.addBtnImageView).setOnClickListener(
                ((MainActivity) requireActivity()).getOnTodoFragmentAddBtnClicked());

        view.findViewById(R.id.viewDoneTodoBtn).setOnClickListener(
                ((MainActivity) requireActivity()).getOnTodoFragmentViewAllBtnClicked());
        
        return view;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void notifyDataSetChanged(){
        todoRecyclerViewAdapter.notifyDataSetChanged();
    }
}