package com.example.alarm.Stopwatch;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.alarm.Model.StopWatch;
import com.example.alarm.R;
import com.example.alarm.sql.SQLiteHelperAlarm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Fragment_Stopwatch extends Fragment implements View.OnClickListener{
    private TextView tv;
    private Button btstart, btstop,btreset,btlap;
    private ListView lv;
    private Handler handler;
    private long MilisecondTime,StartTime,TimeBuff,UpdateTime=0L,historyTime = 0L;

    private int Seconds,Minutes,MiliSeconds;

    private String[] ListElements = new String[]{};

    private List<String> ListElementsArrayList;

    private ArrayAdapter<String> adapter;

    private SQLiteHelperAlarm db;
    public Fragment_Stopwatch() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stopwatch, container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);

        handler = new Handler();
        db = new SQLiteHelperAlarm(getContext());

        List<StopWatch> list = db.getAllStopWatch();
        Log.d(TAG, "listzie: " + list.size());
        ListElementsArrayList = new ArrayList<String>(Arrays.asList(ListElements));
        if(list.size()>0){

            tv.setText(list.get(0).getTime());
            if(list.get(0).getLap()!=null) {
                Log.d(TAG, "tv_time" + list.get(0).getTime());
                for (StopWatch x : list) {
                    ListElementsArrayList.add(x.getLap());
                    Log.d(TAG, "lap" + x.getLap());
                }
            }
        }
        adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1,ListElementsArrayList);
        lv.setAdapter(adapter);


        historyTime =  History();

        btstart.setEnabled(true);
        btstop.setEnabled(false);
        btreset.setEnabled(false);
        btlap.setEnabled(false);

        btstart.setOnClickListener(this);
        btstop.setOnClickListener(this);
        btreset.setOnClickListener(this);
        btlap.setOnClickListener(this);



    }

    public Long History(){
        String[] parts = tv.getText().toString().split(":");
        long minutes = Long.parseLong(parts[0]);
        long seconds = Long.parseLong(parts[1]);
        long millis = Long.parseLong(parts[2]);

        return (minutes * 60 * 1000) + (seconds * 1000) + millis;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    private void initView(View view){
        tv = view.findViewById(R.id.tvStopWatch);
        btstart = view.findViewById(R.id.btStartSW);
        btstop = view.findViewById(R.id.btStopSW);
        btreset = view.findViewById(R.id.btResetSW);
        btlap = view.findViewById(R.id.btsavelap);
        lv = view.findViewById(R.id.lvSW);
    }

    @Override
    public void onClick(View v) {
        if(v==btstart){
            StartTime = SystemClock.uptimeMillis();
            handler.postDelayed(runnable, 0);

            btreset.setEnabled(false);
            btstart.setEnabled(false);
            btlap.setEnabled(true);
            btstop.setEnabled(true);
        }
        if(v==btstop){
            btreset.setEnabled(true);
            btstart.setEnabled(true);
            btlap.setEnabled(false);
            btstop.setEnabled(false);

            db.deleteAllStopwatches();
            List<StopWatch> result = new ArrayList<>();
            if(ListElementsArrayList.size()!=0) {
                for (int i = 0; i < ListElementsArrayList.size(); i++) {
                    Log.d(TAG, "Create lap " + "tv:" + tv.getText() + "lap:" + ListElementsArrayList.get(i));
                    StopWatch t = new StopWatch(tv.getText().toString(), ListElementsArrayList.get(i));
                    result.add(t);
                }
                db.addStopwatches(result);
            }else{
                StopWatch tmp = new StopWatch(tv.getText().toString());
                db.addOneStopwatch(tmp);
            }

            TimeBuff += MilisecondTime;
            handler.removeCallbacks(runnable);
            btreset.setEnabled(true);
        }
        if(v==btreset){
            btreset.setEnabled(false);
            btstart.setEnabled(true);
            btlap.setEnabled(false);
            btstop.setEnabled(false);

            historyTime = 0;
            db.deleteAllStopwatches();
            MilisecondTime =0L;
            StartTime = 0L;
            TimeBuff = 0L;
            UpdateTime = 0L;
            Seconds = 0;
            Minutes = 0;
            MiliSeconds = 0;

            tv.setText("00:00:00");
            ListElementsArrayList.clear();
            adapter.notifyDataSetChanged();
        }
        if(v==btlap){
            ListElementsArrayList.add(tv.getText().toString());
            adapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        db.deleteAllStopwatches();
        List<StopWatch> result = new ArrayList<>();
        if(ListElementsArrayList.size()!=0) {
            for (int i = 0; i < ListElementsArrayList.size(); i++) {
                Log.d(TAG, "Create lap " + "tv:" + tv.getText() + "lap:" + ListElementsArrayList.get(i));
                StopWatch t = new StopWatch(tv.getText().toString(), ListElementsArrayList.get(i));
                result.add(t);
            }
            db.addStopwatches(result);
        }else{
            StopWatch tmp = new StopWatch(tv.getText().toString());
            db.addOneStopwatch(tmp);
        }

    }

    public Runnable runnable = new Runnable() {
        @Override
        public void run() {
            MilisecondTime = SystemClock.uptimeMillis() - StartTime;
            UpdateTime = TimeBuff + MilisecondTime + historyTime;
            Seconds = (int) (UpdateTime / 1000);
            Minutes = Seconds / 60;
            Seconds = Seconds % 60;

            MiliSeconds = (int) (UpdateTime % 1000);

            tv.setText(""+Minutes+":"
                       + String.format("%02d", Seconds)+":"
                       + String.format("%03d", MiliSeconds));
            handler.postDelayed(this, 0);
        }
    };
}
