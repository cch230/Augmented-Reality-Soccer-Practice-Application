package com.example.arspapp_ui;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class viewpager extends AppCompatActivity
{
    ViewPager vp;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager);

        vp = (ViewPager)findViewById(R.id.vp);
        Button btnvp_ph = (Button)findViewById(R.id.btnvp_ph);
        Button btnvp_sp = (Button)findViewById(R.id.btnvp_sp);
        Button btnvp_sh = (Button)findViewById(R.id.btnvp_sh);
        Button btnvp_tr = (Button)findViewById(R.id.btnvp_tr);
        Button btnvp_pi = (Button)findViewById(R.id.btnvp_pi);

        vp.setAdapter(new pagerAdapter(getSupportFragmentManager()));
        vp.setCurrentItem(0);

        btnvp_ph.setOnClickListener(movePageListener);
        btnvp_ph.setTag(0);
        btnvp_sp.setOnClickListener(movePageListener);
        btnvp_sp.setTag(1);
        btnvp_sh.setOnClickListener(movePageListener);
        btnvp_sh.setTag(2);
        btnvp_tr.setOnClickListener(movePageListener);
        btnvp_tr.setTag(3);
        btnvp_pi.setOnClickListener(movePageListener);
        btnvp_pi.setTag(4);
    }

    View.OnClickListener movePageListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            int tag = (int) v.getTag();
            vp.setCurrentItem(tag);
        }
    };

    private class pagerAdapter extends FragmentStatePagerAdapter
    {
        public pagerAdapter(FragmentManager fm)
        {
            super(fm);
        }
        @Override
        public Fragment getItem(int position)
        {
            switch(position)
            {
                case 0:
                    return new linechart_ph();
                case 1:
                    return new linechart_sp();
                case 2:
                    return new linechart_sh();
                case 3:
                    return new linechart_tr();
                case 4:
                    return new linechart_pi();
                default:
                    return null;
            }
        }
        @Override
        public int getCount()
        {
            return 5;
        }
    }
}
