package com.example.teammanagement.Utils;


import java.io.Serializable;

public class SportUser implements Serializable {

    private String sportName;
    private String level;

    public SportUser(String sportName, String level) {
        this.sportName = sportName;
        this.level = level;
    }

    public String getSportName() {
        return sportName;
    }

    public void setSportName(String sportName) {
        this.sportName = sportName;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }


}
