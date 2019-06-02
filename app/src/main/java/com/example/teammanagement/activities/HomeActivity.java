package com.example.teammanagement.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.teammanagement.R;
import com.example.teammanagement.Utils.Constants;
import com.example.teammanagement.Utils.Question;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements Serializable {
    ImageButton btn_profile;
    ImageButton btn_friends;
    ImageButton btn_map;
    ImageButton btn_teams;
    ImageButton btn_faq;
    ImageButton btn_contact;
    Intent intent;
    private List<String> listQuestions = new ArrayList<>();
    private List<Question> lista=new ArrayList<>();
    private HashMap<String,List<String>> listaAnswers= new HashMap<>();
    private static final String URL = Constants.FAQ_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initComponents();

        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), MyProfileActivity.class);
                startActivity(intent);
            }
        });
        btn_friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), SearchUserActivity.class);
                startActivity(intent);
            }
        });

        btn_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), MapActivity.class);
                startActivity(intent);
            }
        });

        btn_teams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), TeamsActivity.class);
                startActivity(intent);
            }
        });

        btn_faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), FaqActivity.class);
                startActivity(intent);
            }
        });

        btn_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), ContactActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initComponents(){
        btn_profile=findViewById(R.id.home_ib_profile);
        btn_friends=findViewById(R.id.home_ib_friends);
        btn_map=findViewById(R.id.home_ib_map);
        btn_teams=findViewById(R.id.home_ib_teams);
        btn_faq=findViewById(R.id.home_ib_faq);
        btn_contact=findViewById(R.id.home_ib_contact);
    }

}
