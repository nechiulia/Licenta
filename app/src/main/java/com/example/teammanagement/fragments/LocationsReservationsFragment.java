package com.example.teammanagement.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageButton;

import com.example.teammanagement.R;
import com.example.teammanagement.activities.LoginActivity;
import com.example.teammanagement.activities.TeamsActivity;

public class LocationsReservationsFragment extends Fragment {

    private ImageButton ibtn_logOut;

    private Intent intent;

    CalendarView calendarView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_locations_reservations,null);
        calendarView=view.findViewById(R.id.fragment_locations_reservations_calendarView);
        ibtn_logOut=view.findViewById(R.id.fragment_locations_reservations_ibtn_logout);
        ibtn_logOut.setOnClickListener(clickLogOut());

        return view;
    }
    private View.OnClickListener clickLogOut() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        };
    }


}
