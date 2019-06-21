package com.example.teammanagement.activities;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.teammanagement.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClaimLocationActivity extends AppCompatActivity {

    TextInputEditText iet_locationName;
    TextInputEditText iet_streetName;
    TextInputEditText iet_streetNumber;
    TextInputEditText iet_email;
    TextInputEditText iet_password;
    TextInputEditText iet_postalCode;
    TextInputEditText iet_city;
    TextInputEditText iet_confirmPassword;
    Button btn_save;
    Button btn_back;
    Intent intent;
    List<String> locationTypeList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claim_location);

        initComponents();
    }

    public void initComponents(){

        iet_email=findViewById(R.id.claimLocation_tid_email);
        iet_password=findViewById(R.id.claimLocation_tid_password);
        iet_locationName=findViewById(R.id.claimLocation_tid_LocationName);
        iet_streetName=findViewById(R.id.claimLocation_tid_streetName);
        iet_streetNumber=findViewById(R.id.claimLocation_tid_streetNumber);
        iet_confirmPassword=findViewById(R.id.claimLocation_tid_confirmPassword);
        iet_city=findViewById(R.id.claimLocation_tid_city);
        iet_postalCode=findViewById(R.id.claimLocation_tid_postalCode);
        btn_save =findViewById(R.id.claimLocation_btn_save);
        btn_back=findViewById(R.id.claimLocation_btn_back);
        locationTypeList =Arrays.asList(getResources().getStringArray(R.array.location_type));


        btn_back.setOnClickListener(clickBack());
        btn_save.setOnClickListener(clickSave());
    }

    private View.OnClickListener clickBack() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
            }
        };
    }

    private View.OnClickListener clickSave() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
            }
        };
    }
}
