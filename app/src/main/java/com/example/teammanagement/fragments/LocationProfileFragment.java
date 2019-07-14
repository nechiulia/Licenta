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
import android.widget.ListView;
import android.widget.TextView;
import com.example.teammanagement.R;
import com.example.teammanagement.Utils.Activity;
import com.example.teammanagement.Utils.Constants;
import com.example.teammanagement.Utils.NewLocation;
import com.example.teammanagement.Utils.Program;
import com.example.teammanagement.activities.LoginActivity;
import com.example.teammanagement.adapters.ExpandableListLocationActivitiesAdapter;
import com.example.teammanagement.adapters.ProgramAdapter;
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
    private TextView tv_program;
    private TextView tv_activities;
    private ImageButton ibtn_logOut;
    private ExpandableListView lv_activities;
    private ListView lv_program;

    private ExpandableListLocationActivitiesAdapter listAdapter;
    private ProgramAdapter adapter;

    private Intent intent;

    private JDBCController jdbcController;
    private Connection c;


    private int currentUserID;
    private int clickedActivityID;
    private NewLocation currentLocation;
    private HashMap<String, Activity> mapActivity = new HashMap<>();
    private List<String> listParentActivities = new ArrayList<>();
    private List<Activity> listActivities=new ArrayList<>();
    private List<Program> listProgram = new ArrayList<>();
    private List<Program> listOrar = new ArrayList<>();
    private List<Integer> listActivitiesID = new ArrayList<>();

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
        tv_program=view.findViewById(R.id.fragment_location_program_hint);
        tv_activities=view.findViewById(R.id.fragment_location_activities_hint);
        lv_activities=view.findViewById(R.id.fragment_location_lv_activities);
        ibtn_logOut=view.findViewById(R.id.fragment_location_ibtn_logout);
        lv_program = view.findViewById(R.id.fragment_location_lv_program);


        tv_program.setOnClickListener(showProgram());
        tv_activities.setOnClickListener(showActivities());
        ibtn_logOut.setOnClickListener(clickLogOut());

        getCurrentUserID();
        selectLocationInfo();
        selectUserInfo();
        selectActivities();
        selectProgram();
        /*selectOrar();*/

        tv_locationName.setText(currentLocation.getLocationName());
        tv_email.setText(currentLocation.getEmail());
        tv_postalCode.setText(currentLocation.getPostalCode());
        tv_address.setText(currentLocation.getAddress());

        adapter = new ProgramAdapter(this.getContext(),R.layout.list_item_program_location_marker_dialog,listProgram,inflater);
        lv_program.setAdapter(adapter);

        listAdapter = new ExpandableListLocationActivitiesAdapter(this.getContext(), listParentActivities, mapActivity, listActivitiesID,currentLocation);
        lv_activities.setAdapter(listAdapter);


        if (adapter != null) {
            int totalHeight = 0;
            for (int i = 0; i < adapter.getCount(); i++) {
                View listItem = adapter.getView(i, null, lv_activities);
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }
            totalHeight+=225;
            ViewGroup.LayoutParams params = lv_activities.getLayoutParams();
            params.height = totalHeight + (lv_activities.getDividerHeight() * (adapter.getCount() - 1));
            lv_activities.setLayoutParams(params);
            lv_activities.requestLayout();
        }

        return view;
    }

    public String getLevel(int level){
        if(level ==0)return getString(R.string.user_sport_level_0);
        else if(level == 1)return getString(R.string.user_sport_level_1);
        else if(level == 2)return getString(R.string.user_sport_level_2);
        else if(level == 3)return getString(R.string.user_sport_level_3);
        else if(level == 4)return getString(R.string.user_sport_level_4);
        else if(level == 5) getString(R.string.user_sport_level_5);
        return "-";
    }

    public void getCurrentUserID(){
        currentUserID = (int) getArguments().get(Constants.CURRENT_USER_ID);
    }

    public String getDayString(int day){
        if(day == 0)return "L:";
        else if(day==1)return "M:";
        else if(day==2)return "M:";
        else if(day==3)return "J:";
        else if(day==4)return "V:";
        else if(day==5)return "S:";
        else return "D:";
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
                    currentLocation.setReservation(r.getByte(7));
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
            try (ResultSet r = s.executeQuery("SELECT ZI,INTERVALORAR FROM PROGRAME WHERE IDLOCATIE=" + currentLocation.getLocationID())) {
                while(r.next()) {
                    Program program = new Program();
                    int day = r.getInt(1);
                    String interval = r.getString(2);
                    if(interval.equals("--:-----:--")){
                        interval="Închis";
                    }
                    program.setDay(getDayString(day));
                    program.setIntervalHours(interval);
                    listProgram.add(program);
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
                    listActivitiesID.add(activity.getActivityID());
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

                if (lv_program.getVisibility() == View.GONE) {
                    lv_program.setVisibility(View.VISIBLE);
                    if (adapter != null) {
                        int totalHeight = 0;
                        for (int i = 0; i < adapter.getCount(); i++) {
                            View listItem = adapter.getView(i, null, lv_activities);
                            listItem.measure(0, 0);
                            totalHeight += listItem.getMeasuredHeight();
                        }
                        totalHeight+=235;
                        ViewGroup.LayoutParams params = lv_activities.getLayoutParams();
                        params.height = totalHeight + (lv_activities.getDividerHeight() * (adapter.getCount() - 1));
                        lv_activities.setLayoutParams(params);
                        lv_activities.requestLayout();
                    }
                } else {
                    lv_program.setVisibility(View.GONE);
                    if (adapter != null) {
                        int totalHeight = 0;
                        for (int i = 0; i < adapter.getCount(); i++) {
                            View listItem = adapter.getView(i, null, lv_activities);
                            listItem.measure(0, 0);
                            totalHeight += listItem.getMeasuredHeight();
                        }
                        totalHeight+=690;
                        ViewGroup.LayoutParams params = lv_activities.getLayoutParams();
                        params.height = totalHeight + (lv_activities.getDividerHeight() * (adapter.getCount() - 1));
                        lv_activities.setLayoutParams(params);
                        lv_activities.requestLayout();
                    }
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
