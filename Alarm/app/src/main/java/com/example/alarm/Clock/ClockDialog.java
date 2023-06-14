package com.example.alarm.Clock;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.alarm.Model.TimeZoneClock;
import com.example.alarm.R;
import com.example.alarm.sql.SQLiteHelperAlarm;

public class ClockDialog extends DialogFragment {
    private Button btCancel,btDelete;
    private OnItemClockDialog mListener;






    public ClockDialog(OnItemClockDialog mListener) {
        this.mListener = mListener;
    }


    private static final String ARG_DATA = "data_clock";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_delete_timezone, container, false);
        return view;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TimeZoneClock data = getArguments().getParcelable(ARG_DATA);

        btCancel = view.findViewById(R.id.btCancelClock);
        btDelete = view.findViewById(R.id.btDeleteClock);

        SQLiteHelperAlarm db = new SQLiteHelperAlarm(getContext());


        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = data.getId();
                db.deleteTimeZone(id);
                mListener.onItemClockDialog();
                dismiss();
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public interface OnItemClockDialog {
        void onItemClockDialog();
    }


}
