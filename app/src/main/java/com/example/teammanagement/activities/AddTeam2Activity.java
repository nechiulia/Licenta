package com.example.teammanagement.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.teammanagement.R;
import com.example.teammanagement.Utils.Constants;
import com.example.teammanagement.Utils.Team;
import com.example.teammanagement.Utils.User;
import com.example.teammanagement.adapters.AddUsersAdapter;
import com.example.teammanagement.adapters.ExpandableListReportAdminAdapter;
import com.example.teammanagement.adapters.ExploreUsersAdapter;
import com.example.teammanagement.database.JDBCController;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AddTeam2Activity extends AppCompatActivity {

    private Button btn_cancel;
    private Button btn_save;
    private ListView lv_users;
    Intent intent;
    private List<User> listUsers = new ArrayList<>();
    AddUsersAdapter adapter;
    JDBCController jdbcController;
    Connection c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_team2);

        jdbcController = JDBCController.getInstance();
        c=jdbcController.openConnection();
        intent=getIntent();
        initData();
        initComponents();
    }

    public void initComponents(){

        btn_cancel=findViewById(R.id.add_team2_btn_cancel);
        btn_save=findViewById(R.id.add_team2_btn_save);
        lv_users=findViewById(R.id.add_team2_lv_users);

        btn_save.setOnClickListener(clickSave());
        btn_cancel.setOnClickListener(clickCancel());


        if(listUsers.size() != 0 ) {
            adapter = new AddUsersAdapter(getApplicationContext(), R.layout.list_item_add_user_to_team, listUsers, getLayoutInflater());
            lv_users.setAdapter(adapter);
        }


    }

    public void initData(){
        try(Statement s = c.createStatement()){
            String command ="SELECT * FROM UTILIZATORI WHERE ROL=0 AND STARE=0;";
            try(ResultSet r =s.executeQuery(command)) {
                while(r.next()){
                    User user= new User(r.getInt(1),r.getString(2),
                            r.getString(3),r.getString(4),r.getInt(5),r.getBytes(6),r.getInt(7));
                    listUsers.add(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
                if(intent.hasExtra(Constants.ADDTEAM1_TEAM)){
                    Team team = (Team) intent.getSerializableExtra(Constants.ADDTEAM1_TEAM);
                    /*if(team != null){
                        try(Statement s = c.createStatement()){
                            String commandTeams ="INSERT INTO ECHIPE VALUES('"+team.getTeamName()+"','"+team.getSport()+"')";
                            int updatedRows=s.executeUpdate(commandTeams);
                            if(updatedRows >0 )Log.d("databaseUpdateUser",String.valueOf(updatedRows));
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }*/
                }
                Intent intent=new Intent(getApplicationContext(),TeamProfileActivity.class);
                startActivity(intent);
            }
        };
    }
}
