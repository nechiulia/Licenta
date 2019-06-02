package com.example.teammanagement.Utils;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Sport implements Parcelable {

    private String sportName;
    private String sportLevel;

    protected Sport(Parcel in) {
        sportName = in.readString();
        sportLevel=in.readString();
    }

    public static final Creator<Sport> CREATOR = new Creator<Sport>() {
        @Override
        public Sport createFromParcel(Parcel in) {
            return new Sport(in);
        }

        @Override
        public Sport[] newArray(int size) {
            return new Sport[size];
        }
    };

    public String getSportName() {
        return sportName;
    }

    public void setSportName(String sportName) {
        this.sportName = sportName;
    }

    public String getSportLevel() {
        return sportLevel;
    }

    public void setSportLevel(String sportLevel) {
        this.sportLevel = sportLevel;
    }

    public Sport(String sportName, String sportLevel) {
        this.sportName = sportName;
        this.sportLevel = sportLevel;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(sportName);
        dest.writeString(sportLevel);
    }
}
