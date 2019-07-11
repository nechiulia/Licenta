package com.example.teammanagement.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.teammanagement.R;
import com.example.teammanagement.Utils.Constants;
import com.example.teammanagement.Utils.NewLocation;
import com.example.teammanagement.activities.LoginActivity;
import com.example.teammanagement.adapters.SpinnerAdapter;
import com.example.teammanagement.database.JDBCController;
import com.example.teammanagement.dialogs.ReservationDialog;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class LocationsReservationsFragment extends Fragment {

    private ImageButton ibtn_logOut;
    private CalendarView calendarView;
    private Spinner spn_activity;
    private TextView tv_noActivitiesReservation;

    private Intent intent;

    private SpinnerAdapter adapter;

    private JDBCController jdbcController;
    private Connection c;

    private int currentUserID;
    private int currentLocationID;
    private List<String> spn_activities_items = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_locations_reservations,null);

        jdbcController= JDBCController.getInstance();
        c=jdbcController.openConnection();

        calendarView=view.findViewById(R.id.fragment_locations_reservations_calendarView);
        ibtn_logOut=view.findViewById(R.id.fragment_locations_reservations_ibtn_logout);
        spn_activity=view.findViewById(R.id.fragment_locations_reservations_spn_activity);
        tv_noActivitiesReservation=view.findViewById(R.id.fragment_locations_reservations_tv_noReservationActivities);

        ibtn_logOut.setOnClickListener(clickLogOut());


        getCurrentUserID();
        getLocationID();
        spn_activities_items.add(getString(R.string.fragment_locations_reservations_spn_item0_hint));
        selectReservationActivities();

        if(spn_activities_items.size() == 1){
            spn_activity.setVisibility(View.GONE);
            calendarView.setVisibility(View.GONE);
            tv_noActivitiesReservation.setVisibility(View.VISIBLE);

        }

        adapter= new SpinnerAdapter(getContext(),R.layout.spinner_item,spn_activities_items,inflater);
        spn_activity.setAdapter(adapter);
        spn_activity.setOnItemSelectedListener(isSelectedActivity());

        return view;
    }

    public void getCurrentUserID(){
        currentUserID = (int) getArguments().get(Constants.CURRENT_USER_ID);
    }

    private void openDialog(){
        Bundle args = new Bundle();
        args.putSerializable(Constants.CURRENT_LOCATION_ID, currentLocationID);

        ReservationDialog reservationDialog = new ReservationDialog();
        reservationDialog.show(getActivity().getSupportFragmentManager(),getString(R.string.register2_addSport_hint));

        reservationDialog.setArguments(args);
        reservationDialog.setCancelable(false);
    }

    public void getLocationID(){
        try(Statement s = c.createStatement()){
            try(ResultSet r = s.executeQuery("SELECT  ID FROM LOCATII WHERE IDUTILIZATOR="+currentUserID)){
                if(r.next()){
                    currentLocationID=r.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void selectReservationActivities(){
        try(Statement s = c.createStatement()){
            try(ResultSet r = s.executeQuery("SELECT DENUMIRE FROM ACTIVITATI WHERE IDLOCATIE="+currentLocationID+" AND REZERVARI=1")) {
                while (r.next()){
                    spn_activities_items.add(r.getString(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public AdapterView.OnItemSelectedListener isSelectedActivity(){
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0){
                    calendarView.setOnClickListener(clickDate());
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    }

    private View.OnClickListener clickDate() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        };
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
