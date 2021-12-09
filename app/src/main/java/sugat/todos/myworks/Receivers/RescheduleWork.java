package sugat.todos.myworks.Receivers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class RescheduleWork extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
//        ArrayList<Todo> todos = (ArrayList<Todo>) new TodoDBHandler(context).getNotDoneTodo();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("sugat.todos.myworks.Channels.Notify",
                    "My Works notification channel", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("This channel is to notify about work");

            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        /*
        Toast.makeText(context.getApplicationContext(), "Rescheduling todo", Toast.LENGTH_SHORT).show();

        for (Todo todo : todos) {
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(todo.getTimeInDate());
            Intent intent1 = new Intent(context, WorkTimeReceiver.class);
            intent1.putExtra(Params.todo_title, todo.getTitle());
            intent1.putExtra(Params.todo_desc, todo.getDesc());
            intent1.putExtra("time", todo.getTimeString());

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0 , intent1, 0);
            ((AlarmManager)context.getSystemService(Context.ALARM_SERVICE))
                    .set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        }
        Toast.makeText(context.getApplicationContext(), "Tasks Rescheduled", Toast.LENGTH_SHORT).show();
        */
        
    }
}
