package sugat.todos.myworks.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import sugat.todos.myworks.Params.Params;
import sugat.todos.myworks.models.Todo;

public class TodoDBHandler extends SQLiteOpenHelper {

    

    public TodoDBHandler(@Nullable Context context) {
        super(context, Params.db_Name, null, Params.db_Version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create = "CREATE TABLE IF NOT EXISTS " + Params.db_MainTable + " ( " +
                Params.Key_id + " INTEGER PRIMARY KEY, " +
                Params.Key_title + " TEXT, " +
                Params.Key_desc + " TEXT," +
                Params.Key_done + " INTEGER DEFAULT 0);";

        db.execSQL(create);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long addTodo(Todo todo){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Params.Key_title, todo.getTitle());
        values.put(Params.Key_desc, todo.getDesc());
        values.put(Params.Key_done, todo.isDoneInt());

        return db.insert(Params.db_MainTable, null, values);
    }

    public List<Todo> getAllTodos(){
        List<Todo> todoList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String find = "SELECT * FROM " + Params.db_MainTable;
        Cursor cursor = db.rawQuery(find, null);

        if (cursor.moveToFirst()){
            do {

                Todo todo = new Todo();
                todo.setId(Integer.parseInt(cursor.getString(0)));
                todo.setTitle(cursor.getString(1));
                todo.setDesc(cursor.getString(2));
                try {
                    todo.setDone(Integer.parseInt(cursor.getString(3))==1);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                todoList.add(todo);

            } while (cursor.moveToNext());
        }

        db.close();
        cursor.close();

        return todoList;
    }

    public void deleteTodoById(int id){
        SQLiteDatabase db = getReadableDatabase();
        db.delete(Params.db_MainTable, Params.Key_id+ "?=", new String[]{String.valueOf(id)});
        db.close();
    }
    
    public List<Todo> getAllDoneTodo(){
        List<Todo> contactList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String select = "SELECT * FROM " + Params.db_MainTable + " WHERE " + Params.Key_done + " = 1";
        Cursor cursor = db.rawQuery(select, null);

        if(cursor.moveToFirst()){
            do{
                Todo contact = new Todo();
                contact.setId(Integer.parseInt(cursor.getString(0)));
                contact.setTitle(cursor.getString(1));
                contact.setDesc(cursor.getString(2));
                try {
                    contact.setDone(Integer.parseInt(cursor.getString(3))==1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                contactList.add(contact);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return contactList;
    }
    public List<Todo> getNotDoneTodo(){
        List<Todo> contactList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String select = "SELECT * FROM " + Params.db_MainTable + " WHERE " + Params.Key_done + " = 0";
        Cursor cursor = db.rawQuery(select, null);

        if(cursor.moveToFirst()){
            do{
                Todo contact = new Todo();
                contact.setId(Integer.parseInt(cursor.getString(0)));
                contact.setTitle(cursor.getString(1));
                contact.setDesc(cursor.getString(2));
                try {
                    contact.setDone(Integer.parseInt(cursor.getString(3))==1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                contactList.add(contact);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return contactList;
    }
    
    public int updateTodo(Todo todo){
        SQLiteDatabase db = getWritableDatabase();
        
        ContentValues values = new ContentValues();
        values.put(Params.Key_title , todo.getTitle());
        values.put(Params.Key_desc , todo.getDesc());
        values.put(Params.Key_done , todo.isDoneInt());
        
        int ret = db.update(Params.db_MainTable, values,
                Params.Key_id + " =?", new String[]{String.valueOf(todo.getId())});

        db.close();
        return ret;
    }
}
