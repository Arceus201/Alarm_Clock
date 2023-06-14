package com.example.alarm.Clock;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alarm.Adapter.ClockAdapter;
import com.example.alarm.Model.TimeZoneClock;
import com.example.alarm.R;
import com.example.alarm.sql.SQLiteHelperAlarm;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Fragment_Clock extends Fragment
            implements  ClockAdapter.TimeZoneListener{
    private TextView time,date;
    private ClockAdapter clockAdapter;
    private RecyclerView recyclerView;
            //txtTimezone,txtTest,timezone;

    private SQLiteHelperAlarm db;

    //Calendar current;
    //Spinner spinner;
    //long miliSeconds;
    ArrayAdapter<String> idAdapter;
//    SimpleDateFormat sdf;
//    Date resultDdate;
    List<TimeZoneClock> list;

    private SimpleDateFormat timeFormat;
    private SimpleDateFormat dateFormat;

    private FloatingActionButton fab;
    public Fragment_Clock() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clock, container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.clocRecycleView);
        time = view.findViewById(R.id.tvTimeNow);
        date = view.findViewById(R.id.tvDateNow);
        fab = view.findViewById(R.id.fabclock);

        timeFormat = new SimpleDateFormat("HH:mm:ss");
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        // update time Current
        Date now = new Date();
        String mDate = dateFormat.format(now);
        date.setText("Current: "+ mDate);
        updateDateTime();

        // set data cho adapter
        clockAdapter = new ClockAdapter();
        db = new SQLiteHelperAlarm(getContext());
        list = db.getAllTimeZone();
        clockAdapter.setList(list);

        LinearLayoutManager manager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(clockAdapter);

        updateTimeCity();

        clockAdapter.setTimeZoneListener(this);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), TimeZoneActivity.class);
                startActivity(intent);
            }
        });




    }

    private void updateTimeCity(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                onResume();
            }
        }, 60000);
    }

    private void updateDateTime() {
        Date now = new Date();

        String mtime = timeFormat.format(now);

        time.setText(mtime);

        // Lặp lại cập nhật thời gian mỗi giây
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateDateTime();
            }
        }, 1000);
    }
    @Override
    public void onResume() {
        super.onResume();
        List<TimeZoneClock> list = db.getAllTimeZone();
        clockAdapter.setList(list);
    }

    @Override
    public void onItemClick(View v, int position) {
        TimeZoneClock data = clockAdapter.getItem(position);
        ClockDialog dialog = new ClockDialog(new ClockDialog.OnItemClockDialog() {
            @Override
            public void onItemClockDialog() {
                clockAdapter.remove(position);
            }
        });
        Bundle args = new Bundle();
        args.putParcelable("data_clock",  data);
        dialog.setArguments(args);
        dialog.show(getChildFragmentManager(), "ClockDialogFragment");
    }


}
