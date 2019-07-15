package com.example.teammanagement.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProfileActivity extends AppCompatActivity{

    private ImageButton ibtn_more;
    private TextView tv_moreFeedback;
    private ListView lv_sports;
    private TextView tv_userName;
    private TextView tv_userComment;
    private TextView tv_userProfileName;
    private ImageView iv_profilePicture;
    private RatingBar ratingBar;
    private RatingBar rb_user;
    private TextView tv_score;

    private Intent intent;

    private User clickedUser;
    private User currentUser;
    private List<SportUser> lv_list_sportItems = new ArrayList<>();

    private SportAdapterNoCheckBox adapter;

    private JDBCController jdbcController;
    private Connection c;

    private Bitmap bitmap;

    List<Feedback> listFeedback = new ArrayList<>();
    private List<String> listSenderNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        intent=getIntent();

        initComponents();
    }

    public void initComponents(){

        jdbcController= JDBCController.getInstance();
        c=jdbcController.openConnection();

        ibtn_more=findViewById(R.id.profile_ibtn_more);
        tv_moreFeedback =findViewById(R.id.profile_tv_moreFeedback);
        lv_sports=findViewById(R.id.profile_lv_sports);
        tv_userComment=findViewById(R.id.profile_tv_comment);
        tv_userName=findViewById(R.id.profile_tv_commentUserName);
        ratingBar=findViewById(R.id.profile_rb_userGivenRating);
        iv_profilePicture=findViewById(R.id.profile_iv_profileImage);
        tv_userProfileName=findViewById(R.id.profile_tv_userName);
        rb_user=findViewById(R.id.profile_rb_userRating);
        tv_score=findViewById(R.id.profile_tv_commentStars);

        ibtn_more.setOnClickListener(clickMore());
        tv_moreFeedback.setOnClickListener(clickMoreReviews());

        getCurrentUser();

        if(intent.hasExtra(Constants.CLICKED_USERID)){
            clickedUser = (User) intent.getSerializableExtra(Constants.CLICKED_USERID);
        }

        initData();

        if(lv_list_sportItems != null) {
            adapter = new SportAdapterNoCheckBox(getApplicationContext(), R.layout.list_item_sports_nocheckbox, lv_list_sportItems, getLayoutInflater());
            lv_sports.setAdapter(adapter);
        }

        selectFeedback();
        if(listFeedback.size() != 0) {
            for (int i = 0; i < listFeedback.size(); i++) {
                selectSenderNames(listFeedback.get(i).getSenderID());
            }
            tv_moreFeedback.setVisibility(View.VISIBLE);
            randomFeedback();
            float x = averageScore();
            BigDecimal result;
            result=round(x,2);
            tv_score.setText(result.toString());
            rb_user.setRating(x);
        }
        else{
            if(isTeammate()){
                tv_moreFeedback.setVisibility(View.VISIBLE);
            }
            rb_user.setVisibility(View.INVISIBLE);
            tv_score.setVisibility(View.INVISIBLE);
            tv_userName.setVisibility(View.INVISIBLE);
            ratingBar.setVisibility(View.INVISIBLE);
            ibtn_more.setVisibility(View.INVISIBLE);
            tv_userComment.setText(getString(R.string.profile_nofeedback_hint));
        }

    }

    public boolean isTeammate(){
        try(Statement s = c.createStatement()){
            try(ResultSet r = s.executeQuery("select * from RolEchipa" +
                    "  where IDUtilizator ="+ currentUser.getIdUser()+" AND " +
                    "IDEchipa IN (SELECT IDEchipa" +
                    " FROM RolEchipa" +
                    " Where IDUtilizator ="+ clickedUser.getIdUser()+")")){
                if(r.next()){
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void getCurrentUser(){
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.APP_SHAREDPREF,MODE_PRIVATE);
        Gson gson = new Gson();
        String json =  sharedPreferences.getString(Constants.CURRENT_USER,"");
        currentUser = gson.fromJson(json,User.class);
    }

    public void initData(){
        if(clickedUser.getProfilePicture() != null) {
            bitmap = BitmapFactory.decodeByteArray(clickedUser.getProfilePicture(), 0, clickedUser.getProfilePicture().length);
            iv_profilePicture.setImageBitmap(Bitmap.createBitmap(bitmap));
        }
        tv_userProfileName.setText(clickedUser.getUserName());
        selectSports();

    }

    public void selectSports(){
        try(Statement s = c.createStatement()){
            String command ="SELECT S.DENUMIRE,SU.NIVEL FROM UTILIZATORI U, SPORTURI S, SPORTUTILIZATOR SU WHERE U.ID=SU.IDUTILIZATOR AND SU.IDSPORT=S.ID AND U.ID='"+ clickedUser.getIdUser()+"';";
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


    public void selectFeedback(){
        int idUser=currentUser.getIdUser();
        if(clickedUser != null){
            idUser=clickedUser.getIdUser();
        }
        try(Statement s = c.createStatement()){
            try(ResultSet r = s.executeQuery("SELECT * FROM FEEDBACKS WHERE  IDRECEPTOR="+idUser+ "AND STARE=0")){
                while(r.next()){
                    Feedback feedback = new Feedback();
                    feedback.setFeedbackID(r.getInt(1));
                    feedback.setReceiverID(r.getInt(2));
                    feedback.setSenderID(r.getInt(3));
                    feedback.setComment(r.getString(4));
                    feedback.setRating(r.getFloat(5));
                    feedback.setDate(r.getString(6));
                    feedback.setState(r.getInt(7));
                    listFeedback.add(feedback);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void selectSenderNames(int userID){
        try(Statement s = c.createStatement()){
            try(ResultSet r = s.executeQuery("SELECT NUMEUTILIZATOR FROM UTILIZATORI WHERE ID="+userID)) {
                if(r.next()){
                    listSenderNames.add(r.getString(1));
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
        else if(level == 5)return getString(R.string.user_sport_level_5);
        return "-";
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
    private View.OnClickListener clickMoreReviews(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(getApplicationContext(), FeedbackActivity.class);
                intent.putExtra(Constants.CLICKED_USER, clickedUser);
                startActivity(intent);
            }
        };
    }

    public void randomFeedback(){
        Random rand = new Random();
        int n=rand.nextInt(listFeedback.size());
        Feedback shownFeedback = listFeedback.get(n);
        String userName = listSenderNames.get(n);

        tv_userName.setText(userName);
        tv_userComment.setText(shownFeedback.getComment());
        ratingBar.setRating(shownFeedback.getRating());

    }

    public float averageScore(){
        float sum=0.0f;
        for(int i= 0 ; i<listFeedback.size();i++){
            sum+=listFeedback.get(i).getRating();
        }
        return sum/listFeedback.size();
    }

    public static BigDecimal round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        intent=new Intent(getApplicationContext(), SearchUserActivity.class);
        startActivity(intent);
    }
}
