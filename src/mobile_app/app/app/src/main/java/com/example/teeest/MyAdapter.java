package com.example.teeest;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    List<String> dates, deviceIds = new ArrayList<String>();
    Context context;

    public MyAdapter(Context ct, List<String> dts, List<String> dIds){
        this.context = ct;
        this.dates = dts;
        this.deviceIds = dIds;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_template, parent, false);
        MyViewHolder viewholderClass = new MyViewHolder(view);
        return viewholderClass;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.ttv1.setText(dates.get(position));
        holder.ttv2.setText(deviceIds.get(position));

    }

    @Override
    public int getItemCount() {
        return dates.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView ttv1, ttv2;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ttv1 = itemView.findViewById(R.id.textDate);
            ttv2 = itemView.findViewById(R.id.deviceText);
        }
    }

    public void AddNewItem(String date, String devId){
        deviceIds.add(devId);
        dates.add(date);
        notifyItemRangeInserted(getItemCount() - 1, getItemCount());

    }

    public void DeleteItem(Integer pos){
        deviceIds.remove(deviceIds.get(pos));
        dates.remove(dates.get(pos));
        notifyItemRemoved(pos);
    }

}
