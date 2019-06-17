package com.example.teammanagement.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.teammanagement.R;
import com.example.teammanagement.Utils.Constants;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


public class Register1Activity extends AppCompatActivity {
    TextInputEditText iet_email;
    TextInputEditText iet_password;
    TextInputEditText iet_confirmPassword;
    TextInputEditText iet_lastName;
    TextInputEditText iet_firstName;
    Button btn_next;
    Button btn_back;
    Button btn_uploadPicture;
    ImageView iv_profilePicture;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register1);

        initComponents();


    }

    public void initComponents(){
        iet_firstName=findViewById(R.id.register1_tid_firstName);
        iet_lastName=findViewById(R.id.register1_tid_lastName);
        iet_email=findViewById(R.id.register1_tid_email);
        iet_password=findViewById(R.id.register1_tid_password);
        iet_confirmPassword=findViewById(R.id.register1_tid_confirmPassword);
        btn_next =findViewById(R.id.register1_btn_next);
        btn_back=findViewById(R.id.register1_btn_back);
        btn_uploadPicture = findViewById(R.id.register1_btn_upload);
        iv_profilePicture = findViewById(R.id.register1_iv_profilePicture);


        btn_next.setOnClickListener(clickNext());
        btn_back.setOnClickListener(clickBack());
        btn_uploadPicture.setOnClickListener(clickUploadPicture());
    }

    private View.OnClickListener clickNext(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if(isValid()){*/

                    String email=iet_email.getText().toString();
                    String password = iet_password.getText().toString();
                    String confirmPassword = iet_confirmPassword.getText().toString();
                    String firstName= iet_firstName.getText().toString();


                    intent=new Intent(getApplicationContext(),Register2Activity.class);
                    startActivity(intent);
                }
                /*else{

                }
            }*/
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
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, Constants.UPLOAD_IMAGE_REQUEST_CODE);
            }
        };
    }


    private boolean isValid(){
        if( isValidFirstName() && isValidLastName() && isValidEmail() && isValidPassword() && isValidConfirmPassword())return true;
         return false;
    }

    private boolean isValidFirstName(){
        if(iet_firstName.getText().length()<3){
            iet_firstName.setError(getString(R.string.register1_firstNameLength_error_hint));
            return false;
        }
        else if(!Character.isUpperCase(iet_firstName.getText().toString().charAt(0))){
            iet_firstName.setError(getString(R.string.register1_firstNameCapitalLetter_error_hint));
            return false;
        }
        else if(!iet_firstName.getText().toString().matches("^[a-zA-Z]*$")){
            iet_firstName.setError(getString(R.string.register1_firstNameLetters_error_hint));
            return false;
        }
        return true;
    }
    private boolean isValidLastName(){
        if(iet_lastName.getText().length()<3){
            iet_lastName.setError(getString(R.string.register1_lastNameLength_error_hint));
            return false;
        }
        else if(!Character.isUpperCase(iet_lastName.getText().toString().charAt(0))){
            iet_lastName.setError(getString(R.string.register1_firstNameCapitalLetter_error_hint));
            return false;
        }
        else if(!iet_lastName.getText().toString().matches("^[a-zA-Z]*$")){
            iet_lastName.setError(getString(R.string.register1_firstNameLetters_error_hint));
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
        else if(!iet_password.getText().toString().matches("^(?=.*[0-9])(?=.*[a-zA-Z])([a-zA-Z0-9]+)$")){
            iet_password.setError(getString(R.string.register_passwordError_oneDigitOneLetter_hint));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Bitmap bitmap = null;
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == Constants.UPLOAD_IMAGE_REQUEST_CODE) {
            Uri chosenImageUri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), chosenImageUri);
                iv_profilePicture.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(getString(R.string.register1_key_firstName_hint),iet_firstName.getText().toString());
        outState.putString(getString(R.string.register1_key_lastName_hint),iet_lastName.getText().toString());
        outState.putString(getString(R.string.register1_key_email_hint),iet_email.getText().toString());
        outState.putString(getString(R.string.register1_key_password_hint),iet_password.getText().toString());
        outState.putString(getString(R.string.register1_key_confirmPassword_hint),iet_confirmPassword.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        iet_firstName.setText(savedInstanceState.getString("FirstName"));
        iet_lastName.setText(savedInstanceState.getString("LastName"));
        iet_email.setText(savedInstanceState.getString("Email"));
        iet_password.setText(savedInstanceState.getString("Password"));
        iet_confirmPassword.setText(savedInstanceState.getString("ConfirmPassword"));
    }
}

