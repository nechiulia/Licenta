package com.example.teammanagement.Utils;

import java.io.Serializable;

public class ReservationItem implements Serializable {

    private int reservationID;
    private String reservationDate;
    private String intervalOrar;
    private String locationName;
    private String activityName;
    private String bookedDate;

    public ReservationItem() {
        this.reservationID=-1;
        this.reservationDate = "";
        this.intervalOrar = "";
        locationName="";
        activityName="";
        bookedDate="";
    }

    public int getReservationID() {
        return reservationID;
    }

    public void setReservationID(int reservationID) {
        this.reservationID = reservationID;
    }

    public String getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(String reservationDate) {
        this.reservationDate = reservationDate;
    }

    public String getIntervalOrar() {
        return intervalOrar;
    }

    public void setIntervalOrar(String intervalOrar) {
        this.intervalOrar = intervalOrar;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getBookedDate() {
        return bookedDate;
    }

    public void setBookedDate(String bookedDate) {
        this.bookedDate = bookedDate;
    }
}
