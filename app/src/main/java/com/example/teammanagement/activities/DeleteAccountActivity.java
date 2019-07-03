package com.example.teammanagement.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.teammanagement.R;
import com.example.teammanagement.Utils.Constants;
import com.example.teammanagement.Utils.User;
import com.example.teammanagement.database.JDBCController;
import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DeleteAccountActivity extends AppCompatActivity {

    Intent intent;
    ImageButton ibtn_back;
    Button btn_yes;
    Button btn_no;
    SharedPreferences sharedPreferences;
    User currentUser;
    Connection c;
    JDBCController jdbcController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_account);
        initComponents();
    }
    private void initComponents() {

        jdbcController=JDBCController.getInstance();
        c=jdbcController.openConnection();

        ibtn_back=findViewById(R.id.deleteAccount_ibtn_back);
        btn_no=findViewById(R.id.deleteAccount_btn_no);
        btn_yes=findViewById(R.id.deleteAccount_btn_yes);

        ibtn_back.setOnClickListener(clickBack());
        btn_no.setOnClickListener(clickNo());
        btn_yes.setOnClickListener(clickYes());
        getUser();
    }

    public void getUser(){
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.APP_SHAREDPREF,MODE_PRIVATE);
        Gson gson = new Gson();
        String json =  sharedPreferences.getString(Constants.CURRENT_USER,"");
        currentUser = gson.fromJson(json, User.class);
    }

    private View.OnClickListener clickBack() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(getApplicationContext(),SettingsActivity.class);
                startActivity(intent);
            }
        };
    }

    private View.OnClickListener clickNo() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(getApplicationContext(),SettingsActivity.class);
                startActivity(intent);
            }
        };
    }

    private View.OnClickListener clickYes() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateState();
                currentUser.setState(2);
                saveCurrentUser();
                intent=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
            }
        };
    }

    public void saveCurrentUser(){
        sharedPreferences=getSharedPreferences(Constants.APP_SHAREDPREF,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(currentUser);
        editor.putString(Constants.CURRENT_USER,json);
        editor.apply();
    }

    public void updateState(){
        try(Statement statement = c.createStatement()){
            statement.executeUpdate("UPDATE UTILIZATORI SET STARE=2 WHERE ID="+currentUser.getIdUser());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
