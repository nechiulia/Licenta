package com.example.teammanagement.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DisableAccountActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_disable_account);
        initComponents();
    }
    private void initComponents() {

        jdbcController=JDBCController.getInstance();
        c=jdbcController.openConnection();

        ibtn_back=findViewById(R.id.disableAccount_ibtn_back);
        btn_no=findViewById(R.id.disableAccount_btn_no);
        btn_yes=findViewById(R.id.disableAccount_btn_yes);

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
                if (getNumberReservations() > 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(DisableAccountActivity.this);
                    builder.setTitle(getString(R.string.delete_account_altertbuilder_title))
                            .setMessage(getString(R.string.disable_account_altertbuilder_message_hint))
                            .setCancelable(false)
                            .setPositiveButton(getString(R.string.editProfile_alertDialog_listEmpty_hint), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                } else {

                    if (getNumberTeams() > 0) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(DisableAccountActivity.this);
                        builder.setTitle(getString(R.string.delete_account_altertbuilder_title))
                                .setMessage(getString(R.string.disable_account_alertBuilder_message_teams_hint))
                                .setCancelable(false)
                                .setPositiveButton(getString(R.string.editProfile_alertDialog_listEmpty_hint), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    } else {
                        updateState();
                        currentUser.setState(2);
                        saveCurrentUser();
                        intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                    }
                }
            }
        };
    }

    public void updateState(){
        try(Statement statement = c.createStatement()){
            statement.executeUpdate("UPDATE UTILIZATORI SET STARE=1 WHERE ID="+currentUser.getIdUser());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveCurrentUser(){
        sharedPreferences=getSharedPreferences(Constants.APP_SHAREDPREF,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(currentUser);
        editor.putString(Constants.CURRENT_USER,json);
        editor.apply();
    }

    public int getNumberTeams(){
        try(Statement s = c.createStatement()){
            try(ResultSet r = s.executeQuery("SELECT COUNT(IDUTILIZATOR) FROM ROLECHIPA WHERE DENUMIRE=N'CĂPITAN' AND IDUTILIZATOR="+currentUser.getIdUser())){
                if(r.next()){
                    return r.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int getNumberReservations(){

        try(Statement s = c.createStatement()){
            try(ResultSet r = s.executeQuery("  SELECT COUNT(ID) FROM REZERVARI WHERE IDECHIPA IN (SELECT IDECHIPA FROM RolEchipa WHERE IDUtilizator ="+currentUser.getIdUser()+") AND STARE=0;")){
                if(r.next()){
                    return r.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
