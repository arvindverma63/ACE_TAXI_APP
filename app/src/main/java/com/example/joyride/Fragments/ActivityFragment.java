package com.example.joyride.Fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.joyride.Activities.BookingActivity;
import com.example.joyride.Adapters.ActivityAdapter;
import com.example.joyride.AlertDialogs.BookingDialog;
import com.example.joyride.Models.ActivityItem;
import com.example.joyride.R;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ActivityFragment extends Fragment {

    private ActivityAdapter activityAdapter;
    private List<ActivityItem> originalList = new ArrayList<>();
    private List<ActivityItem> filteredList = new ArrayList<>();
    private RecyclerView recyclerView;
    private TextInputEditText searchEditText;
    private ImageButton datePicker;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String selectedDate;
    private ImageButton addActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_activity, container, false);
        addActivity = (ImageButton) view.findViewById(R.id.add_activity);
        addActivity.setOnClickListener(view1 -> {
           Intent intent = new Intent(getContext(),BookingActivity.class);
           startActivity(intent);
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        recyclerView = view.findViewById(R.id.recyclerView);
        searchEditText = view.findViewById(R.id.searchEditText);
        datePicker = view.findViewById(R.id.datePicker);
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

        // Set up date picker button
        datePicker.setOnClickListener(view1 -> {
            showDatePickerDialog();
        });

        // Populate the data list with dummy activities and dates
        for (int i = 1; i <= 50; i++) {
            String date = "2024-12-" + (i % 30 + 1); // Example of dates (can replace with actual date)
            originalList.add(new ActivityItem("Activity " + i, date));
        }
        filteredList.addAll(originalList);

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        activityAdapter = new ActivityAdapter(getContext(), filteredList, this::showModal);
        recyclerView.setAdapter(activityAdapter);

        // Set up search functionality
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterList(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void filterList(String query) {
        filteredList.clear();
        if (query.isEmpty()) {
            filteredList.addAll(originalList);
        } else {
            for (ActivityItem item : originalList) {
                if (item.getTitle().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(item);
                }
            }
        }
        activityAdapter.notifyDataSetChanged();
    }

    public void showModal(String item) {
        BookingDialog bookingDialog = new BookingDialog(getContext());
        bookingDialog.showBookingModal(item);
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                (view, year, monthOfYear, dayOfMonth) -> {
                    // Set the selected date
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, monthOfYear);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    selectedDate = dateFormat.format(calendar.getTime());
                    // Display selected date
                    Toast.makeText(getContext(), "Selected Date: " + selectedDate, Toast.LENGTH_SHORT).show();
                    // Call the filter method to filter RecyclerView data
                    filterDataByDate(selectedDate);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    private void filterDataByDate(String selectedDate) {
        filteredList.clear();
        for (ActivityItem item : originalList) {
            if (item.getDate().equals(selectedDate)) {
                filteredList.add(item);
            }
        }
        activityAdapter.notifyDataSetChanged();
    }



}
