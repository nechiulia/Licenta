package com.example.teammanagement.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.teammanagement.R;
import com.example.teammanagement.Utils.Activity;
import com.example.teammanagement.Utils.Constants;
import com.example.teammanagement.Utils.NewLocation;
import com.example.teammanagement.activities.LoginActivity;
import com.example.teammanagement.adapters.ExpandableListLocationActivitiesAdapter;
import com.example.teammanagement.database.JDBCController;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LocationProfileFragment extends Fragment {

    private TextView tv_locationName;
    private TextView tv_email;
    private TextView tv_postalCode;
    private TextView tv_address;
    private TextView tv_monday;
    private TextView tv_tuesday;
    private TextView tv_wednesday;
    private TextView tv_thursday;
    private TextView tv_friday;
    private TextView tv_saturday;
    private TextView tv_sunday;
    private TextView tv_program;
    private TextView tv_activities;
    private ImageButton ibtn_logOut;
    private LinearLayout layout_hours;
    private ExpandableListView lv_activities;

    private ExpandableListLocationActivitiesAdapter listAdapter;

    private Intent intent;

    private JDBCController jdbcController;
    private Connection c;

    private int currentUserID;
    private NewLocation currentLocation;
    private HashMap<String, Activity> mapActivity = new HashMap<>();
    private List<String> listParentActivities = new ArrayList<>();
    private List<Activity> listActivities=new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        jdbcController= JDBCController.getInstance();
        c=jdbcController.openConnection();

        View view = inflater.inflate(R.layout.fragment_location_profile,null);
        tv_locationName=view.findViewById(R.id.fragment_location_locationName);
        tv_email=view.findViewById(R.id.fragment_location_email);
        tv_postalCode=view.findViewById(R.id.fragment_location_postalCode);
        tv_address=view.findViewById(R.id.fragment_location_locationAddress);
        tv_monday=view.findViewById(R.id.fragment_location_monday_Hours);
        tv_tuesday=view.findViewById(R.id.fragment_location__tuesday_Hours);
        tv_wednesday =view.findViewById(R.id.fragment_location_wednesday_Hours);
        tv_thursday=view.findViewById(R.id.fragment_location_thursday_Hours);
        tv_friday=view.findViewById(R.id.fragment_location_friday_Hours);
        tv_saturday=view.findViewById(R.id.fragment_location_saturday_Hours);
        tv_sunday=view.findViewById(R.id.fragment_location_sunday_Hours);
        layout_hours=view.findViewById(R.id.fragment_location_container_program);
        tv_program=view.findViewById(R.id.fragment_location_program_hint);
        tv_activities=view.findViewById(R.id.fragment_location_activities_hint);
        lv_activities=view.findViewById(R.id.fragment_location_lv_activities);
        ibtn_logOut=view.findViewById(R.id.fragment_location_ibtn_logout);


        tv_program.setOnClickListener(showProgram());
        tv_activities.setOnClickListener(showActivities());
        ibtn_logOut.setOnClickListener(clickLogOut());

        getCurrentUserID();
        selectLocationInfo();
        selectUserInfo();
        selectActivities();
        selectProgram();

        tv_locationName.setText(currentLocation.getLocationName());
        tv_email.setText(currentLocation.getEmail());
        tv_postalCode.setText(currentLocation.getPostalCode());
        tv_address.setText(currentLocation.getAddress());

        if(listParentActivities.size() != 0 && mapActivity.size() !=0) {
            listAdapter = new ExpandableListLocationActivitiesAdapter(this.getContext(), listParentActivities, mapActivity);
            lv_activities.setAdapter(listAdapter);
        }
        return view;
    }

    public String getLevel(int level){
        if(level ==0)return getString(R.string.user_sport_level_0);
        else if(level == 1)return getString(R.string.user_sport_level_1);
        else if(level == 2)return getString(R.string.user_sport_level_2);
        else if(level == 3)return getString(R.string.user_sport_level_3);
        else if(level == 4)return getString(R.string.user_sport_level_4);
        return getString(R.string.user_sport_level_5);
    }

    public void getKeys(List<Activity> list_Activities){

        for(Activity a: list_Activities){
            listParentActivities.add(a.getActivityName());
        }
    }

    public void getCurrentUserID(){
        currentUserID = (int) getArguments().get(Constants.CURRENT_USER_ID);
    }

    public void selectLocationInfo(){
        try(Statement s = c.createStatement()){
            try(ResultSet r = s.executeQuery("SELECT * FROM LOCATII WHERE IDUTILIZATOR="+currentUserID)){
                if(r.next()){
                    currentLocation = new NewLocation();
                    currentLocation.setLocationID(r.getInt(1));
                    currentLocation.setLocationName(r.getString(2));
                    currentLocation.setPostalCode(r.getString(3));
                    currentLocation.setAddress(r.getString(4));
                    currentLocation.setLatitude(r.getDouble(5));
                    currentLocation.setLongitude(r.getDouble(6));
                    currentLocation.setResevation(r.getByte(7));
                    currentLocation.setState(r.getInt(8));
                    currentLocation.setUserID(r.getInt(9));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void selectProgram(){
        try(Statement s = c.createStatement()){
            for(int i=0; i< 7; i++) {
                try (ResultSet r = s.executeQuery("SELECT INTERVALORAR FROM PROGRAME WHERE IDLOCATIE=" + currentLocation.getLocationID()+" AND ZI="+i)) {
                    if(r.next()) {
                        String interval = r.getString(1);
                        if(interval.equals("--:-----:--")){
                            interval="Închis";
                        }
                        if(i == 0){
                            tv_monday.setText(interval);
                        }
                        else if(i == 1){
                            tv_tuesday.setText(interval);
                        }
                        else if(i == 2){
                            tv_wednesday.setText(interval);
                        }
                        else if(i == 3){
                            tv_thursday.setText(interval);
                        }
                        else if(i == 4){
                            tv_friday.setText(interval);
                        }
                        else if(i == 5){
                            tv_saturday.setText(interval);
                        }
                        else if(i == 6){
                            tv_sunday.setText(interval);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void selectUserInfo(){
        try(Statement s =c.createStatement()){
            try(ResultSet r = s.executeQuery("SELECT EMAIL FROM UTILIZATORI WHERE ID="+currentUserID)){
                if(r.next()){
                    currentLocation.setEmail(r.getString(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void selectActivities(){
        try(Statement s = c.createStatement()){
            try(ResultSet r = s.executeQuery("SELECT * FROM ACTIVITATI WHERE IDLOCATIE="+currentLocation.getLocationID())){
                while(r.next()){
                    Activity activity = new Activity();
                    activity.setActivityID(r.getInt(1));
                    activity.setActivityName(r.getString(2));
                    activity.setSport(r.getString(3));
                    activity.setTrainer(r.getString(4));
                    String difficulty = getLevel(r.getInt(5));
                    activity.setDifficultyLevel(difficulty);
                    activity.setPrice(r.getDouble(6));
                    activity.setReservation(r.getInt(7));
                    activity.setLocationID(currentLocation.getLocationID());
                    listActivities.add(activity);
                    listParentActivities.add(activity.getActivityName());
                    mapActivity.put(activity.getActivityName(),activity);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private View.OnClickListener showProgram() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(layout_hours.getVisibility() == View.GONE) {
                    layout_hours.setVisibility(View.VISIBLE);
                }
                else{
                    layout_hours.setVisibility(View.GONE);
                }
            }
        };
    }
    private View.OnClickListener showActivities() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(lv_activities.getVisibility() == View.GONE) {
                    lv_activities.setVisibility(View.VISIBLE);
                }
                else{
                    lv_activities.setVisibility(View.GONE);
                }
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
