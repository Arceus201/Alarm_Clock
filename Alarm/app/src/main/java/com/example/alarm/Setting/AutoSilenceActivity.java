package com.example.alarm.Setting;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;

import com.example.alarm.Model.Setting;
import com.example.alarm.R;
import com.example.alarm.sql.SQLiteHelperAlarm;

public class AutoSilenceActivity extends AppCompatActivity
        implements View.OnClickListener{

    private ImageButton btCancel,btAdd;
    private SQLiteHelperAlarm db;
    private Setting st;

    private RadioButton st0,st1,st2,st3,st4,st5,st6,st7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_silence);
        initView();

        db = new SQLiteHelperAlarm(this);
        st = db.getSetting();

        setCheck(st.getAuto_silence());


        btCancel.setOnClickListener(this);
        btAdd.setOnClickListener(this);
    }
    public void initView(){
        btCancel = findViewById(R.id.st_as_CanCel);
        btAdd = findViewById(R.id.st_as_Add);
        st0 = findViewById(R.id.as_never);
        st1 = findViewById(R.id.as_1);
        st2 = findViewById(R.id.as_2);
        st3 = findViewById(R.id.as_3);
        st4= findViewById(R.id.as_4);
        st5 = findViewById(R.id.as_5);
        st6 = findViewById(R.id.as_6);
        st7 = findViewById(R.id.as_7);

    }

    private void setCheck(int as) {
        if(as == 0) st0.setChecked(true);
        else if (as == 1) st1.setChecked(true);
        else if (as == 5) st2.setChecked(true);
        else if (as == 10) st3.setChecked(true);
        else if (as == 15) st4.setChecked(true);
        else if (as == 20) st5.setChecked(true);
        else if (as == 25) st6.setChecked(true);
        else  st7.setChecked(true);
    }

    private int getCheck() {
        int as;
        if(st0.isChecked()) as = 0;
        else if(st1.isChecked()) as = 1;
        else if(st2.isChecked()) as = 5;
        else if(st3.isChecked()) as = 10;
        else if(st4.isChecked()) as = 15;
        else if(st5.isChecked()) as = 20;
        else if(st6.isChecked()) as = 25;
        else as = 30;

        return as;
    }



    @Override
    public void onClick(View v) {
        if(v == btCancel){
            finish();
        }
        if(v==btAdd){
            int auto_silence = getCheck();
            Setting se = new Setting(st.getId(),auto_silence,st.getSource());
            db.updateSetting(se);
            finish();
        }
    }
}