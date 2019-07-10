package com.example.teammanagement.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.teammanagement.R;
import com.example.teammanagement.Utils.Activity;
import com.example.teammanagement.Utils.Announcement;
import com.example.teammanagement.Utils.Constants;
import com.example.teammanagement.Utils.Location;
import com.example.teammanagement.Utils.Sport;
import com.example.teammanagement.adapters.HoursAdapter;
import com.example.teammanagement.adapters.SpinnerAdapter;
import com.example.teammanagement.adapters.SportAdapter;
import com.example.teammanagement.database.JDBCController;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddActivityDialog extends AppCompatDialogFragment {


    private TextInputEditText iet_activityName;
    private TextInputEditText iet_trainer;
    private TextInputEditText iet_price;
    private TextView tv_program;
    private Spinner spn_sport;
    private Spinner spn_difiiculty;
    private CheckBox ck_reservation;

    private Spinner spn_monday_open_hour;
    private Spinner spn_tuesday_open_hour;
    private Spinner spn_wednesday_open_hour;
    private Spinner spn_thursday_open_hour;
    private Spinner spn_friday_open_hour;
    private Spinner spn_saturday_open_hour;
    private Spinner spn_sunday_open_hour;
    private Spinner spn_monday_close_hour;
    private Spinner spn_tuesday_close_hour;
    private Spinner spn_wednesday_close_hour;
    private Spinner spn_thursday_close_hour;
    private Spinner spn_friday_close_hour;
    private Spinner spn_saturday_close_hour;
    private Spinner spn_sunday_close_hour;
    private Spinner spn_monday_open_minute;
    private Spinner spn_tuesday_open_minute;
    private Spinner spn_wednesday_open_minute;
    private Spinner spn_thursday_open_minute;
    private Spinner spn_friday_open_minute;
    private Spinner spn_saturday_open_minute;
    private Spinner spn_sunday_open_minute;
    private Spinner spn_monday_close_minute;
    private Spinner spn_tuesday_close_minute;
    private Spinner spn_wednesday_close_minute;
    private Spinner spn_thursday_close_minute;
    private Spinner spn_friday_close_minute;
    private Spinner spn_saturday_close_minute;
    private Spinner spn_sunday_close_minute;

    private AlertDialog.Builder builder;
    private LayoutInflater inflater;
    private AddAnnouncementDialog.AddAnnouncementDialogListner listener;
    private AlertDialog dialog;
    private View view;
    private Activity activity;

    private JDBCController jdbcController;
    private Connection c;


    private int ok=0;
    private int currentUserID;
    private  int locationID;
    private Location currentLocation;
    private List<String> spn_hours_items = new ArrayList<>();
    private List<String> spn_minutes_items = new ArrayList<>();
    private List<String> spn_difiiculty_items = new ArrayList<>();
    private ArrayList<String> list_toGoToDialog = new ArrayList<>() ;

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        jdbcController= JDBCController.getInstance();
        c=jdbcController.openConnection();

        initComponents();
        createBuilder();


        dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
        return dialog;
    }

    public void createBuilder(){
        builder.setView(view)
                .setTitle("Adăugare activitate")
                .setNegativeButton("Anulează", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Salvează", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       /* String message=iet_message.getText().toString();
                        listener.applyTexts(message,ok);*/
                    }
                });
    }

    private void initComponents(){
        builder = new AlertDialog.Builder(getActivity(),R.style.BlackTextDialog);
        inflater = getActivity().getLayoutInflater();
        view =inflater.inflate(R.layout.add_activity_dialog,null);

        /*activity=(Activity) getArguments().get("LocationID");*/

        tv_program= view.findViewById(R.id.add_activity_dialog_tv_program_hint);
        iet_activityName=view.findViewById(R.id.add_activity_dialog_tid_activityName);
        iet_trainer=view.findViewById(R.id.add_activity_dialog_tid_trainer);
        iet_price=view.findViewById(R.id.add_activity_dialog_tid_price);
        spn_difiiculty=view.findViewById(R.id.add_activity_dialog_spn_difficulty);
        spn_sport=view.findViewById(R.id.add_activity_dialog_spn_sport);
        ck_reservation=view.findViewById(R.id.add_activity_dialog_ck_reservation);


        spn_monday_open_hour=view.findViewById(R.id.add_activity_dialog_monday_open_hours);
        spn_tuesday_open_hour=view.findViewById(R.id.add_activity_dialog_tuesday_open_hours);
        spn_wednesday_open_hour=view.findViewById(R.id.add_activity_dialog_wednesday_open_hours);
        spn_thursday_open_hour=view.findViewById(R.id.add_activity_dialog_thursday_open_hours);
        spn_friday_open_hour=view.findViewById(R.id.add_activity_dialog_friday_open_hours);
        spn_saturday_open_hour=view.findViewById(R.id.add_activity_dialog_saturday_open_hours);
        spn_sunday_open_hour=view.findViewById(R.id.add_activity_dialog_sunday_open_hours);

        spn_monday_close_hour=view.findViewById(R.id.add_activity_dialog_monday_close_hours);
        spn_tuesday_close_hour=view.findViewById(R.id.add_activity_dialog_tuesday_close_hours);
        spn_wednesday_close_hour=view.findViewById(R.id.add_activity_dialog_wednesday_close_hours);
        spn_thursday_close_hour=view.findViewById(R.id.add_activity_dialog_thursday_close_hours);
        spn_friday_close_hour=view.findViewById(R.id.add_activity_dialog_friday_close_hours);
        spn_saturday_close_hour=view.findViewById(R.id.add_activity_dialog_saturday_close_hours);
        spn_sunday_close_hour=view.findViewById(R.id.add_activity_dialog_sunday_close_hours);

        spn_monday_open_minute=view.findViewById(R.id.add_activity_dialog_monday_open_minutes);
        spn_tuesday_open_minute=view.findViewById(R.id.add_activity_dialog_tuesday_open_minutes);
        spn_wednesday_open_minute=view.findViewById(R.id.add_activity_dialog_wednesday_open_minutes);
        spn_thursday_open_minute=view.findViewById(R.id.add_activity_dialog_thursday_open_minutes);
        spn_friday_open_minute=view.findViewById(R.id.add_activity_dialog_friday_open_minutes);
        spn_saturday_open_minute=view.findViewById(R.id.add_activity_dialog_saturday_open_minutes);
        spn_sunday_open_minute=view.findViewById(R.id.add_activity_dialog_sunday_open_minutes);

        spn_monday_close_minute=view.findViewById(R.id.add_activity_dialog_monday_close_minutes);
        spn_tuesday_close_minute=view.findViewById(R.id.add_activity_dialog_tuesday_close_minutes);
        spn_wednesday_close_minute=view.findViewById(R.id.add_activity_dialog_wednesday_close_minutes);
        spn_thursday_close_minute=view.findViewById(R.id.add_activity_dialog_thursday_close_minutes);
        spn_friday_close_minute=view.findViewById(R.id.add_activity_dialog_friday_close_minutes);
        spn_saturday_close_minute=view.findViewById(R.id.add_activity_dialog_saturday_close_minutes);
        spn_sunday_close_minute=view.findViewById(R.id.add_activity_dialog_sunday_close_minutes);


       /* getCurrentUserID();*/

        HoursAdapter spn_hours_adapter=new HoursAdapter(getContext(),R.layout.spinner_item,spn_hours_items,inflater);
        spn_monday_open_hour.setAdapter(spn_hours_adapter);
        spn_tuesday_open_hour.setAdapter(spn_hours_adapter);
        spn_wednesday_open_hour.setAdapter(spn_hours_adapter);
        spn_thursday_open_hour.setAdapter(spn_hours_adapter);
        spn_friday_open_hour.setAdapter(spn_hours_adapter);
        spn_saturday_open_hour.setAdapter(spn_hours_adapter);
        spn_sunday_open_hour.setAdapter(spn_hours_adapter);

        spn_monday_close_hour.setAdapter(spn_hours_adapter);
        spn_tuesday_close_hour.setAdapter(spn_hours_adapter);
        spn_wednesday_close_hour.setAdapter(spn_hours_adapter);
        spn_thursday_close_hour.setAdapter(spn_hours_adapter);
        spn_friday_close_hour.setAdapter(spn_hours_adapter);
        spn_saturday_close_hour.setAdapter(spn_hours_adapter);
        spn_sunday_close_hour.setAdapter(spn_hours_adapter);

        SpinnerAdapter spn_minutes_adapter=new SpinnerAdapter(getContext(),R.layout.spinner_item,spn_minutes_items,inflater);
        spn_monday_open_minute.setAdapter(spn_minutes_adapter);
        spn_tuesday_open_minute.setAdapter(spn_minutes_adapter);
        spn_wednesday_open_minute.setAdapter(spn_minutes_adapter);
        spn_thursday_open_minute.setAdapter(spn_minutes_adapter);
        spn_friday_open_minute.setAdapter(spn_minutes_adapter);
        spn_saturday_open_minute.setAdapter(spn_minutes_adapter);
        spn_sunday_open_minute.setAdapter(spn_minutes_adapter);

        spn_monday_close_minute.setAdapter(spn_minutes_adapter);
        spn_tuesday_close_minute.setAdapter(spn_minutes_adapter);
        spn_wednesday_close_minute.setAdapter(spn_minutes_adapter);
        spn_thursday_close_minute.setAdapter(spn_minutes_adapter);
        spn_friday_close_minute.setAdapter(spn_minutes_adapter);
        spn_saturday_close_minute.setAdapter(spn_minutes_adapter);
        spn_sunday_close_minute.setAdapter(spn_minutes_adapter);

        list_toGoToDialog.add(getString(R.string.register2_sportSpinner_item0));
        initDataSpinner();

        spn_difiiculty_items= Arrays.asList(getResources().getStringArray(R.array.dialog_level));

        SpinnerAdapter sport_adapter=new SpinnerAdapter(getContext(),R.layout.spinner_item,list_toGoToDialog,inflater);
        spn_sport.setAdapter(sport_adapter);

        SpinnerAdapter difficulty_adapter=new SpinnerAdapter(getContext(),R.layout.spinner_item,spn_difiiculty_items,inflater);
        spn_difiiculty.setAdapter(difficulty_adapter);



        if(activity != null) {
            /*iet_message.setText(announcement.getMessage());*/
            ok=1;
        }

        /*iet_message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(iet_message.getText().length()>2){
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                }
            }
        });*/

    }

  /*  public void getCurrentUserID(){
        currentUserID = (int) getArguments().get(Constants.CURRENT_USER_ID);
    }*/

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


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

       /* try {
            listener=(AddAnnouncementDialog.AddAnnouncementDialogListner) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+getString(R.string.addSportDialog_error_classCastException));
        }*/
    }

    public interface AddAnnouncementDialogListner{
       /* void applyTexts( String message, int ok);*/
    }
}