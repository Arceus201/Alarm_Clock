package com.example.alarm.Alarm;

import static android.content.ContentValues.TAG;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.example.alarm.Model.Time;
import com.example.alarm.sql.SQLiteHelperAlarm;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class AlarmDAO {

    public static void setAlarm(Context context, Time t){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Log.d(TAG, "setAlarm: " + t.getId() );
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(t.getGio()));
        calendar.set(Calendar.MINUTE, Integer.parseInt(t.getPhut()));
        
        Intent intent = new Intent(context,AlarmReceiver.class);
        Bundle bundle = new Bundle();
        bundle.putInt("time", t.getId());
        intent.putExtras(bundle);


        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,t.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT );

        //repeat theo ngay

        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            String repeat = t.getRepeat();
            if(repeat.equals("Daily"))  repeat = "Mon Tue Wed Thu Fri Sat Sun";
            String[] repeatValues = repeat.split(" ");
//            int today = (calendar.get(Calendar.DAY_OF_MONTH));
            switch (repeat) {
                case "Once":
                    // đảm bảo day_of_month<max ngay trong 1 thang
                    int maxDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                    int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
                    if (currentDay + 1 <= maxDayOfMonth) {
                        calendar.set(Calendar.DAY_OF_MONTH, currentDay + 1);
                    } else {
                        // Đã vượt qua số ngày trong tháng, hãy chuyển sang tháng tiếp theo
                        calendar.add(Calendar.MONTH, 1);
                        calendar.set(Calendar.DAY_OF_MONTH, 1);
                    }
//                    calendar.set(Calendar.DAY_OF_MONTH, (calendar.get(Calendar.DAY_OF_MONTH))+1);
                    break;
                default:
                    Set<Integer> daysOfWeek = getDaysOfWeek(repeatValues);
                    for(Integer day:daysOfWeek) {
                        if(calendar.get(Calendar.DAY_OF_WEEK) < day ) {
                            calendar.set(Calendar.DAY_OF_WEEK, day);
                            break;
                        }
                    }
                    break;
            }
        }else{
            String repeat = t.getRepeat();
            if (repeat.equals("Daily")) {
                repeat = "Mon Tue Wed Thu Fri Sat Sun";
            }
            String[] repeatValues = repeat.split(" ");
            switch (repeat) {
                case "Once":
                    break;
                default:
                    Set<Integer> daysOfWeek = getDaysOfWeek(repeatValues);
                    for(Integer day:daysOfWeek) {
                        if(calendar.get(Calendar.DAY_OF_WEEK) <= day ) {
                            calendar.set(Calendar.DAY_OF_WEEK, day);
                            break;
                        }
                    }
                    break;
            }
        }

        //repeat theo phut
        SQLiteHelperAlarm db = new SQLiteHelperAlarm(context);
        int x = db.getSetting().getAuto_silence();


        //báo thức chỉ chạy 1 lần
        if(t.getMode_autoSilence() == 1 || x == 0 ) {
            if (Build.VERSION.SDK_INT >= 23) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            } else if (Build.VERSION.SDK_INT >= 19) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
        }else{// báo thức có lặp
            long repeatInterval = x * 60 * 1000;

            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), repeatInterval, pendingIntent);
        }


        // Restart alarm if device is rebooted
//        ComponentName receiver = new ComponentName(context, SampleBootReceiver.class);
//        PackageManager pm = context.getPackageManager();
//        pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
//                PackageManager.DONT_KILL_APP);

    }

    private static Set<Integer> getDaysOfWeek(String[] repeatValues) {
        Set<Integer> daysOfWeek = new HashSet<>();
        Calendar calendar = Calendar.getInstance();

        for (String repeatValue : repeatValues) {
            switch (repeatValue.trim().toLowerCase(Locale.ENGLISH)) {
                case "mon":
                    daysOfWeek.add(Calendar.MONDAY);
                    break;
                case "tue":
                    daysOfWeek.add(Calendar.TUESDAY);
                    break;
                case "wed":
                    daysOfWeek.add(Calendar.WEDNESDAY);
                    break;
                case "thu":
                    daysOfWeek.add(Calendar.THURSDAY);
                    break;
                case "fri":
                    daysOfWeek.add(Calendar.FRIDAY);
                    break;
                case "sat":
                    daysOfWeek.add(Calendar.SATURDAY);
                    break;
                case "sun":
                    daysOfWeek.add(Calendar.SUNDAY);
                    break;
            }
        }

        return daysOfWeek;
    }

    public static void cancelAlrm(Context context,int alarmId){
        Log.d(TAG, "cancelAlrm: ");
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarmId, intent,  PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        // Hủy báo thức đã được đặt
        alarmManager.cancel(pendingIntent);


        // Disable alarm
//        ComponentName receiver = new ComponentName(context, SampleBootReceiver.class);
//        PackageManager pm = context.getPackageManager();
//        pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
//                PackageManager.DONT_KILL_APP);
    }

}
