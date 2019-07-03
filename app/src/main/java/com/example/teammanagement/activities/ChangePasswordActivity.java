package com.example.teammanagement.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.teammanagement.R;
import com.example.teammanagement.Utils.Constants;
import com.example.teammanagement.Utils.Sport;
import com.example.teammanagement.Utils.User;
import com.example.teammanagement.database.JDBCController;
import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ChangePasswordActivity extends AppCompatActivity {

    Intent intent;
    Button btn_save;
    ImageButton btn_back;
    TextInputEditText et_oldPassword;
    TextInputEditText et_newPassword;
    TextInputEditText et_confirmNewPassword;
    SharedPreferences sharedPreferences;
    User currentUser;
    Connection c;
    JDBCController jdbcController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        initComponents();
    }
    private void initComponents() {

        jdbcController=JDBCController.getInstance();
        c=jdbcController.openConnection();

        btn_back=findViewById(R.id.changePassword_ibtn_back);
        btn_save=findViewById(R.id.changePassword_btn_Save);
        et_confirmNewPassword=findViewById(R.id.changePassword_tid_confirmPassword);
        et_newPassword=findViewById(R.id.changePassword_tid_newPassword);
        et_oldPassword=findViewById(R.id.changePassword_tid_oldPassword);

        btn_save.setOnClickListener(clickSave());
        btn_back.setOnClickListener(clickBack());
        getUser();
    }

    public void getUser(){
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.APP_SHAREDPREF,MODE_PRIVATE);
        Gson gson = new Gson();
        String json =  sharedPreferences.getString(Constants.CURRENT_USER,"");
        currentUser = gson.fromJson(json, User.class);
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
                    updatePassword();
                    currentUser.setPassword(et_newPassword.getText().toString());
                    saveCurrentUser();
                    intent=new Intent(getApplicationContext(),SettingsActivity.class);
                    startActivity(intent);
                }
            }
        };
    }

    public void updatePassword(){
        try(Statement statement = c.createStatement()){
            statement.executeUpdate("UPDATE UTILIZATORI SET PAROLA='"+et_newPassword.getText().toString()+"' WHERE ID="+currentUser.getIdUser());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveCurrentUser(){
        sharedPreferences=getSharedPreferences(Constants.APP_SHAREDPREF,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(currentUser);
        editor.putString(Constants.CURRENT_USER,json);
        editor.apply();
    }

    private boolean isValid(){
        return isValidOldPassword() && isValidNewPassword() && isValidConfirmPassword();
    }

    private boolean isValidNewPassword(){
        if(et_newPassword.getText() == null
                || et_newPassword.getText().toString().trim().isEmpty()
                || et_newPassword.getText().toString().contains(" ")){
            et_newPassword.setError(getString(R.string.register_passwordError_hint));
            return false;
        }
        else if(et_newPassword.getText().length() < 9 ){
            et_newPassword.setError(getString(R.string.register_passwordError_tooShort_hint));
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

    public boolean selectOldPassword(){
        try (Statement s = c.createStatement()) {
            String command = "SELECT PAROLA FROM UTILIZATORI WHERE ID=" + currentUser.getIdUser();
            try (ResultSet r = s.executeQuery(command)) {
                if(r.next()) {
                    return r.getString(1).equals(et_oldPassword.getText().toString());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean isValidOldPassword(){
        if(et_oldPassword.getText().toString().length()!=0) {
            boolean isValidOldTable = selectOldPassword();
            if(isValidOldTable){
                return true;
            }
            else{
                et_oldPassword.setError("Parola nu este corectă");
            }
        }
        else{
            et_oldPassword.setError("Introduceți parola veche");
            return false;
        }
        return false;
    }
}

