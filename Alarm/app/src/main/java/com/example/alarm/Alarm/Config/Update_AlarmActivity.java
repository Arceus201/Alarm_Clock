package com.example.alarm.Alarm.Config;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.alarm.Alarm.AlarmDAO;
import com.example.alarm.Alarm.Dialog.RingToneDialog;
import com.example.alarm.Model.Time;
import com.example.alarm.Alarm.Dialog.UpdateLabelDialog;
import com.example.alarm.Alarm.Dialog.UpdateRepeatDialog;
import com.example.alarm.R;
import com.example.alarm.sql.SQLiteHelperAlarm;

public class Update_AlarmActivity extends AppCompatActivity
        implements View.OnClickListener,
        UpdateRepeatDialog.OnDataPassUpdate
        , UpdateLabelDialog.OnDataPassLabel
        ,RingToneDialog.OnDataSource{
    private ImageButton btCancel,btUpdate;
    private TimePicker updateTime;
    private TextView tvRepeat,tvLabel,tvRingTone;
    private LinearLayout ln,linearLayoutlabel,linearLayoutRingTone;

    private SwitchCompat sw;

    private Time time;
    String historyTime;

    private AlarmDAO alarmDAO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_alarm);

        initView();

        Intent intent = getIntent();
        time =  intent.getParcelableExtra("updateAlarm");

        // luu lai thoi gian cu
        historyTime = time.getGio()+":"+time.getPhut();


        updateTime.setIs24HourView(true);
        updateTime.setHour(Integer.parseInt(time.getGio()));
        updateTime.setMinute(Integer.parseInt(time.getPhut()));

        tvRepeat.setText(time.getRepeat());
        tvLabel.setText(time.getLabel());
        tvRingTone.setText(time.getSource());

        int mode_silence = time.getMode_autoSilence();

        Log.d(TAG, "Mode: " + mode_silence);
        if(mode_silence == 0) sw.setChecked(false);
        else sw.setChecked(true);

        btCancel.setOnClickListener(this);
        btUpdate.setOnClickListener(this);
        ln.setOnClickListener(this);
        linearLayoutlabel.setOnClickListener(this);
        linearLayoutRingTone.setOnClickListener(this);
    }

    private void initView(){
        btCancel = findViewById(R.id.btCanCel);
        btUpdate = findViewById(R.id.btUpdate);
        updateTime = findViewById(R.id.setTime);
        tvRepeat = findViewById(R.id.tvRepeat);
        ln = findViewById(R.id.LnUpdateRepeat);
        tvLabel = findViewById(R.id.tvUpdateLabel);
        linearLayoutlabel = findViewById(R.id.LnUpdateLabel);
        tvRingTone = findViewById(R.id.tvUpdateRingTone);
        linearLayoutRingTone = findViewById(R.id.LnUpdateRingTone);
        sw = findViewById(R.id.update_switch);
    }

    @Override
    public void onClick(View v) {
        if(v == btCancel){
            finish();
        }
        if(v==btUpdate){
            if(time.getMode()==1){
                alarmDAO.cancelAlrm(this, time.getId());
            }
            String hour = ""+updateTime.getHour();
            String minute = ""+updateTime.getMinute();
            String repeat= tvRepeat.getText().toString();
            String label = tvLabel.getText().toString();
            String soure = tvRingTone.getText().toString();

            boolean auto = sw.isChecked();
            int mode_autosilence = 0;
            if(auto == true) mode_autosilence=1;

            int mode =1;

            Time t = new Time(time.getId(),hour,minute,mode,repeat,label,soure,mode_autosilence);
            SQLiteHelperAlarm db = new SQLiteHelperAlarm(this);

            // nếu chỉ updat các tiêu chí khác or update mà thời gian không trùng
            //thì cho phép update
            String newTime = hour+":"+minute;
            if(historyTime.equals(newTime) || db.checkDuplicate(hour, minute) == false) {
                db.update(t);
                alarmDAO.setAlarm(this, t);
            }
            else{
                Toast.makeText(getApplicationContext(), "Báo thức đã tồn tại", Toast.LENGTH_SHORT).show();
            }

            finish();
        }
        if(v==ln){
            UpdateRepeatDialog dialog = new UpdateRepeatDialog();
            Bundle args = new Bundle();
            args.putString("Code_repeat",  tvRepeat.getText().toString());
            dialog.setArguments(args);
            dialog.show(getSupportFragmentManager(), "UpdateRepeat");
        }
        if(v==linearLayoutlabel){
            UpdateLabelDialog dialog = new UpdateLabelDialog();
            Bundle args = new Bundle();
            args.putString("Code_label",  tvLabel.getText().toString());
            dialog.setArguments(args);
            dialog.show(getSupportFragmentManager(), "UpdateLabel");
        }
        if(v==linearLayoutRingTone){
            RingToneDialog dialog = new RingToneDialog();
            Bundle args = new Bundle();
            args.putString("Code_source",  tvRingTone.getText().toString());
            dialog.setArguments(args);
            dialog.show(getSupportFragmentManager(), "RingTone");
        }
    }

    @Override
    public void onDataPassUpdate(String data) {
        if(data.equals("")) data = "Once";
        tvRepeat.setText(data);
    }

    @Override
    public void onDataPassLabel(String data) {
        tvLabel.setText(data);
    }

    @Override
    public void onDataSource(String data) {
        if(data.equals("")) data = "Ring 1";
        tvRingTone.setText(data);
    }
}