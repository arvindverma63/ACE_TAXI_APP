package com.example.joyride.Activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.joyride.Logics.LoginManager;
import com.example.joyride.Logics.UpdateFCMApi;
import com.example.joyride.R;

public class LoginActivity extends AppCompatActivity {

    EditText edit_username,edit_password;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        edit_username = (EditText) findViewById(R.id.edit_username);
        edit_password = (EditText) findViewById(R.id.edit_password);
    }

    public void login(View view){

        LoginManager loginManager = new LoginManager(this);
        String username = edit_username.getText().toString();
        String password = edit_password.getText().toString();
        loginManager.login(username,password);

    }
}