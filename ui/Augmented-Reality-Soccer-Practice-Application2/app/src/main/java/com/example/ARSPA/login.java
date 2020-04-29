package com.example.ARSPA;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class login extends AppCompatActivity {
    private TextView sign_text;
    private TextView find_text;
    private Button login_in_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sign_text = findViewById(R.id.sign_text);
        find_text = findViewById(R.id.find_text);
        login_in_login = findViewById(R.id.login_in_login);
    }

    public void ClickInLogin(View v) {
        Intent intent;
        if (v == sign_text) {
            intent = new Intent(login.this, sineup.class);
            startActivity(intent);
        }
        else if (v == find_text) {
            intent = new Intent(login.this, findaccount.class);
            startActivity(intent);

        }

        else if(v ==login_in_login ) {
            intent = new Intent(login.this, bottombar.class);
            startActivity(intent);
        }

    }
}
