package com.example.teammanagement.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.teammanagement.R;
import com.example.teammanagement.database.JDBCController;

import java.sql.Connection;

public class TeamReservationsActivity extends AppCompatActivity {

    private ListView lv_reservations;

    private Intent intent;

    private JDBCController jdbcController;
    private Connection c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_reservations);

        initComponents();
    }

    public void initComponents(){
        jdbcController= JDBCController.getInstance();
        c=jdbcController.openConnection();


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        intent=new Intent(getApplicationContext(),MapsActivity.class);
        startActivity(intent);
    }
}
