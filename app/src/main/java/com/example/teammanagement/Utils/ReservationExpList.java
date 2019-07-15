package com.example.teammanagement.Utils;

import java.io.Serializable;

public class ReservationExpList implements Serializable {

    private int reservationID;
    private String reservationDate;
    private String intervalOrar;

    public ReservationExpList(int reservationID,String reservationDate, String intervalOrar) {
        this.reservationID=reservationID;
        this.reservationDate = reservationDate;
        this.intervalOrar = intervalOrar;
    }

    public ReservationExpList() {
        this.reservationID=-1;
        this.reservationDate = "";
        this.intervalOrar = "";
    }

    public String getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(String reservationDate) {
        this.reservationDate = reservationDate;
    }

    public int getReservationID() {
        return reservationID;
    }

    public void setReservationID(int reservationID) {
        this.reservationID = reservationID;
    }

    public String getIntervalOrar() {
        return intervalOrar;
    }

    public void setIntervalOrar(String intervalOrar) {
        this.intervalOrar = intervalOrar;
    }
}
