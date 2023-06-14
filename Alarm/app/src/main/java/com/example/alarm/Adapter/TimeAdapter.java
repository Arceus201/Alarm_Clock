package com.example.alarm.Adapter;

import static android.content.ContentValues.TAG;

import android.app.PendingIntent;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alarm.Model.Time;
import com.example.alarm.R;

import java.util.ArrayList;
import java.util.List;

public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.ViewHolder> {


    private List<Time> list;
    Context context;
    private  List<Time> listBackup;
    private ItemListener itemListener;
    private OnSwitchClickListener switchClickListener;

    private PendingIntent pendingIntent;

    public TimeAdapter(@NonNull Context context) {
        this.context = context;
        list = new ArrayList<>();
        listBackup = new ArrayList<>();
    }
    public void remove(int position){
        list.remove(position);
        notifyDataSetChanged();
    }
    public void setItemListener(ItemListener itemListener) {
        this.itemListener = itemListener;
    }

    public void setOnSwitchClickListener(OnSwitchClickListener listener) {
        this.switchClickListener = listener;
    }

    public void setList(List<Time> list) {
        this.list = list;
        notifyDataSetChanged();


    }
    public Time getItem(int position){
        return list.get(position);


    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alarm, parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            final Time t = list.get(position);
            int m = t.getMode();

            String mgio = t.getGio();
            if(mgio.length()==1) mgio = "0"+mgio;
            String mphut = t.getPhut();
            if(mphut.length()==1) mphut = "0"+mphut;

            holder.gio.setText(mgio);
            holder.phut.setText(mphut);
            if(m==1) holder.mode.setChecked(true);
            else {
                holder.mode.setChecked(false);
            }
            holder.repeat.setText(t.getRepeat());

            holder.mode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (switchClickListener != null && position != RecyclerView.NO_POSITION) {
                        Log.d(TAG, "Click ");
                        switchClickListener.onSwitchClick(position, isChecked);
                    }
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemListener.onItemClick(v, position);
                }
            });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
                    implements  View.OnClickListener{

        TextView gio,phut,repeat;
        LinearLayout item_alarm;
        SwitchCompat mode;

        public ViewHolder(@NonNull View v) {
            super(v);
            item_alarm = v.findViewById(R.id.item_alarm);
            gio = v.findViewById(R.id.tvGio);
            phut = v.findViewById(R.id.tvPhut);
            mode = v.findViewById(R.id.swich);
            repeat = v.findViewById(R.id.tvRepeat);
        }

        @Override
        public void onClick(View v) {
            if(itemListener!=null){
                itemListener.onItemClick(v, getAdapterPosition());
            }
        }
    }

    public interface ItemListener{
        void onItemClick(View v,int position);
    }
    public interface OnSwitchClickListener {
        void onSwitchClick(int position, boolean isChecked);
    }

}
