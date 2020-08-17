package com.example.arspapp_ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;


public class shoot2 extends AppCompatActivity {

    ImageButton shoot2_btn;
    RadioButton radio_left;
    RadioButton radio_right;
    RadioGroup shrd_group;
    public int radio_count=-1 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_shoot2);
        shoot2_btn = findViewById(R.id.shoot2_btn);
        radio_left = findViewById(R.id.radio_left);
        radio_right = findViewById(R.id.radio_right);
        shrd_group = findViewById(R.id.shrd_group);
    }

    public void onClick(View v) {
        Intent intent;
        if(v==shoot2_btn) {


            switch (shrd_group.getCheckedRadioButtonId()) {
                case R.id.radio_left:
                    radio_count = 1;
                    intent = new Intent(shoot2.this, CameraActvity.class);
                    intent.putExtra("key",radio_count);
                    startActivity(intent);
                    break;

                case R.id.radio_right:
                    radio_count = 0;
                    intent = new Intent(shoot2.this, CameraActvity.class);
                    intent.putExtra("key",radio_count);
                    startActivity(intent);
                    break;

            }
        }
    }
}

