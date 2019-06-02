package com.example.teammanagement.activities;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.teammanagement.R;

public class LoginActivity extends AppCompatActivity {
    Button btn_login;
    TextInputEditText iet_email;
    TextInputEditText iet_password;
    TextView tv_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        initComponents();

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),LoadingActivity.class);
                startActivity(intent);
            }
        });

        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), Register1Activity.class);
                startActivity(intent);
            }
        });
    }

    private void initComponents(){
        btn_login=findViewById(R.id.login_btn_login);
        iet_email=findViewById(R.id.login_tid_email);
        iet_password=findViewById(R.id.login_tid_password);
        tv_register=findViewById(R.id.login_tv_register);
    }

    private boolean isValid(){
        return true;
    }
}
