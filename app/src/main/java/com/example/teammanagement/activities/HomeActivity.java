package com.example.teammanagement.activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.teammanagement.R;
import com.example.teammanagement.Utils.Constants;
import com.example.teammanagement.Utils.Question;
import com.example.teammanagement.Utils.User;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements Serializable {
    ImageButton btn_profile;
    ImageButton btn_users;
    ImageButton btn_map;
    ImageButton btn_teams;
    ImageButton btn_faq;
    ImageButton btn_contact;
    Intent intent;
    private List<String> listQuestions = new ArrayList<>();
    private List<Question> lista=new ArrayList<>();
    private HashMap<String,List<String>> listaAnswers= new HashMap<>();
    private static final String URL = Constants.FAQ_URL;
    private static final String TAG = "HomeActivity";
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initComponents();

    }

    private void initComponents(){
        btn_profile=findViewById(R.id.home_ibtn_profile);
        btn_users=findViewById(R.id.home_ibtn_users);
        btn_map=findViewById(R.id.home_ibtn_map);
        btn_teams=findViewById(R.id.home_ibtn_teams);
        btn_faq=findViewById(R.id.home_ibtn_faq);
        btn_contact=findViewById(R.id.home_ibtn_contact);

        btn_profile.setOnClickListener(clickProfile());
        btn_users.setOnClickListener(clickUsers());
        btn_map.setOnClickListener(clickMaps());
        btn_teams.setOnClickListener(clickTeams());
        btn_faq.setOnClickListener(clickFaq());
        btn_contact.setOnClickListener(clickContact());

        SharedPreferences sharedPreferences = getSharedPreferences(Constants.APP_SHAREDPREF,MODE_PRIVATE);
        Gson gson = new Gson();
        String json =  sharedPreferences.getString(Constants.CURRENT_USER,"");
        user= gson.fromJson(json,User.class);
        Log.d("Current User",user.getUserName());

    }

    public boolean isServiceOK(){
        Log.d(TAG,getString(R.string.home_GoogleServices_check_hint));
        int avalible = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(HomeActivity.this);

        if(avalible == ConnectionResult.SUCCESS){
            Log.d(TAG,getString(R.string.home_GoogleServices_fine_hint));
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(avalible)){
            Log.d(TAG,getString(R.string.home_GoogleServices_errorOccured_hint));
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(HomeActivity.this,avalible,Constants.ERROR_DIALOG_REQUEST);
            dialog.show();
        }
        else{
            Toast.makeText(this, getString(R.string.home_googleMaps_errorMessage_hint), Toast.LENGTH_LONG).show();
        }
        return false;
    }

    private View.OnClickListener clickProfile() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), MyProfileActivity.class);
                startActivity(intent);
            }
        };
    }

    private View.OnClickListener clickUsers() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), SearchUserActivity.class);
                startActivity(intent);
            }
        };
    }

    private View.OnClickListener clickMaps() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isServiceOK()) {
                    intent = new Intent(getApplicationContext(), MapsActivity.class);
                    startActivity(intent);
                }
            }
        };
    }

    private View.OnClickListener clickTeams() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), TeamsActivity.class);
                startActivity(intent);
            }
        };
    }

    private View.OnClickListener clickFaq() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), FaqActivity.class);
                startActivity(intent);
            }
        };
    }

    private View.OnClickListener clickContact() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isServiceOK()) {
                    intent = new Intent(getApplicationContext(), ContactActivity.class);
                    startActivity(intent);
                }
            }
        };
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
