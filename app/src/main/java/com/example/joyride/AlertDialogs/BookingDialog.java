package com.example.joyride.AlertDialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.example.joyride.R;

public class BookingDialog {
    private Context context;
    public BookingDialog(Context context){
        this.context = context;
    }
    public void showBookingModal(String item){
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.edit_modal,null);
        dialog.setView(dialogView);
        dialog.setTitle("Booking Id: "+item);
        dialog.setCancelable(true);
        dialog.show();
    }

    public void addBookingModal(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.add_activity_modal,null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.show();
    }
}
