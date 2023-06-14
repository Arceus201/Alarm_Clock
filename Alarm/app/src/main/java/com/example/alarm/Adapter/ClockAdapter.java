package com.example.alarm.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alarm.Model.TimeZoneClock;
import com.example.alarm.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class ClockAdapter extends RecyclerView.Adapter<ClockAdapter.ViewHolder>{
    private Calendar current;
    private long miliSeconds;
    private ArrayAdapter<String> adapter;
    private SimpleDateFormat sdf;
    private Date resultDdate;

    private TimeZoneListener mListener;

    private List<TimeZoneClock> list;
    private  List<TimeZoneClock> listBackup;

    public void setTimeZoneListener(TimeZoneListener mListener) {
        this.mListener = mListener;
    }

    public ClockAdapter() {
        list = new ArrayList<>();
        listBackup = new ArrayList<>();
    }

    public void remove(int position){
        list.remove(position);
        notifyDataSetChanged();
    }

    public void setList(List<TimeZoneClock> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_clock, parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TimeZoneClock t = list.get(position);
        String idTimeZone = t.getIdtimezone();

        // xu ly time, date
        getGMTtime();
        TimeZone timeZone = TimeZone.getTimeZone(idTimeZone);
        miliSeconds = miliSeconds + timeZone.getRawOffset();
        resultDdate = new Date(miliSeconds);

        holder.tvcity.setText(idTimeZone);
        holder.tvtimecity.setText(""+new SimpleDateFormat("HH:mm").format(resultDdate));
        holder.tvdatecity.setText(""+ new SimpleDateFormat("MMMM dd").format(resultDdate));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(adapter.getContext(), idTimeZone, Toast.LENGTH_SHORT).show();
                mListener.onItemClick(v, position);
            }
        });
    }

    @Override


    public int getItemCount() {
        return list.size();
    }

    public TimeZoneClock getItem(int position){
        return list.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder
            implements  View.OnClickListener {

        TextView tvcity,tvtimecity,tvdatecity;
        public ViewHolder(@NonNull View v) {
            super(v);

            tvcity = v.findViewById(R.id.tvCity);
            tvtimecity = v.findViewById(R.id.tvtimeCity);
            tvdatecity = v.findViewById(R.id.tvDateCity);

        }

        @Override
        public void onClick(View v) {
            if(mListener!=null){
                mListener.onItemClick(v, getAdapterPosition());
            }
        }
    }
    private  void getGMTtime(){
        current = Calendar.getInstance();

        miliSeconds = current.getTimeInMillis();

        TimeZone tzCurrent =  current.getTimeZone();
        int offset = tzCurrent.getRawOffset();
        if(tzCurrent.inDaylightTime(new Date())){
            offset = offset + tzCurrent.getDSTSavings();
        }
        miliSeconds = miliSeconds - offset;
    }

    public interface TimeZoneListener{
        void onItemClick(View v,int position);
    }
}
