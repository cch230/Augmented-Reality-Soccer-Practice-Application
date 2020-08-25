package com.example.arspapp_ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

public class physical2 extends AppCompatActivity {
    Spinner spinner3;
    ImageButton physical2_btn;
    RadioButton radio_left;
    RadioButton radio_right;
    RadioGroup shrd_group;

    private String text;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_physical2);
            radio_left = findViewById(R.id.radio_left2);
            radio_right = findViewById(R.id.radio_right2);
            spinner3 = findViewById(R.id.spinner3);
            physical2_btn = findViewById(R.id.physical2_btn);
            shrd_group = findViewById(R.id.radio_group2);

            final String[] data = getResources().getStringArray(R.array.physical_array);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,data);
            spinner3.setAdapter(adapter);


        }
        public void onClick(View v){
            Intent intent;

            text = spinner3.getSelectedItem().toString();
            if (v==physical2_btn) {
                switch (shrd_group.getCheckedRadioButtonId()) {
                    case R.id.radio_left2:
                        intent = new Intent(physical2.this, quick_camera.class);
                        intent.putExtra("key", text);
                        startActivity(intent);
                        break;

                    case R.id.radio_right2:
                        intent = new Intent(physical2.this, physical_camera.class);
                        intent.putExtra("key", text);
                        startActivity(intent);
                        break;
                }
            }
        }
    }



    /*Spinner spinner3 = findViewById(R.id.spinner3);

            final String[] data = getResources().getStringArray(R.array.physical_array);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,data);
            spinner3.setAdapter(adapter);*/