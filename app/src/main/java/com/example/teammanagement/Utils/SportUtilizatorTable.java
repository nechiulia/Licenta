package com.example.teammanagement.Utils;

import java.io.Serializable;

public class SportUtilizatorTable implements Serializable {

    private int idUtilizator;
    private int idSport;
    private int sportLevel;

    public SportUtilizatorTable(int idUtilizator, int idSport, int sportLevel) {
        this.idUtilizator = idUtilizator;
        this.idSport = idSport;
        this.sportLevel = sportLevel;
    }

    public SportUtilizatorTable(int idSport, int sportLevel) {
        this.idSport = idSport;
        this.sportLevel = sportLevel;
    }

    public SportUtilizatorTable() {
        this.idUtilizator=-1;
        this.idSport=-1;
        sportLevel=-1;
    }

    public int getIdUtilizator() {
        return idUtilizator;
    }

    public void setIdUtilizator(int idUtilizator) {
        this.idUtilizator = idUtilizator;
    }

    public int getIdSport() {
        return idSport;
    }

    public void setIdSport(int idSport) {
        this.idSport = idSport;
    }

    public int getSportLevel() {
        return sportLevel;
    }

    public void setSportLevel(int sportLevel) {
        this.sportLevel = sportLevel;
    }
}
