package com.example.teammanagement.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import com.example.teammanagement.R;
import com.example.teammanagement.Utils.Constants;
import com.example.teammanagement.Utils.Feedback;
import com.example.teammanagement.Utils.User;
import com.example.teammanagement.adapters.FeedbackAdapter;
import com.example.teammanagement.database.JDBCController;
import com.example.teammanagement.dialogs.AddAnnouncementDialog;
import com.example.teammanagement.dialogs.AddFeedbackDialog;
import com.example.teammanagement.dialogs.BottomDialogReport;
import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FeedbackActivity extends AppCompatActivity implements AddFeedbackDialog.AddFeedbackDialogListner {
    Intent intent;
    ListView lv_feedback;
    private ImageButton ibtn_add;
    private ImageButton ibtn_back;

    private JDBCController jdbcController;
    private Connection c;

    private User clickedUser;
    private User currentUser;
    List<Feedback> listFeedback = new ArrayList<>();
    private List<String> listSenderNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        intent = getIntent();

        initComponents();
    }
    private void initComponents() {
        lv_feedback=findViewById(R.id.feedback_lv_feedback);
        ibtn_add=findViewById(R.id.feedback_ibtn_addFeedback);
        ibtn_back=findViewById(R.id.feedback_ibtn_back);

        jdbcController = JDBCController.getInstance();
        c=jdbcController.openConnection();

        getCurrentUser();
        if (intent.hasExtra(Constants.CLICKED_USER)) {
            clickedUser= (User) intent.getSerializableExtra(Constants.CLICKED_USER);
            if(isTeammate()) {
                ibtn_add.setVisibility(View.VISIBLE);
                ibtn_add.setOnClickListener(clickAddFeedback());
            }
        }

        selectFeedback();
        for(int i=0;i< listFeedback.size();i++){
            selectSenderNames(listFeedback.get(i).getSenderID());
        }

        FeedbackAdapter adapter = new FeedbackAdapter(getApplicationContext(),R.layout.list_item_feedback, listFeedback,listSenderNames,getLayoutInflater());
        lv_feedback.setAdapter(adapter);
        ibtn_back.setOnClickListener(clickBack());
    }

    public void getCurrentUser(){
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.APP_SHAREDPREF,MODE_PRIVATE);
        Gson gson = new Gson();
        String json =  sharedPreferences.getString(Constants.CURRENT_USER,"");
        currentUser = gson.fromJson(json,User.class);
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

    private View.OnClickListener clickAddFeedback(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openDialog();
            }
        };
    }

    private View.OnClickListener clickBack(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clickedUser!=null) {
                    intent = new Intent(getApplicationContext(), ProfileActivity.class);
                    intent.putExtra(Constants.CLICKED_USERID,clickedUser);
                    startActivity(intent);
                }
                else{
                    intent = new Intent(getApplicationContext(), MyProfileActivity.class);
                    startActivity(intent);
                }
            }
        };
    }

    private void openDialog(){
        AddFeedbackDialog addFeedbackDialog = new AddFeedbackDialog();
        addFeedbackDialog.show(getSupportFragmentManager(),"feedback");
        addFeedbackDialog.setCancelable(false);
    }

    public void myClickHandler(View view){
        //pentru a trimite mai departe ca email
        /*ConstraintLayout parentRow = (ConstraintLayout)view.getParent();
        TextView child = (TextView)parentRow.getChildAt(0);
        child.getText()*/
        BottomDialogReport bottomDialog =new BottomDialogReport();
        bottomDialog.show(getSupportFragmentManager(),getString(R.string.profile_tag_bottomDialog_hint));

    }

    public void insertFeedback(String message, float rating){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        String data = dateFormat.format(date); //2016/11/16 12:08:43
        try(Statement s = c.createStatement()){
            s.executeUpdate("INSERT INTO FEEDBACKS VALUES("+clickedUser.getIdUser()+","
                    +currentUser.getIdUser()+",'"+message+"',"+rating+",'"+data+"',0)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void apply(String message, float rating) {
        insertFeedback(message,rating);
        listFeedback.clear();
        listSenderNames.clear();
        selectFeedback();
        for(int i=0;i< listFeedback.size();i++){
            selectSenderNames(listFeedback.get(i).getSenderID());
        }
    }


}
