package com.example.teammanagement.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.teammanagement.R;
import com.example.teammanagement.Utils.Activity;
import com.example.teammanagement.Utils.Announcement;
import com.example.teammanagement.Utils.Constants;
import com.example.teammanagement.Utils.NewLocation;
import com.example.teammanagement.adapters.ExpandableListLocationActivitiesAdapter;
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

    private AlertDialog.Builder builder;
    private LayoutInflater inflater;
    private AlertDialog dialog;
    private View view;

    private JDBCController jdbcController;
    private Connection c;

    private ExpandableListLocationActivitiesAdapter listAdapter;

    private String locationPostalCode;
    private NewLocation currentLocation = new NewLocation();
    private HashMap<String, Activity> mapActivity = new HashMap<>();
    private List<String> listParentActivities = new ArrayList<>();
    private List<Activity> listActivities=new ArrayList<>();


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
        lv_program=view.findViewById(R.id.location_marker_dialog_lv_program);
        elv_activities=view.findViewById(R.id.location_marker_dialog_lv_activities);
        ibtn_close=view.findViewById(R.id.location_marker_dialog_ibtn_close);
        btn_reservation=view.findViewById(R.id.location_marker_dialog_btn_reservation);
        tv_locationName=view.findViewById(R.id.location_marker_dialog_locationName);

        btn_reservation.setVisibility(View.GONE);

        getLocationPostalCode();
        getLocationInfo();
        selectUserInfo();
        initData();
        selectActivities();

        btn_reservation.setOnClickListener(clickReservation());
        ibtn_close.setOnClickListener(clickClose());

        if(listParentActivities.size() != 0 && mapActivity.size() !=0) {
            listAdapter = new ExpandableListLocationActivitiesAdapter(this.getContext(), listParentActivities, mapActivity);
            elv_activities.setAdapter(listAdapter);
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
        return getString(R.string.user_sport_level_5);
    }


    public void getLocationInfo(){
        try(Statement s = c.createStatement()){
            try(ResultSet r = s.executeQuery("SELECT * FROM LOCATII WHERE CODPOSTAL='"+locationPostalCode+"'")){
                if(r.next()){
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

   /* public void selectProgram(){
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
    }*/

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

    public void getLocationPostalCode(){
        locationPostalCode = (String) getArguments().get(Constants.MAP_LOCATION_POSTALCODE);
    }

    private View.OnClickListener clickReservation() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
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
