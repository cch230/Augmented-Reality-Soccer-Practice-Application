package com.example.ARSPA;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class profile extends AppCompatActivity {
    private ImageButton setting_icon_btn,get_frame1_btn,get_frame2_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        setting_icon_btn = findViewById(R.id.setting_icon);
        get_frame1_btn = findViewById(R.id.frame1_btn);
        get_frame2_btn = findViewById(R.id.frame2_btn);
    }

    public void ClickSetting(View v) {
        Intent intent;
        if (v== setting_icon_btn) {
            intent = new Intent(profile.this, setting.class);
            startActivity(intent);
        }
        else if(v == get_frame1_btn) {
            intent = new Intent(profile.this, statistic.class);
            startActivity(intent);
        }
        else if(v==get_frame2_btn){
            intent = new Intent(profile.this, statistic.class);
            startActivity(intent);
        }
    }
}
