package com.example.teammanagement.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.teammanagement.R;

public class DisableAccountActivity extends AppCompatActivity {

    Intent intent;
    ImageButton ibtn_back;
    Button btn_yes;
    Button btn_no;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disable_account);
        initComponents();
    }
    private void initComponents() {
        ibtn_back=findViewById(R.id.disableAccount_ibtn_back);
        btn_no=findViewById(R.id.disableAccount_btn_no);
        btn_yes=findViewById(R.id.disableAccount_btn_yes);

        ibtn_back.setOnClickListener(clickBack());
        btn_no.setOnClickListener(clickNo());
        btn_yes.setOnClickListener(clickYes());
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
                intent=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
            }
        };
    }
}
