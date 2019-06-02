package com.example.teammanagement.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.teammanagement.R;
import com.example.teammanagement.Utils.Constants;
import com.example.teammanagement.Utils.Feedback;
import com.example.teammanagement.Utils.Sport;
import com.example.teammanagement.adapters.SportAdapterNoCheckBox;
import com.example.teammanagement.dialogs.BottomDialogReport;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UserProfileActivity extends AppCompatActivity{

    ImageButton ibtn_back;
    ImageButton ibtn_more;
    TextView tv_moreFeedback;
    ListView lv_sports;
    TextView tv_userName;
    TextView tv_userComment;
    RatingBar ratingBar;
    Intent intent;
    List<Sport> lv_list_sportItems = new ArrayList<>();
    List<Feedback> list_feedback = new ArrayList<>();
    SportAdapterNoCheckBox adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        initComponents();
    }

    public void initComponents(){
        ibtn_back=findViewById(R.id.profile_ibtn_back);
        ibtn_more=findViewById(R.id.profile_ibtn_more);
        tv_moreFeedback =findViewById(R.id.profile_tv_moreFeedback);
        lv_sports=findViewById(R.id.profile_lv_sports);
        tv_userComment=findViewById(R.id.profile_tv_comment);
        tv_userName=findViewById(R.id.profile_tv_commentUserName);
        ratingBar=findViewById(R.id.profile_rb_userGivenRating);

        ibtn_back.setOnClickListener(clickBack());
        ibtn_more.setOnClickListener(clickMore());
        tv_moreFeedback.setOnClickListener(clickMoreReviews());
        lv_list_sportItems.add(new Sport("Fotbal","Intermediar"));
        list_feedback.add(new Feedback("Alexandru Matei","Marius este o fire competitivă și un bun coechipier.Am jucat cu el acum cateva saptamani si ne-am distrat pe cinste. Sper sa mai am ocazia sa joc cu el.Marius este o fire competitivă și un bun coechipier. Am jucat cu el acum cateva saptamani si ne-am distrat pe cinste. Sper sa mai am ocazia sa joc cu el ",4.0f));
        list_feedback.add(new Feedback("Alexandru Matei","Marius este o fire competitivă și un bun coechipier. Am jucat cu el acum cateva saptamani si ne-am distrat pe cinste. Sper sa mai am ocazia sa joc cu el.Marius este o fire competitivă și un bun coechipier. Am jucat cu el acum cateva saptamani si ne-am distrat pe cinste. Sper sa mai am ocazia sa joc cu el",4.0f));
        randomFeedback();


        if(lv_list_sportItems != null) {
            adapter = new SportAdapterNoCheckBox(getApplicationContext(), R.layout.list_item_sports_nocheckbox, lv_list_sportItems, getLayoutInflater());
            lv_sports.setAdapter(adapter);
        }
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
