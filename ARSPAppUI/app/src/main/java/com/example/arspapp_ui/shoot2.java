package com.example.arspapp_ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;



public class shoot2 extends AppCompatActivity {

    private Spinner spinner1;
    ImageButton shoot2_btn;
    RadioButton radio_left;
    RadioButton radio_right;
    RadioGroup shrd_group;
    private int  radio_count;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_shoot2);
        Spinner spinner1 = findViewById(R.id.spinner1);
        shoot2_btn = findViewById(R.id.shoot2_btn);
        radio_left = findViewById(R.id.radio_left);
        radio_right = findViewById(R.id.radio_right);
        shrd_group = findViewById(R.id.shrd_group);

        final String[] data = getResources().getStringArray(R.array.shoot_array);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,data);
        spinner1.setAdapter(adapter);

    }

    public void onClick(View v) {
        Intent intent;
        if(v==shoot2_btn) {


            switch (shrd_group.getCheckedRadioButtonId()) {
                case R.id.radio_left:
                    intent = new Intent(shoot2.this, CameraActvity.class);
                    startActivity(intent);
                    radio_count = 1;
                    break;

                case R.id.radio_right:
                    intent = new Intent(shoot2.this, CameraActvity.class);
                    startActivity(intent);
                    radio_count = 0;
                    break;

            }
        }




    }


}

