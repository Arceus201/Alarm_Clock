package com.example.alarm.Alarm.Dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.alarm.R;

public class UpdateLabelDialog extends DialogFragment  implements View.OnClickListener{
    private Button btCancel,btOk;
    private EditText ed;
    private static final String ARG_DATA = "Code_label";

    public UpdateLabelDialog() {

    }
    private OnDataPassLabel dataPasser;

    public interface OnDataPassLabel {
        void onDataPassLabel(String data);
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        dataPasser = (OnDataPassLabel) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_lable_alarm, container, false);
        // Setup UI components and event listeners
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        String Label = getArguments().getString(ARG_DATA);

        initView(v);

        ed.setText(Label);
        btCancel.setOnClickListener(this);
        btOk.setOnClickListener(this);

    }

    private void initView(View v){
        ed = v.findViewById(R.id.tvSetLabel);
        btCancel = v.findViewById(R.id.btCancelLabel);
        btOk = v.findViewById(R.id.btAddLabel);
    }

    @Override
    public void onClick(View v) {
        if(v == btCancel){
            dismiss();
        }
        if(v == btOk){
            dataPasser.onDataPassLabel(ed.getText().toString());
            dismiss();
        }
    }
}
