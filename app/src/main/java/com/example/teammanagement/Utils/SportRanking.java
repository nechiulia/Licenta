package com.example.teammanagement.Utils;

import java.io.Serializable;

public class SportRanking implements Serializable {

    private int nrCrt;
    private int SportID;
    private String sportName;
    private int nrUsers;

    public SportRanking() {
        this.nrCrt=-1;
        this.nrUsers=-1;
        this.SportID=-1;
        this.sportName="";
    }

    public int getNrCrt() {
        return nrCrt;
    }



    public void setNrCrt(int nrCrt) {
        this.nrCrt = nrCrt;
    }

    public int getSportID() {
        return SportID;
    }

    public void setSportID(int sportID) {
        SportID = sportID;
    }

    public String getSportName() {
        return sportName;
    }

    public void setSportName(String sportName) {
        this.sportName = sportName;
    }

    public int getNrUsers() {
        return nrUsers;
    }

    public void setNrUsers(int nrUsers) {
        this.nrUsers = nrUsers;
    }
}
