package com.example.teammanagement.activities;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.teammanagement.R;

public class ChangePasswordActivity extends AppCompatActivity {

    Intent intent;
    Button btn_save;
    ImageButton btn_back;
    TextInputEditText et_oldPassword;
    TextInputEditText et_newPassword;
    TextInputEditText et_confirmNewPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        initComponents();
    }
    private void initComponents() {
        btn_back=findViewById(R.id.changePassword_ibtn_back);
        btn_save=findViewById(R.id.changePassword_btn_Save);
        et_confirmNewPassword=findViewById(R.id.changePassword_tid_confirmPassword);
        et_newPassword=findViewById(R.id.changePassword_tid_newPassword);
        et_oldPassword=findViewById(R.id.changePassword_tid_oldPassword);

        btn_save.setOnClickListener(clickSave());
        btn_back.setOnClickListener(clickBack());
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

    private View.OnClickListener clickSave() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValid()) {
                    intent=new Intent(getApplicationContext(),SettingsActivity.class);
                    startActivity(intent);
                }
                else{

                }
            }
        };
    }

    private boolean isValid(){
        if(isValidOldPassword() && isValidNewPassword() && isValidConfirmPassword())return true;
        return false;
    }

    private boolean isValidNewPassword(){
        if(et_newPassword.getText() == null
                || et_newPassword.getText().toString().trim().isEmpty()
                || et_newPassword.getText().toString().contains(" ")){
            et_newPassword.setError(getString(R.string.signUp_passwordError_hint));
            return false;
        }
        else if(et_newPassword.getText().length() < 9 ){
            et_newPassword.setError(getString(R.string.signUp_passwordError_tooShort_hint));
            return false;
        }
        else if(!et_newPassword.getText().toString().matches("^(?=.*[0-9])(?=.*[a-zA-Z])([a-zA-Z0-9]+)$")){
            et_newPassword.setError(getString(R.string.signUp_passwordError_oneDigitOneLetter_hint));
            return false;
        }
        return true;
    }

    private boolean isValidConfirmPassword(){
        if(!et_newPassword.getText().toString().equals(et_confirmNewPassword.getText().toString())){
            et_confirmNewPassword.setError(getString(R.string.confirmPassword_error_hint));
            return false;
        }
        return true;
    }

    private boolean isValidOldPassword(){
        return true;
    }

}
