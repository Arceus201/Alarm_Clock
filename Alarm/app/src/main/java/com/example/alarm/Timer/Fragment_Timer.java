package com.example.alarm.Timer;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.alarm.Model.MTimer;
import com.example.alarm.R;
import com.example.alarm.sql.SQLiteHelperAlarm;

import java.util.Locale;

public class Fragment_Timer extends Fragment implements View.OnClickListener{


    private TextView mTimer;
    private Button btStartPause;

    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private TimePicker timePicker;

    private long mTimeLeftInMillis ;
    private SQLiteHelperAlarm db;
    private int ok=0;

    private long mEndTime;
    public Fragment_Timer() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timer, container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = new SQLiteHelperAlarm(getContext());

        initView(view);

        MTimer timer = db.getTimer();

        if(timer!=null){
            mTimer.setText(timer.getTime());
        }


        //ActivityCompat.requestPermissions(getActivity(), new String[]{FOREGROUND_SERVICE}, PackageManager.PERMISSION_GRANTED);

    }

    private void initView(View view) {
        mTimer = view.findViewById(R.id.tv_countdown);
        btStartPause = view.findViewById(R.id.bt_start_pause);
        timePicker = view.findViewById(R.id.time_picker);

        mTimeLeftInMillis = slove(mTimer.getText().toString());

        mTimer.setOnClickListener(this);

        btStartPause.setOnClickListener(this);

        timePicker.setIs24HourView(true);
    }

    private Long slove(String time){
        String [] tmp = time.split(":");
        Long Time = Long.parseLong(tmp[0])*60000*60 + Long.parseLong(tmp[1])*60000 + Long.parseLong(tmp[2])*1000;
        return Time;
    }



    @Override
    public void onClick(View v) {

        if(v==btStartPause){
            if(mTimerRunning){

                pauseTimer();
                getContext().stopService(new Intent(getContext(),TimerService.class));

            }else{
                if(ok==1){
                    int hour = timePicker.getCurrentHour();
                    int minute =timePicker.getCurrentMinute();
                    mTimer.setText(String.format(Locale.getDefault(), "%02d:%02d:%02d", hour, minute,0));
                    ok=0;
                }

                startTimer();
                Intent intentService = new Intent(getContext(), TimerService.class);
                String TimeSet = mTimer.getText().toString();
                intentService.putExtra("Timer", TimeSet);
                getContext().startService(intentService);
            }
        }
        if(v==mTimer){
            pauseTimer();
            timePicker.setVisibility(View.VISIBLE);
            mTimer.setVisibility(View.GONE);
            ok=1;
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        db.deleteTimer();
        MTimer timer = new MTimer(mTimer.getText().toString());
        db.addTimer(timer);
    }

    private void startTimer() {
        mTimer.setVisibility(View.VISIBLE);
        timePicker.setVisibility(View.GONE);

       // mTimer.setText(String.format(Locale.getDefault(),"%02d:%02d:%02d",timePicker.getHour(),timePicker.getMinute(),0));
        Log.d(TAG, "startTimer: " + mTimer.getText());
        mTimeLeftInMillis = slove(mTimer.getText().toString());

        Log.d(TAG, "startTimer: " + mTimeLeftInMillis);
        mEndTime =System.currentTimeMillis()+mTimeLeftInMillis;
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                btStartPause.setText("Start");
                updateButtons();
            }
        }.start();

        mTimerRunning = true;
        updateButtons();
    }

    private void pauseTimer(){
        mTimer.setVisibility(View.VISIBLE);
        timePicker.setVisibility(View.GONE);
        if(mCountDownTimer!=null){
            mCountDownTimer.cancel();
            mTimerRunning = false;
            updateButtons();
        }
    }
    private void updateCountDownText(){
        int hours = (int) (mTimeLeftInMillis/1000)/3600;
        int minutes = (int) ((mTimeLeftInMillis/1000)/60)%60;
        int seconds = (int) (mTimeLeftInMillis/1000)%60;

        String timeLeftFormatted = String.format(Locale.getDefault(),"%02d:%02d:%02d",hours, minutes,seconds);
        mTimer.setText(timeLeftFormatted);
        btStartPause.setVisibility(View.VISIBLE);
    }

    private void updateButtons(){
        if(mTimerRunning){
            btStartPause.setText("Pause");
        }else{
            btStartPause.setText("start");

            if(mTimeLeftInMillis<1000){
                btStartPause.setVisibility(View.INVISIBLE);
            }else {
                btStartPause.setVisibility(View.VISIBLE);
            }
        }
    }

}
