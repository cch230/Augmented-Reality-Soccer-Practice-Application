package com.example.arspapp_ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

public class shoot2 extends AppCompatActivity {

    private Spinner spinner1;
    ImageButton shoot2_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_shoot2);
        Spinner spinner1 = findViewById(R.id.spinner1);
        shoot2_btn = findViewById(R.id.shoot2_btn);

        final String[] data = getResources().getStringArray(R.array.shoot_array);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,data);
        spinner1.setAdapter(adapter);

    }

    public void onClick(View v) {
        Intent intent;
        if(v==shoot2_btn) {
            intent = new Intent(shoot2.this, mainmenu.class);
            startActivity(intent);
        }
    }
}


