package com.example.teammanagement.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.teammanagement.R;
import com.example.teammanagement.Utils.Constants;
import com.example.teammanagement.activities.LoginActivity;
import com.example.teammanagement.adapters.HoursAdapter;
import com.example.teammanagement.adapters.SpinnerAdapter;
import com.example.teammanagement.database.JDBCController;
import com.example.teammanagement.dialogs.AddActivityDialog;
import com.example.teammanagement.dialogs.AddSportDialog;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EditLocationFragment extends Fragment {

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

    private Intent intent;

    private JDBCController jdbcController;
    private Connection c;

    private int currentUserID;
    private int locationID;
    private List<String> spn_hours_items = new ArrayList<>();
    private List<String> spn_minutes_items = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_location,null);

        jdbcController= JDBCController.getInstance();
        c=jdbcController.openConnection();

        ibtn_logOut=view.findViewById(R.id.fragment_edit_location_ibtn_logout);
        ibtn_addActivity = view.findViewById(R.id.fragment_edit_location_ibtn_addActivity);
        ibtn_removeActivity=view.findViewById(R.id.fragment_edit_location_ibtn_removeActivity);
        btn_save = view.findViewById(R.id.fragment_edit_location_btn_save);
        tv_program=view.findViewById(R.id.fragment_edit_location_tv_program_hint);

        linearLayout= view.findViewById(R.id.fragment_edit_location_container_program);

        spn_monday_open_hour=view.findViewById(R.id.fragment_edit_location_monday_open_hours);
        spn_tuesday_open_hour=view.findViewById(R.id.fragment_edit_location_tuesday_open_hours);
        spn_wednesday_open_hour=view.findViewById(R.id.fragment_edit_location_wednesday_open_hours);
        spn_thursday_open_hour=view.findViewById(R.id.fragment_edit_location_thursday_open_hours);
        spn_friday_open_hour=view.findViewById(R.id.fragment_edit_location_friday_open_hours);
        spn_saturday_open_hour=view.findViewById(R.id.fragment_edit_location_saturday_open_hours);
        spn_sunday_open_hour=view.findViewById(R.id.fragment_edit_location_sunday_open_hours);

        spn_monday_close_hour=view.findViewById(R.id.fragment_edit_location_monday_close_hours);
        spn_tuesday_close_hour=view.findViewById(R.id.fragment_edit_location_tuesday_close_hours);
        spn_wednesday_close_hour=view.findViewById(R.id.fragment_edit_location_wednesday_close_hours);
        spn_thursday_close_hour=view.findViewById(R.id.fragment_edit_location_thursday_close_hours);
        spn_friday_close_hour=view.findViewById(R.id.fragment_edit_location_friday_close_hours);
        spn_saturday_close_hour=view.findViewById(R.id.fragment_edit_location_saturday_close_hours);
        spn_sunday_close_hour=view.findViewById(R.id.fragment_edit_location_sunday_close_hours);

        spn_monday_open_minute=view.findViewById(R.id.fragment_edit_location_monday_open_minutes);
        spn_tuesday_open_minute=view.findViewById(R.id.fragment_edit_location_tuesday_open_minutes);
        spn_wednesday_open_minute=view.findViewById(R.id.fragment_edit_location_wednesday_open_minutes);
        spn_thursday_open_minute=view.findViewById(R.id.fragment_edit_location_thursday_open_minutes);
        spn_friday_open_minute=view.findViewById(R.id.fragment_edit_location_friday_open_minutes);
        spn_saturday_open_minute=view.findViewById(R.id.fragment_edit_location_saturday_open_minutes);
        spn_sunday_open_minute=view.findViewById(R.id.fragment_edit_location_sunday_open_minutes);

        spn_monday_close_minute=view.findViewById(R.id.fragment_edit_location_monday_close_minutes);
        spn_tuesday_close_minute=view.findViewById(R.id.fragment_edit_location_tuesday_close_minutes);
        spn_wednesday_close_minute=view.findViewById(R.id.fragment_edit_location_wednesday_close_minutes);
        spn_thursday_close_minute=view.findViewById(R.id.fragment_edit_location_thursday_close_minutes);
        spn_friday_close_minute=view.findViewById(R.id.fragment_edit_location_friday_close_minutes);
        spn_saturday_close_minute=view.findViewById(R.id.fragment_edit_location_saturday_close_minutes);
        spn_sunday_close_minute=view.findViewById(R.id.fragment_edit_location_sunday_close_minutes);

        tv_program.setOnClickListener(clickProgram());
        ibtn_logOut.setOnClickListener(clickLogOut());
        ibtn_addActivity.setOnClickListener(clickAddActivity());
        ibtn_removeActivity.setOnClickListener(clickRemoveActivity());
        btn_save.setOnClickListener(clickSave());

        getCurrentUserID();

        spn_hours_items = Arrays.asList(getResources().getStringArray(R.array.hours));
        spn_minutes_items = Arrays.asList(getResources().getStringArray(R.array.minutes));

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

        SpinnerAdapter  spn_minutes_adapter=new SpinnerAdapter(getContext(),R.layout.spinner_item,spn_minutes_items,inflater);
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

        spn_monday_open_hour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("da", spn_hours_items.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    private void openDialog(){
        /*Bundle args = new Bundle();
        args.putSerializable("LocationID", locationID);
*/

        AddActivityDialog addActivityDialog = new AddActivityDialog();
        addActivityDialog.show(getActivity().getSupportFragmentManager(),getString(R.string.register2_addSport_hint));

        /*addActivityDialog.setArguments(args);*/
        addActivityDialog.setCancelable(false);
    }


    public void getCurrentUserID(){
        currentUserID = (int) getArguments().get(Constants.CURRENT_USER_ID);
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
                openDialog();
            }
        };
    }
    private View.OnClickListener clickRemoveActivity() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        };
    }
    private View.OnClickListener clickSave() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        };
    }

    private View.OnClickListener clickProgram() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(linearLayout.getVisibility() == View.GONE) {
                    linearLayout.setVisibility(View.VISIBLE);
                }
                else{
                    linearLayout.setVisibility(View.GONE);
                }
            }
        };
    }
}
