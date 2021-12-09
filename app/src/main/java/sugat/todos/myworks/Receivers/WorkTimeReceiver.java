package sugat.todos.myworks.Receivers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import sugat.todos.myworks.Params.Params;
import sugat.todos.myworks.R;

import static android.provider.Settings.System.DEFAULT_RINGTONE_URI;

public class WorkTimeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager manager = context.getSystemService(NotificationManager.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel Channel = new NotificationChannel(
                    Params.NotificationChannel,
                    "My Works notification channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            manager.createNotificationChannel(Channel);
        }

        MediaPlayer mp = MediaPlayer.create(context, DEFAULT_RINGTONE_URI);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, Params.NotificationChannel)
                .setSmallIcon(R.drawable.splash_image)
                .setContentTitle(intent.getStringExtra(Params.todo_title))
                .setContentText(intent.getStringExtra(Params.todo_desc))
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        manager.notify(200,builder.build());
        mp.start();
    }
}