package com.example.arspapp_ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

public class physical2 extends AppCompatActivity {
    Spinner spinner3;
    ImageButton physical2_btn;
    private String text;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_physical2);

            spinner3 = findViewById(R.id.spinner3);
            physical2_btn = findViewById(R.id.physical2_btn);
            final String[] data = getResources().getStringArray(R.array.physical_array);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,data);
            spinner3.setAdapter(adapter);


        }
        public void onClick(View v){
            Intent intent;

            text = spinner3.getSelectedItem().toString();
            if (v==physical2_btn) {
                intent = new Intent(physical2.this, test35.class);
                intent.putExtra("key", text);
                startActivity(intent);
            }
        }
    }



    /*Spinner spinner3 = findViewById(R.id.spinner3);

            final String[] data = getResources().getStringArray(R.array.physical_array);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,data);
            spinner3.setAdapter(adapter);*/