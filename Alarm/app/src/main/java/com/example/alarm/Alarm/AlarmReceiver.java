package com.example.alarm.Alarm;

import static android.content.ContentValues.TAG;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.alarm.R;
import com.example.alarm.sql.SQLiteHelperAlarm;

import java.util.List;

public class AlarmReceiver extends BroadcastReceiver {
    private MediaPlayer mediaPlayer;
    private AlarmDAO alarmDAO;

    private SQLiteHelperAlarm db;

    @Override
    public void onReceive(Context context, Intent intent) {

            Log.d(TAG, "onReceive: " + "action");
            Bundle bundle = intent.getExtras();
            int s = bundle.getInt("time");
            Log.d(TAG, "onReceive: " + s);

            Intent intentService = new Intent(context, AlarmService.class);
            intentService.putExtras(bundle);
            context.startService(intentService);


            int actionMussic = intent.getIntExtra("action_music", 0);
            if (actionMussic != 0) {
                Intent intentService1 = new Intent(context, AlarmService.class);
                intentService1.putExtra("action_service", actionMussic);
//                context.startService(intentService1);
                context.startService(intentService1);
                Log.d(TAG, "onReceive: " + s);
            }



    }
}
