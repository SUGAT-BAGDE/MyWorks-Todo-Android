package sugat.todos.myworks.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;

import sugat.todos.myworks.MainActivity;
import sugat.todos.myworks.R;
import sugat.todos.myworks.models.Todo;

public class AddOrUpdateTodoFragment extends Fragment {

    private final boolean toUpdate;
    private Todo todo;
    private Calendar calendar;

    public AddOrUpdateTodoFragment(boolean toUpdate, @Nullable Todo todo) throws Exception {
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
            ((TextView)view.findViewById(R.id.todoTimeSelectTextView)).setText(todo.getTimeString());
        }

        view.findViewById(R.id.todoTimeSelectBtn).setOnClickListener(v ->
                getDateTime(view.findViewById(R.id.todoTimeSelectTextView)));

        view.findViewById(R.id.addTodoBtn).setOnClickListener(v -> {
            String title = todoTitleEditText.getText().toString();
            String desc = todoDescEditText.getText().toString();

            if (!title.equals("") && !desc.equals("") &&
                    calendar.getTimeInMillis() > Calendar.getInstance().getTimeInMillis()){
                if (toUpdate){
                    ((MainActivity)requireActivity()).editTodoSql(todo);
                }
                else {
                    ((MainActivity) requireActivity()).addTodo(new Todo(title, desc, calendar.getTime()));
                }
                getParentFragmentManager().popBackStack();
            }
            else {
                Toast.makeText(getContext(), "Title or desc cannot empty and pick valid time", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
    
    private void getDateTime(@NonNull TextView textView){
        GregorianCalendar calendar = new GregorianCalendar();
        
        DatePickerDialog datePicker = new DatePickerDialog(getContext(),
                (view, year, month, dayOfMonth) -> {
            
                    calendar.set(GregorianCalendar.YEAR, year);
                    calendar.set(GregorianCalendar.MONTH, month);
                    calendar.set(GregorianCalendar.DAY_OF_MONTH, dayOfMonth);

                    TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                            (view2, hourOfDay, minute) -> {
                                calendar.set(GregorianCalendar.HOUR, hourOfDay);
                                calendar.set(GregorianCalendar.MINUTE, minute);
                                calendar.set(GregorianCalendar.SECOND, 0);
                                textView.setText(Todo.dateFormat.format(calendar.getTime()));
                                setCalendar(calendar);
                            },
                            calendar.get(GregorianCalendar.HOUR),
                            calendar.get(GregorianCalendar.MINUTE),
                            true);
                    timePickerDialog.show();
                },
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePicker.show();
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }
}