package com.example.alarm.Alarm.Dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alarm.Adapter.RingToneAdapter;
import com.example.alarm.R;

public class RingToneDialog extends DialogFragment implements RingToneAdapter.ItemSourceListener{
    private RecyclerView recyclerView;
    int ACITION = 0;
    private MediaPlayer mediaPlayer;
    private  RingToneAdapter adapter;
    private String Source = "";

    private static final String ARG_DATA = "Code_source";

    public RingToneDialog() {
    }

    private OnDataSource dataPasser;

    public interface OnDataSource {
        void onDataSource(String data);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        dataPasser = (OnDataSource) context;
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
        View view = inflater.inflate(R.layout.dialog_source, container, false);
        // Setup UI components and event listeners
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Source = getArguments().getString(ARG_DATA);

        recyclerView = view.findViewById(R.id.recyclerSource);

        String[] data = {"Ring 1", "Ring 2", "Ring 3", "Ring 4","Ring 5"};
        adapter = new RingToneAdapter(data);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter.setItemSourceListener(this);

    }

    @Override
    public void onItemClick(View v, int position) {
        if(ACITION==1)
            mediaPlayer.pause();
        Source = adapter.getItem(position);
        int Song ;
        if(Source.equals("Ring 2")) Song = R.raw.ringtone2;
        else if(Source.equals("Ring 3")) Song = R.raw.ringtone3;
        else if(Source.equals("Ring 4")) Song = R.raw.ringtone4;
        else if(Source.equals("Ring 5")) Song = R.raw.ringtone5;
        else  Song = R.raw.ringtone1;

        mediaPlayer = MediaPlayer.create(getContext(), Song);
        mediaPlayer.start();
        ACITION = 1;

    }
    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        dataPasser.onDataSource(Source);
        if(ACITION==1)
            mediaPlayer.pause();
        dismiss();
    }

}
