package com.example.alarm.Alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.alarm.Model.Time;
import com.example.alarm.sql.SQLiteHelperAlarm;

import java.util.List;

public class SampleBootReceiver extends BroadcastReceiver {
    private AlarmDAO alarmDAO;
    private SQLiteHelperAlarm db;
    @Override
    public void onReceive(Context context, Intent intent) {
        //intent.getAction().equals("android.intent.action.BOOT_COMPLETED")
        if (intent.getAction()!=null) {
            db = new SQLiteHelperAlarm(context);
            Toast.makeText(context, "Boot Complete.", Toast.LENGTH_LONG).show();
            List<Time> list = db.getAll2();
            for (Time x : list) {
                alarmDAO.setAlarm(context, x);
            }
        }
    }
}
