package com.example.teammanagement.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.teammanagement.R;
import com.example.teammanagement.Utils.Constants;
import com.example.teammanagement.Utils.Orar;
import com.example.teammanagement.adapters.ReservationAdapter;
import com.example.teammanagement.database.JDBCController;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReservationDialog extends AppCompatDialogFragment {

    private AlertDialog.Builder builder;
    private LayoutInflater inflater;
    private AlertDialog dialog;
    private View view;

    private ListView lv_reservations;
    private TextView tv_noOrar;

    private JDBCController jdbcController;
    private Connection c;

    private ReservationAdapter adapter;

    private Orar orar;
    private int currentLocationID;
    private String selectedActivityName;
    private int selectedActivityID;
    private String selectedDate;
    private String startHour;
    private String finishHour;
    private String name;
    private int currentDayOfWeek;
    private List<Orar> listOrar = new ArrayList<>();

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

    private void initComponents(){
        builder = new AlertDialog.Builder(getActivity(), R.style.BlackTextDialog);
        inflater = getActivity().getLayoutInflater();
        view =inflater.inflate(R.layout.reservation_dialog,null);

        lv_reservations=view.findViewById(R.id.reservation_dialog_lv);
        tv_noOrar=view.findViewById(R.id.reservation_dialog_tv_noOrar);

        getLocationID();
        getSelectedActivityName();
        getSelectedDate();

        getActivityID();
        getSelectedDayOrar();
        createViewHours();

        if(lv_reservations.getVisibility()== View.VISIBLE) {
            adapter = new ReservationAdapter(getActivity().getApplicationContext(), R.layout.list_item_reservation_dialog, listOrar, inflater);
            lv_reservations.setAdapter(adapter);
        }

        lv_reservations.setOnItemLongClickListener(clickDelete());
    }

    public void createBuilder(){
        builder.setView(view)
                .setTitle("Rezervări pentru ziua de "+selectedDate)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
    }

    public void createViewHours(){

        if(!startHour.equals("-") && !finishHour.equals("-")){
            int startH = Integer.parseInt(startHour);
            int finishH = Integer.parseInt(finishHour);
            String hi;
            String hj;
            for (int i = startH; i < finishH - 1; i++) {
                if (i < 10) {
                    hi = "0" + String.valueOf(i) + ":00";
                } else {
                    hi = String.valueOf(i) + ":00";
                }
                if (i + 1 < 10) {
                    hj = "0" + String.valueOf(i + 1) + ":00";
                } else {
                    if(i == 23){
                        hj="00";
                    }
                    else {
                        hj = String.valueOf(i + 1) + ":00";
                    }
                }
                Orar orar = new Orar(hi, hj);
                listOrar.add(orar);
            }
        }
        else{
            lv_reservations.setVisibility(View.GONE);
            tv_noOrar.setVisibility(View.VISIBLE);
        }
    }

    public void getActivityID(){
        try(Statement s = c.createStatement()){
            try(ResultSet r = s.executeQuery("SELECT ID FROM ACTIVITATI WHERE DENUMIRE='"+selectedActivityName+"'")){
                if(r.next()){
                    selectedActivityID = r.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getSelectedDayOrar(){
        try(Statement s = c.createStatement()){
            try(ResultSet r = s.executeQuery("SELECT INTERVALORAR FROM ORAR WHERE IDACTIVITATE="+selectedActivityID+" AND ZI="+ currentDayOfWeek)){
                if(r.next()){
                    String line = r.getString(1);
                    if(line.length() >1){
                        String[] t = line.split("-");
                        startHour=t[0];
                        finishHour =t[1];
                        Orar orar = new Orar(startHour, finishHour,name);
                    }
                    else{
                        startHour="-";
                        finishHour="-";
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getName(){
        try(Statement s = c.createStatement()){
            try(ResultSet r = s.executeQuery("SELECT IDECHIPA, IDUTILIZATOR FROM REZERVARI WHERE IDACTIVITATE="+selectedActivityID)){
                if(r.next()){
                    String team = r.getString(1);
                    String user = r.getString(2);
                    if(team != null){
                        name=team;
                    }
                    else{
                        name=user;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private AdapterView.OnItemLongClickListener clickDelete()
    {
        return new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {


                return false;
            }
        };
    }


    public void getLocationID(){
        currentLocationID = (int) getArguments().get(Constants.CURRENT_LOCATION_ID);
    }

    public void getSelectedActivityName(){
        selectedActivityName = (String) getArguments().get(Constants.SELECTED_ACTIVITY_NAME);
    }

    public void getSelectedDate(){
        selectedDate = (String) getArguments().get(Constants.SELECTED_DATE);
        try {
            Date date = new SimpleDateFormat("dd/MM/yyyy").parse(selectedDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int dayOfWeek=calendar.get(Calendar.DAY_OF_WEEK);
            getDayDB(dayOfWeek);

        }
        catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void getDayDB(int dayWeek){
        if(dayWeek == 1) currentDayOfWeek =6;
        else if(dayWeek == 2) currentDayOfWeek =0;
        else if(dayWeek == 3) currentDayOfWeek =1;
        else if(dayWeek == 4) currentDayOfWeek =2;
        else if(dayWeek == 5) currentDayOfWeek =3;
        else if(dayWeek == 6) currentDayOfWeek =4;
        else if(dayWeek == 7) currentDayOfWeek =5;
    }



}