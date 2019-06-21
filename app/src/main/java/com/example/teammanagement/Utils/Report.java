package com.example.teammanagement.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Report {

    private String userSender;
    private String userReceiver;
    private String feedback;
    private Date dateFeedback;

    public Report(String userSender, String userReceiver, String feedback, Date dateFeedback) {
        this.userSender = userSender;
        this.userReceiver = userReceiver;
        this.feedback = feedback;
        this.dateFeedback = dateFeedback;
    }

    public String getUserSender() {
        return userSender;
    }

    public void setUserSender(String userSender) {
        this.userSender = userSender;
    }

    public String getUserReceiver() {
        return userReceiver;
    }

    public void setUserReceiver(String userReceiver) {
        this.userReceiver = userReceiver;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public Date getDateFeedback() {
        return dateFeedback;
    }

    public void setDateFeedback(Date dateFeedback) {
        this.dateFeedback = dateFeedback;
    }

    @Override
    public String toString() {
        return "Report{" +
                "userSender='" + userSender + '\'' +
                ", userReceiver='" + userReceiver + '\'' +
                ", feedback='" + feedback + '\'' +
                ", dateFeedback=" + new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").format(dateFeedback )+
                '}';
    }
}
