package com.example.ARSPA;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class privacy extends AppCompatActivity {
    ImageButton change1, change2, save_btn;
    TextView leave_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);

        change1 = findViewById(R.id.change1);
        change2 = findViewById(R.id.change2);
        save_btn = findViewById(R.id.save_btn);
        leave_btn = findViewById(R.id.leave_btn);


    }

        public void OnClickHandler(View view) {
            if (view == change1 ) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setTitle("변경을 완료 하시겠습니까?");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getApplicationContext(), "변경을 완료하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getApplicationContext(), "변경을 취소하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                });


                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }

            else if (view == change2){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setTitle("변경을 완료 하시겠습니까?");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getApplicationContext(), "변경을 완료하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getApplicationContext(), "변경을 취소하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                });


                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }

            else if(view == save_btn) {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setTitle("저장을 완료하였습니다.");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                       Intent intent = new Intent(privacy.this, setting.class);
                        startActivity(intent);
                    }
                });



                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }

            else if (view == leave_btn) {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setTitle("회원 탈퇴를 하시겠습니까?");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getApplicationContext(), "탈퇴를 완료하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getApplicationContext(), "탈퇴를 취소하셨습니다.", Toast.LENGTH_SHORT).show();
                    }
                });


                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        }


    }


