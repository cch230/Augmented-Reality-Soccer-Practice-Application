package com.example.ARSPA;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;

import javax.xml.transform.Result;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class profile extends Fragment {
    public boolean readStoragePermission;
    private static final int REQUEST_CODE = 0;
    ImageView empty_picture;
    ImageButton setting_icon_btn,  get_frame2_btn;
    public ImageButton get_frame1_btn;
    View view;

    // @Override
    // public void onCreate(Bundle savedInstanceState) {
    //    super.onCreate(savedInstanceState);
    //  getContext(R.layout.activity_profile);}
    public profile() {

    }


    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_profile, container, false);
        setting_icon_btn = (ImageButton) view.findViewById(R.id.setting_icon);
        get_frame1_btn = (ImageButton) view.findViewById(R.id.frame1_btn);
        get_frame2_btn = (ImageButton) view.findViewById(R.id.frame2_btn);
        empty_picture = view.findViewById(R.id.empty_picture);

        setting_icon_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (v == setting_icon_btn) {
                    intent = new Intent(getActivity(), setting.class);
                    startActivity(intent);
                }
            }
        });

        get_frame1_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), bottombar.class);
                startActivity(intent);

            }
        });

        get_frame2_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), statistic.class);
                startActivity(intent);
            }
        });

        empty_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
        return view;

    }


   /* public void checkSelfPermission() {
        String temp = "";

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            temp += Manifest.permission.READ_EXTERNAL_STORAGE + " ";
        }

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            temp += Manifest.permission.WRITE_EXTERNAL_STORAGE + " ";
        }

        if (TextUtils.isEmpty(temp) == false) {
            ActivityCompat.requestPermissions(getActivity(), temp.trim().split(" "), 1);
        } else {
            Toast.makeText(getContext(), "권한을 모두 허용", Toast.LENGTH_SHORT).show();
        }

    }*/

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                try {
                    InputStream in = getActivity().getContentResolver().openInputStream(data.getData());
                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();
                    empty_picture.setImageBitmap(img);

                } catch (Exception e) {
                    e.printStackTrace();
                }


            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getContext(), "사진 선택 취소", Toast.LENGTH_LONG).show();
            }
        }


    }

}





