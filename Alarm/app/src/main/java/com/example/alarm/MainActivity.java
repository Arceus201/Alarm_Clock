package com.example.alarm;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.alarm.Adapter.ViewPagerAdapter;
import com.example.alarm.Alarm.AlarmDAO;
import com.example.alarm.Alarm.Config.setAlarm;
import com.example.alarm.Model.Time;
import com.example.alarm.sql.SQLiteHelperAlarm;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_BOOT_PERMISSION = 123;
    private boolean isPermissionRequested = false;

    private FloatingActionButton fab;
    private BottomNavigationView navigationView;
    private ViewPager viewPager;


    private SQLiteHelperAlarm db;

    private AlarmDAO alarmDAO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG,  "Main");

        alarmDAO = new AlarmDAO();
        db = new SQLiteHelperAlarm(this);
        List<Time> list = db.getAll2();
        for (Time x : list) {
            alarmDAO.setAlarm(this, x);
        }

   /*     ComponentName receiver = new ComponentName(this, SampleBootReceiver.class);
        PackageManager pm = this.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);*/


        initView();


        // add alarm
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, setAlarm.class);
                startActivity(intent);
            }
        });

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), 4);
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            // chon trang nao
            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0: navigationView.getMenu().findItem(R.id.icAlarm).setChecked(true);
                        break;
                    case 1: navigationView.getMenu().findItem(R.id.icClock).setChecked(true);
                        break;
                    case 2: navigationView.getMenu().findItem(R.id.icStopwatch).setChecked(true);
                        break;
                    case 3: navigationView.getMenu().findItem(R.id.icTimer).setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.icAlarm:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.icClock:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.icStopwatch:
                        viewPager.setCurrentItem(2);
                        break;
                    case R.id.icTimer:
                        viewPager.setCurrentItem(3);
                        break;
                }
                return true;
            }
        });

        // chuyen huong tu thong bao
//        Bundle extras = getIntent().getExtras();
//        if (extras != null && extras.containsKey("fragment_alarm")) {
//            String fragmentName = extras.getString("fragment_alarm");
//            if (fragmentName != null && fragmentName.equals("alarm")) {
//                Log.d(TAG, "chuyển đến fragment alarm");
//                viewPager.setCurrentItem(0); // Chuyển đến fragment Alarm trong ViewPager (vị trí 0)
//            }
//        } else if (extras != null && extras.containsKey("fragment_timer")) {
//            Log.d(TAG, "chuyển đến fragment timer");
//            String fragmentName = extras.getString("fragment_timer");
//            if (fragmentName != null && fragmentName.equals("timer")) {
//                viewPager.setCurrentItem(3); // Chuyển đến fragment timer trong ViewPager (vị trí 3)
//            }
//        }

    }




    private void initView(){
        viewPager = findViewById(R.id.viewPager);
        navigationView = findViewById(R.id.navigation);
        fab = findViewById(R.id.fab);

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}