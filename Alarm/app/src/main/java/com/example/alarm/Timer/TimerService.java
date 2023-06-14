package com.example.alarm.Timer;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.alarm.MainActivity;
import com.example.alarm.R;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class TimerService extends Service {
    private static final String CHANNEL_ID = "NotificationChannelID";



    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String s = intent.getStringExtra("Timer");
        final Long[] timeRemaining = {chuyen1(s)};


        final Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                timeRemaining[0]--;

                NotificationUpdate(chuyen2(timeRemaining[0]));
                if (timeRemaining[0] <= 0){
                    timer.cancel();
                }
            }
        }, 0, 1000);
        return super.onStartCommand(intent, flags, startId);
    }

    private Long chuyen1(String timeString){
        String[] tokens = timeString.split(":");
        Long hours = Long.parseLong(tokens[0]);
        Long minutes =  Long.parseLong(tokens[1]);
        Long seconds = Long.parseLong(tokens[2]);

        Long totalSeconds = hours * 3600 + minutes * 60 + seconds;
        return totalSeconds;
    }

    private String chuyen2(Long totalSeconds ){
        Long hours = TimeUnit.SECONDS.toHours(totalSeconds);
        Long minutes = TimeUnit.SECONDS.toMinutes(totalSeconds) - TimeUnit.HOURS.toMinutes(hours);
        Long seconds = totalSeconds - (int)TimeUnit.MINUTES.toSeconds(minutes) - (int)TimeUnit.HOURS.toSeconds(hours);

        String timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        return timeString;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    public void NotificationUpdate(String timeLeft) {
        try {
            Intent notificationIntent = new Intent(this, MainActivity.class);
            notificationIntent.putExtra("fragment_timer", "timer");

            final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

            final Notification[] notification = {new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("My Timer")
                    .setContentText("Timer Remaing : " + timeLeft)
                    .setSmallIcon(R.drawable.notifications)
                    .setContentIntent(pendingIntent)
                    .build()};
            startForeground(1, notification[0]);
            NotificationChannel notificationChannel = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationChannel = new NotificationChannel(CHANNEL_ID, "My Counter Service", NotificationManager.IMPORTANCE_DEFAULT);
            }
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationManager.createNotificationChannel(notificationChannel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int Randomid(){
        Random rand = new Random();

        // Sinh một số ngẫu nhiên trong khoảng từ 10000 đến 100000
        return rand.nextInt(100000 - 10000) + 10000;
    }
}