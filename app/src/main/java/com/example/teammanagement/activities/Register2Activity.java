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
import com.example.teammanagement.adapters.SportAdapter;
import com.example.teammanagement.dialogs.AddSportDialog;
import com.example.teammanagement.R;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Register2Activity extends AppCompatActivity implements AddSportDialog.AddSportDialogListener {

    Button btn_save;
    ImageButton ibtn_addSport;
    Intent intent;
    ListView lv_sports;
    List<Sport> lv_list_sportItems = new ArrayList<>();
    ImageButton ibtn_removeSport;
    SportAdapter adapter;
    View row;
    CheckBox ck_box;
    ArrayList<String> list_toGoToDialog ;
    ArrayList<String> lv_list_currentSportsName = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);
        initComponents();
        this.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));
        this.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK));
    }

    private void initComponents() {
        btn_save=findViewById(R.id.register2_btn_save);
        ibtn_addSport =findViewById(R.id.register2_ibtn_addSport);
        lv_sports=findViewById(R.id.register2_lv_sports);
        lv_sports.setItemsCanFocus(true);
        ibtn_removeSport=findViewById(R.id.register2_ibtn_removeSport);

        ibtn_addSport.setOnClickListener(clickAddSport());
        btn_save.setOnClickListener(clickSave());
        ibtn_removeSport.setOnClickListener(clickRemove());
        list_toGoToDialog= new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.dialog_sports)));

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
        lv_list_sportItems.add(new Sport(sport,level));
        list_toGoToDialog= new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.dialog_sports)));
        adapter.notifyDataSetChanged();
    }
}
