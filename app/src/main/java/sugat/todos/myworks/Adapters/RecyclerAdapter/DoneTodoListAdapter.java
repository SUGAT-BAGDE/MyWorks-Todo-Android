package sugat.todos.myworks.Adapters.RecyclerAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import sugat.todos.myworks.Listners.TodoDeleteListener;
import sugat.todos.myworks.Listners.TodoDoneListener;
import sugat.todos.myworks.R;
import sugat.todos.myworks.models.Todo;

public class DoneTodoListAdapter extends RecyclerView.Adapter<DoneTodoListAdapter.ViewHolder> {

    private ArrayList<Todo> localDataSet;
    private final TodoDeleteListener listener;

    public DoneTodoListAdapter(ArrayList<Todo> dataSet, TodoDeleteListener listener){
        localDataSet = dataSet;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final ImageView deleteBtn;

        public ViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.doneTodoTitle);
            deleteBtn = view.findViewById(R.id.doneTodoDeleteBtn);
            deleteBtn.setMaxHeight(deleteBtn.getHeight());
        }

        public void setTextViewText(String text) {
            textView.setText(text);
        }

        public void setOnClickListener (@NonNull  View.OnClickListener listener){
            deleteBtn.setOnClickListener(listener);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.done_todo_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Todo todo = localDataSet.get(position);
        viewHolder.setTextViewText(todo.getTitle());
        viewHolder.setOnClickListener(view -> listener.listen(todo.getId()));
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
