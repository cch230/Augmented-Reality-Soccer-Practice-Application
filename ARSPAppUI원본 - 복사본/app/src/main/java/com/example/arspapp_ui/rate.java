package com.example.arspapp_ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class rate extends AppCompatActivity {
    ImageButton submit_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);

    }

    public void OnClickHandler(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("성공적으로 제출이 완료되었습니다.");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
            }
        });


        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

}
