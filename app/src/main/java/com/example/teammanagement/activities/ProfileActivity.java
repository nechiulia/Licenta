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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProfileActivity extends AppCompatActivity{

    private ImageButton ibtn_back;
    private ImageButton ibtn_more;
    private TextView tv_moreFeedback;
    private ListView lv_sports;
    private TextView tv_userName;
    private TextView tv_userComment;
    private TextView tv_userProfileName;
    private ImageView iv_profilePicture;
    private RatingBar ratingBar;

    private Intent intent;

    private User user;
    private List<SportUser> lv_list_sportItems = new ArrayList<>();
    private List<Feedback> list_feedback = new ArrayList<>();

    private SportAdapterNoCheckBox adapter;

    private JDBCController jdbcController;
    private Connection c;

    private Bitmap bitmap;

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

        ibtn_back=findViewById(R.id.profile_ibtn_back);
        ibtn_more=findViewById(R.id.profile_ibtn_more);
        tv_moreFeedback =findViewById(R.id.profile_tv_moreFeedback);
        lv_sports=findViewById(R.id.profile_lv_sports);
        tv_userComment=findViewById(R.id.profile_tv_comment);
        tv_userName=findViewById(R.id.profile_tv_commentUserName);
        ratingBar=findViewById(R.id.profile_rb_userGivenRating);
        iv_profilePicture=findViewById(R.id.profile_iv_profileImage);
        tv_userProfileName=findViewById(R.id.profile_tv_userName);

        ibtn_back.setOnClickListener(clickBack());
        ibtn_more.setOnClickListener(clickMore());
        tv_moreFeedback.setOnClickListener(clickMoreReviews());

        if(intent.hasExtra(Constants.CLICKED_USERID)){
            user= (User) intent.getSerializableExtra(Constants.CLICKED_USERID);
        }

        initData();

        list_feedback.add(new Feedback("Alexandru Matei","Marius este o fire competitivă și un bun coechipier.Am jucat cu el acum cateva saptamani si ne-am distrat pe cinste. Sper sa mai am ocazia sa joc cu el.Marius este o fire competitivă și un bun coechipier. Am jucat cu el acum cateva saptamani si ne-am distrat pe cinste. Sper sa mai am ocazia sa joc cu el ",4.0f));
        list_feedback.add(new Feedback("Alexandru Matei","Marius este o fire competitivă și un bun coechipier. Am jucat cu el acum cateva saptamani si ne-am distrat pe cinste. Sper sa mai am ocazia sa joc cu el.Marius este o fire competitivă și un bun coechipier. Am jucat cu el acum cateva saptamani si ne-am distrat pe cinste. Sper sa mai am ocazia sa joc cu el",4.0f));
        randomFeedback();


        if(lv_list_sportItems != null) {
            adapter = new SportAdapterNoCheckBox(getApplicationContext(), R.layout.list_item_sports_nocheckbox, lv_list_sportItems, getLayoutInflater());
            lv_sports.setAdapter(adapter);
        }

    }

    public void initData(){
        if(user.getProfilePicture() != null) {
            bitmap = BitmapFactory.decodeByteArray(user.getProfilePicture(), 0, user.getProfilePicture().length);
            iv_profilePicture.setImageBitmap(Bitmap.createBitmap(bitmap));
        }
        tv_userProfileName.setText(user.getUserName());
        selectSports();

    }

    public void selectSports(){
        try(Statement s = c.createStatement()){
            String command ="SELECT S.DENUMIRE,SU.NIVEL FROM UTILIZATORI U, SPORTURI S, SPORTUTILIZATOR SU WHERE U.ID=SU.IDUTILIZATOR AND SU.IDSPORT=S.ID AND U.ID='"+user.getIdUser()+"';";
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
        else if(level == 5) getString(R.string.user_sport_level_5);
        return "-";
    }

    private View.OnClickListener clickBack(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(getApplicationContext(),SearchUserActivity.class);
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
