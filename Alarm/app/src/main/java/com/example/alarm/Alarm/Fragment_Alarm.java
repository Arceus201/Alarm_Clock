package com.example.alarm.Alarm;

import static android.content.ContentValues.TAG;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alarm.Adapter.TimeAdapter;
import com.example.alarm.Alarm.Dialog.MyDialog;
import com.example.alarm.Model.Time;
import com.example.alarm.R;
import com.example.alarm.Setting.AlarmRingToneActivity;
import com.example.alarm.Setting.AutoSilenceActivity;
import com.example.alarm.sql.SQLiteHelperAlarm;

import java.util.List;

public class Fragment_Alarm extends Fragment
                implements TimeAdapter.ItemListener
                , TimeAdapter.OnSwitchClickListener {
    private TimeAdapter timeAdapter;
    private TextView tv_alarm;
    private  RecyclerView recyclerView;

    private Button btshow;

    private  List<Time> list;
    private SQLiteHelperAlarm db;

    private AlarmDAO alarmDAO;

    public Fragment_Alarm() {
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if(bundle==null) return;

            int acctionMusic = bundle.getInt("action_music");

            if(acctionMusic == 3){
                int id = bundle.getInt("updatemode");
                db.updateMode(id,0);
                onResume();
            }

        }
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_alarm, container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated: ");
        super.onViewCreated(view, savedInstanceState);

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(broadcastReceiver,new IntentFilter("send_data_to_alarm"));

        recyclerView = view.findViewById(R.id.recyclerview);

       // Intent intent = getIntent();
        timeAdapter = new TimeAdapter(getContext());
        db = new SQLiteHelperAlarm(getContext());

        setHasOptionsMenu(true);

        List<Time> list = db.getAll();
        timeAdapter.setList(list);

        LinearLayoutManager manager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(timeAdapter);

        timeAdapter.setItemListener(this);
        timeAdapter.setOnSwitchClickListener(this);


    }

    // khoi tao menu
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_setting, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    // bat su kien cho menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.settingRingTone:
                Intent intent = new Intent(getContext(), AlarmRingToneActivity.class);
                startActivity(intent);
                break;
            case R.id.settingAutoSilence:
                Intent intent2 = new Intent(getContext(), AutoSilenceActivity.class);
                startActivity(intent2);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume: ");
        super.onResume();
        List<Time> list = db.getAll();
        timeAdapter.setList(list);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(broadcastReceiver);
    }


    @Override
    public void onItemClick(View v, int position) {
        Time data = timeAdapter.getItem(position);

        Log.d(TAG, "onItemClick: " + data.getMode_autoSilence());
        MyDialog dialog = new MyDialog(new MyDialog.OnItemClickListenerDialog() {
            @Override
            public void onItemClickDialog() {
                timeAdapter.remove(position);
            }
        });
        Bundle args = new Bundle();
        args.putParcelable("data",  data);
        dialog.setArguments(args);
        //dialog.newInstance(data);
        dialog.show(getChildFragmentManager(), "YourDialogFragment");

    }

    @Override
    public void onSwitchClick(int position, boolean isChecked) {
        Time t = timeAdapter.getItem(position);
        int mode=0;
        if(isChecked==true) {
            Log.d(TAG, "onSwitchClick: ");
            mode = 1;
           alarmDAO.setAlarm(getContext(),t);
        }else {
            Log.d(TAG, "onSwitchClick: ");
            sendAcciontoService(3);
           alarmDAO.cancelAlrm(getContext(), t.getId());
        }



        db.updateMode(t.getId(),mode);
    }

    private void sendAcciontoService(int action){
        Intent intent = new Intent(getContext(),AlarmService.class);
        intent.putExtra("action_service", action);
        getContext().startService(intent);
    }




}
