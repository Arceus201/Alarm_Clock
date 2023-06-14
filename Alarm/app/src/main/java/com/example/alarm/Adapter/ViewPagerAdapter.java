package com.example.alarm.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.alarm.Alarm.Fragment_Alarm;
import com.example.alarm.Alarm.Fragment_Alarm;
import com.example.alarm.Clock.Fragment_Clock;
import com.example.alarm.Stopwatch.Fragment_Stopwatch;
import com.example.alarm.Timer.Fragment_Timer;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
   int pageNumber;

    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.pageNumber = behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new Fragment_Alarm();
            case 1: return new Fragment_Clock();
            case 2: return new Fragment_Stopwatch();
            case 3: return new Fragment_Timer();
            default: return new Fragment_Alarm();
        }
    }

    @Override
    public int getCount() {
        return pageNumber;
    }
}
