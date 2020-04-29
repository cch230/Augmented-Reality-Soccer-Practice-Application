package com.example.ARSPA;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class train extends Fragment {

    private View view;
    ImageButton physical_btn, shoot_btn, trapping_btn;

    public train(){

    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_train,container,false);
        physical_btn = view.findViewById(R.id.physical_btn);
        shoot_btn = view.findViewById(R.id.shoot_btn);
        trapping_btn = view.findViewById(R.id.trapping_btn);

        physical_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), physical2.class);
                startActivity(intent);
            }
        });

        shoot_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), shoot2.class);
                startActivity(intent);
            }
        });

        trapping_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),trapping2.class);
                startActivity(intent);
            }
        });
        return view;
    }
}
