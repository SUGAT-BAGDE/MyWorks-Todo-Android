package sugat.todos.myworks.Receivers;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import sugat.todos.myworks.data.TodoDBHandler;
import sugat.todos.myworks.models.Todo;

public class RescheduleWork extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ArrayList<Todo> todos = (ArrayList<Todo>) new TodoDBHandler(context).getNotDoneTodo();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("sugat.todos.myworks.Channels.Notify",
                    "My Works notification channel", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("This channel is to notify about work");

            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        for (Todo todo : todos) {
            Intent intent1 = new Intent(context, WorkTimeReceiver.class);
            intent1.putExtra("title", todo.getTitle());
            intent1.putExtra("desc", todo.getDesc());
            intent1.putExtra("time", todo.getTimeString());

            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(todo.getTimeInDate());

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0 , intent1, 0);
            ((AlarmManager)context.getSystemService(Context.ALARM_SERVICE))
                    .set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
        
    }
}
