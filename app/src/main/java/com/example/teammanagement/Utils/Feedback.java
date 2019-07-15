package com.example.teammanagement.Utils;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Feedback implements Serializable {

    private int feedbackID;
    private int senderID;
    private int receiverID;
    private String comment;
    private float rating;
    private String date;
    private int state;

    public Feedback(){
        this.feedbackID=-1;
        this.receiverID=-1;
        this.senderID=-1;
        this.comment="";
        this.date="";
        this.rating=0.0f;
        this.state=0;
    }

    public int getFeedbackID() {
        return feedbackID;
    }

    public void setFeedbackID(int feedbackID) {
        this.feedbackID = feedbackID;
    }

    public int getSenderID() {
        return senderID;
    }

    public void setSenderID(int senderID) {
        this.senderID = senderID;
    }

    public int getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(int receiverID) {
        this.receiverID = receiverID;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
