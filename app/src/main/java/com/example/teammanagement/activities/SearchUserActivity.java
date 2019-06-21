package com.example.teammanagement.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import com.example.teammanagement.R;
import com.example.teammanagement.Utils.User;
import com.example.teammanagement.adapters.ExploreUsersAdapter;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class SearchUserActivity extends AppCompatActivity {
    ListView lv_users;
    EditText et_userName;
    ExploreUsersAdapter lv_adapter;
    List<User> list_users= new ArrayList<>();
    Intent intent;
    byte[] byteArray;
    List<String> list_userName = new ArrayList<>();
    User user1;
    User user2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        this.setTitle(getString(R.string.loading_iv_socialTeam_hint));

        Thread timer = new Thread(){
            @Override
            public void run() {
                Drawable d=getDrawable(R.drawable.woman);
                Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
                byte[] byteArray = stream.toByteArray();
                user1 = new User("Pipel",byteArray);
                user2 = new User("Gigel",byteArray);
                list_users.add(user1);
                list_users.add(user2);
            }
        };
        timer.start();
        try {
            timer.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        initComponents();
     }

    public void initComponents(){
        lv_users=findViewById(R.id.search_lv_userList);
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

    private AdapterView.OnItemClickListener clickItem(){
        return new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
            }
        };
    }

}
