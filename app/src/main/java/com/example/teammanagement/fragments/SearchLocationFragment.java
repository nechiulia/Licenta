package com.example.teammanagement.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.teammanagement.R;
import com.example.teammanagement.activities.LoginActivity;

public class SearchLocationFragment extends Fragment {

    Intent intent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_search_location,null);


        return view;

    }

    private View.OnClickListener clickLogOut(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        };
    }
}
