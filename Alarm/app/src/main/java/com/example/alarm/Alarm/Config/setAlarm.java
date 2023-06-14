package com.example.alarm.Alarm.Config;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
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

public class setAlarm extends AppCompatActivity
        implements View.OnClickListener
        , UpdateRepeatDialog.OnDataPassUpdate
        , UpdateLabelDialog.OnDataPassLabel
        ,RingToneDialog.OnDataSource{
    private ImageButton btCancel,btAdd;
    private TextView tvRepeat,tvLabel,tvRingTone;
    private TimePicker setTime;
    private FragmentManager fragmentManager;
    private SwitchCompat sw;
    private LinearLayout linearLayout, linearLayoutlabel, linearLayoutRingTone;

    private AlarmDAO alarmDAO;
    //private static final int REQUEST_CODE= 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alarm);

        initView();

        SQLiteHelperAlarm dbst = new SQLiteHelperAlarm(this);
        String source = dbst.getSetting().getSource();
        tvRingTone.setText(source);

        setTime.setIs24HourView(true);
        btCancel.setOnClickListener(this);
        btAdd.setOnClickListener(this);
        linearLayout.setOnClickListener(this);
        linearLayoutlabel.setOnClickListener(this);
        linearLayoutRingTone.setOnClickListener(this);
    }

    private void initView(){
        btCancel = findViewById(R.id.btCanCel);
        btAdd = findViewById(R.id.btAdd);
        setTime = findViewById(R.id.setTime);
        tvRepeat = findViewById(R.id.tvRepeat);
        linearLayout = findViewById(R.id.LnsetRepeat);
        tvLabel = findViewById(R.id.tvLabel);
        linearLayoutlabel = findViewById(R.id.LnsetLabel);
        tvRingTone = findViewById(R.id.tvRingTone);
        linearLayoutRingTone = findViewById(R.id.LnsetRingTone);
        sw = findViewById(R.id.set_switch);
    }



    @Override
    public void onClick(View v) {
        if(v == btCancel){
            finish();
        }
        if(v==btAdd){
           String hour = ""+setTime.getHour();
           String minute = ""+setTime.getMinute();
           String repeat =  tvRepeat.getText().toString();
           String label = tvLabel.getText().toString();
           String source = tvRingTone.getText().toString();

           boolean auto = sw.isChecked();
           int mode_autosilence = 0;
           if(auto == true) mode_autosilence=1;
           int mode =1;

            Time t = new Time(hour,minute,mode,repeat,label,source,mode_autosilence);
            SQLiteHelperAlarm db = new SQLiteHelperAlarm(this);

            if(db.checkDuplicate(hour, minute) == false) {
                db.addAlarm(t);
                Time time = db.getTimeByKey(hour, minute, repeat);
                Log.d(TAG, "setAlarm: " + time.getId());
                alarmDAO.setAlarm(this, time);
            }else{
                Toast.makeText(getApplicationContext(), "Báo thức đã tồn tại", Toast.LENGTH_SHORT).show();
            }

            finish();
        }
        if(v==linearLayout){
//            UpdateRepeatDialog dialogFragment = new UpdateRepeatDialog();
//            dialogFragment.show(getSupportFragmentManager(), "MyDialogFragment");
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