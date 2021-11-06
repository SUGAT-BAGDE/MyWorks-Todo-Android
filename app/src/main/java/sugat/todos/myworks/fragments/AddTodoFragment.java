package sugat.todos.myworks.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import sugat.todos.myworks.MainActivity;
import sugat.todos.myworks.R;
import sugat.todos.myworks.models.Todo;

public class AddTodoFragment extends Fragment {

    private boolean toUpdate;
    private Todo todo;

    public AddTodoFragment(boolean toUpdate, @Nullable Todo todo) throws Exception {
        if (toUpdate && todo != null){
            this.toUpdate = true;
            this.todo = todo;
        }
        else if (todo == null && toUpdate){
            throw new Exception("if toUpdate is true then todo cannot be null");
        }
        else {
            this.toUpdate = false;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_todo,container,false);

        EditText todoTitleEditText = view.findViewById(R.id.todoTitleEditText);
        EditText todoDescEditText = view.findViewById(R.id.todoDescEditText);

        if(toUpdate){
            todoDescEditText.setText(this.todo.getDesc());
            todoTitleEditText.setText(this.todo.getTitle());
            ((Button)view.findViewById(R.id.addTodoBtn)).setText(R.string.update);
        }

        view.findViewById(R.id.addTodoBtn).setOnClickListener(v->{
            String title = todoTitleEditText.getText().toString();
            String desc = todoDescEditText.getText().toString();

            if (!title.equals("") && title != null && !desc.equals("") && desc != null){
                ((MainActivity)requireActivity()).addTodo(new Todo(title,desc));
            }
            else {
                Toast.makeText(getContext(), "Title or desc cannot empty", Toast.LENGTH_SHORT).show();
            }
            getParentFragmentManager().popBackStack();
        });



        return view;
    }
}