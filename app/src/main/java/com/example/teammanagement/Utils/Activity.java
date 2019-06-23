package com.example.teammanagement.Utils;

public class Activity  {

    private String activityName;
    private String trainer;
    private String sport;
    private boolean reservation;
    private int difficultyLevel;
    private double price;


    public Activity(String activityName, String trainer, String sport, boolean reservation, int difficultyLevel, double price) {
        this.activityName = activityName;
        this.trainer = trainer;
        this.sport = sport;
        this.reservation = reservation;
        this.difficultyLevel = difficultyLevel;
        this.price = price;
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

    public boolean getReservation() {
        return reservation;
    }

    public void setReservation(boolean reservation) {
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

    @Override
    public String toString() {
        return "Activity{" +
                "activityName='" + activityName + '\'' +
                ", trainer='" + trainer + '\'' +
                ", sport='" + sport + '\'' +
                ", reservation=" + reservation +
                ", difficultyLevel=" + difficultyLevel +
                ", price=" + price +
                '}';
    }
}
