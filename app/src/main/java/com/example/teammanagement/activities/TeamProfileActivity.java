package com.example.teammanagement.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.teammanagement.R;
import com.example.teammanagement.Utils.Constants;
import com.example.teammanagement.Utils.Team;
import com.example.teammanagement.Utils.Teammate;
import com.example.teammanagement.Utils.User;
import com.example.teammanagement.adapters.TeammatesAdapter;
import com.example.teammanagement.database.JDBCController;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeamProfileActivity extends AppCompatActivity {

    private ImageButton ibtn_back;
    private ImageButton ibtn_edit;
    private TextView tv_teamName;
    private TextView tv_teamSport;
    private ListView lv_teammates;
    private TextView tv_announcements;
    private  TextView tv_reservations;

    private Intent intent;

    private TeammatesAdapter adapter;

    private Connection c;
    private JDBCController jdbcController;

    private User currentUser;
    private int currentTeamID;
    private Team currentTeam = new Team();
    private String currentUserRole;
    private List<Integer> list_teammatesID = new ArrayList<>();
    private List<Teammate> list_teammates = new ArrayList<>();
    private Map<Integer,byte[]> list_teammatesPhotos = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_profile);

        intent=getIntent();

        initComponents();
    }

    public void initComponents(){

        jdbcController = JDBCController.getInstance();
        c=jdbcController.openConnection();

        ibtn_back=findViewById(R.id.teamProfile_ibtn_back);
        ibtn_edit=findViewById(R.id.teamProfile_ibtn_edit);
        tv_teamName=findViewById(R.id.teamProfile_tv_teamName);
        tv_teamSport=findViewById(R.id.teamProfile_tv_sport);
        lv_teammates=findViewById(R.id.teamProfile_lv_players);
        tv_announcements=findViewById(R.id.teamProfile_tv_announcements);
        tv_reservations=findViewById(R.id.teamProfile_tv_reservation);

        getUser();
        getTeam();
        selectTeamInfo();
        selectTeammatesID();

        for(int i = 0; i< list_teammatesID.size(); i++){
            getTeammatesInfo(list_teammatesID.get(i));
        }

        for(Teammate teammate: list_teammates){
            getRoles(teammate);
        }

        Thread t1 = new Thread(){
            @Override
            public void run() {
                getUserProfilePicture();
            }
        };
        t1.start();
        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        tv_teamName.setText(currentTeam.getTeamName());
        tv_teamSport.setText(currentTeam.getSport());

        if(list_teammates.size() != 0){
            adapter = new TeammatesAdapter(getApplicationContext(), R.layout.list_item_teammates, list_teammates, getLayoutInflater());
            lv_teammates.setAdapter(adapter);
        }


        ibtn_back.setOnClickListener(clickBack());
        ibtn_edit.setOnClickListener(clickEdit());
        tv_announcements.setOnClickListener(clickAnnouncements());
        tv_reservations.setOnClickListener(clickReservations());

        getCurrentUserTeamRole();
        if(!currentUserRole.equals(getString(R.string.role_captain_hint)))ibtn_edit.setVisibility(View.GONE);
    }

    public void getUser(){
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.APP_SHAREDPREF,MODE_PRIVATE);
        Gson gson = new Gson();
        String json =  sharedPreferences.getString(Constants.CURRENT_USER,"");
        currentUser= gson.fromJson(json, User.class);
    }



    public void getTeam(){

        if(intent.hasExtra(Constants.CURRENT_TEAM_ID)){
            currentTeamID=(Integer) intent.getSerializableExtra(Constants.CURRENT_TEAM_ID);
        }

        getTeamInfo();
    }

    public void getTeamInfo(){
        try(Statement s = c.createStatement()){
            try(ResultSet r = s.executeQuery("SELECT * FROM ECHIPE WHERE ID="+currentTeamID)){
                if(r.next()){
                    currentTeam.setId(currentTeamID);
                    currentTeam.setTeamName(r.getString(2));
                    currentTeam.setSport(r.getString(3));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void selectTeammatesID(){
        try(Statement s = c.createStatement()){
            try(ResultSet r = s.executeQuery("SELECT IDUTILIZATOR FROM ROLECHIPA WHERE IDECHIPA="+currentTeam.getId()+" AND IDUTILIZATOR!="+currentUser.getIdUser())){
                while(r.next()){
                    list_teammatesID.add(r.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void selectTeamInfo(){
        try(Statement s = c.createStatement()){
            try(ResultSet r = s.executeQuery("SELECT DENUMIRE,SPORT FROM ECHIPE WHERE ID= "+currentTeam.getId())){
                if(r.next()) {
                    currentTeam.setTeamName(r.getString(1));
                    currentTeam.setSport(r.getString(2));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getCurrentUserTeamRole(){
        try(Statement s = c.createStatement()){
            try(ResultSet r = s.executeQuery("SELECT DENUMIRE FROM ROLECHIPA WHERE IDUTILIZATOR="+currentUser.getIdUser()+" AND IDECHIPA="+currentTeam.getId())){
                if(r.next()){
                    currentUserRole=r.getString(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getUserProfilePicture(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        for(Teammate user: list_teammates) {
            DatabaseReference myRef = database.getReference(String.valueOf(user.getUserID()));
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String valoare = dataSnapshot.getValue(String.class);
                    String key=dataSnapshot.getKey();
                    if( valoare != null) {
                        byte[] decodedString = Base64.decode(valoare, Base64.DEFAULT);
                        list_teammatesPhotos.put(Integer.parseInt(key),decodedString);
                        for(Teammate u: list_teammates) {
                            if(Integer.parseInt(key) == u.getUserID()) {
                                u.setUserProfilePicture(decodedString);
                                break;
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError error) {
                }
            });
        }
    }

    public void getTeammatesInfo(int userID){
        try(Statement s = c.createStatement()){
            String command ="SELECT ID,NUMEUTILIZATOR FROM UTILIZATORI WHERE ROL=0 AND STARE=0 AND ID="+userID+";";
            try(ResultSet r =s.executeQuery(command)) {
                if(r.next()){
                    Teammate user= new Teammate();
                    user.setUserID(r.getInt(1));
                    user.setUserName(r.getString(2));
                    list_teammates.add(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getRoles(Teammate teammate){
        try(Statement s = c.createStatement()){
            try(ResultSet r = s.executeQuery("SELECT DENUMIRE FROM ROLECHIPA WHERE IDECHIPA="+currentTeam.getId()+" AND IDUTILIZATOR="+teammate.getUserID())){
                if(r.next()){
                    teammate.setTeamRole(r.getString(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private View.OnClickListener clickBack() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(getApplicationContext(), TeamsActivity.class);
                startActivity(intent);
            }
        };
    }

    private View.OnClickListener clickEdit() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(getApplicationContext(), EditTeamActivity.class);
                intent.putExtra(Constants.CURRENT_TEAM_ID,currentTeam.getId());
                startActivity(intent);
            }
        };
    }

    private View.OnClickListener clickAnnouncements() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(getApplicationContext(), AnnouncementsActivity.class);
                intent.putExtra(Constants.CURRENT_TEAM_ID,currentTeam.getId());
                startActivity(intent);
            }
        };
    }

    private View.OnClickListener clickReservations() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(getApplicationContext(), TeamReservationsActivity.class);
                intent.putExtra(Constants.CURRENT_TEAM_ID,currentTeam.getId());
                startActivity(intent);
            }
        };
    }


}
