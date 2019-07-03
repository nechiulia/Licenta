package com.example.teammanagement.Utils;

import java.io.Serializable;

public class Team implements Serializable {

    private int id;
    private String teamName;
    private String sport;

    public Team(String teamName, String sport) {
        this.teamName = teamName;
        this.sport = sport;
    }

    public Team(int id, String teamName, String sport) {
        this.id = id;
        this.teamName = teamName;
        this.sport = sport;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }
}
