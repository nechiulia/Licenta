package com.example.teammanagement.Utils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;

public interface Constants {

    String TIME_FORMAT="dd/MM/yyyy HH:mm";
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat simpleDateFormat=new SimpleDateFormat(TIME_FORMAT);

    String ADD_KEY="addQuestions";
    String AD_KEY="add";
    String SEND_SPORTSLIST="listDialog";
    String MORE_ACTIONS="report_dialog";

    int UPLOAD_IMAGE_REQUEST_CODE=100;
    String FAQ_URL= "https://api.myjson.com/bins/k4jmm";
    String FAQ_LIST_HINT ="faq_list";
    String APP_SHAREDPREF ="App_Settings";

    int ERROR_DIALOG_REQUEST = 9001;
    float DEFAULT_ZOOM=17F;

    String ADDTEAM1_TEAM="NewTeam";

    String CURRENT_USER= "Current User Info";

    String NEW_USER= "New User";
    String CLICKED_USERID ="ClickedUserID";
    String CLICKED_TEAM ="ClickedTeamID";
    String CURRENT_TEAM="Current Team Info";
    String ADDED_TEAMMATES="Added teammates";
    String CURRENT_TEAM_ID ="Current team ID";
    String CURRENT_USER_ID ="Current location ID";
    String CURRENT_LOCATION_ID="LocationID";

    int ADD_TEAMMATES_CODE =101;
}
