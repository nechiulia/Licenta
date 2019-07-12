package com.example.teammanagement.Utils;

import java.io.Serializable;

public class Orar implements Serializable {

    private String startHour;
    private String finishHour;
    private String name;

    public Orar(String startHour, String finishHour, String name) {
        this.startHour = startHour;
        this.finishHour = finishHour;
        this.name = name;
    }

    public Orar(String startHour, String finishHour) {
        this.startHour = startHour;
        this.finishHour = finishHour;
        this.name="";
    }

    public String getStartHour() {
        return startHour;
    }

    public void setStartHour(String startHour) {
        this.startHour = startHour;
    }

    public String getFinishHour() {
        return finishHour;
    }

    public void setFinishHour(String finishHour) {
        this.finishHour = finishHour;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
