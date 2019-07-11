package com.example.teammanagement.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.teammanagement.R;
import com.example.teammanagement.Utils.Activity;
import com.example.teammanagement.Utils.Announcement;
import com.example.teammanagement.Utils.Constants;
import com.example.teammanagement.Utils.NewLocation;
import com.example.teammanagement.Utils.Teammate;
import com.example.teammanagement.activities.AddTeam2Activity;
import com.example.teammanagement.activities.LoginActivity;
import com.example.teammanagement.adapters.ActivitiesAdapter;
import com.example.teammanagement.adapters.ExpandableListLocationActivitiesAdapter;
import com.example.teammanagement.database.JDBCController;
import com.example.teammanagement.dialogs.AddActivityDialog;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditLocationFragment extends Fragment implements AddActivityDialog.AddActivityListener {

    private ImageButton ibtn_logOut;
    private TextView tv_program;
    private LinearLayout linearLayout;
    private TextView tv_monday;
    private TextView tv_tuesday;
    private TextView tv_wednsdey;
    private TextView tv_thursday;
    private TextView tv_friday;
    private TextView tv_saturday;
    private TextView tv_sunday;
    private ImageButton ibtn_addActivity;
    private ImageButton ibtn_removeActivity;
    private ListView lv_activities;
    private Button btn_save;
    private EditText et_monday_open_hour;
    private EditText et_tuesday_open_hour;
    private EditText et_wednesday_open_hour;
    private EditText et_thursday_open_hour;
    private EditText et_friday_open_hour;
    private EditText et_saturday_open_hour;
    private EditText et_sunday_open_hour;
    private EditText et_monday_close_hour;
    private EditText et_tuesday_close_hour;
    private EditText et_wednesday_close_hour;
    private EditText et_thursday_close_hour;
    private EditText et_friday_close_hour;
    private EditText et_saturday_close_hour;
    private EditText et_sunday_close_hour;
    private EditText et_monday_open_minute;
    private EditText et_tuesday_open_minute;
    private EditText et_wednesday_open_minute;
    private EditText et_thursday_open_minute;
    private EditText et_friday_open_minute;
    private EditText et_saturday_open_minute;
    private EditText et_sunday_open_minute;
    private EditText et_monday_close_minute;
    private EditText et_tuesday_close_minute;
    private EditText et_wednesday_close_minute;
    private EditText et_thursday_close_minute;
    private EditText et_friday_close_minute;
    private EditText et_saturday_close_minute;
    private EditText et_sunday_close_minute;

    private Intent intent;

    private ActivitiesAdapter adapter;


    private JDBCController jdbcController;
    private Connection c;

    private int currentUserID;
    private int clickedActivityID=-1;
    private NewLocation currentLocation;
    private Map<Integer,String> mapProgram = new HashMap<>();
    private List<String> listParentActivities = new ArrayList<>();
    private List<Activity> listActivities=new ArrayList<>();
    private List<Activity> checkedActivities = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_location,null);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);


        jdbcController= JDBCController.getInstance();
        c=jdbcController.openConnection();

        ibtn_logOut=view.findViewById(R.id.fragment_edit_location_ibtn_logout);
        ibtn_addActivity = view.findViewById(R.id.fragment_edit_location_ibtn_addActivity);
        ibtn_removeActivity=view.findViewById(R.id.fragment_edit_location_ibtn_removeActivity);
        btn_save = view.findViewById(R.id.fragment_edit_location_btn_save);
        tv_program=view.findViewById(R.id.fragment_edit_location_tv_program_hint);
        lv_activities=view.findViewById(R.id.fragment_edit_location_lv_activities);

        linearLayout= view.findViewById(R.id.fragment_edit_location_container_program);

        et_monday_open_hour =view.findViewById(R.id.fragment_edit_location_monday_open_hours);
        et_tuesday_open_hour =view.findViewById(R.id.fragment_edit_location_tuesday_open_hours);
        et_wednesday_open_hour =view.findViewById(R.id.fragment_edit_location_wednesday_open_hours);
        et_thursday_open_hour =view.findViewById(R.id.fragment_edit_location_thursday_open_hours);
        et_friday_open_hour =view.findViewById(R.id.fragment_edit_location_friday_open_hours);
        et_saturday_open_hour =view.findViewById(R.id.fragment_edit_location_saturday_open_hours);
        et_sunday_open_hour =view.findViewById(R.id.fragment_edit_location_sunday_open_hours);

        et_monday_close_hour =view.findViewById(R.id.fragment_edit_location_monday_close_hours);
        et_tuesday_close_hour =view.findViewById(R.id.fragment_edit_location_tuesday_close_hours);
        et_wednesday_close_hour =view.findViewById(R.id.fragment_edit_location_wednesday_close_hours);
        et_thursday_close_hour =view.findViewById(R.id.fragment_edit_location_thursday_close_hours);
        et_friday_close_hour =view.findViewById(R.id.fragment_edit_location_friday_close_hours);
        et_saturday_close_hour =view.findViewById(R.id.fragment_edit_location_saturday_close_hours);
        et_sunday_close_hour =view.findViewById(R.id.fragment_edit_location_sunday_close_hours);

        et_monday_open_minute =view.findViewById(R.id.fragment_edit_location_monday_open_minutes);
        et_tuesday_open_minute =view.findViewById(R.id.fragment_edit_location_tuesday_open_minutes);
        et_wednesday_open_minute =view.findViewById(R.id.fragment_edit_location_wednesday_open_minutes);
        et_thursday_open_minute =view.findViewById(R.id.fragment_edit_location_thursday_open_minutes);
        et_friday_open_minute =view.findViewById(R.id.fragment_edit_location_friday_open_minutes);
        et_saturday_open_minute =view.findViewById(R.id.fragment_edit_location_saturday_open_minutes);
        et_sunday_open_minute =view.findViewById(R.id.fragment_edit_location_sunday_open_minutes);

        et_monday_close_minute =view.findViewById(R.id.fragment_edit_location_monday_close_minutes);
        et_tuesday_close_minute =view.findViewById(R.id.fragment_edit_location_tuesday_close_minutes);
        et_wednesday_close_minute =view.findViewById(R.id.fragment_edit_location_wednesday_close_minutes);
        et_thursday_close_minute =view.findViewById(R.id.fragment_edit_location_thursday_close_minutes);
        et_friday_close_minute =view.findViewById(R.id.fragment_edit_location_friday_close_minutes);
        et_saturday_close_minute =view.findViewById(R.id.fragment_edit_location_saturday_close_minutes);
        et_sunday_close_minute =view.findViewById(R.id.fragment_edit_location_sunday_close_minutes);

        ibtn_logOut.setOnClickListener(clickLogOut());
        ibtn_addActivity.setOnClickListener(clickAddActivity());
        ibtn_removeActivity.setOnClickListener(clickRemoveActivity());
        btn_save.setOnClickListener(clickSave());

        getCurrentUserID();
        selectLocationInfo();
        initializeMap();
        selectProgram();
        selectActivities();

        adapter = new ActivitiesAdapter(getActivity().getApplicationContext(),R.layout.list_item_edit_location_activities, listActivities, getLayoutInflater());
        lv_activities.setAdapter(adapter);
        lv_activities.setOnItemClickListener(clickActivity());


        return view;
    }

    public void initializeMap(){
        mapProgram.put(0,"");
        mapProgram.put(1,"");
        mapProgram.put(2,"");
        mapProgram.put(3,"");
        mapProgram.put(4,"");
        mapProgram.put(5,"");
        mapProgram.put(6,"");
    }

    public void getProgram(){

        mapProgram.put(0, et_monday_open_hour.getText().toString().trim() +":"+ et_monday_open_minute.getText().toString().trim() +"-"+ et_monday_close_hour.getText().toString().trim() +":"+ et_monday_close_minute.getText().toString().trim());
        mapProgram.put(1, et_tuesday_open_hour.getText().toString().trim() +":"+ et_tuesday_open_minute.getText().toString().trim() +"-"+ et_tuesday_close_hour.getText().toString().trim() +":"+ et_tuesday_close_minute.getText().toString().trim());
        mapProgram.put(2, et_wednesday_open_hour.getText().toString().trim() +":"+ et_wednesday_open_minute.getText().toString().trim() +"-"+ et_wednesday_close_hour.getText().toString().trim() +":"+ et_wednesday_close_minute.getText().toString().trim());
        mapProgram.put(3, et_thursday_open_hour.getText().toString().trim() +":"+ et_thursday_open_minute.getText().toString().trim() +"-"+ et_thursday_close_hour.getText().toString().trim() +":"+ et_thursday_close_minute.getText().toString().trim());
        mapProgram.put(4, et_friday_open_hour.getText().toString().trim() +":"+ et_friday_open_minute.getText().toString().trim() +"-"+ et_friday_close_hour.getText().toString().trim() +":"+ et_friday_close_minute.getText().toString().trim());
        mapProgram.put(5, et_saturday_open_hour.getText().toString().trim() +":"+ et_saturday_open_minute.getText().toString().trim() +"-"+ et_saturday_close_hour.getText().toString().trim() +":"+ et_saturday_close_minute.getText().toString().trim());
        mapProgram.put(6, et_sunday_open_hour.getText().toString().trim() +":"+ et_sunday_open_minute.getText().toString().trim() +"-"+ et_sunday_close_hour.getText().toString().trim() +":"+ et_sunday_close_minute.getText().toString().trim());

        for(int i=0; i< mapProgram.size();i++) {
            if(mapProgram.get(i).equals(getString(R.string.closed_location_bd))){
                mapProgram.put(i,getString(R.string.location_closed));
            }
        }
    }

    private void openDialog(){
        Bundle args = new Bundle();
        args.putSerializable(Constants.CURRENT_LOCATION_ID, currentLocation.getLocationID());
        args.putSerializable(Constants.CLICKED_ACTIVITY_ID, clickedActivityID);

        AddActivityDialog addActivityDialog = new AddActivityDialog();
        addActivityDialog.show(getActivity().getSupportFragmentManager(),getString(R.string.register2_addSport_hint));

        addActivityDialog.setArguments(args);
        addActivityDialog.setTargetFragment(this,0);
        addActivityDialog.setCancelable(false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        selectActivities();
        adapter.notifyDataSetChanged();
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
            for(int i=0; i< mapProgram.size(); i++) {
                try (ResultSet r = s.executeQuery("SELECT INTERVALORAR FROM PROGRAME WHERE IDLOCATIE=" + currentLocation.getLocationID()+" AND ZI="+i)) {
                    if(r.next()) {
                        String interval = r.getString(1);
                        String[] openClose= interval.trim().split("");
                        String open_hour=openClose[1]+openClose[2];
                        String open_minute=openClose[4]+openClose[5];
                        String close_hour = openClose[7]+openClose[8];
                        String close_minute=openClose[10]+openClose[11];
                        if(open_hour.equals("--")){
                            open_hour="";
                            open_minute="";
                            close_hour="";
                            close_minute="";
                        }
                        else if(open_minute.equals("--")){
                            open_hour="";
                            open_minute="";
                            close_hour="";
                            close_minute="";
                        }
                        else if(close_hour.equals("--")){
                            open_hour="";
                            open_minute="";
                            close_hour="";
                            close_minute="";
                        }
                        else if(close_minute.equals("--")){
                            open_hour="";
                            open_minute="";
                            close_hour="";
                            close_minute="";
                        }
                        if(i == 0){
                            et_monday_open_hour.setText(open_hour);
                            et_monday_open_minute.setText(open_minute);
                            et_monday_close_hour.setText(close_hour);
                            et_monday_close_minute.setText(close_minute);
                        }
                        else if(i == 1){
                            et_tuesday_open_hour.setText(open_hour);
                            et_tuesday_open_minute.setText(open_minute);
                            et_tuesday_close_hour.setText(close_hour);
                            et_tuesday_close_minute.setText(close_minute);
                        }
                        else if(i == 2){
                            et_wednesday_open_hour.setText(open_hour);
                            et_wednesday_open_minute.setText(open_minute);
                            et_wednesday_close_hour.setText(close_hour);
                            et_wednesday_close_minute.setText(close_minute);
                        }
                        else if(i == 3){
                            et_thursday_open_hour.setText(open_hour);
                            et_thursday_open_minute.setText(open_minute);
                            et_thursday_close_hour.setText(close_hour);
                            et_thursday_close_minute.setText(close_minute);
                        }
                        else if(i == 4){
                            et_friday_open_hour.setText(open_hour);
                            et_friday_open_minute.setText(open_minute);
                            et_friday_close_hour.setText(close_hour);
                            et_friday_close_minute.setText(close_minute);
                        }
                        else if(i == 5){
                            et_saturday_open_hour.setText(open_hour);
                            et_saturday_open_minute.setText(open_minute);
                            et_saturday_close_hour.setText(close_hour);
                            et_saturday_close_minute.setText(close_minute);
                        }
                        else if(i == 6){
                            et_sunday_open_hour.setText(open_hour);
                            et_sunday_open_minute.setText(open_minute);
                            et_sunday_close_hour.setText(close_hour);
                            et_sunday_close_minute.setText(close_minute);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertProgram(){
        try(Statement s = c.createStatement()){
            for(int i=0; i< mapProgram.size();i++) {
                s.executeUpdate("INSERT INTO PROGRAME VALUES("+i+",'"+mapProgram.get(i)+"',"+currentLocation.getLocationID()+")");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateProgram(){
        try(Statement s = c.createStatement()){
            for(int i=0; i< mapProgram.size();i++) {
                s.executeUpdate("UPDATE PROGRAME SET INTERVALORAR='"+mapProgram.get(i)+"' WHERE IDLOCATIE="+currentLocation.getLocationID()+" AND ZI="+i);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean verifyExistance(){
        try(Statement s = c.createStatement()){
            try(ResultSet r = s.executeQuery("SELECT * FROM PROGRAME WHERE IDLOCATIE="+currentLocation.getLocationID())){
                if(r.next()){
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
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
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteOrar(Activity activity){
        try(Statement s = c.createStatement()){
            s.executeUpdate("DELETE FROM ORAR WHERE IDACTIVITATE="+activity.getActivityID());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteActivity(Activity activity){
        try(Statement s = c.createStatement()){
            s.executeUpdate("DELETE FROM ACTIVITATI WHERE ID="+activity.getActivityID());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getCurrentUserID(){
        currentUserID = (int) getArguments().get(Constants.CURRENT_USER_ID);
    }

    public String getLevel(int level){
        if(level ==0)return getString(R.string.user_sport_level_0);
        else if(level == 1)return getString(R.string.user_sport_level_1);
        else if(level == 2)return getString(R.string.user_sport_level_2);
        else if(level == 3)return getString(R.string.user_sport_level_3);
        else if(level == 4)return getString(R.string.user_sport_level_4);
        return getString(R.string.user_sport_level_5);
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
    private View.OnClickListener clickAddActivity() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedActivityID=-1;
                openDialog();
            }
        };
    }
    private View.OnClickListener clickRemoveActivity() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkedActivities=adapter.getCheckedActivities();
                for(Activity a : checkedActivities){
                    deleteOrar(a);
                    deleteActivity(a);
                }
                listActivities.removeAll(checkedActivities);
                checkedActivities.clear();
                adapter.notifyDataSetChanged();
            }
        };
    }
    private View.OnClickListener clickSave() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValid() && isValidMonday() && isValidTuesday() && isValidWednesday() && isValidThursday() && isValidFriday() && isValidSaturday() && isValidSunday()) {
                    getProgram();
                    if(verifyExistance()){
                        updateProgram();
                    }else{
                        insertProgram();
                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(getString(R.string.edit_location_fragment_alertDialog_save_title))
                            .setMessage(getString(R.string.edit_location_fragment_alertDialog_save_message))
                            .setCancelable(false)
                            .setPositiveButton(getString(R.string.edit_location_fragment_alertDialog_save_btn), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        };
    }

    private AdapterView.OnItemClickListener clickActivity(){
        return new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                clickedActivityID=listActivities.get(position).getActivityID();
                openDialog();


            }
        };
    }

    public boolean isValid(){
        if(et_monday_open_hour.getText().toString().trim().length() !=2 && et_monday_open_hour.getText().toString().trim().length() !=0){
            et_monday_open_hour.setError(getString(R.string.hour_error));
            return false;
        }
        if(et_monday_open_minute.getText().toString().trim().length()!=2 && et_monday_open_minute.getText().toString().trim().length() !=0) {
            et_monday_open_minute.setError(getString(R.string.minutes_error));
            return false;
        }
        if(et_monday_close_hour.getText().toString().trim().length() != 2 && et_monday_close_hour.getText().toString().trim().length() !=0){
            et_monday_close_hour.setError(getString(R.string.hour_error));
            return false;
        }
        if(et_monday_close_minute.getText().toString().trim().length()!=2 && et_monday_close_hour.getText().toString().trim().length() !=0) {
            et_monday_close_minute.setError(getString(R.string.minutes_error));
            return false;
        }

        if(et_tuesday_open_hour.getText().toString().trim().length() !=2 && et_tuesday_open_hour.getText().toString().trim().length() !=0){
            et_tuesday_open_hour.setError(getString(R.string.hour_error));
            return false;
        }
        if(et_tuesday_open_minute.getText().toString().trim().length()!=2 && et_tuesday_open_minute.getText().toString().trim().length()!=0) {
            et_tuesday_open_minute.setError(getString(R.string.minutes_error));
            return false;
        }
        if(et_tuesday_close_hour.getText().toString().trim().length() != 2 && et_tuesday_close_hour.getText().toString().trim().length() != 0){
            et_tuesday_close_hour.setError(getString(R.string.hour_error));
            return false;
        }
        if(et_tuesday_close_minute.getText().toString().trim().length()!=2 && et_tuesday_close_minute.getText().toString().trim().length()!=0) {
            et_tuesday_close_minute.setError(getString(R.string.minutes_error));
            return false;
        }

        if(et_wednesday_open_hour.getText().toString().trim().length() !=2 && et_wednesday_open_hour.getText().toString().trim().length() !=0){
            et_wednesday_open_hour.setError(getString(R.string.hour_error));
            return false;
        }
        if(et_wednesday_open_minute.getText().toString().trim().length()!=2 && et_wednesday_open_minute.getText().toString().trim().length()!=0) {
            et_wednesday_open_minute.setError(getString(R.string.minutes_error));
            return false;
        }
        if(et_wednesday_close_hour.getText().toString().trim().length() != 2 && et_wednesday_close_hour.getText().toString().trim().length() != 0){
            et_wednesday_close_hour.setError(getString(R.string.hour_error));
            return false;
        }
        if(et_wednesday_close_minute.getText().toString().trim().length()!=2 && et_wednesday_close_minute.getText().toString().trim().length()!=0) {
            et_wednesday_close_minute.setError(getString(R.string.minutes_error));
            return false;
        }

        if(et_thursday_open_hour.getText().toString().trim().length() !=2 && et_thursday_open_hour.getText().toString().trim().length() !=0){
            et_thursday_open_hour.setError(getString(R.string.hour_error));
            return false;
        }
        if(et_thursday_open_minute.getText().toString().trim().length()!=2 && et_thursday_open_minute.getText().toString().trim().length()!=0) {
            et_thursday_open_minute.setError(getString(R.string.minutes_error));
            return false;
        }
        if(et_thursday_close_hour.getText().toString().trim().length() != 2 && et_thursday_close_hour.getText().toString().trim().length() != 0){
            et_thursday_close_hour.setError(getString(R.string.hour_error));
            return false;
        }
        if(et_thursday_close_minute.getText().toString().trim().length()!=2 && et_thursday_close_minute.getText().toString().trim().length()!=0) {
            et_thursday_close_minute.setError(getString(R.string.minutes_error));
            return false;
        }

        if(et_friday_open_hour.getText().toString().trim().length() !=2 && et_friday_open_hour.getText().toString().trim().length() !=0){
            et_friday_open_hour.setError(getString(R.string.hour_error));
            return false;
        }
        if(et_friday_open_minute.getText().toString().trim().length()!=2 && et_friday_open_minute.getText().toString().trim().length()!=0) {
            et_friday_open_minute.setError(getString(R.string.minutes_error));
            return false;
        }
        if(et_friday_close_hour.getText().toString().trim().length() != 2 && et_friday_close_hour.getText().toString().trim().length() != 0){
            et_friday_close_hour.setError(getString(R.string.hour_error));
            return false;
        }
        if(et_friday_close_minute.getText().toString().trim().length()!=2 && et_friday_close_minute.getText().toString().trim().length()!=0) {
            et_friday_close_minute.setError(getString(R.string.minutes_error));
            return false;
        }

        if(et_saturday_open_hour.getText().toString().trim().length() !=2 && et_saturday_open_hour.getText().toString().trim().length() !=0){
            et_saturday_open_hour.setError(getString(R.string.hour_error));
            return false;
        }
        if(et_saturday_open_minute.getText().toString().trim().length()!=2 && et_saturday_open_minute.getText().toString().trim().length()!=0 ) {
            et_saturday_open_minute.setError(getString(R.string.minutes_error));
            return false;
        }
        if(et_saturday_close_hour.getText().toString().trim().length() != 2 && et_saturday_close_hour.getText().toString().trim().length() != 0){
            et_saturday_close_hour.setError(getString(R.string.hour_error));
            return false;
        }
        if(et_saturday_close_minute.getText().toString().trim().length()!=2 && et_saturday_close_minute.getText().toString().trim().length()!=0) {
            et_saturday_close_minute.setError(getString(R.string.minutes_error));
            return false;
        }

        if(et_sunday_open_hour.getText().toString().trim().length() !=2 && et_sunday_open_hour.getText().toString().trim().length() !=0){
            et_sunday_open_hour.setError(getString(R.string.hour_error));
            return false;
        }
        if(et_sunday_open_minute.getText().toString().trim().length()!=2 && et_sunday_open_minute.getText().toString().trim().length()!=0) {
            et_sunday_open_minute.setError(getString(R.string.minutes_error));
            return false;
        }
        if(et_sunday_close_hour.getText().toString().trim().length() != 2 && et_sunday_close_hour.getText().toString().trim().length() != 0){
            et_sunday_close_hour.setError(getString(R.string.hour_error));
            return false;
        }
        if(et_sunday_close_minute.getText().toString().trim().length()!=2 && et_sunday_close_minute.getText().toString().trim().length()!=0) {
            et_sunday_close_minute.setError(getString(R.string.minutes_error));
            return false;
        }
        return true;
    }

    public boolean isValidMonday(){
        if(et_monday_open_hour.getText().toString().trim().length() ==2) {
            if(et_monday_open_minute.getText().toString().trim().length() ==2){
                if(et_monday_close_hour.getText().toString().trim().length() ==2){
                    if(et_monday_close_minute.getText().toString().trim().length() ==2){
                        return true;
                    }
                }
            }
        }
        else if(et_monday_open_hour.getText().toString().trim().length() ==0) {
            if(et_monday_open_minute.getText().toString().trim().length() ==0){
                if(et_monday_close_hour.getText().toString().trim().length() ==0){
                    if(et_monday_close_minute.getText().toString().trim().length() ==0){
                        return true;
                    }
                }
            }
        }
        et_monday_open_hour.setError(getString(R.string.line_error_hours));
        return false;
    }

    public boolean isValidTuesday(){
        if(et_tuesday_open_hour.getText().toString().trim().length() ==2) {
            if(et_tuesday_open_minute.getText().toString().trim().length() ==2){
                if(et_tuesday_close_hour.getText().toString().trim().length() ==2){
                    if(et_tuesday_close_minute.getText().toString().trim().length() ==2){
                        return true;
                    }
                }
            }
        }
        else if(et_tuesday_open_hour.getText().toString().trim().length() ==0) {
            if(et_tuesday_open_minute.getText().toString().trim().length() ==0){
                if(et_tuesday_close_hour.getText().toString().trim().length() ==0){
                    if(et_tuesday_close_minute.getText().toString().trim().length() ==0){
                        return true;
                    }
                }
            }
        }
        et_tuesday_open_hour.setError(getString(R.string.line_error_hours));
        return false;
    }

    public boolean isValidWednesday(){
        if(et_wednesday_open_hour.getText().toString().trim().length() ==2) {
            if(et_wednesday_open_minute.getText().toString().trim().length() ==2){
                if(et_wednesday_close_hour.getText().toString().trim().length() ==2){
                    if(et_wednesday_close_minute.getText().toString().trim().length() ==2){
                        return true;
                    }
                }
            }
        }
        else if(et_wednesday_open_hour.getText().toString().trim().length() ==0) {
            if(et_wednesday_open_minute.getText().toString().trim().length() ==0){
                if(et_wednesday_close_hour.getText().toString().trim().length() ==0){
                    if(et_wednesday_close_minute.getText().toString().trim().length() ==0){
                        return true;
                    }
                }
            }
        }
        et_wednesday_open_hour.setError(getString(R.string.line_error_hours));
        return false;
    }

    public boolean isValidThursday(){
        if(et_thursday_open_hour.getText().toString().trim().length() ==2) {
            if(et_thursday_open_minute.getText().toString().trim().length() ==2){
                if(et_thursday_close_hour.getText().toString().trim().length() ==2){
                    if(et_thursday_close_minute.getText().toString().trim().length() ==2){
                        return true;
                    }
                }
            }
        }
        else if(et_thursday_open_hour.getText().toString().trim().length() ==0) {
            if(et_thursday_open_minute.getText().toString().trim().length() ==0){
                if(et_thursday_close_hour.getText().toString().trim().length() ==0){
                    if(et_thursday_close_minute.getText().toString().trim().length() ==0){
                        return true;
                    }
                }
            }
        }
        et_thursday_open_hour.setError(getString(R.string.line_error_hours));
        return false;
    }

    public boolean isValidFriday(){
        if(et_friday_open_hour.getText().toString().trim().length() ==2) {
            if(et_friday_open_minute.getText().toString().trim().length() ==2){
                if(et_friday_close_hour.getText().toString().trim().length() ==2){
                    if(et_friday_close_minute.getText().toString().trim().length() ==2){
                        return true;
                    }
                }
            }
        }
        else if(et_friday_open_hour.getText().toString().trim().length() ==0) {
            if(et_friday_open_minute.getText().toString().trim().length() ==0){
                if(et_friday_close_hour.getText().toString().trim().length() ==0){
                    if(et_friday_close_minute.getText().toString().trim().length() ==0){
                        return true;
                    }
                }
            }
        }
        et_friday_open_hour.setError(getString(R.string.line_error_hours));
        return false;
    }

    public boolean isValidSaturday(){
        if(et_saturday_open_hour.getText().toString().trim().length() ==2) {
            if(et_saturday_open_minute.getText().toString().trim().length() ==2){
                if(et_saturday_close_hour.getText().toString().trim().length() ==2){
                    if(et_saturday_close_minute.getText().toString().trim().length() ==2){
                        return true;
                    }
                }
            }
        }
        else if(et_saturday_open_hour.getText().toString().trim().length() ==0) {
            if(et_saturday_open_minute.getText().toString().trim().length() ==0){
                if(et_saturday_close_hour.getText().toString().trim().length() ==0){
                    if(et_saturday_close_minute.getText().toString().trim().length() ==0){
                        return true;
                    }
                }
            }
        }
        et_saturday_open_hour.setError(getString(R.string.line_error_hours));
        return false;
    }

    public boolean isValidSunday(){
        if(et_sunday_open_hour.getText().toString().trim().length() ==2) {
            if(et_sunday_open_minute.getText().toString().trim().length() ==2){
                if(et_sunday_close_hour.getText().toString().trim().length() ==2){
                    if(et_sunday_close_minute.getText().toString().trim().length() ==2){
                        return true;
                    }
                }
            }
        }
        else if(et_sunday_open_hour.getText().toString().trim().length() ==0) {
            if(et_sunday_open_minute.getText().toString().trim().length() ==0){
                if(et_sunday_close_hour.getText().toString().trim().length() ==0){
                    if(et_sunday_close_minute.getText().toString().trim().length() ==0){
                        return true;
                    }
                }
            }
        }
        et_sunday_open_hour.setError(getString(R.string.line_error_hours));
        return false;
    }


    @Override
    public void notifyChanges() {
        listActivities.clear();
        selectActivities();
        adapter.notifyDataSetChanged();
    }
}
