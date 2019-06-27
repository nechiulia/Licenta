package com.example.teammanagement.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.example.teammanagement.Utils.SportUtilizator;
import com.example.teammanagement.Utils.User;
import com.example.teammanagement.adapters.SportAdapterNoCheckBox;
import com.example.teammanagement.dialogs.BottomDialogReport;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MyProfileActivity extends AppCompatActivity
        /*implements BottomDialogReport.BottomDialogListener */
{

    Button btn_logOut;
    ImageButton ibtn_back;
    ImageButton ibtn_edit;
    ImageButton ibtn_more;
    TextView tv_settings;
    TextView tv_moreFeedback;
    ListView lv_sports;
    TextView tv_userName;
    TextView tv_userComment;
    RatingBar ratingBar;
    ImageView iv_profilePicture;
    Intent intent;
    SportAdapterNoCheckBox adapter;
    User newUser;
    Bitmap bitmap;

    List<SportUtilizator> lv_list_sportItems = new ArrayList<>();
    List<Feedback> list_feedback = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myprofile);
        initComponents();

    }

    private void initComponents() {
        btn_logOut=findViewById(R.id.myProfile_btn_logOut);
        ibtn_back=findViewById(R.id.myProfile_ibtn_back);
        ibtn_edit=findViewById(R.id.myProfile_ibtn_edit);
        ibtn_more=findViewById(R.id.myProfile_ibtn_more);
        tv_settings=findViewById(R.id.myProfile_tv_settings);
        tv_moreFeedback =findViewById(R.id.myProfile_tv_moreFeedback);
        lv_sports=findViewById(R.id.myProfile_lv_sports);
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

        lv_list_sportItems.add(new SportUtilizator("Fotbal","Intermediar"));
        list_feedback.add(new Feedback("Alexandru Matei","Marius este o fire competitivă și un bun coechipier.Am jucat cu el acum cateva saptamani si ne-am distrat pe cinste. Sper sa mai am ocazia sa joc cu el.Marius este o fire competitivă și un bun coechipier. Am jucat cu el acum cateva saptamani si ne-am distrat pe cinste. Sper sa mai am ocazia sa joc cu el ",4.0f));
        list_feedback.add(new Feedback("Alexandru Matei","Marius este o fire competitivă și un bun coechipier. Am jucat cu el acum cateva saptamani si ne-am distrat pe cinste. Sper sa mai am ocazia sa joc cu el.Marius este o fire competitivă și un bun coechipier. Am jucat cu el acum cateva saptamani si ne-am distrat pe cinste. Sper sa mai am ocazia sa joc cu el",4.0f));
        randomFeedback();


        if(lv_list_sportItems != null) {
            adapter = new SportAdapterNoCheckBox(getApplicationContext(), R.layout.list_item_sports_nocheckbox, lv_list_sportItems, getLayoutInflater());
            lv_sports.setAdapter(adapter);
        }


        SharedPreferences sharedPreferences = getSharedPreferences(Constants.APP_SHAREDPREF,MODE_PRIVATE);
        Gson gson = new Gson();
        String json =  sharedPreferences.getString(Constants.CURRENT_USER,"");
        newUser= gson.fromJson(json,User.class);
        Log.d("Current User",String.valueOf(newUser.getProfilePicture()));

        if(newUser.getProfilePicture().length !=0) {
            bitmap = BitmapFactory.decodeByteArray(newUser.getProfilePicture(), 0, newUser.getProfilePicture().length);
            iv_profilePicture.setImageBitmap(Bitmap.createBitmap(bitmap));
        }
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
