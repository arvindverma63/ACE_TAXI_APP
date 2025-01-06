package com.example.joyride.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.joyride.Models.RideHistory;
import com.example.joyride.R;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private final List<RideHistory> rideHistoryList;

    public HistoryAdapter(List<RideHistory> rideHistoryList) {
        this.rideHistoryList = rideHistoryList;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_list, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        RideHistory history = rideHistoryList.get(position);
        holder.textViewPickupLocation.setText("Pickup: " + history.getPickupLocation());
        holder.textViewDropLocation.setText("Drop: " + history.getDropLocation());
        holder.textViewFare.setText("Fare: " + history.getFare());
        holder.textViewDate.setText(history.getDate());
        holder.textViewTime.setText(history.getTime());
    }

    @Override
    public int getItemCount() {
        return rideHistoryList.size();
    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView textViewPickupLocation, textViewDropLocation, textViewFare, textViewDate, textViewTime;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewPickupLocation = itemView.findViewById(R.id.textViewPickupLocation);
            textViewDropLocation = itemView.findViewById(R.id.textViewDropLocation);
            textViewFare = itemView.findViewById(R.id.textViewFare);
            textViewDate = itemView.findViewById(R.id.textViewRideDate);
            textViewTime = itemView.findViewById(R.id.textViewRideTime);
        }
    }
}
