package com.example.ARSPA;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;

public class setting extends AppCompatActivity {
    private TextView login_setting_btn, manage_setting_btn, privacy_setting_btn, notice_setting_btn, rate_setting_btn, info_setting_btn;
    ImageView empty_picture;
    private static final int REQUEST_CODE = 0;

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
        empty_picture = findViewById(R.id.empty_picture);


        empty_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                try {
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();
                    empty_picture.setImageBitmap(img);

                } catch (Exception e) {
                    e.printStackTrace();
                }


            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
            }
        }


    }



}
