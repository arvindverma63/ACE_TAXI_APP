package com.example.joyride.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.joyride.Models.ActivityItem;
import com.example.joyride.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ViewHolder> {

    private List<ActivityItem> dataList = new ArrayList<>();
    private Context context;

    // Interface for item click
    public interface OnItemClickListener {
        void onItemClick(String item);
    }

    private OnItemClickListener onItemClickListener;

    public ActivityAdapter(Context context, List<ActivityItem> dataList, OnItemClickListener listener) {
        this.context = context;
        this.dataList = dataList;
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public ActivityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityAdapter.ViewHolder holder, int position) {
        ActivityItem data = dataList.get(position);
        holder.itemTitle.setText(data.getTitle());
        holder.itemSubtitle.setText("Subtitle for " + data.getTitle());
        holder.itemDate.setText(data.getDate());
        holder.itemView.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(data.getTitle());
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView itemTitle, itemSubtitle;
        TextView itemDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemTitle = itemView.findViewById(R.id.itemTitle);
            itemSubtitle = itemView.findViewById(R.id.itemSubtitle);
            itemDate = itemView.findViewById(R.id.itemDate);
        }
    }
}
