package com.example.teammanagement.dialogs;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.teammanagement.R;
import com.example.teammanagement.Utils.Activity;
import com.example.teammanagement.Utils.Constants;
import com.example.teammanagement.Utils.NewLocation;
import com.example.teammanagement.Utils.Program;
import com.example.teammanagement.activities.AddReservationActivity;
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

public class LocationMarkerDialog extends AppCompatDialogFragment {


    private TextView tv_address;
    private TextView tv_postalCode;
    private TextView tv_email;
    private ListView lv_program;
    private ExpandableListView elv_activities;
    private Button btn_reservation;
    private ImageButton ibtn_close;
    private TextView tv_locationName;
    private TextView tv_noLocation;
    private TextView tv_address_hint;
    private TextView tv_postalCode_hint;
    private TextView tv_email_hint;
    private TextView tv_program;
    private TextView tv_activities;


    private AlertDialog.Builder builder;
    private LayoutInflater inflater;
    private AlertDialog dialog;
    private View view;

    private JDBCController jdbcController;
    private Connection c;

    private ExpandableListLocationActivitiesAdapter listAdapter;
    private ProgramAdapter adapter;

    private Intent intent;

    private Double latitude;
    private Double longitude;
    private List<Integer> listActivitiesID = new ArrayList<>();
    private NewLocation currentLocation = new NewLocation();
    private HashMap<String, Activity> mapActivity = new HashMap<>();
    private List<String> listParentActivities = new ArrayList<>();
    private List<Activity> listActivities=new ArrayList<>();
    private List<Program> listProgram = new ArrayList<>();
    private List<Program> listOrar = new ArrayList<>();


    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        jdbcController= JDBCController.getInstance();
        c=jdbcController.openConnection();

        initComponents();
        createBuilder();


        dialog = builder.create();
        dialog.show();
        return dialog;
    }

    public void createBuilder(){
        builder.setView(view)
                .setCancelable(false);
    }

    private void initComponents(){
        builder = new AlertDialog.Builder(getActivity(),R.style.BlackTextDialog);
        inflater = getActivity().getLayoutInflater();
        view =inflater.inflate(R.layout.location_marker_dialog,null);


        tv_address=view.findViewById(R.id.location_marker_dialog_locationAddress);
        tv_postalCode=view.findViewById(R.id.location_marker_dialog_postalCode);
        tv_email=view.findViewById(R.id.location_marker_dialog_email);
        tv_address_hint=view.findViewById(R.id.location_marker_dialog_locationAddress_hint);
        tv_postalCode_hint=view.findViewById(R.id.location_marker_dialog_postalCode_hint);
        tv_activities=view.findViewById(R.id.location_marker_dialog_tv_activities_hint);
        tv_program=view.findViewById(R.id.location_marker_dialog_tv_program_hint);
        tv_email_hint=view.findViewById(R.id.location_marker_dialog_email_hint);
        lv_program=view.findViewById(R.id.location_marker_dialog_lv_program);
        elv_activities=view.findViewById(R.id.location_marker_dialog_lv_activities);
        ibtn_close=view.findViewById(R.id.location_marker_dialog_ibtn_close);
        btn_reservation=view.findViewById(R.id.location_marker_dialog_btn_reservation);
        tv_locationName=view.findViewById(R.id.location_marker_dialog_locationName);
        tv_noLocation=view.findViewById(R.id.location_marker_dialog_tv_noLocationFound);


        ibtn_close.setOnClickListener(clickClose());

        getLocationLatLong();
        getLocationInfo();
        if(currentLocation.getLocationID()!=-1 && currentLocation.getState() == 0) {
            selectUserInfo();
            initData();
            selectProgram();
            selectActivities();
            if(listActivities.size()==0){
                elv_activities.setVisibility(View.GONE);
            }
            if(listProgram.size() ==0){
                lv_program.setVisibility(View.GONE);
            }

            int reservationActivities=selectCountReservationActivities();
            if (currentLocation.getReservation() == 1 && reservationActivities>=1) {
                btn_reservation.setVisibility(View.VISIBLE);
                btn_reservation.setOnClickListener(clickReservation());
            }

            adapter = new ProgramAdapter(this.getContext(), R.layout.list_item_program_location_marker_dialog, listProgram, inflater);
            lv_program.setAdapter(adapter);



            listAdapter = new ExpandableListLocationActivitiesAdapter(this.getContext(), listParentActivities, mapActivity, listActivitiesID, currentLocation);
            elv_activities.setAdapter(listAdapter);


        }
        else{
            tv_address.setVisibility(View.GONE);
            tv_postalCode.setVisibility(View.GONE);
            tv_email.setVisibility(View.GONE);
            tv_address_hint.setVisibility(View.GONE);
            tv_postalCode_hint.setVisibility(View.GONE);
            tv_email_hint.setVisibility(View.GONE);
            tv_activities.setVisibility(View.GONE);
            tv_program.setVisibility(View.GONE);
            lv_program.setVisibility(View.GONE);
            elv_activities.setVisibility(View.GONE);
            btn_reservation.setVisibility(View.GONE);
            tv_locationName.setVisibility(View.GONE);
            tv_noLocation.setVisibility(View.VISIBLE);
        }
    }

    public void initData(){
        tv_locationName.setText(currentLocation.getLocationName());
        tv_address.setText(currentLocation.getAddress());
        tv_postalCode.setText(currentLocation.getPostalCode());
        tv_email.setText(currentLocation.getEmail());

    }

    public String getLevel(int level){
        if(level ==0)return getString(R.string.user_sport_level_0);
        else if(level == 1)return getString(R.string.user_sport_level_1);
        else if(level == 2)return getString(R.string.user_sport_level_2);
        else if(level == 3)return getString(R.string.user_sport_level_3);
        else if(level == 4)return getString(R.string.user_sport_level_4);
        else if(level == 5)return getString(R.string.user_sport_level_5);
        return "-";
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

    public int selectCountReservationActivities(){
        try(Statement s = c.createStatement()){
            try(ResultSet r = s.executeQuery("SELECT COUNT(*) FROM ACTIVITATI WHERE REZERVARI = 1 AND IDLOCATIE="+currentLocation.getLocationID())){
                if(r.next()){
                    return r.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void getLocationInfo(){
        try(Statement s = c.createStatement()){
            try(ResultSet r = s.executeQuery("SELECT * FROM LOCATII WHERE LATITUDINE="+latitude+" AND LONGITUDINE ="+longitude)){
                if(r.next()){
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
    private void selectUserInfo(){
        try(Statement s =c.createStatement()){
            try(ResultSet r = s.executeQuery("SELECT EMAIL FROM UTILIZATORI WHERE ID="+currentLocation.getUserID())){
                if(r.next()){
                    currentLocation.setEmail(r.getString(1));
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


    public void getLocationLatLong(){
        latitude = (Double) getArguments().get(Constants.MAP_LATITUDE);
        longitude = (Double)getArguments().get(Constants.MAP_LONGITUDE);
    }

    private View.OnClickListener clickReservation() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getContext(), AddReservationActivity.class);
                intent.putExtra(Constants.CURRENT_LOCATION_ID,currentLocation.getLocationID());
                startActivity(intent);
            }
        };
    }

    private View.OnClickListener clickClose() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        };
    }

}
