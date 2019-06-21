package com.example.teammanagement.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.example.teammanagement.R;
import com.example.teammanagement.Utils.Constants;
import com.example.teammanagement.Utils.Sport;
import com.example.teammanagement.adapters.SportAdapter;
import com.example.teammanagement.dialogs.AddSportDialog;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EditProfileActivity extends AppCompatActivity implements AddSportDialog.AddSportDialogListener{

    Button btn_upload;
    Button btn_save;
    Button btn_cancel;
    ImageButton ibtn_back;
    ImageButton ibtn_addSport;
    ImageButton ibtn_removeSport;
    ListView lv_sports;
    ImageView iv_profilePicture;
    TextView tv_email;
    Intent intent;
    SportAdapter adapter;
    View row;
    CheckBox ck_box;

    List<Sport> lv_list_sportItems = new ArrayList<>();
    ArrayList<String> list_toGoToDialog ;
    ArrayList<String> lv_list_currentSportsName = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        initComponents();
    }

    private void initComponents() {

        btn_cancel=findViewById(R.id.editProfile_btn_cancel);
        btn_save=findViewById(R.id.editProfile_btn_save);
        btn_upload=findViewById(R.id.editProfile_btn_upload);
        ibtn_addSport=findViewById(R.id.editProfile_ibtn_addSport);
        ibtn_removeSport=findViewById(R.id.editProfile_ibtn_removeSport);
        lv_sports=findViewById(R.id.editProfile_lv_sports);
        lv_sports.setItemsCanFocus(true);
        tv_email=findViewById(R.id.editProfile_tv_email);
        ibtn_back=findViewById(R.id.editProfile_ibtn_back);
        iv_profilePicture=findViewById(R.id.editProfile_iv_profileImage);

        btn_upload.setOnClickListener(clickUpload());
        btn_save.setOnClickListener(clickSave());
        btn_cancel.setOnClickListener(clickCancel());
        ibtn_removeSport.setOnClickListener(clickRemoveSport());
        ibtn_addSport.setOnClickListener(clickAddSport());
        ibtn_back.setOnClickListener(clickBack());

        list_toGoToDialog= new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.dialog_sports)));

        if(lv_list_sportItems !=null) {
            adapter = new SportAdapter(getApplicationContext(), R.layout.list_item_sports_darktext, lv_list_sportItems, getLayoutInflater());
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

    private View.OnClickListener clickSave() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValid()){
                    intent=new Intent(getApplicationContext(), MyProfileActivity.class);
                    startActivity(intent);
                }
            }
        };
    }

    private View.OnClickListener clickBack() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(getApplicationContext(), MyProfileActivity.class);
                startActivity(intent);
            }
        };
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

    private boolean isValid(){
        if( isValidList())return true;
        return false;
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
