package com.example.alarm.Alarm.Dialog;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.alarm.Alarm.AlarmDAO;
import com.example.alarm.Model.Time;
import com.example.alarm.R;
import com.example.alarm.Alarm.Config.Update_AlarmActivity;
import com.example.alarm.sql.SQLiteHelperAlarm;

public class MyDialog extends DialogFragment {
    private Button btSetting,btDelete;
    private OnItemClickListenerDialog mListener;
    private AlarmDAO alarmDAO;






    public MyDialog(OnItemClickListenerDialog mListener) {
        this.mListener = mListener;
    }
//    public MyDialog() {
//    }

    private static final String ARG_DATA = "data";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_setting_alarm, container, false);
        return view;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Time data = getArguments().getParcelable(ARG_DATA);

        btSetting = view.findViewById(R.id.btSettinglAlarm);
        btDelete = view.findViewById(R.id.btDeleteAlarm);

        SQLiteHelperAlarm db = new SQLiteHelperAlarm(getContext());



        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //xoa alarm co id tuong ung
                int id = data.getId();
                db.delete(id);
                mListener.onItemClickDialog();

                //Neu no dang duoc dat bao thuc thi tat bao thuc
                if(data.getMode() == 1) alarmDAO.cancelAlrm(getContext(), id);
                dismiss();
            }
        });

        btSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Update_AlarmActivity.class);
                intent.putExtra("updateAlarm", data);
                startActivity(intent);
                dismiss();
            }
        });
    }

    public interface OnItemClickListenerDialog {
        void onItemClickDialog();
    }


}
