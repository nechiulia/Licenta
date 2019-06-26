package com.example.teammanagement.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.example.teammanagement.adapters.SportAdapter;
import com.example.teammanagement.database.JDBCController;
import com.example.teammanagement.dialogs.AddSportDialog;
import com.example.teammanagement.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Register2Activity extends AppCompatActivity implements AddSportDialog.AddSportDialogListener {

    Button btn_save;
    Button btn_cancel;
    ImageButton ibtn_addSport;
    Intent intent;
    ListView lv_sports;
    List<SportUtilizator> lv_list_sportItems = new ArrayList<>();
    ImageButton ibtn_removeSport;
    SportAdapter adapter;
    View row;
    CheckBox ck_box;
    JDBCController jdbcController;
    Connection c;
    ArrayList<String> list_toGoToDialog = new ArrayList<>() ;
    ArrayList<String> lv_list_currentSportsName = new ArrayList<>();
    ArrayList<String> list_selectDB;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);
        initComponents();
        this.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));
        this.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK));
    }

    private void initComponents() {
        jdbcController = new JDBCController();
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
        initData();
        list_selectDB= new ArrayList<>();
        list_selectDB.addAll(list_toGoToDialog);

        if(lv_list_sportItems !=null) {
            adapter = new SportAdapter(getApplicationContext(), R.layout.list_item_sports, lv_list_sportItems, getLayoutInflater());
            lv_sports.setAdapter(adapter);
            lv_sports.setOnItemClickListener(listItemClick());
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
                try(Statement s = c.createStatement()){
                    String command ="";
                    try(ResultSet r =s.executeQuery(command)) {
                        while(r.next()){
                            Sport sport = new Sport(r.getString(2),r.getInt(3));
                            list_toGoToDialog.add(sport.getDenumire());
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                intent=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
            }
        };
    }

    private View.OnClickListener clickCancel() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(getApplicationContext(),LoginActivity.class);
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


    public void initData(){
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
}
