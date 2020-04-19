package com.example.ARSPA;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class setting extends AppCompatActivity {
    private TextView login_setting_btn, manage_setting_btn, privacy_setting_btn, notice_setting_btn, rate_setting_btn, info_setting_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        login_setting_btn = findViewById(R.id.login_in_setting);
        manage_setting_btn = findViewById(R.id.manage_in_setting);
        privacy_setting_btn = findViewById(R.id.privacy_in_setting);
        notice_setting_btn = findViewById(R.id.notice_in_setting);
        rate_setting_btn = findViewById(R.id.rate_in_setting);
        info_setting_btn = findViewById(R.id.info_in_setting);
    }

    public void ClickInSetting (View v) {
        Intent intent;

        if(v==login_setting_btn) {
            intent = new Intent(setting.this, login.class);
            startActivity(intent);
        }
        else if(v==manage_setting_btn) {
            intent = new Intent(setting.this, manage.class);
            startActivity(intent);
        }

        else if(v==privacy_setting_btn) {
            intent = new Intent(setting.this, privacy.class);
            startActivity(intent);
        }

        else if(v==notice_setting_btn) {
            intent = new Intent(setting.this, notice.class);
            startActivity(intent);
        }

        else if(v==rate_setting_btn) {
            intent = new Intent(setting.this, rate.class);
            startActivity(intent);
        }

        else if(v==info_setting_btn) {
            intent = new Intent(setting.this, info.class);
            startActivity(intent);
        }
    }
}
