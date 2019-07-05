package com.example.teammanagement.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.example.teammanagement.R;
import com.example.teammanagement.Utils.Constants;
import com.example.teammanagement.Utils.Feedback;
import com.example.teammanagement.Utils.SportUser;
import com.example.teammanagement.Utils.User;
import com.example.teammanagement.adapters.SportAdapterNoCheckBox;
import com.example.teammanagement.database.JDBCController;
import com.example.teammanagement.dialogs.BottomDialogReport;
import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MyProfileActivity extends AppCompatActivity
{

    private Button btn_logOut;
    private ImageButton ibtn_back;
    private ImageButton ibtn_edit;
    private ImageButton ibtn_more;
    private TextView tv_settings;
    private TextView tv_moreFeedback;
    private ListView lv_sports;
    private TextView tv_userName;
    private TextView tv_userComment;
    private TextView tv_userProfile;
    private RatingBar ratingBar;
    private ImageView iv_profilePicture;

    private Intent intent;

    private SportAdapterNoCheckBox adapter;

    private User currentUser;
    private List<SportUser> lv_list_sportItems = new ArrayList<>();
    private List<Feedback> list_feedback = new ArrayList<>();

    private Bitmap bitmap;

    private JDBCController jdbcController;
    private Connection c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myprofile);

        initComponents();
    }

    private void initComponents() {

        jdbcController = JDBCController.getInstance();
        c=jdbcController.openConnection();

        btn_logOut=findViewById(R.id.myProfile_btn_logOut);
        ibtn_back=findViewById(R.id.myProfile_ibtn_back);
        ibtn_edit=findViewById(R.id.myProfile_ibtn_edit);
        ibtn_more=findViewById(R.id.myProfile_ibtn_more);
        tv_settings=findViewById(R.id.myProfile_tv_settings);
        tv_moreFeedback =findViewById(R.id.myProfile_tv_moreFeedback);
        lv_sports=findViewById(R.id.myProfile_lv_sports);
        tv_userProfile =findViewById(R.id.myProfile_tv_userName);
        tv_userComment=findViewById(R.id.myProfile_tv_comment);
        tv_userName=findViewById(R.id.myProfile_tv_commentUserName);
        ratingBar=findViewById(R.id.myProfile_rb_userGivenRating);
        iv_profilePicture=findViewById(R.id.myProfile_iv_profileImage);


        ibtn_back.setOnClickListener(clickBack());
        ibtn_more.setOnClickListener(clickMore());
        ibtn_edit.setOnClickListener(clickEdit());
        btn_logOut.setOnClickListener(clickLogOut());
        tv_settings.setOnClickListener(clickSettings());
        tv_moreFeedback.setOnClickListener(clickMoreReviews());

        list_feedback.add(new Feedback("Alexandru Matei","Ionel este o fire competitivă și un bun coechipier.Am jucat cu el acum cateva saptamani si ne-am distrat pe cinste. ",4.0f));
        list_feedback.add(new Feedback("Alexandru Matei","Ionel este o fire competitivă și un bun coechipier. Am jucat cu el acum cateva saptamani si ne-am distrat pe cinste. ",4.0f));
        randomFeedback();

        getCurrentUser();

        initData();

        if(lv_list_sportItems != null) {
            adapter = new SportAdapterNoCheckBox(getApplicationContext(), R.layout.list_item_sports_nocheckbox, lv_list_sportItems, getLayoutInflater());
            lv_sports.setAdapter(adapter);
        }

    }

    public void initData(){
        if(currentUser.getProfilePicture() != null) {
            bitmap = BitmapFactory.decodeByteArray(currentUser.getProfilePicture(), 0, currentUser.getProfilePicture().length);
            iv_profilePicture.setImageBitmap(Bitmap.createBitmap(bitmap));
        }
        tv_userProfile.setText(currentUser.getUserName());
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

    public void getCurrentUser(){
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.APP_SHAREDPREF,MODE_PRIVATE);
        Gson gson = new Gson();
        String json =  sharedPreferences.getString(Constants.CURRENT_USER,"");
        currentUser = gson.fromJson(json,User.class);
    }

    private View.OnClickListener clickBack(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(getApplicationContext(),HomeActivity.class);
                startActivity(intent);
            }
        };
    }
    private View.OnClickListener clickMore(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomDialogReport bottomDialog =new BottomDialogReport();
                bottomDialog.show(getSupportFragmentManager(), Constants.MORE_ACTIONS);
            }
        };
    }
    private View.OnClickListener clickEdit(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(getApplicationContext(),EditProfileActivity.class);
                startActivity(intent);
            }
        };
    }
    private View.OnClickListener clickLogOut(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
            }
        };
    }

    private View.OnClickListener clickSettings(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(getApplicationContext(),SettingsActivity.class);
                startActivity(intent);
            }
        };
    }
    private View.OnClickListener clickMoreReviews(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(getApplicationContext(), FeedbackActivity.class);
                startActivity(intent);
            }
        };
    }

    public void randomFeedback(){
        Random rand = new Random();
        int n=rand.nextInt(list_feedback.size());
        Feedback shownFeedback = list_feedback.get(n);

        tv_userName.setText(shownFeedback.getUserName());
        tv_userComment.setText(shownFeedback.getComment());
        ratingBar.setRating(shownFeedback.getRating());

    }
}
