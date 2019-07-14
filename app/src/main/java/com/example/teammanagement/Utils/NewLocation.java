package com.example.teammanagement.Utils;

import java.io.Serializable;

public class NewLocation implements Serializable {

    private int locationID;
    private int userID;
    private String email;
    private String locationName;
    private String postalCode;
    private String address;
    private double latitude;
    private double longitude;
    private int reservation;
    private int state;
    private String userName;

    public NewLocation() {
        this.locationID = -1;
        this.userID = -1;
        this.email = "";
        this.locationName = "";
        this.postalCode = "";
        this.address = "";
        this.reservation = -1;
        this.state = -1;
        this.userName="";
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getLocationID() {
        return locationID;
    }

    public void setLocationID(int locationID) {
        this.locationID = locationID;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getReservation() {
        return reservation;
    }

    public void setReservation(int reservation) {
        this.reservation = reservation;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userName) {
        this.userID = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "NewLocation{" +
                "userName='" + userID + '\'' +
                ", email='" + email + '\'' +
                ", locationName='" + locationName + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
