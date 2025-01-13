package com.example.joyride.Fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.joyride.Adapters.FutureRideAdapter;
import com.example.joyride.Models.FutureRide;
import com.example.joyride.R;

import java.util.ArrayList;
import java.util.List;

public class FutureFregment extends Fragment {

    private RecyclerView recyclerViewFuture;
    private FutureRideAdapter adapter;
    private List<FutureRide> futureRides;

    public FutureFregment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // Handle any parameters passed to the fragment
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_future_fregment, container, false);

        // Initialize RecyclerView
        recyclerViewFuture = view.findViewById(R.id.recyclerViewFuture);
        recyclerViewFuture.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize Data
        initializeFutureRides();

        // Set up adapter
        adapter = new FutureRideAdapter(futureRides);
        recyclerViewFuture.setAdapter(adapter);

        return view;
    }

    private void initializeFutureRides() {
        futureRides = new ArrayList<>();
        futureRides.add(new FutureRide("Airport Drop", "2025-01-15", "10:00 AM", "123 Main Street", "Airport Terminal 3", 150.00));
        futureRides.add(new FutureRide("City Center Visit", "2025-01-16", "2:30 PM", "456 Elm Street", "City Mall", 120.00));
        futureRides.add(new FutureRide("Train Station Pickup", "2025-01-17", "6:00 PM", "789 Oak Lane", "Central Station", 90.00));
    }
}
