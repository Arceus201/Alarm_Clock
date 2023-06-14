package com.example.alarm.Alarm.Dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.alarm.R;

public class UpdateRepeatDialog extends DialogFragment implements View.OnClickListener{
    private CheckBox all,mon,tue,wed,thu,fri,sat,sun;
    private Button update;
    private String s="";
    private static final String ARG_DATA = "Code_repeat";


    public UpdateRepeatDialog() {

    }

    private OnDataPassUpdate dataPasser;

    public interface OnDataPassUpdate {
        void onDataPassUpdate(String data);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        dataPasser = (OnDataPassUpdate) context;
    }
    @Override
    public void onStart() {
        super.onStart();

        // Lấy thông tin về kích thước của DialogFragment
        int width = getResources().getDimensionPixelSize(R.dimen.dialog_width);
        int height = getResources().getDimensionPixelSize(R.dimen.dialog_height);

        // Thiết lập kích thước cho cửa sổ của DialogFragment
        Window window = getDialog().getWindow();
        window.setLayout(width, height);

        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();

        // Thiết lập vị trí xuống dưới màn hình
        params.gravity = Gravity.BOTTOM;
        params.y = 100;

        // Cập nhật thuộc tính của DialogFragment
        getDialog().getWindow().setAttributes(params);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_repeat, container, false);
        // Setup UI components and event listeners
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        String Repeat = getArguments().getString(ARG_DATA);


        initView(v);

        if(Repeat.equals("Once")){
            all.setChecked(false);
            mon.setChecked(false);
            tue.setChecked(false);
            wed.setChecked(false);
            thu.setChecked(false);
            fri.setChecked(false);
            sat.setChecked(false);
            sun.setChecked(false);
        }else if(Repeat.equals("Daily")){
            all.setChecked(false);
            mon.setChecked(true);
            tue.setChecked(true);
            wed.setChecked(true);
            thu.setChecked(true);
            fri.setChecked(true);
            sat.setChecked(true);
            sun.setChecked(true);
        }
        else{
            if(Repeat.contains("Mon"))  mon.setChecked(true);
            if(Repeat.contains("Tue"))  tue.setChecked(true);
            if(Repeat.contains("Wed"))  wed.setChecked(true);
            if(Repeat.contains("Thu"))  thu.setChecked(true);
            if(Repeat.contains("Fri"))  fri.setChecked(true);
            if(Repeat.contains("Sat"))  sat.setChecked(true);
            if(Repeat.contains("Sun"))  sun.setChecked(true);
        }


        all.setOnClickListener(this);

        update.setOnClickListener(this);



    }
    private void initView(View v){

        all = v.findViewById(R.id.ckAll);
        mon = v.findViewById(R.id.ckMon);
        tue = v.findViewById(R.id.ckTue);
        wed = v.findViewById(R.id.ckWed);
        thu = v.findViewById(R.id.ckThu);
        fri = v.findViewById(R.id.ckFri);
        sat = v.findViewById(R.id.ckSat);
        sun = v.findViewById(R.id.ckSun);
        update = v.findViewById(R.id.btRepeat);
    }

    @Override
    public void onClick(View v) {
        if(v==all){
            if(all.isChecked()) {
                mon.setChecked(true);
                tue.setChecked(true);
                wed.setChecked(true);
                thu.setChecked(true);
                fri.setChecked(true);
                sat.setChecked(true);
                sun.setChecked(true);
            }
            else{
                mon.setChecked(false);
                tue.setChecked(false);
                wed.setChecked(false);
                thu.setChecked(false);
                fri.setChecked(false);
                sat.setChecked(false);
                sun.setChecked(false);
            }
        }

        if(v==update){
            if(all.isChecked() ||(mon.isChecked() && tue.isChecked() && wed.isChecked()
                                    && thu.isChecked() && fri.isChecked() && sat.isChecked() && sun.isChecked())){
                s="Daily";
            }
            else{
                String smon="",stue="",swed="",sthu="",sfri="",ssat="",ssun="";
                s="";
                if(mon.isChecked()) smon = "Mon ";

                if(tue.isChecked()) stue = "Tue ";

                if(wed.isChecked()) swed = "Wed ";

                if(thu.isChecked()) sthu = "Thu ";

                if(fri.isChecked()) sfri = "Fri ";

                if(sat.isChecked()) ssat = "Sat ";

                if(sun.isChecked()) ssun = "Sun ";

                s = smon+stue+swed+sthu+sfri+ssat+ssun;
            }

//            Intent intent = new Intent();
//            intent.putExtra("key", s);
//            getActivity().setResult(Activity.RESULT_OK, intent);
            dataPasser.onDataPassUpdate(s);
            dismiss();
        }
    }


}
