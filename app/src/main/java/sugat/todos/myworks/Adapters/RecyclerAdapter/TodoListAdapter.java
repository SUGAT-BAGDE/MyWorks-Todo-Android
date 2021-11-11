package sugat.todos.myworks.Adapters.RecyclerAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import sugat.todos.myworks.Listners.TodoDoneListener;
import sugat.todos.myworks.R;
import sugat.todos.myworks.models.Todo;

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.ViewHolder> {

    private ArrayList<Todo> localDataSet;
    private TodoDoneListener listener;

    public TodoListAdapter(ArrayList<Todo> dataSet, TodoDoneListener listener){
        localDataSet = dataSet;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final ImageView doneBtn;

        public ViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.todoTitle);
            doneBtn = view.findViewById(R.id.todoDoneBtn);
            doneBtn.setMaxHeight(doneBtn.getHeight());
        }

        public TextView getTextView() {
            return textView;
        }
        
        public void setOnClickListener (@NonNull  View.OnClickListener listener){
            doneBtn.setOnClickListener(listener);
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.todo_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Todo todo = localDataSet.get(position);
        viewHolder.getTextView().setText(todo.getTitle());
        viewHolder.setOnClickListener(view -> listener.listen(localDataSet.get(position)));
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
