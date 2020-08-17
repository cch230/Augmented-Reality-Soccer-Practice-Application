package com.example.arspapp_ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;

public class video extends Fragment {

    private View view;
    private ImageButton bookmark_1,bookmark_2;
    private VideoView video_1,video_2;
    private String DETAIL_PATH = "DCIM/test1/";
    Context context;
    @Nullable

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_video,container,false);
        bookmark_1 = view.findViewById(R.id.bookmark_1);
        bookmark_2 = view.findViewById(R.id.bookmark_2);
        video_1 = view.findViewById(R.id.video_1);
        video_2 = view.findViewById(R.id.video_2);


        // getActivity().requestWindowFeature(Window.FEATURE_NO_TITLE);
      /*  MediaController mediaController = new MediaController(getContext());
        mediaController.setAnchorView(video_1);

        Uri video = Uri.parse("android.resource://" + getActivity().getPackageName()+"/"+R.raw.chi);
        video_1.setMediaController(mediaController);

        video_1.setVideoURI(video);

        video_1.requestFocus();

        video_1.start();

*/
        String str1=getVideoFilePath1();
        String str2=getVideoFilePath2();

        MediaController mc = new MediaController(getContext());
        MediaController mc2 = new MediaController(getContext());
        video_1.setMediaController(mc);
        //비디오 경로 설정
        video_1.setVideoURI(Uri.parse(str1));
        ///포커스를 설정
        video_1.requestFocus();
        //비디오 뷰의 재생 준비가 완료되었을 때 수행할 내용
        video_2.setMediaController(mc2);
        //비디오 경로 설정
        video_2.setVideoURI(Uri.parse(str2));
        ///포커스를 설정
        video_2.requestFocus();
        //비디오 뷰의 재생 준비가 완료되었을 때 수행할 내용
        bookmark_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v==bookmark_1) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.setPackage("com.kakao.talk");
                    // intent.putExtra(Intent.EXTRA_SUBJECT, title);
                    //intent.putExtra(Intent.EXTRA_TEXT, content);
                    Intent chooser = Intent.createChooser(intent, "공유");
                    startActivity(chooser);

                }
            }


        });

        bookmark_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v==bookmark_2) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.setPackage("com.kakao.talk");
                    // intent.putExtra(Intent.EXTRA_SUBJECT, title);
                    //intent.putExtra(Intent.EXTRA_TEXT, content);
                    Intent chooser = Intent.createChooser(intent, "공유");
                    startActivity(chooser);
                }
            }
        });
        return view;
    }
    private String getVideoFilePath1() {
        File dir = Environment.getExternalStorageDirectory().getAbsoluteFile();

        String path = dir.getPath() + "/" + DETAIL_PATH + "L_infront (31).mp4";
        return  path;
    }
    private String getVideoFilePath2() {
        File dir = Environment.getExternalStorageDirectory().getAbsoluteFile();

        String path = dir.getPath() + "/" + DETAIL_PATH + "L_infront (20).mp4";
        return  path;
    }
}





