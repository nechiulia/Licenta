package com.example.teammanagement.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.teammanagement.R;
import com.example.teammanagement.adapters.ExpandableListNewLocationAdminAdapter;
import com.example.teammanagement.fragments.NewLocationsFragment;
import com.example.teammanagement.fragments.ReportsFragment;
import com.example.teammanagement.fragments.SearchLocationFragment;

public class HomeAdminActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, ExpandableListNewLocationAdminAdapter.OnSwitchFragment {

    private TextView tv_title;
    private ImageButton ibtn_aprove;
    private ImageButton ibtn_remove;

    private BottomNavigationView  navigation;

    private Intent intent;

    private final Fragment fragment_newLocations = new NewLocationsFragment();
    private final Fragment fragment_searchLocation = new SearchLocationFragment();
    private  final Fragment fragment_reports = new ReportsFragment();
    private final FragmentManager fm = getSupportFragmentManager();
    private Fragment active = fragment_reports;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_admin);
        fm.beginTransaction().add(R.id.home_admin_fragment_containerAdmin, fragment_newLocations, "newLoc").hide(fragment_newLocations).commit();
        fm.beginTransaction().add(R.id.home_admin_fragment_containerAdmin, fragment_searchLocation, "searchLoc").hide(fragment_searchLocation).commit();
        fm.beginTransaction().add(R.id.home_admin_fragment_containerAdmin, fragment_reports, "reports").commit();


        navigation = findViewById(R.id.home_admin_bottomNavigationView);
        navigation.setOnNavigationItemSelectedListener(this);
        navigation.setSelectedItemId(R.id.navigation_reports);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;

        switch (menuItem.getItemId()){
            case R.id.navigation_reports:
                fm.beginTransaction().hide(active).show(fragment_reports).commit();
                active = fragment_reports;
                return true;
            case R.id.navigation_newLocations:
                fm.beginTransaction().hide(active).show(fragment_newLocations).commit();
                active = fragment_newLocations;
                return true;
            case R.id.navigation_map:
                fm.beginTransaction().hide(active).show(fragment_searchLocation).commit();
                active = fragment_searchLocation;
                return true;

        }
        return false;
    }

    @Override
    public void onSwitchFragment(int id) {
        navigation.setSelectedItemId(id);
    }

    @Override
    public void onBackPressed() {
        intent=new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }
}
