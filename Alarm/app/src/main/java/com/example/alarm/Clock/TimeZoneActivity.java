package com.example.alarm.Clock;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.alarm.Model.TimeZoneClock;
import com.example.alarm.R;
import com.example.alarm.sql.SQLiteHelperAlarm;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

public class TimeZoneActivity extends AppCompatActivity implements
             SearchView.OnQueryTextListener{


    private ListView lv;
    private SearchView sr;

    private ArrayAdapter<String> adapter;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_zone);

        lv = findViewById(R.id.mlistview);
        sr = findViewById(R.id.searchTimeZone);

        initListView();

        sr.setOnQueryTextListener(this);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Handle item click event
                String selectedID = (String) (parent.getItemAtPosition(position));
                SQLiteHelperAlarm db = new SQLiteHelperAlarm(TimeZoneActivity.this);
                List<TimeZoneClock> list = db.getAllTimeZone();

                int check = 1;
                for(TimeZoneClock t: list){
                    if(t.getIdtimezone().equals(selectedID)){
                        check=0;break;
                    }
                }
                if(check==1){
                    db.addTimeZone(selectedID);
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Clock " +selectedID+" đã tồn tại", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    private void initListView(){
        String [] idArray = TimeZone.getAvailableIDs();

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,idArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lv.setAdapter(adapter);
    }

    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    // khi thay doi -them,xoa
    public boolean onQueryTextChange(String newText) {
        filter(newText);
        return false;
    }
    private void filter(String s){
        // tao ds de luu sau khi loc
        List<String> filterlist = new ArrayList<>();
        // duyet tren ds backup
        if(s.equals("")){
            initListView();
        }

        for (int i = 0; i < adapter.getCount(); i++) {
            String item = adapter.getItem(i);
            if(item.toLowerCase().contains(s.toLowerCase())){
                filterlist.add(item);
            }
            // Xử lý với phần tử item tại vị trí i
        }

        if(filterlist.isEmpty()){
            Toast.makeText(this, "khong tim duoc", Toast.LENGTH_LONG).show();

        }else{// neu tim dc thi day vao  filterlist
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,filterlist);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            lv.setAdapter(adapter);
        }
    }


}