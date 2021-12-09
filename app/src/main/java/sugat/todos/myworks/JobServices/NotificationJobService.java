package sugat.todos.myworks.JobServices;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import sugat.todos.myworks.Params.Params;
import sugat.todos.myworks.R;

import static android.provider.Settings.System.DEFAULT_RINGTONE_URI;

public class NotificationJobService extends JobService {

    private MediaPlayer mp;

    @Override
    public boolean onStartJob(JobParameters params) {
        NotificationManager manager = getSystemService(NotificationManager.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel Channel = new NotificationChannel(
                    Params.NotificationChannel,
                    "My Works notification channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            manager.createNotificationChannel(Channel);
        }

        mp = MediaPlayer.create(getApplicationContext(), DEFAULT_RINGTONE_URI);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), Params.NotificationChannel)
                .setSmallIcon(R.drawable.splash_image)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                        R.mipmap.ic_launcher))
                .setContentTitle(params.getExtras().getString(Params.todo_title))
                .setContentText(params.getExtras().getString(Params.todo_desc))
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        manager.notify(params.getJobId(), builder.build());

        mp.start();

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        if (mp!=null) mp.stop();
        return false;
    }
}
