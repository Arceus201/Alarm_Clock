package com.example.alarm.Setting;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;

import com.example.alarm.Model.Setting;
import com.example.alarm.R;
import com.example.alarm.sql.SQLiteHelperAlarm;

public class AlarmRingToneActivity extends AppCompatActivity
                implements View.OnClickListener {
    private ImageButton btCancel,btAdd;

    private MediaPlayer mediaPlayer;

    private SQLiteHelperAlarm db;
    private Setting st;

    int ACITION = 0;
    private RadioButton r1,r2,r3,r4,r5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_ring_tone);
        initView();

        db = new SQLiteHelperAlarm(this);
        st = db.getSetting();

        setCheck(st.getSource());


        btCancel.setOnClickListener(this);
        btAdd.setOnClickListener(this);
        r1.setOnClickListener(this);
        r2.setOnClickListener(this);
        r3.setOnClickListener(this);
        r4.setOnClickListener(this);
        r5.setOnClickListener(this);


    }
    public void initView(){
        btCancel = findViewById(R.id.st_rt_CanCel);
        btAdd = findViewById(R.id.st_rt_Add);
        r1 = findViewById(R.id.st_ringtone1);
        r2 = findViewById(R.id.st_ringtone2);
        r3 = findViewById(R.id.st_ringtone3);
        r4 = findViewById(R.id.st_ringtone4);
        r5 = findViewById(R.id.st_ringtone5);

    }

    private void setCheck(String s) {
        if(s.equals("Ring 1")) r1.setChecked(true);
        else if (s.equals("Ring 2")) r2.setChecked(true);
        else if (s.equals("Ring 3")) r3.setChecked(true);
        else if (s.equals("Ring 4")) r4.setChecked(true);
        else r5.setChecked(true);
    }

    private String getCheck() {
        String s;
        if(r1.isChecked()) s = "Ring 1";
        else if (r2.isChecked()) s = "Ring 2";
        else if (r3.isChecked()) s = "Ring 3";
        else if (r4.isChecked()) s = "Ring 4";
        else s = "Ring 5";

        return s;
    }


    @Override
    public void onClick(View v) {
        int Song;
        if(v == btCancel){
            if(ACITION==1)
                mediaPlayer.pause();
            finish();
        }
        if(v==btAdd){
            String soure = getCheck();
            Setting se = new Setting(st.getId(),st.getAuto_silence(),soure);
            db.updateSetting(se);

            if(ACITION==1)
                mediaPlayer.pause();
            finish();
        }
        if(v==r1){
            if(ACITION==1)
                mediaPlayer.pause();
            Song = R.raw.ringtone1;
            mediaPlayer = MediaPlayer.create(this, Song);
            mediaPlayer.start();
            ACITION = 1;
        }
        if(v==r2){
            if(ACITION==1)
                mediaPlayer.pause();
            Song = R.raw.ringtone2;
            mediaPlayer = MediaPlayer.create(this, Song);
            mediaPlayer.start();
            ACITION = 1;
        }
        if(v==r3){
            if(ACITION==1)
                mediaPlayer.pause();
            Song = R.raw.ringtone3;
            mediaPlayer = MediaPlayer.create(this, Song);
            mediaPlayer.start();
            ACITION = 1;
        }
        if(v==r4){
            if(ACITION==1)
                mediaPlayer.pause();
            Song = R.raw.ringtone4;
            mediaPlayer = MediaPlayer.create(this, Song);
            mediaPlayer.start();
            ACITION = 1;
        }
        if(v==r5){
            if(ACITION==1)
                mediaPlayer.pause();
            Song = R.raw.ringtone5;
            mediaPlayer = MediaPlayer.create(this, Song);
            mediaPlayer.start();
            ACITION = 1;
        }
    }
}