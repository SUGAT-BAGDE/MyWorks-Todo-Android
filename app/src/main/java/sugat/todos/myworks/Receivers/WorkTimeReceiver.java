package sugat.todos.myworks.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.text.SimpleDateFormat;
import java.util.Locale;

import sugat.todos.myworks.MainActivity;
import sugat.todos.myworks.R;

public class WorkTimeReceiver extends BroadcastReceiver {

    private TextToSpeech textToSpeech;
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("KK:mm aaa");

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, MainActivity.NotificationChannel)
                .setSmallIcon(R.drawable.splash_image)
                .setContentTitle(intent.getStringExtra("title"))
                .setContentText(intent.getStringExtra("desc"))
                .setPriority(NotificationCompat.PRIORITY_HIGH);        
        
        MediaPlayer mediaPlayer = MediaPlayer.create(context, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        mediaPlayer.start();
                
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);

        notificationManagerCompat.notify(200,builder.build());

        textToSpeech = new TextToSpeech(context.getApplicationContext(), new TextToSpeech.OnInitListener(){
            @Override
            public void onInit(int status) {

                if (status == TextToSpeech.SUCCESS) {

                    int result = textToSpeech.setLanguage(Locale.getDefault());
                    textToSpeech.setSpeechRate(1);
                    
                    if ( result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                        Log.e("tts", "language ");
                    }
                    else {
                        CharSequence charSequence = "It is " + intent.getStringExtra("time")
                                + ". You Need to do " + intent.getStringExtra("title");

                        textToSpeech.speak(charSequence,
                                TextToSpeech.QUEUE_FLUSH, null, null);
                    }
                }
                else {
                    Log.e("tts", "init");
                }
            }
        });

    }
}