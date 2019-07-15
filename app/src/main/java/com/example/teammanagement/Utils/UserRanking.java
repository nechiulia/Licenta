package com.example.teammanagement.Utils;

import java.io.Serializable;

public class UserRanking implements Serializable {

    private int idUser;
    private String userName;
    private int state;
    private byte[] profilePicture;
    private float score;
    private int nrCrt;

    public UserRanking() {
        this.idUser = -1;
        this.userName = "";
        this.state = -1;
        this.profilePicture = null;
        this.score=0.0f;
        this.nrCrt=-1;
    }

    public int getNrCrt() {
        return nrCrt;
    }

    public void setNrCrt(int nrCrt) {
        this.nrCrt = nrCrt;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }
}
