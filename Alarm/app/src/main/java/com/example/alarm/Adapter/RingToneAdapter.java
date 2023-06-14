package com.example.alarm.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.alarm.R;

public class RingToneAdapter extends RecyclerView.Adapter<RingToneAdapter.ViewHolder>{

    private String[] mData;
    private ItemSourceListener itemSourceListener;
    public void setItemSourceListener(ItemSourceListener itemListener) {
        this.itemSourceListener = itemListener;
    }

    public RingToneAdapter(String[] data) {
        mData = data;
    }

    public String getItem(int position) {
        return mData[position];
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_source, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextView.setText(mData[position]);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemSourceListener.onItemClick(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
        public TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.tvsource);
            mTextView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

        }
    }
    public interface ItemSourceListener{
        void onItemClick(View v,int position);
    }

}
