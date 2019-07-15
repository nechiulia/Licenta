package com.example.teammanagement.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import com.example.teammanagement.R;
import com.example.teammanagement.Utils.Activity;
import com.example.teammanagement.Utils.Constants;
import com.example.teammanagement.Utils.NewLocation;
import com.example.teammanagement.Utils.Sport;
import com.example.teammanagement.adapters.SpinnerAdapter;
import com.example.teammanagement.database.JDBCController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddActivityDialog extends AppCompatDialogFragment {


    private TextInputEditText iet_activityName;
    private TextInputEditText iet_trainer;
    private TextInputEditText iet_price;
    private TextView tv_program;
    private Spinner spn_sport;
    private Spinner spn_difiiculty;
    private CheckBox ck_reservation;
    private Button btn_save;
    private Button btn_cancel;

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

    private AlertDialog.Builder builder;
    private LayoutInflater inflater;
    private AlertDialog dialog;
    private View view;
    private Activity activity;

    private JDBCController jdbcController;
    private Connection c;

    private AddActivityListener listener;


    private int ok1=0;
    private int ok2=0;
    private int newActivityID;
    private int clickedActivityID;
    private  int currentLocationID;
    private NewLocation currentLocation;
    private List<String> spn_difiiculty_items = new ArrayList<>();
    private ArrayList<String> list_toGoToDialog = new ArrayList<>() ;
    private Map<Integer,String> mapOrar = new HashMap<>();

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        try {
            listener=(AddActivityListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(getString(R.string.addSportDialog_error_classCastException));
        }

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
                .setTitle(R.string.add_activity_dialog_title)
                .setCancelable(false);
    }

    private void initComponents(){
        builder = new AlertDialog.Builder(getActivity(),R.style.BlackTextDialog);
        inflater = getActivity().getLayoutInflater();
        view =inflater.inflate(R.layout.add_activity_dialog,null);

        tv_program= view.findViewById(R.id.add_activity_dialog_tv_program_hint);
        iet_activityName=view.findViewById(R.id.add_activity_dialog_tid_activityName);
        iet_trainer=view.findViewById(R.id.add_activity_dialog_tid_trainer);
        iet_price=view.findViewById(R.id.add_activity_dialog_tid_price);
        spn_difiiculty=view.findViewById(R.id.add_activity_dialog_spn_difficulty);
        spn_sport=view.findViewById(R.id.add_activity_dialog_spn_sport);
        ck_reservation=view.findViewById(R.id.add_activity_dialog_ck_reservation);
        btn_cancel=view.findViewById(R.id.add_activity_dialog_btn_cancel);
        btn_save=view.findViewById(R.id.add_activity_dialog_btn_save);
        btn_save.setEnabled(false);


        et_monday_open_hour=view.findViewById(R.id.add_activity_dialog_monday_open_hours);
        et_tuesday_open_hour=view.findViewById(R.id.add_activity_dialog_tuesday_open_hours);
        et_wednesday_open_hour=view.findViewById(R.id.add_activity_dialog_wednesday_open_hours);
        et_thursday_open_hour=view.findViewById(R.id.add_activity_dialog_thursday_open_hours);
        et_friday_open_hour=view.findViewById(R.id.add_activity_dialog_friday_open_hours);
        et_saturday_open_hour=view.findViewById(R.id.add_activity_dialog_saturday_open_hours);
        et_sunday_open_hour=view.findViewById(R.id.add_activity_dialog_sunday_open_hours);

        et_monday_close_hour=view.findViewById(R.id.add_activity_dialog_monday_close_hours);
        et_tuesday_close_hour=view.findViewById(R.id.add_activity_dialog_tuesday_close_hours);
        et_wednesday_close_hour=view.findViewById(R.id.add_activity_dialog_wednesday_close_hours);
        et_thursday_close_hour=view.findViewById(R.id.add_activity_dialog_thursday_close_hours);
        et_friday_close_hour=view.findViewById(R.id.add_activity_dialog_friday_close_hours);
        et_saturday_close_hour=view.findViewById(R.id.add_activity_dialog_saturday_close_hours);
        et_sunday_close_hour=view.findViewById(R.id.add_activity_dialog_sunday_close_hours);

        getLocationID();
        getClickedActivityID();
        initializeMap();
        selectLocationInfo();

        if(currentLocation.getReservation() == 0){
            ck_reservation.setVisibility(View.GONE);
        }


        list_toGoToDialog.add(getString(R.string.register2_sportSpinner_item0));
        initDataSpinner();

        spn_difiiculty_items= Arrays.asList(getResources().getStringArray(R.array.dialog_difficulty));

        SpinnerAdapter sport_adapter=new SpinnerAdapter(getContext(),R.layout.spinner_item,list_toGoToDialog,inflater);
        spn_sport.setAdapter(sport_adapter);

        SpinnerAdapter difficulty_adapter=new SpinnerAdapter(getContext(),R.layout.spinner_item,spn_difiiculty_items,inflater);
        spn_difiiculty.setAdapter(difficulty_adapter);

        spn_sport.setOnItemSelectedListener(isSelectedSport());
        spn_difiiculty.setOnItemSelectedListener(isSelectedDifficulty());

        btn_cancel.setOnClickListener(clickCancel());
        btn_save.setOnClickListener(clickSave());

        if(clickedActivityID != -1){
            selectActivity();
            selectOrar();
            iet_activityName.setText(activity.getActivityName());
            iet_activityName.setEnabled(false);
            iet_trainer.setText(activity.getTrainer());
            iet_price.setText(String.valueOf(activity.getPrice()));
            spn_sport.setSelection(getItemSport());
            spn_sport.setEnabled(false);
            spn_difiiculty.setSelection(getItemDifficulty());
            spn_difiiculty.setEnabled(false);
            iet_trainer.setEnabled(false);
            boolean checked=false;
            if(activity.getReservation() == 1){
                checked=true;
            }
            ck_reservation.setChecked(checked);
            ck_reservation.setEnabled(false);
        }

    }

    public int getItemSport(){
        for(int i=0; i< list_toGoToDialog.size(); i++){
            if(list_toGoToDialog.get(i).equals(activity.getSport())){
                return i;
            }
        }
        return -1;
    }

    public int getItemDifficulty(){
        for(int i=0; i< spn_difiiculty_items.size(); i++){
            if(spn_difiiculty_items.get(i).equals(activity.getDifficultyLevel())){
                return i;
            }
        }
        return -1;
    }

    public void getLocationID(){
        currentLocationID = (int) getArguments().get(Constants.CURRENT_LOCATION_ID);
    }

    public void getClickedActivityID(){
        clickedActivityID = (int) getArguments().get(Constants.CLICKED_ACTIVITY_ID);
    }

    public void initDataSpinner(){
        try(Statement s = c.createStatement()){
            String command ="SELECT * FROM SPORTURI;";
            try(ResultSet r =s.executeQuery(command)) {
                while(r.next()){
                    Sport sport = new Sport(r.getString(2),r.getInt(3));
                    list_toGoToDialog.add(sport.getDenumire());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void selectActivity(){
        try(Statement s = c.createStatement()){
            String command ="SELECT * FROM ACTIVITATI WHERE ID="+clickedActivityID;
            try(ResultSet r =s.executeQuery(command)) {
                if(r.next()){
                    activity = new Activity();
                    activity.setActivityID(r.getInt(1));
                    activity.setActivityName(r.getString(2));
                    activity.setSport(r.getString(3));
                    activity.setTrainer(r.getString(4));
                    String difficulty = getLevel(r.getInt(5));
                    activity.setDifficultyLevel(difficulty);
                    activity.setPrice(r.getDouble(6));
                    activity.setReservation(r.getInt(7));
                    activity.setLocationID(currentLocationID);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertActivity(){
        int reservation=0;
        if(ck_reservation.isChecked()){
            reservation=1;
        }
        int level=getLevelInt(spn_difiiculty.getSelectedItem().toString());
        String trainer="-";
        if(iet_trainer.getText().length() !=0){
            trainer=iet_trainer.getText().toString().trim();
        }
        try(PreparedStatement s = c.prepareStatement("INSERT INTO ACTIVITATI VALUES(N'"+iet_activityName.getText().toString().trim()
                +"',N'"+spn_sport.getSelectedItem().toString().trim()+"','"+
                trainer+"',"+ level+","+
                Double.parseDouble(iet_price.getText().toString().trim())+","+ reservation+","+currentLocationID+")",Statement.RETURN_GENERATED_KEYS)){
            int updatedRows=s.executeUpdate();
            ResultSet r=s.getGeneratedKeys();
            if(r.next()){
                if(updatedRows >0 ) {
                    Log.d("databaseUpdateUser", String.valueOf(updatedRows));
                    newActivityID=r.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void selectOrar(){
        try(Statement s = c.createStatement()){
            for(int i=0; i< mapOrar.size(); i++) {
                try (ResultSet r = s.executeQuery("SELECT INTERVALORAR FROM ORAR WHERE IDACTIVITATE=" + clickedActivityID+" AND ZI="+i)) {
                    if(r.next()) {
                        String interval = r.getString(1);
                        String[] openClose= interval.trim().split("");
                        String open_hour;
                        String close_hour;
                        if(openClose.length>2) {
                            open_hour = openClose[1] + openClose[2];
                            close_hour = openClose[4] + openClose[5];
                        }else{
                            open_hour=openClose[1];
                            close_hour=openClose[1];
                        }
                        if(open_hour.equals(getString(R.string.closed_location_bd))){
                            open_hour=getString(R.string.empty_string);
                            close_hour=getString(R.string.empty_string);
                        }
                        else if(close_hour.equals(getString(R.string.closed_location_bd))){
                            open_hour=getString(R.string.empty_string);
                            close_hour=getString(R.string.empty_string);
                        }
                        if(i == 0){
                            et_monday_open_hour.setText(open_hour);
                            et_monday_close_hour.setText(close_hour);
                        }
                        else if(i == 1){
                            et_tuesday_open_hour.setText(open_hour);
                            et_tuesday_close_hour.setText(close_hour);
                        }
                        else if(i == 2){
                            et_wednesday_open_hour.setText(open_hour);
                            et_wednesday_close_hour.setText(close_hour);
                        }
                        else if(i == 3){
                            et_thursday_open_hour.setText(open_hour);
                            et_thursday_close_hour.setText(close_hour);
                        }
                        else if(i == 4){
                            et_friday_open_hour.setText(open_hour);
                            et_friday_close_hour.setText(close_hour);
                        }
                        else if(i == 5){
                            et_saturday_open_hour.setText(open_hour);
                            et_saturday_close_hour.setText(close_hour);
                        }
                        else if(i == 6){
                            et_sunday_open_hour.setText(open_hour);
                            et_sunday_close_hour.setText(close_hour);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void initializeMap(){
        mapOrar.put(0,getString(R.string.empty_string));
        mapOrar.put(1,getString(R.string.empty_string));
        mapOrar.put(2,getString(R.string.empty_string));
        mapOrar.put(3,getString(R.string.empty_string));
        mapOrar.put(4,getString(R.string.empty_string));
        mapOrar.put(5,getString(R.string.empty_string));
        mapOrar.put(6,getString(R.string.empty_string));
    }

    public void getOrar(){

        mapOrar.put(0, et_monday_open_hour.getText().toString().trim() +getString(R.string.closed_location_bd)+ et_monday_close_hour.getText().toString().trim() );
        mapOrar.put(1, et_tuesday_open_hour.getText().toString().trim() +getString(R.string.closed_location_bd)+ et_tuesday_close_hour.getText().toString().trim() );
        mapOrar.put(2, et_wednesday_open_hour.getText().toString().trim() +getString(R.string.closed_location_bd)+ et_wednesday_close_hour.getText().toString().trim());
        mapOrar.put(3, et_thursday_open_hour.getText().toString().trim() +getString(R.string.closed_location_bd)+ et_thursday_close_hour.getText().toString().trim() );
        mapOrar.put(4, et_friday_open_hour.getText().toString().trim() +getString(R.string.closed_location_bd)+ et_friday_close_hour.getText().toString().trim());
        mapOrar.put(5, et_saturday_open_hour.getText().toString().trim() +getString(R.string.closed_location_bd)+ et_saturday_close_hour.getText().toString().trim() );
        mapOrar.put(6, et_sunday_open_hour.getText().toString().trim() +getString(R.string.closed_location_bd)+ et_sunday_close_hour.getText().toString().trim());

    }


    public void insertOrar(){
        try(Statement s = c.createStatement()){
            for(int i=0; i< mapOrar.size();i++) {
                s.executeUpdate("INSERT INTO ORAR VALUES("+i+",'"+mapOrar.get(i)+"',"+newActivityID+")");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateOrar(){
        try(Statement s = c.createStatement()){
            for(int i=0; i< mapOrar.size();i++) {
                s.executeUpdate("UPDATE ORAR SET INTERVALORAR='"+mapOrar.get(i)+"' WHERE IDACTIVITATE="+clickedActivityID+" AND ZI="+i);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateActivity(){
        int reservation=0;
        if(ck_reservation.isChecked()){
            reservation=1;
        }
        String trainer="-";
        if(iet_trainer.getText().length() !=0){
            trainer=iet_trainer.getText().toString().trim();
        }
        try(Statement s = c.createStatement()){
            s.executeUpdate("UPDATE ACTIVITATI SET ANTRENOR=N'"+trainer+
                    "', PRET="+Double.parseDouble(iet_price.getText().toString().trim())+", REZERVARI="+reservation+" WHERE ID="+clickedActivityID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean selectActivityName(){
        try(Statement s = c.createStatement()){
            try(ResultSet r = s.executeQuery("SELECT ID FROM ACTIVITATI WHERE IDLOCATIE="+currentLocationID+" AND DENUMIRE='"+iet_activityName.getText().toString().trim()+"'")){
                if(r.next()){
                    iet_activityName.setError("Ați adăugat deja o activitate cu acest nume");
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void selectLocationInfo(){
        try(Statement s = c.createStatement()){
            try(ResultSet r = s.executeQuery("SELECT * FROM LOCATII WHERE ID="+currentLocationID)){
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

    private int getLevelInt(String level){
        if(level.equals(getString(R.string.user_sport_level_0)))return 0;
        else if(level.equals(getString(R.string.user_sport_level_1)))return 1;
        else if(level.equals(getString(R.string.user_sport_level_2)))return 2;
        else if(level.equals(getString(R.string.user_sport_level_3)))return 3;
        else if(level.equals(getString(R.string.user_sport_level_4)))return 4;
        else if(level.equals(getString(R.string.user_sport_level_5)))return 5;
        else if(level.equals("-"))return 6;
        return -2;
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

    private View.OnClickListener clickSave() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid()) {
                    getOrar();
                    if (clickedActivityID != -1) {
                        updateActivity();
                        updateOrar();
                        listener.notifyChanges();
                        dialog.dismiss();
                    } else {
                        if(!selectActivityName()) {
                            insertActivity();
                            insertOrar();
                            listener.notifyChanges();
                            dialog.dismiss();
                        }
                    }

                }
            }
        };
    }

    private View.OnClickListener clickCancel() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               dialog.dismiss();
            }
        };
    }

    public AdapterView.OnItemSelectedListener isSelectedSport(){
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0){
                    ok1 = 1;
                }
                if(ok1 ==1 && ok2 ==1){
                    btn_save.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    }

    public AdapterView.OnItemSelectedListener isSelectedDifficulty(){
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0){
                    ok2=1;
                }
                if(ok1 ==1 && ok2 ==1){
                    btn_save.setEnabled(true);
                }
                if(position ==7){
                    iet_trainer.setText("");
                    iet_trainer.setEnabled(false);
                }
                else{
                    iet_trainer.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    }

    public boolean isValid(){
        if(isValidActivityName() && isValidTrainer() && isValidHoursLength() && isValidMonday()
        &&isValidTuesday() && isValidWednesday() && isValidThursday() && isValidFriday() &&  isValidSaturday() && isValidSunday()){
            return true;
        }
        return false;
    }

    public boolean isValidActivityName(){
        if(iet_activityName.getText().length()<3){
            iet_activityName.setError(getString(R.string.register1_nameLength_error_hint));
            return false;
        }
        else if(!Character.isUpperCase(iet_activityName.getText().toString().charAt(0))){
            iet_activityName.setError(getString(R.string.register1_nameCapitalLetter_error_hint));
            return false;
        }
        return true;
    }

    public boolean isValidTrainer(){
        if(iet_trainer.length() != 0) {
            if (iet_trainer.getText().length() < 3) {
                iet_trainer.setError(getString(R.string.register1_nameLength_error_hint));
                return false;
            } else if (!Character.isUpperCase(iet_trainer.getText().toString().charAt(0))) {
                iet_trainer.setError(getString(R.string.register1_nameCapitalLetter_error_hint));
                return false;
            }
        }
        return true;
    }

    public boolean isValidHoursLength(){
        if(et_monday_open_hour.getText().toString().trim().length() !=2 && et_monday_open_hour.getText().toString().trim().length() !=0){
            et_monday_open_hour.setError(getString(R.string.hour_error));
            return false;
        }
        if(et_monday_close_hour.getText().toString().trim().length() != 2 && et_monday_close_hour.getText().toString().trim().length() !=0){
            et_monday_close_hour.setError(getString(R.string.hour_error));
            return false;
        }

        if(et_tuesday_open_hour.getText().toString().trim().length() !=2 && et_tuesday_open_hour.getText().toString().trim().length() !=0){
            et_tuesday_open_hour.setError(getString(R.string.hour_error));
            return false;
        }
        if(et_tuesday_close_hour.getText().toString().trim().length() != 2 && et_tuesday_close_hour.getText().toString().trim().length() != 0){
            et_tuesday_close_hour.setError(getString(R.string.hour_error));
            return false;
        }
        if(et_wednesday_open_hour.getText().toString().trim().length() !=2 && et_wednesday_open_hour.getText().toString().trim().length() !=0){
            et_wednesday_open_hour.setError(getString(R.string.hour_error));
            return false;
        }
        if(et_wednesday_close_hour.getText().toString().trim().length() != 2 && et_wednesday_close_hour.getText().toString().trim().length() != 0){
            et_wednesday_close_hour.setError(getString(R.string.hour_error));
            return false;
        }
        if(et_thursday_open_hour.getText().toString().trim().length() !=2 && et_thursday_open_hour.getText().toString().trim().length() !=0){
            et_thursday_open_hour.setError(getString(R.string.hour_error));
            return false;
        }
        if(et_thursday_close_hour.getText().toString().trim().length() != 2 && et_thursday_close_hour.getText().toString().trim().length() != 0){
            et_thursday_close_hour.setError(getString(R.string.hour_error));
            return false;
        }
        if(et_friday_open_hour.getText().toString().trim().length() !=2 && et_friday_open_hour.getText().toString().trim().length() !=0){
            et_friday_open_hour.setError(getString(R.string.hour_error));
            return false;
        }
        if(et_friday_close_hour.getText().toString().trim().length() != 2 && et_friday_close_hour.getText().toString().trim().length() != 0){
            et_friday_close_hour.setError(getString(R.string.hour_error));
            return false;
        }
        if(et_saturday_open_hour.getText().toString().trim().length() !=2 && et_saturday_open_hour.getText().toString().trim().length() !=0){
            et_saturday_open_hour.setError(getString(R.string.hour_error));
            return false;
        }
        if(et_saturday_close_hour.getText().toString().trim().length() != 2 && et_saturday_close_hour.getText().toString().trim().length() != 0){
            et_saturday_close_hour.setError(getString(R.string.hour_error));
            return false;
        }
        if(et_sunday_open_hour.getText().toString().trim().length() !=2 && et_sunday_open_hour.getText().toString().trim().length() !=0){
            et_sunday_open_hour.setError(getString(R.string.hour_error));
            return false;
        }
        if(et_sunday_close_hour.getText().toString().trim().length() != 2 && et_sunday_close_hour.getText().toString().trim().length() != 0){
            et_sunday_close_hour.setError(getString(R.string.hour_error));
            return false;
        }
        return true;
    }

    public boolean isValidMonday(){
        if(et_monday_open_hour.getText().toString().trim().length() ==2 && et_monday_close_hour.getText().toString().trim().length() ==2) {
            return true;
        }
        else if(et_monday_open_hour.getText().toString().trim().length() ==0 && et_monday_close_hour.getText().toString().trim().length() ==0) {
            return true;
        }
        et_monday_open_hour.setError(getString(R.string.line_error_hours));
        return false;
    }

    public boolean isValidTuesday(){
        if(et_tuesday_open_hour.getText().toString().trim().length() ==2 && et_tuesday_close_hour.getText().toString().trim().length() ==2) {
            return true;
        }
        else if(et_tuesday_open_hour.getText().toString().trim().length() ==0 && et_tuesday_close_hour.getText().toString().trim().length() ==0) {
            return true;
        }
        et_tuesday_open_hour.setError(getString(R.string.line_error_hours));
        return false;
    }

    public boolean isValidWednesday(){
        if(et_wednesday_open_hour.getText().toString().trim().length() ==2 && et_wednesday_close_hour.getText().toString().trim().length() ==2) {
            return true;
        }
        else if(et_wednesday_open_hour.getText().toString().trim().length() ==0 && et_wednesday_close_hour.getText().toString().trim().length() ==0) {
            return true;
        }
        et_wednesday_open_hour.setError(getString(R.string.line_error_hours));
        return false;
    }

    public boolean isValidThursday(){
        if(et_thursday_open_hour.getText().toString().trim().length() ==2 && et_thursday_close_hour.getText().toString().trim().length() ==2) {
            return true;
        }
        else if(et_thursday_open_hour.getText().toString().trim().length() ==0 && et_thursday_close_hour.getText().toString().trim().length() ==0) {
            return true;
        }
        et_thursday_open_hour.setError(getString(R.string.line_error_hours));
        return false;
    }

    public boolean isValidFriday(){
        if(et_friday_open_hour.getText().toString().trim().length() ==2 && et_friday_close_hour.getText().toString().trim().length() ==2) {
            return true;
        }
        else if(et_friday_open_hour.getText().toString().trim().length() ==0 && et_friday_close_hour.getText().toString().trim().length() ==0) {
            return true;
        }
        et_friday_open_hour.setError(getString(R.string.line_error_hours));
        return false;
    }

    public boolean isValidSaturday(){
        if(et_saturday_open_hour.getText().toString().trim().length() ==2 && et_saturday_close_hour.getText().toString().trim().length() ==2 ) {
            return true;
        }
        else if(et_saturday_open_hour.getText().toString().trim().length() ==0 && et_saturday_close_hour.getText().toString().trim().length() ==0) {
            return true;
        }
        et_saturday_open_hour.setError(getString(R.string.line_error_hours));
        return false;
    }

    public boolean isValidSunday(){
        if(et_sunday_open_hour.getText().toString().trim().length() ==2 && et_sunday_close_hour.getText().toString().trim().length() ==2) {
            return true;
        }
        else if(et_sunday_open_hour.getText().toString().trim().length() ==0 && et_sunday_close_hour.getText().toString().trim().length() ==0) {
            return true;
        }
        et_sunday_open_hour.setError(getString(R.string.line_error_hours));
        return false;
    }

    public interface AddActivityListener{
        void notifyChanges();
    }

}