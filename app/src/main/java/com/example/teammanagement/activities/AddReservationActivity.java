package com.example.teammanagement.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.teammanagement.R;
import com.example.teammanagement.Utils.Activity;
import com.example.teammanagement.Utils.Constants;
import com.example.teammanagement.Utils.Team;
import com.example.teammanagement.Utils.User;
import com.example.teammanagement.adapters.SpinnerAdapter;
import com.example.teammanagement.database.JDBCController;
import com.example.teammanagement.dialogs.ReservationAdminLocationDialog;
import com.example.teammanagement.dialogs.ReservationUserDialog;
import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddReservationActivity extends AppCompatActivity implements ReservationUserDialog.ReservationUserListener {

    private Spinner spn_activity;
    private Spinner spn_team;
    private CalendarView calendarView;
    private Button btn_save;
    private TextView tv_team;
    private TextView tv_activity;

    private SpinnerAdapter spn_activity_adapter;
    private SpinnerAdapter spn_teams_adapter;

    private Intent intent;

    private JDBCController jdbcController;
    private Connection c;

    private int currentLocationID;
    private User currentUser;
    private int minPlayers;
    private int ok=0;
    private String selectedDate;
    private String selectedInterval;
    private int selectedActivityID;
    private Team selectedTeam;
    private List<String> spn_activities_items = new ArrayList<>();
    private Activity selectedActivity;
    private List<Team> list_teams = new ArrayList<>();
    private List<String> spn_teams_items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reservation);

        initComponents();
    }

    public void initComponents(){

        jdbcController= JDBCController.getInstance();
        c=jdbcController.openConnection();

        intent=getIntent();

        if(intent.hasExtra(Constants.CURRENT_LOCATION_ID)){
            currentLocationID= (int) intent.getSerializableExtra(Constants.CURRENT_LOCATION_ID);
        }

        sharedCurrentUser();

        spn_activity = findViewById(R.id.activity_add_reservation_spn_activity);
        spn_team=findViewById(R.id.activity_add_reservation_spn_team);
        calendarView=findViewById(R.id.activity_add_reservation_cv);
        btn_save=findViewById(R.id.activity_add_reservation_btn_save);
        tv_team=findViewById(R.id.activity_add_reservation_tv_team);
        tv_activity=findViewById(R.id.activity_add_reservation_tv_activity);

        spn_activities_items.add(getString(R.string.fragment_locations_reservations_spn_item0_hint));
        selectReservationActivities();

        spn_activity_adapter = new SpinnerAdapter(getBaseContext(),R.layout.white_spinner_item,spn_activities_items,getLayoutInflater());
        spn_activity.setAdapter(spn_activity_adapter);

        spn_activity.setOnItemSelectedListener(isSelectedActivity());
        btn_save.setOnClickListener(clickSave());

        calendarView.setOnClickListener(clickDate());
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                if(spn_activity.getSelectedItemPosition()!=0) {
                    int monthCorrect = month + 1;
                    selectedDate = dayOfMonth + "/" + monthCorrect + "/" + year;
                    try {
                        Date date = new SimpleDateFormat("dd/MM/yyyy").parse(selectedDate);
                        Date currentDate = new Date();
                        if (date.after(currentDate)) {
                            openDialog();
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(AddReservationActivity.this);
                            builder.setTitle("Detalii rezervare")
                                    .setMessage("Nu puteți face rezervări pentru zilele anterioare. Selectați o dată viitoare.")
                                    .setPositiveButton(getString(R.string.editProfile_alertDialog_listEmpty_hint), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddReservationActivity.this);
                    builder.setTitle("Detalii rezervare")
                            .setMessage("Pentru a putea vizualiza orele disponibile pentru rezervare, este necesar să selectați o activitate.")
                            .setPositiveButton(getString(R.string.editProfile_alertDialog_listEmpty_hint), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });
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

    public void sharedCurrentUser(){
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.APP_SHAREDPREF,MODE_PRIVATE);
        Gson gson = new Gson();
        String json =  sharedPreferences.getString(Constants.CURRENT_USER,"");
        currentUser = gson.fromJson(json, User.class);
    }

    public void selectReservationActivities(){
        try(Statement s = c.createStatement()){
            try(ResultSet r = s.executeQuery("SELECT DENUMIRE FROM ACTIVITATI WHERE IDLOCATIE="+currentLocationID+" AND REZERVARI=1")) {
                while (r.next()){
                    spn_activities_items.add(r.getString(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getSelectedActivity(){
        try(Statement s = c.createStatement()){
            try(ResultSet r = s.executeQuery("SELECT * FROM ACTIVITATI WHERE IDLOCATIE="+currentLocationID+ "AND DENUMIRE='"+spn_activity.getSelectedItem().toString()+"'")){
                if(r.next()){
                    selectedActivity = new Activity();
                    selectedActivity.setActivityID(r.getInt(1));
                    selectedActivity.setActivityName(r.getString(2));
                    selectedActivity.setSport(r.getString(3));
                    selectedActivity.setTrainer(r.getString(4));
                    String difficulty = getLevel(r.getInt(5));
                    selectedActivity.setDifficultyLevel(difficulty);
                    selectedActivity.setPrice(r.getDouble(6));
                    selectedActivity.setReservation(r.getInt(7));
                    selectedActivity.setLocationID(currentLocationID);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getSelectedSportMinPlayers(){
        try(Statement s = c.createStatement()){
            try(ResultSet r = s.executeQuery("SELECT NRMINJUCATORI FROM SPORTURI WHERE DENUMIRE='"+selectedActivity.getSport()+"'")){
                if(r.next()){
                    minPlayers= r.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void selectTeams(){
        try(Statement s = c.createStatement()){
            try(ResultSet r = s.executeQuery("SELECT * FROM ECHIPE e, ROLECHIPA r WHERE e.ID=R.IDECHIPA AND R.IDUTILIZATOR="+currentUser.getIdUser()+ " AND e.SPORT='"+selectedActivity.getSport()+"'" +
                    " AND R.DENUMIRE=N'CĂPITAN'")){
                while(r.next()){
                    Team team = new Team();
                    team.setId(r.getInt(1));
                    team.setTeamName(r.getString(2));
                    team.setSport(r.getString(3));
                    list_teams.add(team);
                    spn_teams_items.add(team.getTeamName());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void getActivityID(String activityName){
        try(Statement s = c.createStatement()){
            try(ResultSet r = s.executeQuery("SELECT ID FROM ACTIVITATI WHERE DENUMIRE='"+activityName+"' AND IDLOCATIE="+currentLocationID)){
                if(r.next()){
                    selectedActivityID = r.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getTeamPlayerNumbers(){
        if(selectedTeam!=null){
            try(Statement s = c.createStatement()){
                try(ResultSet r = s.executeQuery("SELECT COUNT(IDUTILIZATOR) FROM ROLECHIPA WHERE IDECHIPA="+selectedTeam.getId())){
                    if(r.next()){
                       return r.getInt(1);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    public void insertReservation(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        String bookedDate =simpleDateFormat.format(date);
        String[] t = selectedInterval.split("-");
        String startHour= t[0];
        String finishHour=t[1];
        try(PreparedStatement s = c.prepareStatement("INSERT INTO REZERVARI(DATA,DATAREALIZARE," +
                "ORAINCEPERE,ORAINCHEIERE,IDECHIPA,IDACTIVITATE,IDUTILIZATOR,STARE) " +
                "VALUES(?,?,?,?,?,?,?,?)")){
            s.setString(1,selectedDate);
            s.setString(2,bookedDate);
            s.setString(3,startHour);
            s.setString(4,finishHour);
            s.setInt(6,selectedActivity.getActivityID());
            s.setInt(5,selectedTeam.getId());
            s.setInt(7,currentUser.getIdUser());
            s.setInt(8,0);
            s.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void openDialog(){
        Bundle args = new Bundle();
        args.putSerializable(Constants.CURRENT_LOCATION_ID, currentLocationID);
        args.putSerializable(Constants.SELECTED_ACTIVITY_NAME,spn_activity.getSelectedItem().toString());
        args.putSerializable(Constants.SELECTED_DATE,selectedDate);

        ReservationUserDialog reservationUserDialog = new ReservationUserDialog();
        reservationUserDialog.show(getSupportFragmentManager(),getString(R.string.addReservation_addReservation_hint));

        reservationUserDialog.setArguments(args);
        reservationUserDialog.setCancelable(false);
    }

    public AdapterView.OnItemSelectedListener isSelectedActivity() {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0 ) {
                    list_teams.clear();
                    spn_teams_items.clear();
                    selectedTeam=null;
                    spn_team.setSelection(0);
                    spn_teams_items.add(getString(R.string.add_reservation_0_item_spinner_teams));
                    getActivityID(spn_activities_items.get(position));
                    getSelectedActivity();
                    getSelectedSportMinPlayers();
                    selectTeams();
                    if (ok == 0) {
                        spn_teams_adapter = new SpinnerAdapter(getBaseContext(), R.layout.white_spinner_item, spn_teams_items, getLayoutInflater());
                        spn_team.setAdapter(spn_teams_adapter);
                        spn_team.setOnItemSelectedListener(selectTeamSpn());
                        ok = 1;
                    }
                }

                }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    }

    public AdapterView.OnItemSelectedListener selectTeamSpn() {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0) {
                    selectedTeam = list_teams.get(position - 1);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    }

    private View.OnClickListener clickDate() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        };
    }


    private View.OnClickListener clickSave() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValidInterval() && isValidSpnActivity()&& isValidTeam()) {
                    insertReservation();
                    intent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent);
                }
            }
        };
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        intent=new Intent(getApplicationContext(),MapsActivity.class);
        startActivity(intent);
    }


    public boolean isValidInterval(){
        if(selectedInterval == null){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Detalii rezervare")
                    .setMessage("Trebuie să selectați un interval orar pentru rezervare! Dați click pe ziua în care doriți să faceți rezervarea și apoi click pe intervalul orar potrivit pentru dvs. ")
                    .setPositiveButton(getString(R.string.editProfile_alertDialog_listEmpty_hint), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
            return false;
        }
        return true;
    }


    public boolean isValidSpnActivity(){
        if(spn_activity.getSelectedItemPosition()==0){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Detalii rezervare")
                    .setMessage("Trebuie să selectați o activitate pentru a putea trece la următorul pas.")
                    .setPositiveButton(getString(R.string.editProfile_alertDialog_listEmpty_hint), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
            return false;
        }
        return true;
    }

    public boolean isValidTeam(){
        int teamPlayers=getTeamPlayerNumbers();

        if(spn_team.getSelectedItemPosition()==0 ){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Detalii rezervare")
                    .setMessage("Trebuie să selectați o echipă pentru a putea trece la următorul pas.")
                    .setPositiveButton(getString(R.string.editProfile_alertDialog_listEmpty_hint), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
            return false;
        }
        if(teamPlayers < minPlayers){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Detalii rezervare")
                    .setMessage("Echipa selectată nu are suficienți jucători pentru a realiza această rezervare. Numărul minim de jucători pentru acest sport este: "+minPlayers)
                    .setPositiveButton(getString(R.string.editProfile_alertDialog_listEmpty_hint), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
            return false;
        }
        return true;
    }

    @Override
    public void applyTexts(String inteval) {
        selectedInterval=inteval;
    }
}
