package com.example.alarm.Alarm;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class AlarmApplication extends Application {
    public static final  String CHANNEL_ID = "CHANNEL_SERVICE_EXAMPLE";
    @Override
    public void onCreate() {
        super.onCreate();
        // tu API > 26 moi can tao
        createChanneNotifiactionlID();
    }

    private void createChanneNotifiactionlID() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Channel Service Example",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setSound(null, null);
            NotificationManager manager = getSystemService(NotificationManager.class);

            if(manager!=null) {
                manager.createNotificationChannel(channel);
            }
        }
    }
}
