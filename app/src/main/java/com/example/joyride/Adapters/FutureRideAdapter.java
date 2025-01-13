package com.example.joyride.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.joyride.Models.FutureRide;
import com.example.joyride.R;

import java.util.List;

public class FutureRideAdapter extends RecyclerView.Adapter<FutureRideAdapter.FutureRideViewHolder> {

    private final List<FutureRide> futureRideList;

    // Constructor to pass the list of future rides
    public FutureRideAdapter(List<FutureRide> futureRideList) {
        this.futureRideList = futureRideList;
    }

    @NonNull
    @Override
    public FutureRideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the future_ride_item layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.future_ride_item, parent, false);
        return new FutureRideViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FutureRideViewHolder holder, int position) {
        // Get the current ride
        FutureRide ride = futureRideList.get(position);

        // Bind the data to the views
        holder.textViewRideName.setText(ride.getRideName());
        holder.textViewRideDate.setText("Date: " + ride.getDate());
        holder.textViewRideTime.setText("Time: " + ride.getTime());
        holder.textViewPickupLocation.setText("Pickup: " + ride.getPickupLocation());
        holder.textViewDropLocation.setText("Drop: " + ride.getDropLocation());
        holder.textViewFare.setText("Fare: £" + ride.getFare());
    }

    @Override
    public int getItemCount() {
        return futureRideList.size();
    }

    // ViewHolder to hold the views for each item
    static class FutureRideViewHolder extends RecyclerView.ViewHolder {
        TextView textViewRideName, textViewRideDate, textViewRideTime, textViewPickupLocation, textViewDropLocation, textViewFare;

        public FutureRideViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewRideName = itemView.findViewById(R.id.textViewRideName);
            textViewRideDate = itemView.findViewById(R.id.textViewRideDate);
            textViewRideTime = itemView.findViewById(R.id.textViewRideTime);
            textViewPickupLocation = itemView.findViewById(R.id.textViewPickupLocation);
            textViewDropLocation = itemView.findViewById(R.id.textViewDropLocation);
            textViewFare = itemView.findViewById(R.id.textViewFare);
        }
    }
}
