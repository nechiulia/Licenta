package com.example.teammanagement.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import com.example.teammanagement.R;
import com.example.teammanagement.Utils.Constants;
import com.example.teammanagement.Utils.User;
import com.example.teammanagement.adapters.ExploreUsersAdapter;
import com.example.teammanagement.database.JDBCController;
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

public class SearchUserActivity extends AppCompatActivity{
    private ListView lv_users;

    private ExploreUsersAdapter lv_adapter;

    private User currentUser;
    private List<User> list_users= new ArrayList<>();
    private Map<Integer,byte[]> listUserPhotos = new HashMap<>();

    private Intent intent;

    private JDBCController jdbcController;
    private Connection c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        this.setTitle(getString(R.string.loading_iv_socialTeam_hint));

        getUser();
        initComponents();
     }

    public void initComponents(){
        jdbcController=JDBCController.getInstance();
        c=jdbcController.openConnection();


        lv_users=findViewById(R.id.search_lv_userList);
        selectUsers();
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

        if(list_users!=null){
            lv_adapter = new ExploreUsersAdapter(getApplicationContext(), R.layout.list_item_users_explorer, list_users, getLayoutInflater());
            lv_users.setAdapter(lv_adapter);
        }
        lv_users.setOnItemClickListener(clickItem());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem searchViewItem = menu.findItem(R.id.app_bar_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint(getString(R.string.searchUser_search_actionBar_hint));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;

            }

            @Override
            public boolean onQueryTextChange(String newText) {
                lv_adapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public void getUser(){
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.APP_SHAREDPREF,MODE_PRIVATE);
        Gson gson = new Gson();
        String json =  sharedPreferences.getString(Constants.CURRENT_USER,"");
        currentUser= gson.fromJson(json,User.class);
    }

    public void selectUsers(){
        try(Statement s = c.createStatement()){
            String command ="SELECT * FROM UTILIZATORI WHERE ROL=0 AND STARE=0 AND ID!="+currentUser.getIdUser()+";";
            try(ResultSet r =s.executeQuery(command)) {
                while(r.next()){
                    User user= new User();
                    user.setIdUser(r.getInt(1));
                    user.setUserName(r.getString(2));
                    user.setEmail(r.getString(3));
                    user.setPassword(r.getString(4));
                    user.setState(r.getInt(5));
                    user.setRole(r.getInt(6));
                    list_users.add(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getUserProfilePicture(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        for(User user: list_users) {
            DatabaseReference myRef = database.getReference(String.valueOf(user.getIdUser()));
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String valoare = dataSnapshot.getValue(String.class);
                    String key=dataSnapshot.getKey();
                    if( valoare != null) {
                        byte[] decodedString = Base64.decode(valoare, Base64.DEFAULT);
                        listUserPhotos.put(Integer.parseInt(key),decodedString);
                        for(User u:list_users ) {
                            if(Integer.parseInt(key) == u.getIdUser()) {
                                u.setProfilePicture(decodedString);
                                break;
                            }
                        }
                    }
                    lv_adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError error) {
                }
            });
        }
    }

    private AdapterView.OnItemClickListener clickItem(){
        return new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User user= (User) parent.getItemAtPosition(position);
                intent = new Intent(getApplicationContext(), ProfileActivity.class);
                intent.putExtra(Constants.CLICKED_USERID,user);
                startActivity(intent);
            }
        };
    }

    @Override
    public void onBackPressed() {
        intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
    }
}
