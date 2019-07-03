package com.example.teammanagement.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.teammanagement.R;
import com.example.teammanagement.Utils.Constants;
import com.example.teammanagement.Utils.Team;
import com.example.teammanagement.Utils.User;
import com.example.teammanagement.adapters.SpinnerWhiteAdapter;
import com.example.teammanagement.database.JDBCController;
import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddTeam1Activity extends AppCompatActivity {

    private Button btn_save;
    private Button btn_cancel;
    private TextInputEditText iet_teamName;
    private Spinner spn_sport;

    private Intent intent;

    private User currentUser;
    private List<String> spnsport_items = new ArrayList<>();
    private List<Integer> idSportList = new ArrayList<>();

    private Connection c;
    private JDBCController jdbcController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_team1);

        initComponents();

    }

    public void initComponents(){

        jdbcController = JDBCController.getInstance();
        c=jdbcController.openConnection();

        btn_cancel=findViewById(R.id.add_team1_btn_cancel);
        btn_save=findViewById(R.id.add_team1_btn_next);
        iet_teamName=findViewById(R.id.add_team1_tid_teamName);
        spn_sport=findViewById(R.id.add_team1_spn_sport);
        btn_save.setEnabled(false);

        spnsport_items.add(getString(R.string.register2_sportSpinner_item0));

        iet_teamName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                iet_teamName.setError(null);
            }
        });

        getUser();
        selectUserSports();
        for(int i=0;i<idSportList.size();i++) {
            getSportsName(idSportList.get(i));
        }

        spn_sport.setOnItemSelectedListener(spn_sport_change());
        SpinnerWhiteAdapter adapter=new SpinnerWhiteAdapter(this,spnsport_items);
        spn_sport.setAdapter(adapter);

        btn_save.setOnClickListener(clickNext());
        btn_cancel.setOnClickListener(clickCancel());
    }

    public void getUser(){
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.APP_SHAREDPREF,MODE_PRIVATE);
        Gson gson = new Gson();
        String json =  sharedPreferences.getString(Constants.CURRENT_USER,"");
        currentUser= gson.fromJson(json,User.class);
    }

    private AdapterView.OnItemSelectedListener spn_sport_change(){
        return new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0){
                   btn_save.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    }

    private View.OnClickListener clickCancel() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(getApplicationContext(),TeamsActivity.class);
                startActivity(intent);
            }
        };
    }

    private View.OnClickListener clickNext() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValidName()&& spn_sport.getSelectedItem()!=null) {
                    Team team = new Team(iet_teamName.getText().toString(),spn_sport.getSelectedItem().toString());
                    intent = new Intent(getApplicationContext(), AddTeam2Activity.class);
                    intent.putExtra(Constants.ADDTEAM1_TEAM,team);
                    startActivity(intent);
                }
            }
        };
    }

    public void selectUserSports(){
        try(Statement s = c.createStatement()){
            try(ResultSet r = s.executeQuery("SELECT IDSPORT FROM SPORTUTILIZATOR WHERE IDUTILIZATOR="+currentUser.getIdUser())){
                while(r.next()){
                    idSportList.add( r.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getSportsName(int idSport){
        try(Statement statement = c.createStatement()){
            try(ResultSet r = statement.executeQuery("SELECT DENUMIRE FROM SPORTURI WHERE ID="+idSport)){
                if(r.next()){
                    spnsport_items.add(r.getString(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isValidName(){
        if(iet_teamName.getText().length()<3){
            iet_teamName.setError(getString(R.string.add_team1_teamName_lengthError_hint));
            return false;
        }
        if(!iet_teamName.getText().toString().matches("^[a-zA-Z0-9_ ]*$")){
            iet_teamName.setError(getString(R.string.add_team1_teamName_digitAndLettersOnlyError_hint));
            return false;
        }
        return true;
    }
}
