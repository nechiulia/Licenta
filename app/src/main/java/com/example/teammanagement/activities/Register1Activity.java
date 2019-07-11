package com.example.teammanagement.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.example.teammanagement.R;
import com.example.teammanagement.Utils.Constants;
import com.example.teammanagement.Utils.User;
import com.example.teammanagement.database.JDBCController;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class Register1Activity extends AppCompatActivity {

    private TextInputEditText iet_email;
    private TextInputEditText iet_password;
    private TextInputEditText iet_confirmPassword;
    private TextInputEditText iet_username;
    private Button btn_next;
    private Button btn_back;
    private Button btn_uploadPicture;
    private ImageView iv_profilePicture;

    private Intent intent;

    private User newUser;
    private byte[] userProfilePicture;

    private JDBCController jdbcController;
    private Connection c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register1);

        initComponents();

    }

    public void initComponents(){

        jdbcController =JDBCController.getInstance();
        c=jdbcController.openConnection();

        iet_email=findViewById(R.id.register1_tid_email);
        iet_password=findViewById(R.id.register1_tid_password);
        iet_confirmPassword=findViewById(R.id.register1_tid_confirmPassword);
        iet_username=findViewById(R.id.register1_tid_userName);
        btn_next =findViewById(R.id.register1_btn_next);
        btn_back=findViewById(R.id.register1_btn_back);
        btn_uploadPicture = findViewById(R.id.register1_btn_upload);
        iv_profilePicture = findViewById(R.id.register1_iv_profilePicture);

        iet_username.addTextChangedListener(changeUserName());
        iet_email.addTextChangedListener(changeEmail());
        iet_password.addTextChangedListener(changePassword());
        iet_confirmPassword.addTextChangedListener(changeChangePassword());

        btn_next.setOnClickListener(clickNext());
        btn_back.setOnClickListener(clickBack());
        btn_uploadPicture.setOnClickListener(clickUploadPicture());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Bitmap bitmap = null;
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == Constants.UPLOAD_IMAGE_REQUEST_CODE) {
            Uri chosenImageUri = data.getData();
            try {
                if(chosenImageUri!= null) {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), chosenImageUri);
                    iv_profilePicture.setImageBitmap(bitmap);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    userProfilePicture = stream.toByteArray();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void insertUserPicture(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(String.valueOf(newUser.getIdUser()));
        String encodedImage = Base64.encodeToString(userProfilePicture, Base64.NO_WRAP);
        myRef.setValue(encodedImage);
    }

    public void insertUser(){
        try(PreparedStatement s =c.prepareStatement("INSERT INTO UTILIZATORI VALUES('" + newUser.getUserName() + "','" + newUser.getEmail() + "','" + newUser.getPassword() + "'," + newUser.getState() + "," + newUser.getRole() + ")",Statement.RETURN_GENERATED_KEYS)){
            int updatedRows=s.executeUpdate();
            ResultSet r = s.getGeneratedKeys();
            if (r.next()) {
                if (updatedRows > 0) {
                    newUser.setIdUser(r.getInt(1));
                }
            }
        }
        catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    private View.OnClickListener clickNext(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValid()){
                    String userName = iet_username.getText().toString();
                    String email=iet_email.getText().toString();
                    String password = iet_password.getText().toString();

                    /*newUser = new User(userName,email,password,0,userProfilePicture,0);*/
                    newUser = new User(userName,email,password,0,0);
                    insertUser();
                    if(userProfilePicture != null) {
                        insertUserPicture();
                    }

                    intent=new Intent(getApplicationContext(),Register2Activity.class);
                    intent.putExtra(Constants.NEW_USER,newUser);
                    startActivity(intent);
                }

            }
        };
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

    private View.OnClickListener clickUploadPicture() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);

                photoPickerIntent.setType(getString(R.string.register1_typeImage_hint));
                startActivityForResult(photoPickerIntent, Constants.UPLOAD_IMAGE_REQUEST_CODE);
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
            }
        };
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(getString(R.string.register1_key_userName_hint),iet_username.getText().toString());
        outState.putString(getString(R.string.register1_key_email_hint),iet_email.getText().toString());
        outState.putString(getString(R.string.register1_key_password_hint),iet_password.getText().toString());
        outState.putString(getString(R.string.register1_key_confirmPassword_hint),iet_confirmPassword.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        iet_username.setText(savedInstanceState.getString(getString(R.string.register1_keyState_userName_hint)));
        iet_email.setText(savedInstanceState.getString(getString(R.string.register1_keyState_email_hint)));
        iet_password.setText(savedInstanceState.getString(getString(R.string.register1_keyState_password_hint)));
        iet_confirmPassword.setText(savedInstanceState.getString(getString(R.string.register1_keyState_confirmPassword_hint)));
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

    private boolean isValid(){
        if( isValidUserName() && isValidEmail() && isValidPassword() && isValidConfirmPassword())return true;
        return false;
    }

    private boolean isValidUserName(){
        if(iet_username.getText().length()<3){
            iet_username.setError(getString(R.string.register1_nameLength_error_hint));
            return false;
        }
        else if(!Character.isUpperCase(iet_username.getText().toString().charAt(0))){
            iet_username.setError(getString(R.string.register1_nameCapitalLetter_error_hint));
            return false;
        }
        else if(!iet_username.getText().toString().matches("^[a-zA-Z_ ]*$")){
            iet_username.setError(getString(R.string.register1_nameLetters_error_hint));
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

}

