package com.example.teammanagement.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import com.example.teammanagement.R;
import com.example.teammanagement.Utils.Feedback;
import com.example.teammanagement.adapters.FeedbackAdapter;
import com.example.teammanagement.dialogs.BottomDialogReport;
import java.util.ArrayList;
import java.util.List;

public class FeedbackActivity extends AppCompatActivity {
    Intent intent;
    ImageButton ibtn_back;
    ListView lv_feedback;

    List<Feedback> listFeedback = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        initComponents();
    }
    private void initComponents() {
        ibtn_back=findViewById(R.id.feedback_ibtn_back);
        lv_feedback=findViewById(R.id.feedback_lv_feedback);

        ibtn_back.setOnClickListener(clickBack());
        listFeedback.add(new Feedback("cineva","blalba",4.3f));
        listFeedback.add(new Feedback("cineva","blalba",4.3f));
        FeedbackAdapter adapter = new FeedbackAdapter(getApplicationContext(),R.layout.list_item_feedback, listFeedback,getLayoutInflater());
        lv_feedback.setAdapter(adapter);
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

    public void myClickHandler(View view){
        //pentru a trimite mai departe ca email
        /*ConstraintLayout parentRow = (ConstraintLayout)view.getParent();
        TextView child = (TextView)parentRow.getChildAt(0);
        child.getText()*/
        BottomDialogReport bottomDialog =new BottomDialogReport();
        bottomDialog.show(getSupportFragmentManager(),getString(R.string.profile_tag_bottomDialog_hint));

    }

}
