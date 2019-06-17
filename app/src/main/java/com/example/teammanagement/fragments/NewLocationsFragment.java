package com.example.teammanagement.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.example.teammanagement.R;


public class NewLocationsFragment extends Fragment {
    ListView lv_newLocations;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_newlocations,null);
        lv_newLocations = (ListView) view.findViewById(R.id.fragment_new_locations_lv);


        return view;

    }
}
