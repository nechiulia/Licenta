package com.example.teammanagement.Utils;

import java.io.Serializable;

public class Teammate implements Serializable {

    private int userID;
    private String userName;
    private String teamRole;
    private byte[] userProfilePicture;

    public Teammate() {
        this.userID=-1;
        this.userName="";
        this.teamRole="";
        this.userProfilePicture=null;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getTeamRole() {
        return teamRole;
    }

    public void setTeamRole(String teamRole) {
        this.teamRole = teamRole;
    }

    public byte[] getUserProfilePicture() {
        return userProfilePicture;
    }

    public void setUserProfilePicture(byte[] userProfilePicture) {
        this.userProfilePicture = userProfilePicture;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
