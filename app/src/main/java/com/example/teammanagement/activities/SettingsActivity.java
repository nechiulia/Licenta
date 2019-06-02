package com.example.teammanagement.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.teammanagement.R;

public class SettingsActivity extends AppCompatActivity {

    Intent intent;
    TextView tv_changePassword;
    TextView tv_disableAccount;
    TextView tv_deleteAccount;
    ImageButton ibtn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initComponents();
    }
    private void initComponents() {
        tv_changePassword=findViewById(R.id.settings_tv_changePassword);
        tv_disableAccount=findViewById(R.id.settings_tv_disableAccount);
        tv_deleteAccount=findViewById(R.id.settings_tv_deleteAccount);
        ibtn_back=findViewById(R.id.settings_ibtn_back);

        tv_changePassword.setOnClickListener(clickChangePassword());
        tv_disableAccount.setOnClickListener(clickDisableAccount());
        tv_deleteAccount.setOnClickListener(clickDeleteAccount());
        ibtn_back.setOnClickListener(clickBack());
    }

    private View.OnClickListener clickBack() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(getApplicationContext(), MyProfileActivity.class);
                startActivity(intent);
            }
        };
    }

    private View.OnClickListener clickChangePassword() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(getApplicationContext(),ChangePasswordActivity.class);
                startActivity(intent);
            }
        };
    }

    private View.OnClickListener clickDisableAccount() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(getApplicationContext(),DisableAccountActivity.class);
                startActivity(intent);
            }
        };
    }

    private View.OnClickListener clickDeleteAccount() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(getApplicationContext(),DeleteAccountActivity.class);
                startActivity(intent);
            }
        };
    }

}
