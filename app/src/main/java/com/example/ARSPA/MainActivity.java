package com.example.ARSPA;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    public static final String PREFS_NAME = "MyPrefs";
    private Button button01,buuton02;

    private int checked;


    protected void onCreate(Bundle state) {

        super.onCreate(state);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        button01 = (Button) findViewById(R.id.login);
        button01 = (Button) findViewById(R.id.signup);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

    }
    public void onClick(View v){
        Intent intent;
        if(v==button01)
        {
            intent = new Intent(MainActivity.this, mainmenu.class);
            startActivity(intent);
        }
    }
}