package com.example.teammanagement.Utils;

import java.io.Serializable;

public class Announcement implements Serializable {

    private int ID;
    private String message;
    private String date;
    private int userID;
    private int teamID;

    public Announcement() {
        this.ID = -1;
        this.message = "";
        this.date = "";
        this.userID = -1;
        this.teamID = -1;
    }

    public Announcement(int ID, String title, String message, String date, int userID, int teamID) {
        this.ID = ID;
        this.message = message;
        this.date = date;
        this.userID = userID;
        this.teamID = teamID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getTeamID() {
        return teamID;
    }

    public void setTeamID(int teamID) {
        this.teamID = teamID;
    }
}
