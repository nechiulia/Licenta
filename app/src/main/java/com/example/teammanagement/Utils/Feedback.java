package com.example.teammanagement.Utils;

import android.os.Parcel;
import android.os.Parcelable;

public class Feedback implements Parcelable {

    private String userName;
    private String comment;
    private float rating;

    public Feedback(String userName, String comment, float rating){
        this.userName=userName;
        this.comment=comment;
        this.rating=rating;
    }

    protected Feedback(Parcel in) {
        userName = in.readString();
        comment=in.readString();
        rating=in.readFloat();
    }

    public static final Creator<Feedback> CREATOR = new Creator<Feedback>() {
        @Override
        public Feedback createFromParcel(Parcel in) {
            return new Feedback(in);
        }

        @Override
        public Feedback[] newArray(int size) {
            return new Feedback[size];
        }
    };

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userName);
        dest.writeString(comment);
        dest.writeDouble(rating);
    }
}
