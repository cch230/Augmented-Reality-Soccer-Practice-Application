package com.example.arspapp_ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

public class physical2 extends AppCompatActivity {
    Spinner spinner3;
    ImageButton physical2_btn;
    RadioButton radio_sp;
    RadioButton radio_st;
    RadioGroup phy_group;
    public int phy_count=-1 ;
    public int phy_timmer=-1;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_physical2);

            spinner3 = findViewById(R.id.spinner3);
            physical2_btn = findViewById(R.id.physical2_btn);
            phy_group = findViewById(R.id.phy_group);
            radio_sp = findViewById(R.id.phy_sp);
            radio_st = findViewById(R.id.phy_st);


        }


        public void onClick(View v) {
            Intent intent;
            spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position==0){
                        phy_timmer=1;
                    }
                    else if (position==1){
                        phy_timmer=2;
                    }
                    else if (position==2){
                        phy_timmer=3;
                    }
                    else{
                        phy_timmer=4;
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });
            if (v == physical2_btn) {

                switch (phy_group.getCheckedRadioButtonId()) {
                    case R.id.phy_sp:
                        phy_count = 1;
                        intent = new Intent(physical2.this, Camera_Phy.class);
                        intent.putExtra("key1", phy_count);
                        intent.putExtra("key2", phy_timmer);
                        startActivity(intent);
                        break;

                    case R.id.phy_st:
                        phy_count = 0;
                        intent = new Intent(physical2.this, Camera_Phy.class);
                        intent.putExtra("key1", phy_count);
                        intent.putExtra("key2", phy_timmer);
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