package com.example.teammanagement.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import com.example.teammanagement.R;
import com.example.teammanagement.Utils.Constants;
import com.example.teammanagement.Utils.Team;
import com.example.teammanagement.Utils.User;
import com.example.teammanagement.adapters.TeamsAdapter;
import com.example.teammanagement.database.JDBCController;
import com.google.gson.Gson;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TeamsActivity extends AppCompatActivity {

    private ImageButton ibtn_back;
    private ImageButton ibtn_addTeam;
    private ListView lv_teams;

    private Intent intent;

    private TeamsAdapter adapter;

    private int longClickedTeamID;
    private User currentUser;
    private String currentUserRole;
    private List<Team> list_teams = new ArrayList<>();
    private List<Integer> list_currentUserTeamsID = new ArrayList<>();

    private Connection c;
    private JDBCController jdbcController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teams);

        initComponents();
    }
    private void initComponents() {

        jdbcController = JDBCController.getInstance();
        c=jdbcController.openConnection();

        ibtn_addTeam=findViewById(R.id.teams_ibtn_addTeam);
        ibtn_back=findViewById(R.id.teams_ibtn_back);
        lv_teams =findViewById(R.id.teams_lv_teams);

        ibtn_back.setOnClickListener(clickBack());
        ibtn_addTeam.setOnClickListener(clickAddTeam());
        lv_teams.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        getUser();
        getIDTeamsCurrentUser();
        for(int i=0;i < list_currentUserTeamsID.size() ;i++){
            getTeamInfo(list_currentUserTeamsID.get(i));
        }

        if(list_teams.size() != 0){
            adapter = new TeamsAdapter(getApplicationContext(), R.layout.list_item_team, list_teams, getLayoutInflater());
            lv_teams.setAdapter(adapter);
        }

        lv_teams.setOnItemClickListener(clickItem());
        lv_teams.setOnItemLongClickListener(leaveTeam());
    }

    public void getUser(){
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.APP_SHAREDPREF,MODE_PRIVATE);
        Gson gson = new Gson();
        String json =  sharedPreferences.getString(Constants.CURRENT_USER,"");
        currentUser= gson.fromJson(json, User.class);
    }

    public void getIDTeamsCurrentUser(){
        try(Statement s = c.createStatement()){
            try(ResultSet r = s.executeQuery("SELECT IDECHIPA FROM ROLECHIPA WHERE IDUTILIZATOR="+currentUser.getIdUser())){
                while(r.next()){
                    list_currentUserTeamsID.add(r.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getTeamInfo(int teamID){
        try(Statement s = c.createStatement()){
            try(ResultSet r = s.executeQuery("SELECT DENUMIRE, SPORT FROM ECHIPE WHERE ID="+teamID)){
                if(r.next()){
                   String denumire= r.getString(1);
                   String sport = r.getString(2);
                   Team team= new Team(teamID,denumire,sport);
                   list_teams.add(team);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getCurrentUserTeamRole(){
        try(Statement s = c.createStatement()){
            try(ResultSet r = s.executeQuery("SELECT DENUMIRE FROM ROLECHIPA WHERE IDUTILIZATOR="+currentUser.getIdUser()+" AND IDECHIPA="+longClickedTeamID)){
                if(r.next()){
                    currentUserRole=r.getString(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteRolEchipaUser(){
        try(Statement s =c.createStatement()){
            s.executeUpdate("DELETE FROM ROLECHIPA WHERE IDECHIPA="+longClickedTeamID+" AND IDUTILIZATOR="+currentUser.getIdUser());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int getNrCaptains(){
        try(Statement s = c.createStatement()) {
            try (ResultSet r = s.executeQuery(" select count(IDUtilizator) from RolEchipa where Denumire = N'CĂPITAN' and IDEchipa=" + longClickedTeamID)) {
                if(r.next()){
                    return r.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private void deleteTeam(){
        try(Statement s = c.createStatement()){
            s.executeUpdate("DELETE FROM ROLECHIPA WHERE IDECHIPA="+longClickedTeamID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private View.OnClickListener clickBack(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(getApplicationContext(),HomeActivity.class);
                startActivity(intent);
            }
        };
    }

    private View.OnClickListener clickAddTeam(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(getApplicationContext(), AddTeam1Activity.class);
                startActivity(intent);
            }
        };
    }

    private AdapterView.OnItemClickListener clickItem(){
        return new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Team team= (Team) parent.getItemAtPosition(position);
                intent = new Intent(getApplicationContext(), TeamProfileActivity.class);
                intent.putExtra(Constants.CURRENT_TEAM_ID,team.getId());
                startActivity(intent);
            }
        };
    }

    private AdapterView.OnItemLongClickListener leaveTeam()
    {
        return new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(TeamsActivity.this);
                builder.setTitle(getString(R.string.teams_alertDialog_title_hint))
                        .setMessage(getString(R.string.teams_alertDialog_message_hint))
                        .setNegativeButton(getString(R.string.teams_alertDialog_negativeButton_hint), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.teams_alertDialog_positiveButton_hint), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                longClickedTeamID=list_teams.get(position).getId();
                                getCurrentUserTeamRole();
                                deleteRolEchipaUser();
                                if(currentUserRole.equals(getString(R.string.role_captain_hint)) && getNrCaptains() == 0 ) {
                                    deleteTeam();
                                }
                                list_teams.remove(position);
                                adapter.notifyDataSetChanged();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                return true;
            }
        };
    }


}
