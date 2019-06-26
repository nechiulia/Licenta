package com.example.teammanagement.Utils;

import android.os.Parcel;
import android.os.Parcelable;

public class SportUtilizator implements Parcelable {

    private String sportName;
    private String sportLevel;

    protected SportUtilizator(Parcel in) {
        sportName = in.readString();
        sportLevel=in.readString();
    }

    public static final Creator<SportUtilizator> CREATOR = new Creator<SportUtilizator>() {
        @Override
        public SportUtilizator createFromParcel(Parcel in) {
            return new SportUtilizator(in);
        }

        @Override
        public SportUtilizator[] newArray(int size) {
            return new SportUtilizator[size];
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

    public SportUtilizator(String sportName, String sportLevel) {
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
