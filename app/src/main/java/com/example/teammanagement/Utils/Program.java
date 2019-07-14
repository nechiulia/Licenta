package com.example.teammanagement.Utils;

import java.io.Serializable;

public class Program implements Serializable {

    private String day;
    private String intervalHours;

    public Program() {

        this.day = "";
        this.intervalHours="";
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getIntervalHours() {
        return intervalHours;
    }

    public void setIntervalHours(String intervalHours) {
        this.intervalHours = intervalHours;
    }
}
