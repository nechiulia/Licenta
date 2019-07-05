package com.example.teammanagement.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.teammanagement.R;
import com.example.teammanagement.Utils.Constants;
import com.example.teammanagement.Utils.Team;
import com.example.teammanagement.Utils.User;
import com.example.teammanagement.adapters.AddUsersAdapter;
import com.example.teammanagement.database.JDBCController;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddTeam2Activity extends AppCompatActivity {

    private Button btn_cancel;
    private Button btn_save;
    private ListView lv_users;

    private Intent intent;

    private AddUsersAdapter adapter;

    private JDBCController jdbcController;
    private Connection c;

    private Team team;
    private User currentUser;
    private List<User> checkedUsers = new ArrayList<>();
    private List<User> listUsers = new ArrayList<>();
    private Map<Integer,byte[]> listUserPhotos = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_team2);

        jdbcController = JDBCController.getInstance();
        c=jdbcController.openConnection();

        intent=getIntent();

        getUser();
        getTeamInfo();
        initData();
        initComponents();
    }

    public void initComponents() {

        btn_cancel = findViewById(R.id.add_team2_btn_cancel);
        btn_save = findViewById(R.id.add_team2_btn_save);
        lv_users = findViewById(R.id.add_team2_lv_users);

        btn_save.setOnClickListener(clickSave());
        btn_cancel.setOnClickListener(clickCancel());

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


        if (listUsers.size() != 0) {
            adapter = new AddUsersAdapter(getApplicationContext(), R.layout.list_item_add_user_to_team, listUsers, getLayoutInflater());
            lv_users.setAdapter(adapter);
        }

    }

    public void initData(){
        try(Statement s = c.createStatement()){
            String command =" select u.ID, u.NumeUtilizator, " +
                    "u.Email, u.Parola, " +
                    "u.Stare, u.Rol" +
                    " from Utilizatori u, Sporturi s, " +
                    "SportUtilizator su where u.ID=su.IDUtilizator and " +
                    "su.IDSport=s.ID and u.rol=0 and u.Stare=0  AND u.ID!="+currentUser.getIdUser()+ "and s.Denumire='"+team.getSport()+"';";
            try(ResultSet r =s.executeQuery(command)) {
                while(r.next()){
                    User user= new User(r.getInt(1),r.getString(2),
                            r.getString(3),
                            r.getString(4),
                            r.getInt(5),
                            r.getInt(6));
                    listUsers.add(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getUser(){
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.APP_SHAREDPREF,MODE_PRIVATE);
        Gson gson = new Gson();
        String json =  sharedPreferences.getString(Constants.CURRENT_USER,"");
        currentUser= gson.fromJson(json,User.class);
    }

    public void getTeamInfo(){
        if(intent.hasExtra(Constants.ADDTEAM1_TEAM)){
            team = (Team) intent.getSerializableExtra(Constants.ADDTEAM1_TEAM);
        }
    }

    public void insertRole(int teamID,User user){
        try(Statement s = c.createStatement()){
            int updatedRows=s.executeUpdate("INSERT INTO ROLECHIPA VALUES("+user.getIdUser()+","+teamID+",N'JUCĂTOR');");
            if(updatedRows >0 ) {
                Log.d("databaseUpdated", String.valueOf(updatedRows));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void insertRoleCaptain(int teamID,User user){
        try(Statement s = c.createStatement()){
            int updatedRows=s.executeUpdate("INSERT INTO ROLECHIPA VALUES("+user.getIdUser()+","+teamID+",N'CĂPITAN');");
            if(updatedRows >0 ) {
                Log.d("databaseUpdated", String.valueOf(updatedRows));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getMinPlayers(){
        try(Statement s = c.createStatement()){
            try(ResultSet r = s.executeQuery("SELECT NRMINJUCATORI FROM SPORTURI WHERE DENUMIRE='"+team.getSport()+"';")){
                if(r.next()){
                    return r.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int insertTeam(){
        if(intent.hasExtra(Constants.ADDTEAM1_TEAM)){
            Team team = (Team) intent.getSerializableExtra(Constants.ADDTEAM1_TEAM);
            if(team != null){
                try(PreparedStatement s = c.prepareStatement("INSERT INTO ECHIPE VALUES('"+team.getTeamName()+"','"+team.getSport()+"')",Statement.RETURN_GENERATED_KEYS)){
                    int updatedRows= s.executeUpdate();
                    ResultSet r=s.getGeneratedKeys();
                    if(r.next()){
                        if(updatedRows >0 ) {
                            Log.d("databaseUpdateUser", String.valueOf(updatedRows));
                            return r.getInt(1);
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return -1;
    }

    public void getUserProfilePicture(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        for(User user: listUsers) {
            DatabaseReference myRef = database.getReference(String.valueOf(user.getIdUser()));
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String valoare = dataSnapshot.getValue(String.class);
                    String key=dataSnapshot.getKey();
                    if( valoare != null) {
                        byte[] decodedString = Base64.decode(valoare, Base64.DEFAULT);
                        listUserPhotos.put(Integer.parseInt(key),decodedString);
                        for(User u:listUsers ) {
                            if(Integer.parseInt(key) == u.getIdUser()) {
                                u.setProfilePicture(decodedString);
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

    private View.OnClickListener clickCancel() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),TeamsActivity.class);
                startActivity(intent);
            }
        };
    }

    private View.OnClickListener clickSave() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int teamID=insertTeam();
                if(teamID != -1) {
                    int minPlayers= getMinPlayers();
                    checkedUsers = adapter.getCheckedUsers();
                    if(checkedUsers.size() >=(minPlayers-1)  && checkedUsers.size()>0) {
                        for(User user:checkedUsers) {
                            insertRole(teamID,user);
                        }
                        insertRoleCaptain(teamID,currentUser);
                        Intent intent = new Intent(getApplicationContext(), TeamsActivity.class);
                        startActivity(intent);
                    }
                    else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(AddTeam2Activity.this);
                        builder.setTitle(getString(R.string.add_team2_alertDialog_title))
                                .setMessage(getString(R.string.add_team2_alertDialog_message)+" "+minPlayers)
                                .setCancelable(false)
                                .setPositiveButton(getString(R.string.editProfile_alertDialog_listEmpty_hint), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                }
            }
        };
    }

}
