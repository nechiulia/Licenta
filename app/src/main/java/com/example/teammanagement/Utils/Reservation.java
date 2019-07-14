package com.example.teammanagement.Utils;

import java.io.Serializable;

public class Reservation implements Serializable {

    private int reservationID;
    private String reservationDate;
    private String bookedDate;
    private String intervalOrar;
    private int IDTeam;
    private int IDActivity;
    private int IDUser;
    private int state;

    public Reservation() {
        this.reservationID=-1;
        this.reservationDate="";
        this.bookedDate="";
        this.intervalOrar="";
        this.IDActivity=-1;
        this.IDTeam=-1;
        this.IDUser=-1;
        this.state=-1;
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

    public String getBookedDate() {
        return bookedDate;
    }

    public void setBookedDate(String bookedDate) {
        this.bookedDate = bookedDate;
    }

    public String getIntervalOrar() {
        return intervalOrar;
    }

    public void setIntervalOrar(String intervalOrar) {
        this.intervalOrar = intervalOrar;
    }

    public int getIDTeam() {
        return IDTeam;
    }

    public void setIDTeam(int IDTeam) {
        this.IDTeam = IDTeam;
    }

    public int getIDActivity() {
        return IDActivity;
    }

    public void setIDActivity(int IDActivity) {
        this.IDActivity = IDActivity;
    }

    public int getIDUser() {
        return IDUser;
    }

    public void setIDUser(int IDUser) {
        this.IDUser = IDUser;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
