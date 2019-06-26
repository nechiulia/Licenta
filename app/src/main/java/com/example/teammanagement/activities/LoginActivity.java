package com.example.teammanagement.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.teammanagement.R;
import com.example.teammanagement.Utils.Constants;
import com.example.teammanagement.Utils.User;
import com.example.teammanagement.database.JDBCController;
import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LoginActivity extends AppCompatActivity {
    Button btn_login;
    TextInputEditText iet_email;
    TextInputEditText iet_password;
    TextView tv_register;
    TextView tv_registerLocation;
    Intent intent;
    User user;
    Connection c;
    JDBCController jdbcController;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        initComponents();

    }

    private void initComponents(){
        btn_login=findViewById(R.id.login_btn_login);
        iet_email=findViewById(R.id.login_tid_email);
        iet_password=findViewById(R.id.login_tid_password);
        tv_register=findViewById(R.id.login_tv_register);
        tv_registerLocation=findViewById(R.id.login_tv_signUpLocation);
        jdbcController=new JDBCController();
        c=jdbcController.openConnection();
       tv_register.setOnClickListener(clickRegisterUser());
       tv_registerLocation.setOnClickListener(clickRegisterLocation());
       btn_login.setOnClickListener(clickLogin());


       iet_email.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {

           }

           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {

           }

           @Override
           public void afterTextChanged(Editable s) {
                iet_email.setError(null);
                iet_password.setError(null);
           }
       });

       iet_password.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {

           }

           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {

           }

           @Override
           public void afterTextChanged(Editable s) {
               iet_email.setError(null);
               iet_password.setError(null);
           }
       });

    }

    private View.OnClickListener clickRegisterUser() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(getApplicationContext(), Register1Activity.class);
                startActivity(intent);
            }
        };
    }
    private View.OnClickListener clickRegisterLocation() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(getApplicationContext(), ClaimLocationActivity.class);
                startActivity(intent);
            }
        };
    }
    private View.OnClickListener clickLogin() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (isUserValid() && user.getState() != 2) {
                        if (user.getState() == 1) updateUserState();
                        if (user.getRole() == 2) {
                            intent = new Intent(getApplicationContext(), HomeAdminActivity.class);
                            startActivity(intent);
                        } else if (user.getRole() == 1) {
                            intent = new Intent(getApplicationContext(), HomeAdminLocationActivity.class);
                            startActivity(intent);
                        } else {
                            intent = new Intent(getApplicationContext(), LoadingActivity.class);
                            startActivity(intent);
                        }
                    } else {
                        iet_email.setError(getString(R.string.login_errorLogin));
                        iet_password.setError(getString(R.string.login_errorLogin));
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
    }

    public boolean isUserValid(){

        try(Statement s = c.createStatement()){
            String command ="SELECT * FROM UTILIZATORI WHERE EMAIL='"+iet_email.getText()+"' AND PAROLA='"+iet_password.getText()+"' ;";
            try(ResultSet r =s.executeQuery(command)) {
                if(r.next()){
                    user= new User(r.getInt(1),r.getString(2),
                                    r.getString(3),r.getString(4),r.getInt(5),r.getInt(6));
                    sharedPreferences=getSharedPreferences(Constants.APP_SHAREDPREF,MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(user);
                    editor.putString(Constants.CURRENT_USER,json);
                    editor.apply();
                    return true;
                }
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void updateUserState(){
        try(Statement s = c.createStatement()){
            String command ="UPDATE UTILIZATORI SET STARE=0 WHERE ID='"+user.getIdUser()+"';";
            int updatedRows=s.executeUpdate(command);
                if(updatedRows >0 )Log.d("databaseUpdateUser",String.valueOf(updatedRows));
            } catch (SQLException e1) {
            e1.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }


}
