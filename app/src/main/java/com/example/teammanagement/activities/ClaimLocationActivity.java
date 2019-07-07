package com.example.teammanagement.activities;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.example.teammanagement.R;
import com.example.teammanagement.database.JDBCController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClaimLocationActivity extends AppCompatActivity {

    private TextInputEditText iet_locationName;
    private TextInputEditText iet_streetName;
    private TextInputEditText iet_streetNumber;
    private TextInputEditText iet_username;
    private TextInputEditText iet_email;
    private TextInputEditText iet_password;
    private TextInputEditText iet_postalCode;
    private TextInputEditText iet_confirmPassword;
    private CheckBox ckB_reservation;
    private Button btn_save;
    private Button btn_back;

    private Intent intent;

    private JDBCController jdbcController;
    private Connection c;

    private int userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claim_location);

        initComponents();
    }

    public void initComponents(){

        jdbcController =JDBCController.getInstance();
        c=jdbcController.openConnection();

        iet_email=findViewById(R.id.claimLocation_tid_email);
        iet_password=findViewById(R.id.claimLocation_tid_password);
        iet_locationName=findViewById(R.id.claimLocation_tid_LocationName);
        iet_streetName=findViewById(R.id.claimLocation_tid_streetName);
        iet_streetNumber=findViewById(R.id.claimLocation_tid_streetNumber);
        iet_confirmPassword=findViewById(R.id.claimLocation_tid_confirmPassword);
        iet_postalCode=findViewById(R.id.claimLocation_tid_postalCode);
        iet_username=findViewById(R.id.claimLocation_tid_userName);
        ckB_reservation=findViewById(R.id.claimLocation_ck_reservation);
        btn_save =findViewById(R.id.claimLocation_btn_save);
        btn_back=findViewById(R.id.claimLocation_btn_back);

        btn_back.setOnClickListener(clickBack());
        btn_save.setOnClickListener(clickSave());
        iet_username.addTextChangedListener(changeUserName());
        iet_email.addTextChangedListener(changeEmail());
        iet_password.addTextChangedListener(changePassword());
        iet_confirmPassword.addTextChangedListener(changeChangePassword());
        iet_locationName.addTextChangedListener(changeLocationName());
        iet_streetNumber.addTextChangedListener(changeStreetNumber());
        iet_streetName.addTextChangedListener(changeStreetName());
        iet_postalCode.addTextChangedListener(changePostalCode());

    }

    public boolean emailExistsInDatabase(){
        try(Statement s = c.createStatement()){
            try(ResultSet r = s.executeQuery("SELECT * FROM UTILIZATORI WHERE EMAIL='"+iet_email.getText().toString()+"';")){
                if(r.next()){
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void insertLocationInfo(){
        int check;
        if(ckB_reservation.isChecked()){
            check=1;
        }
        else{
           check=0;
        }
        try(Statement s =c.createStatement()){
            s.executeUpdate("INSERT INTO LOCATII VALUES(N'"+iet_locationName.getText().toString()
                    +"','"+iet_postalCode.getText().toString()+"',N'"+iet_streetName.getText().toString()+" "+iet_streetNumber.getText().toString()+"',null,null,"+check+",2,"+ userID+")");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertUser(){
        try(PreparedStatement s =c.prepareStatement("INSERT INTO UTILIZATORI VALUES('" + iet_username.getText().toString() + "','" + iet_email.getText().toString() + "','" + iet_password.getText().toString() + "',2,1);",Statement.RETURN_GENERATED_KEYS)){
            int updatedRows=s.executeUpdate();
            ResultSet r = s.getGeneratedKeys();
            if (r.next()) {
                if (updatedRows > 0) {
                   userID=r.getInt(1);
                }
            }
        }
        catch (SQLException e1) {
            e1.printStackTrace();
        }
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
                if(isValid()) {
                    insertUser();
                    insertLocationInfo();
                    intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        };
    }

    private TextWatcher changeUserName(){
        return new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                iet_password.setError(null);
                iet_email.setError(null);
                iet_confirmPassword.setError(null);
                iet_username.setError(null);
                iet_locationName.setError(null);
                iet_postalCode.setError(null);
                iet_streetName.setError(null);
                iet_streetNumber.setError(null);
            }
        };
    }
    private TextWatcher changeEmail(){
        return new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                iet_password.setError(null);
                iet_email.setError(null);
                iet_confirmPassword.setError(null);
                iet_username.setError(null);
                iet_locationName.setError(null);
                iet_postalCode.setError(null);
                iet_streetName.setError(null);
                iet_streetNumber.setError(null);
            }
        };
    }
    private TextWatcher changePassword(){
        return new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                iet_password.setError(null);
                iet_email.setError(null);
                iet_confirmPassword.setError(null);
                iet_username.setError(null);
                iet_locationName.setError(null);
                iet_postalCode.setError(null);
                iet_streetName.setError(null);
                iet_streetNumber.setError(null);
            }
        };
    }
    private TextWatcher changeChangePassword(){
        return new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                iet_password.setError(null);
                iet_email.setError(null);
                iet_confirmPassword.setError(null);
                iet_username.setError(null);
                iet_locationName.setError(null);
                iet_postalCode.setError(null);
                iet_streetName.setError(null);
                iet_streetNumber.setError(null);
            }
        };
    }
    private TextWatcher changeLocationName(){
        return new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                iet_password.setError(null);
                iet_email.setError(null);
                iet_confirmPassword.setError(null);
                iet_username.setError(null);
                iet_locationName.setError(null);
                iet_postalCode.setError(null);
                iet_streetName.setError(null);
                iet_streetNumber.setError(null);
            }
        };
    }

    private TextWatcher changeStreetName(){
        return new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                iet_password.setError(null);
                iet_email.setError(null);
                iet_confirmPassword.setError(null);
                iet_username.setError(null);
                iet_locationName.setError(null);
                iet_postalCode.setError(null);
                iet_streetName.setError(null);
                iet_streetNumber.setError(null);
            }
        };
    }

    private TextWatcher changeStreetNumber(){
        return new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                iet_password.setError(null);
                iet_email.setError(null);
                iet_confirmPassword.setError(null);
                iet_username.setError(null);
                iet_locationName.setError(null);
                iet_postalCode.setError(null);
                iet_streetName.setError(null);
                iet_streetNumber.setError(null);
            }
        };
    }

    private TextWatcher changePostalCode(){
        return new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                iet_password.setError(null);
                iet_email.setError(null);
                iet_confirmPassword.setError(null);
                iet_username.setError(null);
                iet_locationName.setError(null);
                iet_postalCode.setError(null);
                iet_streetName.setError(null);
                iet_streetNumber.setError(null);
            }
        };
    }

    private boolean isValid(){
        if( isValidLocationName() && isValidStreetName() && isValidStreetNumber()  &&
        isValidUserName() && isValidEmail() && isValidPassword() && isValidConfirmPassword())return true;
        return false;
    }

    private boolean isValidUserName(){
        if(iet_username.getText().length()<3){
            iet_username.setError(getString(R.string.register1_firstNameLength_error_hint));
            return false;
        }
        else if(!Character.isUpperCase(iet_username.getText().toString().charAt(0))){
            iet_username.setError(getString(R.string.register1_firstNameCapitalLetter_error_hint));
            return false;
        }
        else if(!iet_username.getText().toString().matches("^[a-zA-Z_ ]*$")){
            iet_username.setError(getString(R.string.register1_firstNameLetters_error_hint));
            return false;
        }
        return true;
    }

    private boolean isValidEmail(){
        if(TextUtils.isEmpty(iet_email.getText())
                || iet_email.getText().toString().trim().isEmpty()
                || !Patterns.EMAIL_ADDRESS.matcher(iet_email.getText().toString()).matches()){
            iet_email.setError(getText(R.string.register_emailError_hint));
            return false;
        }
        if(emailExistsInDatabase()){
            iet_email.setError(getString(R.string.register_emailError_alreadyExists_hint));
            return false;
        }
        return true;
    }

    private boolean isValidPassword(){
        if(iet_password.getText() == null
                || iet_password.getText().toString().trim().isEmpty()
                || iet_password.getText().toString().contains(" ")){
            iet_password.setError(getString(R.string.register_passwordError_hint));
            return false;
        }
        else if(iet_password.getText().length() < 9 ){
            iet_password.setError(getString(R.string.register_passwordError_tooShort_hint));
            return false;
        }
        return true;
    }

    private boolean isValidConfirmPassword(){
        if(!iet_password.getText().toString().equals(iet_confirmPassword.getText().toString())){
            iet_confirmPassword.setError(getString(R.string.register1_passwordDoesNotMatch_hint));
            return false;
        }
        return true;
    }

    private boolean isValidLocationName(){
        if(!iet_locationName.getText().toString().matches("^[a-zA-Z0-9_ ]*$")){
            iet_locationName.setError(getString(R.string.claim_location_isvalid_lettersAndDigits_error_hint));
            return false;
        }
        else if(iet_locationName.getText().length() < 3){
            iet_locationName.setError(getString(R.string.claim_location_isvalidLocation_error_hint));
            return false;
        }
        return true;
    }

    private boolean isValidStreetNumber(){
        if(!iet_streetNumber.getText().toString().matches("^[a-zA-Z0-9_ ]*$")){
            iet_streetNumber.setError(getString(R.string.claim_location_isvalid_lettersAndDigits_error_hint));
            return false;
        }
        return true;
    }

    private boolean isValidStreetName(){
        if(!iet_streetName.getText().toString().matches("^[a-zA-Z0-9_ ]*$")){
            iet_streetName.setError(getString(R.string.claim_location_isvalid_lettersAndDigits_error_hint));
            return false;
        }
        else if(iet_streetName.getText().length() < 3){
            iet_streetName.setError(getString(R.string.claim_location_isvalidstreetName_length_error_hint));
            return false;
        }
        return true;
    }

}
