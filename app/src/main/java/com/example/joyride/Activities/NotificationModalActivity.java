package com.example.joyride.Activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.joyride.AlertDialogs.BookingDialog;
import com.example.joyride.R;

public class NotificationModalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notification_modal);

        String message = getIntent().getStringExtra("message");

        BookingDialog dialog = new BookingDialog(this);
        dialog.showBookingModal(message);
    }
}