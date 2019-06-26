package com.example.teammanagement.Utils;

import java.io.Serializable;

public class Team implements Serializable {

    private String teamName;
    private String sport;

    public Team(String teamName, String sport) {
        this.teamName = teamName;
        this.sport = sport;
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
