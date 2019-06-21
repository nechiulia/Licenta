package com.example.teammanagement.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.teammanagement.R;

public class TeamsActivity extends AppCompatActivity {

    private ImageButton ibtn_back;
    private ImageButton ibtn_addTeam;
    private ListView lv_echipe;
    private  Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teams);
        initComponents();
    }
    private void initComponents() {

        ibtn_addTeam=findViewById(R.id.teams_ibtn_addTeam);
        ibtn_back=findViewById(R.id.teams_ibtn_back);
        lv_echipe=findViewById(R.id.teams_lv_teams);

        ibtn_back.setOnClickListener(clickBack());
        ibtn_addTeam.setOnClickListener(clickAddTeam());
        lv_echipe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
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

    private AdapterView.OnItemClickListener clickTeam(){
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent=new Intent(getApplicationContext(), TeamProfileActivity.class);
                startActivity(intent);
            }
        };
    }


}
