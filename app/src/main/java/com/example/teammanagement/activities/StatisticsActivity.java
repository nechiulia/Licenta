package com.example.teammanagement.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.teammanagement.R;
import com.example.teammanagement.Utils.Constants;
import com.example.teammanagement.Utils.User;
import com.example.teammanagement.fragments.RankingUsersFragment;
import com.example.teammanagement.fragments.SportsRankingFragment;
import com.google.gson.Gson;

public class StatisticsActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private Intent intent;

    private BottomNavigationView navigation;

    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        sharedCurrentUser();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.statistics_bottomNavigationView);
        navigation.setOnNavigationItemSelectedListener(this);
        loadFragment(new SportsRankingFragment());

    }

    public void sharedCurrentUser(){
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.APP_SHAREDPREF,MODE_PRIVATE);
        Gson gson = new Gson();
        String json =  sharedPreferences.getString(Constants.CURRENT_USER,"");
        currentUser = gson.fromJson(json, User.class);
    }

    private boolean loadFragment(Fragment fragment){
        if(fragment != null){

            Bundle bundle = new Bundle();
            bundle.putInt(Constants.CURRENT_USER_ID,currentUser.getIdUser());
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.statistics_fragment_containerAdmin,fragment).commit();
            return true;
        }
        return false;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;

        switch (menuItem.getItemId()){
            case R.id.navigation_statistics_ranking:
                fragment = new RankingUsersFragment();
                break;
            case R.id.navigation_statistics_sports:
                fragment = new SportsRankingFragment();
                break;


        }
        return loadFragment(fragment);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        intent=new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
    }
}
