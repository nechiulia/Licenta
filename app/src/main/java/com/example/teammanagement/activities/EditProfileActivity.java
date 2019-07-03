package com.example.teammanagement.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.teammanagement.R;
import com.example.teammanagement.Utils.Constants;
import com.example.teammanagement.Utils.Sport;
import com.example.teammanagement.Utils.SportUser;
import com.example.teammanagement.Utils.SportUserTable;
import com.example.teammanagement.Utils.User;
import com.example.teammanagement.adapters.SportAdapter;
import com.example.teammanagement.database.JDBCController;
import com.example.teammanagement.dialogs.AddSportDialog;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EditProfileActivity extends AppCompatActivity implements AddSportDialog.AddSportDialogListener{

    Button btn_upload;
    Button btn_save;
    Button btn_cancel;
    ImageButton ibtn_addSport;
    ImageButton ibtn_removeSport;
    ListView lv_sports;
    ImageView iv_profilePicture;
    TextInputEditText iet_email;
    TextInputEditText iet_userName;
    Intent intent;
    SportAdapter adapter;
    View row;
    CheckBox ck_box;
    User currentUser;
    Bitmap bitmap;
    SharedPreferences sharedPreferences;

    List<SportUser> lv_list_sportItems = new ArrayList<>();
    ArrayList<String> list_toGoToDialog = new ArrayList<>();
    ArrayList<String> lv_list_currentSportsName = new ArrayList<>();
    ArrayList<String> list_selectDB;

    List<SportUserTable> list_to_table = new ArrayList<>();

    JDBCController jdbcController;
    Connection c;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        initComponents();
    }

    private void initComponents() {

        jdbcController = JDBCController.getInstance();
        c=jdbcController.openConnection();

        btn_cancel=findViewById(R.id.editProfile_btn_cancel);
        btn_save=findViewById(R.id.editProfile_btn_save);
        btn_upload=findViewById(R.id.editProfile_btn_upload);
        ibtn_addSport=findViewById(R.id.editProfile_ibtn_addSport);
        ibtn_removeSport=findViewById(R.id.editProfile_ibtn_removeSport);
        lv_sports=findViewById(R.id.editProfile_lv_sports);
        lv_sports.setItemsCanFocus(true);
        iet_email=findViewById(R.id.editProfile_tid_email);
        iet_userName=findViewById(R.id.editProfile_tid_userName);
        iv_profilePicture=findViewById(R.id.editProfile_iv_profileImage);

        btn_upload.setOnClickListener(clickUpload());
        btn_save.setOnClickListener(clickSave());
        btn_cancel.setOnClickListener(clickCancel());
        ibtn_removeSport.setOnClickListener(clickRemoveSport());
        ibtn_addSport.setOnClickListener(clickAddSport());

        list_toGoToDialog.add("Selectează sportul");
        initDataSpinner();
        list_selectDB= new ArrayList<>();
        list_selectDB.addAll(list_toGoToDialog);

        iet_userName.addTextChangedListener(changeUserName());

        getUser();
        initData();

        if(lv_list_sportItems !=null) {
            adapter = new SportAdapter(getApplicationContext(), R.layout.list_item_sports_darktext, lv_list_sportItems, getLayoutInflater());
            lv_sports.setAdapter(adapter);
            lv_sports.setOnItemClickListener(listItemClick());
        }

    }

    public void initDataSpinner(){
        try(Statement s = c.createStatement()){
            String command ="SELECT * FROM SPORTURI;";
            try(ResultSet r =s.executeQuery(command)) {
                while(r.next()){
                    Sport sport = new Sport(r.getString(2),r.getInt(3));
                    list_toGoToDialog.add(sport.getDenumire());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getUser(){
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.APP_SHAREDPREF,MODE_PRIVATE);
        Gson gson = new Gson();
        String json =  sharedPreferences.getString(Constants.CURRENT_USER,"");
        currentUser = gson.fromJson(json, User.class);
    }

    public void initData(){
        if(currentUser.getProfilePicture() != null) {
            bitmap = BitmapFactory.decodeByteArray(currentUser.getProfilePicture(), 0, currentUser.getProfilePicture().length);
            iv_profilePicture.setImageBitmap(Bitmap.createBitmap(bitmap));
        }
        iet_email.setText(currentUser.getEmail());
        iet_userName.setText(currentUser.getUserName());

        selectSports();
    }

    public void selectSports(){
        try(Statement s = c.createStatement()){
            String command ="SELECT S.DENUMIRE,SU.NIVEL FROM UTILIZATORI U, SPORTURI S, SPORTUTILIZATOR SU WHERE U.ID=SU.IDUTILIZATOR AND SU.IDSPORT=S.ID AND U.ID='"+currentUser.getIdUser()+"';";
            try(ResultSet r =s.executeQuery(command)) {
                while(r.next()){
                    String level = getLevel(r.getInt(2));
                    SportUser sportUser = new SportUser(r.getString(1),level);
                    lv_list_sportItems.add(sportUser);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getLevel(int level){
        if(level ==0)return getString(R.string.user_sport_level_0);
        else if(level == 1)return getString(R.string.user_sport_level_1);
        else if(level == 2)return getString(R.string.user_sport_level_2);
        else if(level == 3)return getString(R.string.user_sport_level_3);
        else if(level == 4)return getString(R.string.user_sport_level_4);
        return getString(R.string.user_sport_level_5);
    }

    private AdapterView.OnItemClickListener listItemClick(){
        return new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                row=view;
                ck_box=row.findViewById(R.id.list_item_sports_ck);
                if(ck_box.isChecked())ck_box.setChecked(false);
                else{
                    ck_box.setChecked(true);
                }
            }
        };
    }

    private View.OnClickListener clickUpload() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                photoPickerIntent.setType(getString(R.string.editProfile_uploadType));
                startActivityForResult(photoPickerIntent, Constants.UPLOAD_IMAGE_REQUEST_CODE);
            }
        };
    }

    public SportUserTable getSportID(SportUser sportUtilizator){
        SportUserTable sportToAdd = new SportUserTable();
        try(Statement s = c.createStatement()){
            String command ="SELECT ID FROM Sporturi WHERE DENUMIRE='"+sportUtilizator.getSportName()+"';";
            try(ResultSet r =s.executeQuery(command)) {
                if(r.next()){
                    sportToAdd.setIdSport(r.getInt(1));
                    sportToAdd.setIdUtilizator(currentUser.getIdUser());
                    sportToAdd.setSportLevel(getLevelInt(sportUtilizator.getLevel()));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sportToAdd;
    }

    public void deleteSportUser(){
        try(Statement s = c.createStatement()){
            s.executeUpdate("DELETE FROM SPORTUTILIZATOR WHERE IDUTILIZATOR="+currentUser.getIdUser()+";");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private View.OnClickListener clickSave() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValid()){

                    if(isValidPicture()) {
                        updateProfilePicture();
                    }
                    updateUserName();
                    deleteSportUser();
                    for (SportUser sportUtilizator : lv_list_sportItems) {
                        list_to_table.add(getSportID(sportUtilizator));
                    }
                    for (SportUserTable sportUtilizatorTable : list_to_table) {
                        insertUserSports(sportUtilizatorTable);
                    }

                    saveCurrentUser();

                    intent=new Intent(getApplicationContext(), MyProfileActivity.class);
                    startActivity(intent);
                }
            }
        };
    }

    public void insertUserSports(SportUserTable sportUtilizatorTable){
        try(Statement s = c.createStatement()){
            String command ="INSERT INTO SPORTUTILIZATOR VALUES("+currentUser.getIdUser()
                    +","+sportUtilizatorTable.getIdSport()+",'"
                    +sportUtilizatorTable.getSportLevel()+"');";
           s.executeUpdate(command);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateUserName(){
        try (Statement s = c.createStatement()) {
            String command = "UPDATE UTILIZATORI SET NUMEUTILIZATOR='"+iet_userName.getText().toString()+"' WHERE ID='" + currentUser.getIdUser() + "';";
            int updatedRows = s.executeUpdate(command);
            if (updatedRows > 0) Log.d("databaseUpdateUser", String.valueOf(updatedRows));
            currentUser.setUserName(iet_userName.getText().toString());
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    private int getLevelInt(String level){
        if(level.equals(getString(R.string.user_sport_level_0)))return 0;
        if(level.equals(getString(R.string.user_sport_level_1)))return 1;
        if(level.equals(getString(R.string.user_sport_level_2)))return 2;
        if(level.equals(getString(R.string.user_sport_level_3)))return 3;
        if(level.equals(getString(R.string.user_sport_level_4)))return 4;
        if(level.equals(getString(R.string.user_sport_level_5)))return 5;
        return -2;
    }

    public void updateProfilePicture(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(String.valueOf(currentUser.getIdUser()));
        String encodedImage = Base64.encodeToString(currentUser.getProfilePicture(), Base64.NO_WRAP);
        myRef.setValue(encodedImage);
    }

    public  boolean isValidPicture(){
        return currentUser.getProfilePicture() != null;
    }
    private View.OnClickListener clickCancel() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(getApplicationContext(), MyProfileActivity.class);
                startActivity(intent);
            }
        };
    }

    private View.OnClickListener clickRemoveSport() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    SparseBooleanArray positionchecker = lv_sports.getCheckedItemPositions();
                    int count = lv_sports.getCount();

                    for(int i = count -1 ; i >= 0; i--){
                        if(positionchecker.get(i)){
                            adapter.remove(lv_list_sportItems.get(i));
                        }
                    }
                    positionchecker.clear();
                    adapter.notifyDataSetChanged();
            }
        };
    }

    private View.OnClickListener clickAddSport() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
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
                iet_userName.setError(null);
            }
        };
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Bitmap bitmap = null;
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == Constants.UPLOAD_IMAGE_REQUEST_CODE) {
            Uri chosenImageUri = data.getData();
            try {
                if(chosenImageUri!=null) {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), chosenImageUri);
                    iv_profilePicture.setImageBitmap(bitmap);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    currentUser.setProfilePicture(stream.toByteArray());

                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
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

    private void openDialog(){
        Bundle args = new Bundle();
        for(int i = 0; i< lv_list_sportItems.size(); i++){
            lv_list_currentSportsName.add(lv_list_sportItems.get(i).getSportName());
        }
        list_toGoToDialog.removeAll(lv_list_currentSportsName);
        args.putSerializable(Constants.SEND_SPORTSLIST, list_toGoToDialog);

        AddSportDialog addSportDialog=new AddSportDialog();
        addSportDialog.show(getSupportFragmentManager(),getString(R.string.register2_addSport_hint));

        addSportDialog.setArguments(args);
        lv_list_currentSportsName.clear();
    }

    @Override
    public void applyTexts(String sport, String level) {
        lv_list_sportItems.add(new SportUser(sport,level));
        list_toGoToDialog= new ArrayList<>(list_selectDB);
        adapter.notifyDataSetChanged();
    }

    public boolean isValid(){
        return isValidList() && isValidUserName();
    }

    private boolean isValidUserName(){
        if(iet_userName.getText().length()<3){
            iet_userName.setError(getString(R.string.register1_firstNameLength_error_hint));
            return false;
        }
        else if(!Character.isUpperCase(iet_userName.getText().toString().charAt(0))){
            iet_userName.setError(getString(R.string.register1_firstNameCapitalLetter_error_hint));
            return false;
        }
        else if(!iet_userName.getText().toString().matches("^[a-zA-Z_ ]*$")){
            iet_userName.setError(getString(R.string.register1_firstNameLetters_error_hint));
            return false;
        }
        return true;
    }

    private boolean isValidList(){
       if(lv_list_sportItems.size() == 0 ){
           AlertDialog.Builder builder = new AlertDialog.Builder(this);
           builder.setTitle(getString(R.string.editProfile_title_alertDialog_sportList))
                   .setMessage(getString(R.string.editProfile_message_alertDialog_sportList))
                   .setCancelable(false)
                   .setPositiveButton(getString(R.string.editProfile_alertDialog_listEmpty_hint), new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                       }
                   });
           AlertDialog alert = builder.create();
           alert.show();
           return false;
       }
       return true;
    }
}
