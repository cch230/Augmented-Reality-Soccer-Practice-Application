package com.example.ARSPA;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.VideoView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class video extends Fragment {

    private View view;
    ImageButton bookmark_1,bookmark_2;
   // VideoView video_1;
    @Nullable

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_video,container,false);
        bookmark_1 = view.findViewById(R.id.bookmark_1);
        bookmark_2 = view.findViewById(R.id.bookmark_2);
        //video_1 = view.findViewById(R.id.video_1);


       // getActivity().requestWindowFeature(Window.FEATURE_NO_TITLE);
      /*  MediaController mediaController = new MediaController(getContext());
        mediaController.setAnchorView(video_1);

        Uri video = Uri.parse("android.resource://" + getActivity().getPackageName()+"/"+R.raw.chi);
        video_1.setMediaController(mediaController);

        video_1.setVideoURI(video);

        video_1.requestFocus();

        video_1.start();

*/

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
}





