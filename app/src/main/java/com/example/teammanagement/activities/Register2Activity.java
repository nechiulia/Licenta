package com.example.teammanagement.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import com.example.teammanagement.Utils.Constants;
import com.example.teammanagement.Utils.Sport;
import com.example.teammanagement.Utils.SportUtilizator;
import com.example.teammanagement.Utils.SportUtilizatorTable;
import com.example.teammanagement.Utils.User;
import com.example.teammanagement.adapters.SportAdapter;
import com.example.teammanagement.database.JConstants;
import com.example.teammanagement.database.JDBCController;
import com.example.teammanagement.dialogs.AddSportDialog;
import com.example.teammanagement.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Register2Activity extends AppCompatActivity implements AddSportDialog.AddSportDialogListener {

    Button btn_save;
    Button btn_cancel;
    ImageButton ibtn_addSport;
    Intent intent;
    ListView lv_sports;
    List<SportUtilizator> lv_list_sportItems = new ArrayList<>();
    List<SportUtilizatorTable> list_to_table = new ArrayList<>();
    ImageButton ibtn_removeSport;
    SportAdapter adapter;
    View row;
    CheckBox ck_box;
    JDBCController jdbcController;
    Connection c;
    ArrayList<String> list_toGoToDialog = new ArrayList<>() ;
    ArrayList<String> lv_list_currentSportsName = new ArrayList<>();
    ArrayList<String> list_selectDB;
    User newUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        intent=getIntent();
        initComponents();

        this.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));
        this.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK));
    }

    private void initComponents() {

        jdbcController = JDBCController.getInstance();
        c=jdbcController.openConnection();

        btn_save=findViewById(R.id.register2_btn_save);
        btn_cancel=findViewById(R.id.register2_btn_cancel);
        ibtn_addSport =findViewById(R.id.register2_ibtn_addSport);
        lv_sports=findViewById(R.id.register2_lv_sports);
        lv_sports.setItemsCanFocus(true);
        ibtn_removeSport=findViewById(R.id.register2_ibtn_removeSport);

        ibtn_addSport.setOnClickListener(clickAddSport());
        btn_save.setOnClickListener(clickSave());
        btn_cancel.setOnClickListener(clickCancel());
        ibtn_removeSport.setOnClickListener(clickRemove());
        list_toGoToDialog.add("Selectează sportul");
        initDataSpinner();
        list_selectDB= new ArrayList<>();
        list_selectDB.addAll(list_toGoToDialog);

        if(lv_list_sportItems !=null) {
            adapter = new SportAdapter(getApplicationContext(), R.layout.list_item_sports, lv_list_sportItems, getLayoutInflater());
            lv_sports.setAdapter(adapter);
            lv_sports.setOnItemClickListener(listItemClick());
        }

        if(intent.hasExtra(Constants.NEW_USER)){
            newUser= (User) intent.getSerializableExtra(Constants.NEW_USER);
        }


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

    private View.OnClickListener clickSave() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(lv_list_sportItems.size()!= 0) {
                    insertUser();
                    getUserID();
                    insertUserPicture();
                    for (SportUtilizator sportUtilizator : lv_list_sportItems) {
                        list_to_table.add(getSportID(sportUtilizator));
                    }
                    for (SportUtilizatorTable sportUtilizatorTable : list_to_table) {
                        insertUserSports(sportUtilizatorTable);
                    }
                    intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(Register2Activity.this);
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
                }
            }
        };
    }

    private View.OnClickListener clickCancel() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
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

    private View.OnClickListener clickRemove() {
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
        lv_list_sportItems.add(new SportUtilizator(sport,level));
        list_toGoToDialog= new ArrayList<>(list_selectDB);
        adapter.notifyDataSetChanged();
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

    public void insertUser(){
        String command= JConstants.INSERT_USER;
        try(PreparedStatement s = c.prepareStatement(command)){
            s.setString(1,newUser.getUserName());
            s.setString(2,newUser.getEmail());
            s.setString(3,newUser.getPassword());
            s.setInt(4,newUser.getState());
            s.setInt(5,newUser.getRole());
            s.execute();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }

    }

    public void insertUserPicture(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(String.valueOf(newUser.getIdUser()));
        String encodedImage = Base64.encodeToString(newUser.getProfilePicture(), Base64.NO_WRAP);
        myRef.setValue(encodedImage);
    }

    public void insertUserSports(SportUtilizatorTable sportUtilizatorTable){
        try(Statement s = c.createStatement()){
            String command ="INSERT INTO SPORTUTILIZATOR VALUES("+newUser.getIdUser()
                    +","+sportUtilizatorTable.getIdSport()+",'"
                    +sportUtilizatorTable.getSportLevel()+"');";
            int updatedRows =s.executeUpdate(command);
            if(updatedRows >0 ) Log.d("Updatesuccesful","Updatesuccesful");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getUserID(){
        try(Statement s = c.createStatement()){
            String command ="SELECT ID FROM UTILIZATORI WHERE EMAIL='"+newUser.getEmail()+"';";
            try(ResultSet r =s.executeQuery(command)) {
                if(r.next()){
                    newUser.setIdUser(r.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public SportUtilizatorTable getSportID(SportUtilizator sportUtilizator){
        SportUtilizatorTable sportToAdd = new SportUtilizatorTable();
        try(Statement s = c.createStatement()){
            String command ="SELECT ID FROM Sporturi WHERE DENUMIRE='"+sportUtilizator.getSportName()+"';";
            try(ResultSet r =s.executeQuery(command)) {
                if(r.next()){
                    sportToAdd.setIdSport(r.getInt(1));
                    sportToAdd.setIdUtilizator(newUser.getIdUser());
                    sportToAdd.setSportLevel(getLevelInt(sportUtilizator.getLevel()));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sportToAdd;
    }

    private int getLevelInt(String level){
        if(level.equals("Începător"))return 0;
        if(level.equals("Elementar"))return 1;
        if(level.equals("Intermediar"))return 2;
        if(level.equals("Post-intermediar"))return 3;
        if(level.equals("Avansat"))return 4;
        if(level.equals("Expert"))return 5;
        return -2;
    }

}
