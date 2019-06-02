package com.example.teammanagement.Utils;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {
    private String userName;
    private byte[] userPhoto;
    private String userFirstName;
    private String userLastName;
    private List<Sport> sportsList;
    private boolean isActive;
    private String password;
    private int idAppRole;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public byte[] getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(byte[] userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public List<Sport> getSportsList() {
        return sportsList;
    }

    public void setSportsList(List<Sport> sportsList) {
        this.sportsList = sportsList;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getIdAppRole() {
        return idAppRole;
    }

    public void setIdAppRole(int idAppRole) {
        this.idAppRole = idAppRole;
    }

    public User(String userName, byte[] userPhoto, String userFirstName, String userLastName, List<Sport> sportsList, boolean isActive, String password, int idAppRole) {
        this.userName = userName;
        this.userPhoto = userPhoto;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.sportsList = sportsList;
        this.isActive = isActive;
        this.password = password;
        this.idAppRole = idAppRole;
    }

    public User(String userName, byte[] userPhoto) {
        this.userName = userName;
        this.userPhoto = userPhoto;
    }

    public User(String userName) {
        this.userName = userName;
    }

}
