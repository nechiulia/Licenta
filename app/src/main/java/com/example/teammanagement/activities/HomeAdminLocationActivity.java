package com.example.teammanagement.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.teammanagement.R;
import com.example.teammanagement.Utils.Constants;
import com.example.teammanagement.Utils.User;
import com.example.teammanagement.dialogs.AddActivityDialog;
import com.example.teammanagement.fragments.EditLocationFragment;
import com.example.teammanagement.fragments.LocationProfileFragment;
import com.example.teammanagement.fragments.LocationsReservationsFragment;
import com.google.gson.Gson;

public class HomeAdminLocationActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {


    private User currentUser;

    public void sharedCurrentUser(){
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.APP_SHAREDPREF,MODE_PRIVATE);
        Gson gson = new Gson();
        String json =  sharedPreferences.getString(Constants.CURRENT_USER,"");
        currentUser = gson.fromJson(json, User.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_admin_location);
        sharedCurrentUser();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.home_admin_location_bottomNavigationView);
        navigation.setOnNavigationItemSelectedListener(this);
        loadFragment(new LocationProfileFragment());

    }
    private boolean loadFragment(Fragment fragment){
        if(fragment != null){

            Bundle bundle = new Bundle();
            bundle.putInt(Constants.CURRENT_USER_ID,currentUser.getIdUser());
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.home_admin_location_fragment_containerAdmin,fragment).commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;

        switch (menuItem.getItemId()){
            case R.id.navigation_locationProfile:
                fragment = new LocationProfileFragment();
                break;
            case R.id.navigation_editProfile:
                fragment = new EditLocationFragment();
                break;
            case R.id.navigation_reservations:
                fragment = new LocationsReservationsFragment();
                break;

        }
        return loadFragment(fragment);
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }
}
