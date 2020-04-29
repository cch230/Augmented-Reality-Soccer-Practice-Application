package com.example.ARSPA;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class bottombar extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private video video;
    private post post;
    private train train;
    private profile profile;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottombar);


        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()) {
                    case R.id.feed:
                        setFrag(0);
                        break;

                    case R.id.play:
                        setFrag(1);
                        break;
                    case R.id.train:
                        setFrag(2);
                        break;
                    case R.id.profile:
                        setFrag(3);
                        break;

                }
                return true;
            }
        });

        video = new video();
        post = new post();
        train = new train();
        profile = new profile();
        setFrag(0);
    }

    private void setFrag(int n) {
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        switch (n) {
            case 0:
                ft.replace(R.id.navi_frame, video);
                ft.commit(); //저장
                break;

            case 1:
                ft.replace(R.id.navi_frame, post);
                ft.commit(); //저장
                break;

            case 2:
                ft.replace(R.id.navi_frame, train);
                ft.commit(); //저장
                break;

            case 3:
                ft.replace(R.id.navi_frame, profile);
                ft.commit(); //저장
                break;



        }

    }

}
