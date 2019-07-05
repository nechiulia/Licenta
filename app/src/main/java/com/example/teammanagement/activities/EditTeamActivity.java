package com.example.teammanagement.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.teammanagement.R;
import com.example.teammanagement.Utils.Constants;
import com.example.teammanagement.Utils.Team;
import com.example.teammanagement.Utils.Teammate;
import com.example.teammanagement.Utils.User;
import com.example.teammanagement.adapters.TeammatesCkAdapter;
import com.example.teammanagement.database.JDBCController;
import com.example.teammanagement.dialogs.AddSportDialog;
import com.example.teammanagement.dialogs.EditRoleDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditTeamActivity extends AppCompatActivity implements EditRoleDialog.EditRoleDialogListener {

    private TextInputEditText iet_teamName;
    private ListView lv_teammates;
    private Button btn_cancel;
    private Button btn_save;
    private ImageButton ibtn_add_temmate;
    private ImageButton ibtn_remove_temmate;

    private Intent intent;

    private TeammatesCkAdapter adapter;

    private JDBCController jdbcController;
    private Connection c;

    private User currentUser;
    private int currentTeamID;
    private Team currentTeam= new Team();
    private int clickedTeammateID;
    private List<Integer> list_teammatesID = new ArrayList<>();
    private List<Teammate> list_teammates = new ArrayList<>();
    private List<Teammate> checkedTeammates = new ArrayList<>();
    private Map<Integer,byte[]> list_teammatesPhotos = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_team);

        intent=getIntent();

        initComponents();
    }

    public void initComponents(){

        jdbcController = JDBCController.getInstance();
        c=jdbcController.openConnection();

        iet_teamName = findViewById(R.id.editTeam_tid_teamName);
        lv_teammates = findViewById(R.id.editTeam_lv_players);
        btn_cancel = findViewById(R.id.editTeam_btn_cancel);
        btn_save=findViewById(R.id.editTeam_btn_save);
        ibtn_add_temmate=findViewById(R.id.editTeam_btn_addTeamMate);
        ibtn_remove_temmate = findViewById(R.id.editTeam_btn_removeTeamMate);

        getCurrentUser();
        getTeam();
        selectTeammatesID();
        for(int i = 0; i< list_teammatesID.size(); i++){
            getTeammatesInfo(list_teammatesID.get(i));
        }

        for(Teammate teammate: list_teammates){
            getRoles(teammate);
        }

        Thread t1 = new Thread(){
            @Override
            public void run() {
                getUserProfilePicture();
            }
        };
        t1.start();
        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



        if(list_teammates.size() != 0){
            adapter = new TeammatesCkAdapter(getApplicationContext(), R.layout.list_item_teammates_ck, list_teammates, getLayoutInflater());
            lv_teammates.setAdapter(adapter);
        }

        iet_teamName.setText(currentTeam.getTeamName());
        iet_teamName.addTextChangedListener(changeTeamName());
        btn_save.setOnClickListener(clickSave());
        btn_cancel.setOnClickListener(clickCancel());
        ibtn_remove_temmate.setOnClickListener(clickRemove());
        ibtn_add_temmate.setOnClickListener(clickAdd());
        lv_teammates.setOnItemLongClickListener(editRole());

    }

    public void getCurrentUser(){
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.APP_SHAREDPREF,MODE_PRIVATE);
        Gson gson = new Gson();
        String json =  sharedPreferences.getString(Constants.CURRENT_USER,"");
        currentUser= gson.fromJson(json, User.class);
    }

    public void getTeam(){

        if(intent.hasExtra(Constants.CURRENT_TEAM_ID)){
            currentTeamID=(Integer) intent.getSerializableExtra(Constants.CURRENT_TEAM_ID);
        }

        getTeamInfo();
    }
    public void getTeamInfo(){
        try(Statement s = c.createStatement()){
            try(ResultSet r = s.executeQuery("SELECT * FROM ECHIPE WHERE ID="+currentTeamID)){
                if(r.next()){
                    currentTeam.setId(currentTeamID);
                    currentTeam.setTeamName(r.getString(2));
                    currentTeam.setSport(r.getString(3));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void selectTeammatesID(){
        try(Statement s = c.createStatement()){
            try(ResultSet r = s.executeQuery("SELECT IDUTILIZATOR FROM ROLECHIPA WHERE IDECHIPA="+currentTeam.getId()+" AND IDUTILIZATOR!="+currentUser.getIdUser())){
                while(r.next()){
                    list_teammatesID.add(r.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getTeammatesInfo(int userID){
        try(Statement s = c.createStatement()){
            String command ="SELECT ID,NUMEUTILIZATOR FROM UTILIZATORI WHERE ROL=0 AND STARE=0 AND ID="+userID+";";
            try(ResultSet r =s.executeQuery(command)) {
               if(r.next()){
                    Teammate user= new Teammate();
                    user.setUserID(r.getInt(1));
                    user.setUserName(r.getString(2));
                    list_teammates.add(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getUserProfilePicture(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        for(Teammate user: list_teammates) {
            DatabaseReference myRef = database.getReference(String.valueOf(user.getUserID()));
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String valoare = dataSnapshot.getValue(String.class);
                    String key=dataSnapshot.getKey();
                    if( valoare != null) {
                        byte[] decodedString = Base64.decode(valoare, Base64.DEFAULT);
                        list_teammatesPhotos.put(Integer.parseInt(key),decodedString);
                        for(Teammate u: list_teammates) {
                            if(Integer.parseInt(key) == u.getUserID()) {
                                u.setUserProfilePicture(decodedString);
                                break;
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError error) {
                }
            });
        }
    }

    public void getRoles(Teammate teammate){
        try(Statement s = c.createStatement()){
            try(ResultSet r = s.executeQuery("SELECT DENUMIRE FROM ROLECHIPA WHERE IDECHIPA="+currentTeam.getId()+" AND IDUTILIZATOR="+teammate.getUserID())){
                if(r.next()){
                   teammate.setTeamRole(r.getString(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getMinPlayers(){
        try(Statement s = c.createStatement()){
            try(ResultSet r = s.executeQuery("SELECT NRMINJUCATORI FROM SPORTURI WHERE DENUMIRE='"+currentTeam.getSport()+"';")){
                if(r.next()){
                    return r.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void insertRole(Teammate teammate){
        try(Statement s = c.createStatement()){
            int updatedRows=s.executeUpdate("INSERT INTO ROLECHIPA VALUES("+teammate.getUserID()+","+currentTeam.getId()+",N'"+teammate.getTeamRole()+"');");
            if(updatedRows >0 ) {
                Log.d("databaseUpdated", String.valueOf(updatedRows));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void updateName(){
        try(Statement s = c.createStatement()){
            s.executeUpdate("UPDATE ECHIPE SET DENUMIRE='"+iet_teamName.getText().toString()+"' WHERE ID="+currentTeam.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteTeammates(){
        try(Statement s = c.createStatement()){
            s.executeUpdate("DELETE FROM ROLECHIPA WHERE IDUTILIZATOR!="+currentUser.getIdUser()+"AND IDECHIPA="+currentTeam.getId()+";");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteTeammate(Teammate teammate){
        try(Statement s = c.createStatement()){
            s.executeUpdate("DELETE FROM ROLECHIPA WHERE IDUTILIZATOR="+teammate.getUserID()+" AND IDECHIPA="+currentTeam.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private View.OnClickListener clickCancel() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(getApplicationContext(), TeamProfileActivity.class);
                intent.putExtra(Constants.CURRENT_TEAM_ID,currentTeam.getId());
                startActivity(intent);
            }
        };
    }

    private View.OnClickListener clickSave() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int minPlayers= getMinPlayers();
                if(isValidName()) {
                    updateName();
                    if (isValidTeammates()) {
                        deleteTeammates();
                        for (Teammate teammate : list_teammates) {
                            insertRole(teammate);
                        }
                        intent = new Intent(getApplicationContext(), TeamProfileActivity.class);
                        intent.putExtra(Constants.CURRENT_TEAM_ID,currentTeam.getId());
                        startActivity(intent);
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(EditTeamActivity.this);
                        builder.setTitle(getString(R.string.add_team2_alertDialog_title))
                                .setMessage(getString(R.string.add_team2_alertDialog_message) + minPlayers)
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
            }
        };
    }

    private TextWatcher changeTeamName(){
        return new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
               iet_teamName.setError(null);
            }
        };
    }

    private View.OnClickListener clickAdd() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(getApplicationContext(), AddTeammatesActivity.class);
                intent.putExtra(Constants.CLICKED_TEAM,currentTeam);
                startActivityForResult(intent,Constants.ADD_TEAMMATES_CODE);
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK&&data!=null) {
            if (requestCode == Constants.ADD_TEAMMATES_CODE) {
                list_teammates.addAll((ArrayList<Teammate>) data.getSerializableExtra(Constants.ADDED_TEAMMATES));
                adapter.notifyDataSetChanged();
            }
        }
    }

    private View.OnClickListener clickRemove() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkedTeammates=adapter.getCheckedTeammates();
                for(Teammate teammate: checkedTeammates){
                    deleteTeammate(teammate);
                }

                list_teammates.removeAll(adapter.getCheckedTeammates());
                adapter.notifyDataSetChanged();
            }
        };
    }

    private AdapterView.OnItemLongClickListener editRole()
    {
        return new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                clickedTeammateID=position;
                openDialog();

                return false;
            }
        };
    }

    public boolean isValidName(){
        if(iet_teamName.getText().length()<3){
            iet_teamName.setError(getString(R.string.add_team1_teamName_lengthError_hint));
            return false;
        }
        if(!iet_teamName.getText().toString().matches("^[a-zA-Z0-9_ ]*$")){
            iet_teamName.setError(getString(R.string.add_team1_teamName_digitAndLettersOnlyError_hint));
            return false;
        }
        return true;
    }

    public boolean isValidTeammates(){
        int minPlayers= getMinPlayers();
        if(list_teammates.size() >=(minPlayers-1)  && list_teammates.size()>0)return true;
        return false;
    }

    private void openDialog(){
        Bundle args = new Bundle();

        EditRoleDialog editRoleDialog=new EditRoleDialog();
        editRoleDialog.show(getSupportFragmentManager(),"Editează roluri");

        editRoleDialog.setArguments(args);
    }


    @Override
    public void applyTexts(String role) {

        list_teammates.get(clickedTeammateID).setTeamRole(role);
        adapter.notifyDataSetChanged();
    }
}
