package com.example.teammanagement.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.teammanagement.R;
import com.example.teammanagement.Utils.Activity;
import com.example.teammanagement.Utils.Report;
import com.example.teammanagement.adapters.ExpandableListLocationActivitiesAdapter;
import com.example.teammanagement.adapters.ExpandableListReportAdminAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class LocationProfileFragment extends Fragment {

    TextView tv_locationName;
    TextView tv_email;
    TextView tv_postalCode;
    TextView tv_address;
    TextView tv_monday;
    TextView tv_tuesday;
    TextView tv_wednsdey;
    TextView tv_thursday;
    TextView tv_friday;
    TextView tv_saturday;
    TextView tv_sunday;
    TextView tv_program;
    TextView tv_activities;
    LinearLayout layout_hours;
    ExpandableListView lv_activities;
    private HashMap<String, Activity> mapActivity = new HashMap<>();
    private List<String> listParentActivities = new ArrayList<>();
    private List<Activity> listActivities=new ArrayList<>();
    ExpandableListLocationActivitiesAdapter listAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_location_profile,null);
        tv_locationName=view.findViewById(R.id.fragment_location_locationName);
        tv_locationName=view.findViewById(R.id.fragment_location_email);
        tv_locationName=view.findViewById(R.id.fragment_location_postalCode);
        tv_locationName=view.findViewById(R.id.fragment_location_locationAddress);
        tv_locationName=view.findViewById(R.id.fragment_location_monday_Hours);
        tv_locationName=view.findViewById(R.id.fragment_location__tuesday_Hours);
        tv_locationName=view.findViewById(R.id.fragment_location_wednesday_Hours);
        tv_locationName=view.findViewById(R.id.fragment_location_thursday_Hours);
        tv_locationName=view.findViewById(R.id.fragment_location_friday_Hours);
        tv_locationName=view.findViewById(R.id.fragment_location_saturday_Hours);
        tv_locationName=view.findViewById(R.id.fragment_location_sunday_Hours);
        layout_hours=view.findViewById(R.id.fragment_location_container_program);
        tv_program=view.findViewById(R.id.fragment_location_program_hint);
        tv_activities=view.findViewById(R.id.fragment_location_activities_hint);
        lv_activities=view.findViewById(R.id.fragment_location_lv_activities);


        tv_program.setOnClickListener(showProgram());
        tv_activities.setOnClickListener(showActivities());
        layout_hours.setVisibility(View.GONE);
        lv_activities.setVisibility(View.GONE);

        initData();
        if(listParentActivities.size() != 0 && mapActivity.size() !=0) {
            listAdapter = new ExpandableListLocationActivitiesAdapter(this.getContext(), listParentActivities, mapActivity);
            lv_activities.setAdapter(listAdapter);
        }
        return view;
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

    public void getKeys(List<Activity> list_Activities){

        for(Activity a: list_Activities){
            listParentActivities.add(a.getActivityName());
        }
    }

    public void initData(){
        listActivities.add(new Activity("Tae Bo","Mirela Dan","Fitness",false,1,25.00));
        listActivities.add(new Activity("Bachatta","Mirela Dan","Fitness",false,1,25.00));
        listActivities.add(new Activity("Kango Jumps","Mirela Dan","Fitness",false,1,25.00));
        listActivities.add(new Activity("Taeds Bo","Mirela Dan","Fitness",false,1,25.00));
        listActivities.add(new Activity("Taesdassdda Bo","Mirela Dan","Fitness",false,1,25.00));
        listActivities.add(new Activity("Taedasfa Bo","Mirela Dan","Fitness",false,1,25.00));
        listActivities.add(new Activity("Taea Bo","Mirela Dan","Fitness",false,1,25.00));
        listActivities.add(new Activity("Taeasa Bo","Mirela Dan","Fitness",false,1,25.00));
        listActivities.add(new Activity("Taeb Bo","Mirela Dan","Fitness",false,1,25.00));
        listActivities.add(new Activity("Taec Bo","Mirela Dan","Fitness",false,1,25.00));
        listActivities.add(new Activity("Tae gBo","Mirela Dan","Fitness",false,1,25.00));
        listActivities.add(new Activity("Taeh Bo","Mirela Dan","Fitness",false,1,25.00));
        listActivities.add(new Activity("Tae lBo","Mirela Dan","Fitness",false,1,25.00));
        listActivities.add(new Activity("Tae jhBo","Mirela Dan","Fitness",false,1,25.00));

        getKeys(listActivities);

        mapActivity.put(listParentActivities.get(0), listActivities.get(0));
        mapActivity.put(listParentActivities.get(1), listActivities.get(1));
        mapActivity.put(listParentActivities.get(2), listActivities.get(2));
        mapActivity.put(listParentActivities.get(3), listActivities.get(3));
        mapActivity.put(listParentActivities.get(4), listActivities.get(4));
        mapActivity.put(listParentActivities.get(5), listActivities.get(5));
        mapActivity.put(listParentActivities.get(6), listActivities.get(6));
        mapActivity.put(listParentActivities.get(7), listActivities.get(7));
        mapActivity.put(listParentActivities.get(8), listActivities.get(8));
        mapActivity.put(listParentActivities.get(9), listActivities.get(9));
        mapActivity.put(listParentActivities.get(10), listActivities.get(10));
        mapActivity.put(listParentActivities.get(11), listActivities.get(11));
        mapActivity.put(listParentActivities.get(12), listActivities.get(12));
        mapActivity.put(listParentActivities.get(13), listActivities.get(13));


    }
}
