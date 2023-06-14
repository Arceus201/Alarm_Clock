package com.example.alarm.Alarm;

import static android.content.ContentValues.TAG;

import static com.example.alarm.Alarm.AlarmApplication.CHANNEL_ID;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.alarm.MainActivity;
import com.example.alarm.Model.Time;
import com.example.alarm.R;
import com.example.alarm.sql.SQLiteHelperAlarm;

import java.util.Calendar;
import java.util.Locale;

public class AlarmService extends Service {
    private static final String ARG_DATA = "time";
    private Time mTime;
    private MediaPlayer mediaPlayer;
    private SQLiteHelperAlarm db;

    public static final int ACTION_PAUSE = 1;

    public static final int ACTION_CLEAR = 3;



    private AlarmDAO alarmDAO;


    private static final int DURATION = 50 * 1000; // Thời gian chạy của service (30 giây)
    private Handler handler;
    private Runnable stopRunnable;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        db = new SQLiteHelperAlarm(getApplicationContext());
        Log.d(TAG, "onstartCommand" );


        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            int id = bundle.getInt("time");
            Log.d(TAG, "onStartCommand: id :" +id);
            Time time = db.getTimeById(id);
  //          Log.d(TAG, "onStartCommand: " + time.getGio()+time.getPhut());
//
            if (time != null) {
                mTime = time;
                startMusic(time.getSource());


                sendNotification(mTime);
            }
        }
        int acction = intent.getIntExtra("action_service", 0);
        handleActionMusic(acction);

        //chay 50s
        handler = new Handler();
        stopRunnable = new Runnable() {
            @Override
            public void run() {
                stopSelf();
            }
        };
        handler.postDelayed(stopRunnable, DURATION);

        return START_STICKY;
    }

    private void startMusic(String Source) {
        int Song;
        if(Source.equals("Ring 2")) Song = R.raw.ringtone2;
        else if(Source.equals("Ring 3")) Song = R.raw.ringtone3;
        else if(Source.equals("Ring 4")) Song = R.raw.ringtone4;
        else if(Source.equals("Ring 5")) Song = R.raw.ringtone5;
        else  Song = R.raw.ringtone1;
        if(mediaPlayer==null) {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), Song);
        }
        mediaPlayer.start();
        //sendActionToActivity(ACTION_START);
    }

    private void handleActionMusic(int action){
        switch(action){
            case ACTION_PAUSE:
                stopSelf();
                break;
            case ACTION_CLEAR:
                stopSelf();
                if(mTime!=null) {
                    alarmDAO.cancelAlrm(this, mTime.getId());
                }
                sendActionToFragmentAlarm(ACTION_CLEAR);
                break;
        }
    }



    private void sendNotification(Time time) {
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("fragment_alarm", "alarm");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.notifications);

        RemoteViews remoteView = new RemoteViews(getPackageName(),R.layout.custom_notification);
        remoteView.setTextViewText(R.id.tv_title, currentTime());
        remoteView.setTextViewText(R.id.tv_song, time.getLabel());
        remoteView.setImageViewBitmap(R.id.img_song, bitmap);
        remoteView.setImageViewResource(R.id.img_play_or_pause, R.drawable.pause);


        remoteView.setOnClickPendingIntent(R.id.img_play_or_pause, getPendingIntent(this,ACTION_PAUSE));
        remoteView.setImageViewResource(R.id.img_play_or_pause, R.drawable.pause);


        remoteView.setOnClickPendingIntent(R.id.img_clear, getPendingIntent(this,ACTION_CLEAR));


        Notification notification = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setSmallIcon(R.drawable.notifications)
                .setContentIntent(pendingIntent)
                .setCustomContentView(remoteView)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSound(null)
                .setFullScreenIntent(pendingIntent, true)
                .build();



        startForeground(time.getId(), notification);
    }


    private PendingIntent getPendingIntent(Context context, int action){
        Intent intent = new Intent(this,AlarmReceiver.class);

        Log.d(TAG, "Action:" + action);
        intent.putExtra("action_music", action);
        return PendingIntent.getBroadcast(context.getApplicationContext(),action, intent, PendingIntent.FLAG_UPDATE_CURRENT);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("Hello", "MyService onDestroy");
        if(mediaPlayer!=null){
            mediaPlayer.release();
            mediaPlayer=null;
        }
        // Hủy bỏ việc đếm thời gian nếu service bị hủy
        if (handler != null && stopRunnable != null) {
            handler.removeCallbacks(stopRunnable);
        }
    }

    private void sendActionToFragmentAlarm(int action){
        Intent intent = new Intent("send_data_to_alarm");
        Bundle bundle = new Bundle();
        bundle.putInt("action_music", action);
        if(mTime!=null) {
            bundle.putInt("updatemode", mTime.getId());
        }
        intent.putExtras(bundle);

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private String currentTime(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);

        String currentTime = String.format(Locale.getDefault(), "%02d:%02d", currentHour, currentMinute);
        return  currentTime;
    }





}
