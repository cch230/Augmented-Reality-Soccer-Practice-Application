package com.example.ARSPA;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class login extends AppCompatActivity {
    private TextView sing_text_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sing_text_btn = findViewById(R.id.sing_text);
    }

    public void ClickInLogin(View v) {
        Intent intent;
        if (v == sing_text_btn) {
            intent = new Intent(login.this, sineup.class);
            startActivity(intent);
        } else
            intent = new Intent(login.this, findaccount.class);
        startActivity(intent);

    }
}
