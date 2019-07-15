package com.example.teammanagement.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.teammanagement.R;
import com.example.teammanagement.Utils.Announcement;
import com.example.teammanagement.Utils.Constants;
import com.example.teammanagement.Utils.User;
import com.example.teammanagement.adapters.AnnouncementsAdapter;
import com.example.teammanagement.database.JDBCController;
import com.example.teammanagement.dialogs.AddAnnouncementDialog;
import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AnnouncementsActivity extends AppCompatActivity implements AddAnnouncementDialog.AddAnnouncementDialogListner {

    private ListView lv_announcements;
    private ImageButton ibtn_back;
    private ImageButton ibtn_add;

    private Intent intent;

    private AnnouncementsAdapter adapter;

    private JDBCController jdbcController;
    private Connection c;

    private int current_teamID;
    private Announcement clickedAnnouncement;
    private int longClickedID;
    private User currentUser;
    private String currentUserRole;
    private List<Announcement> list_announcements = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcements);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        intent=getIntent();

        initComponents();
    }

    public void initComponents(){

        jdbcController = JDBCController.getInstance();
        c=jdbcController.openConnection();

        ibtn_back = findViewById(R.id.announcements_ibtn_back);
        ibtn_add = findViewById(R.id.announcements_ibtn_add);
        lv_announcements = findViewById(R.id.announcements_lv_announcements);


        getUser();
        if(intent.hasExtra(Constants.CURRENT_TEAM_ID)){
            current_teamID=(Integer) intent.getSerializableExtra(Constants.CURRENT_TEAM_ID);
        }

        getUserRole();
        if(currentUserRole.equals(getString(R.string.role_captain_hint))||currentUserRole.equals(getString(R.string.role_trainer_hnt))){
            ibtn_add.setVisibility(View.VISIBLE);
            ibtn_add.setOnClickListener(clickAdd());
            lv_announcements.setOnItemLongClickListener(clickDelete());
            lv_announcements.setOnItemClickListener(clickEdit());
        }

        getTeamAnnouncements();

        if(list_announcements != null){
            adapter = new AnnouncementsAdapter(getApplicationContext(),R.layout.list_item_annoucements,list_announcements,getLayoutInflater());
            lv_announcements.setAdapter(adapter);
        }

        ibtn_back.setOnClickListener(clickBack());


    }

    public void getUser(){
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.APP_SHAREDPREF,MODE_PRIVATE);
        Gson gson = new Gson();
        String json =  sharedPreferences.getString(Constants.CURRENT_USER,"");
        currentUser = gson.fromJson(json, User.class);
    }

    public void insertAnnouncement(String message){
        Date date = new Date();
        try(PreparedStatement s = c.prepareStatement("INSERT INTO ANUNTURI VALUES('"+ Constants.simpleDateFormat.format(date)+"',N'"+message+"',"+currentUser.getIdUser()+","+current_teamID+");",Statement.RETURN_GENERATED_KEYS)){
            int updatedRows=s.executeUpdate();
            ResultSet r=s.getGeneratedKeys();
            if(r.next()){
                if(updatedRows >0 ) {
                    Log.d("databaseUpdateUser", String.valueOf(updatedRows));
                    Announcement announcement = new Announcement();
                    announcement.setID(r.getInt(1));
                    announcement.setDate(Constants.simpleDateFormat.format(date));
                    announcement.setMessage(message);
                    announcement.setUserID(currentUser.getIdUser());
                    announcement.setTeamID(current_teamID);
                    list_announcements.add(announcement);
                    adapter.notifyDataSetChanged();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getTeamAnnouncements(){
        try(Statement s = c.createStatement()){
            try(ResultSet r = s.executeQuery("SELECT * FROM ANUNTURI WHERE IDECHIPA="+current_teamID)){
                while(r.next()){
                    Announcement announcement = new Announcement();
                    announcement.setID(r.getInt(1));
                    announcement.setDate(r.getString(2));
                    announcement.setMessage(r.getString(3));
                    announcement.setUserID(r.getInt(4));
                    announcement.setTeamID(r.getInt(5));
                    list_announcements.add(announcement);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getUserRole(){
        try(Statement s = c.createStatement()){
            try(ResultSet r = s.executeQuery("SELECT DENUMIRE FROM ROLECHIPA WHERE IDUTILIZATOR="+currentUser.getIdUser()+" AND IDECHIPA="+current_teamID)){
                if(r.next()){
                    currentUserRole=r.getString(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteAnnouncement(){
        try(Statement s = c.createStatement()){
            s.executeUpdate("DELETE FROM ANUNTURI WHERE ID="+longClickedID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateAnnouncement(String message){
        Date date = new Date();
        try(Statement s = c.createStatement()){
            s.executeUpdate("UPDATE ANUNTURI SET DATA='"+Constants.simpleDateFormat.format(date)+"', MESAJ=N'"+message+"' WHERE ID="+clickedAnnouncement.getID());
            for(Announcement a: list_announcements){
                if(a.getID() == clickedAnnouncement.getID()){
                    a.setMessage(message);
                    a.setDate(Constants.simpleDateFormat.format(date));
                    adapter.notifyDataSetChanged();
                    break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void openDialog(){
        Bundle args = new Bundle();

        AddAnnouncementDialog addAnnouncementDialog = new AddAnnouncementDialog();
        addAnnouncementDialog.show(getSupportFragmentManager(),getString(R.string.announcements_tag_hint));

        if(clickedAnnouncement != null){
            args.putSerializable("announcement", clickedAnnouncement);
        }

        addAnnouncementDialog.setArguments(args);
        addAnnouncementDialog.setCancelable(false);
    }

    private View.OnClickListener clickBack() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               intent = new Intent(getApplicationContext(),TeamProfileActivity.class);
                intent.putExtra(Constants.CURRENT_TEAM_ID,current_teamID);
                startActivity(intent);
            }
        };
    }

    private View.OnClickListener clickAdd() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedAnnouncement = null;
                openDialog();
            }
        };
    }

    private AdapterView.OnItemClickListener clickEdit(){
        return new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                clickedAnnouncement = (Announcement) parent.getItemAtPosition(position);
                openDialog();
            }
        };
    }

    private AdapterView.OnItemLongClickListener clickDelete()
    {
        return new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(AnnouncementsActivity.this);
                builder.setTitle(getString(R.string.announcements_alertDialog_delete_hint))
                        .setMessage(getString(R.string.announcements_alertDialog_delete_message_hint))
                        .setCancelable(false)
                        .setNegativeButton(getString(R.string.announcements_alertDialog_negativeBtn_hint), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setPositiveButton(getString(R.string.announcements_alertDialog_positiveBtn_hint), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                longClickedID =list_announcements.get(position).getID();
                                deleteAnnouncement();
                                list_announcements.remove(position);
                                adapter.notifyDataSetChanged();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                return true;
            }
        };
    }

    @Override
    public void applyTexts(String message, int ok) {
        if(ok == 0) {
            insertAnnouncement(message);
        }
        else{
            updateAnnouncement(message);
        }
    }
}
