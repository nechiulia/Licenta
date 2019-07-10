package com.example.teammanagement.Utils;

import java.io.Serializable;

public class Activity  implements Serializable {

    private int activityID;
    private int locationID;
    private String activityName;
    private String trainer;
    private String sport;
    private int reservation;
    private int difficultyLevel;
    private double price;

    public Activity() {
        this.activityID=-1;
        this.locationID=-1;
        activityName="";
        trainer="";
        sport="";
        reservation=0;
        difficultyLevel=0;
        price=0.0;
    }


    public Activity(String activityName, String trainer, String sport, int reservation, int difficultyLevel, double price) {
        this.activityName = activityName;
        this.trainer = trainer;
        this.sport = sport;
        this.reservation = reservation;
        this.difficultyLevel = difficultyLevel;
        this.price = price;
    }

    public int getActivityID() {
        return activityID;
    }

    public void setActivityID(int activityID) {
        this.activityID = activityID;
    }

    public int getLocationID() {
        return locationID;
    }

    public void setLocationID(int locationID) {
        this.locationID = locationID;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getTrainer() {
        return trainer;
    }

    public void setTrainer(String trainer) {
        this.trainer = trainer;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public int getReservation() {
        return reservation;
    }

    public void setReservation(int reservation) {
        this.reservation = reservation;
    }

    public int getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(int difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
