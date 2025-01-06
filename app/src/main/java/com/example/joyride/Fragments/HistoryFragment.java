package com.example.joyride.Fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.joyride.Adapters.HistoryAdapter;
import com.example.joyride.Models.RideHistory;
import com.example.joyride.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {

    private RecyclerView recyclerViewHistory;
    private HistoryAdapter historyAdapter;
    private List<RideHistory> rideHistoryList;
    private List<RideHistory> filteredList;
    private TextInputEditText searchEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        // Initialize RecyclerView
        recyclerViewHistory = view.findViewById(R.id.recyclerViewHistory);
        recyclerViewHistory.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize search bar
        searchEditText = view.findViewById(R.id.searchEditText);

        // Initialize data
        rideHistoryList = getRideHistoryData();
        filteredList = new ArrayList<>(rideHistoryList);

        // Initialize adapter
        historyAdapter = new HistoryAdapter(filteredList);
        recyclerViewHistory.setAdapter(historyAdapter);

        // Add search functionality
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterRideHistory(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        return view;
    }

    private List<RideHistory> getRideHistoryData() {
        List<RideHistory> historyList = new ArrayList<>();

        // Sample data
        historyList.add(new RideHistory("123 Main Street", "456 Elm Street", "£150.00", "24 Dec 2024", "12:30 PM"));
        historyList.add(new RideHistory("789 Oak Avenue", "101 Pine Road", "£200.00", "23 Dec 2024", "11:00 AM"));
        historyList.add(new RideHistory("555 Maple Drive", "202 Cedar Lane", "£180.00", "22 Dec 2024", "10:15 AM"));

        return historyList;
    }

    private void filterRideHistory(String query) {
        filteredList.clear();
        if (query.isEmpty()) {
            filteredList.addAll(rideHistoryList);
        } else {
            for (RideHistory history : rideHistoryList) {
                if (history.getPickupLocation().toLowerCase().contains(query.toLowerCase()) ||
                        history.getDropLocation().toLowerCase().contains(query.toLowerCase()) ||
                        history.getDate().toLowerCase().contains(query.toLowerCase()) ||
                        history.getTime().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(history);
                }
            }
        }
        historyAdapter.notifyDataSetChanged();
    }
}
