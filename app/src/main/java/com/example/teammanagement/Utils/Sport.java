package com.example.teammanagement.Utils;

import java.io.Serializable;

public class Sport implements Serializable {

    private String denumire;
    private int nrMinJucatori;

    public Sport(String denumire, int nrMinJucatori) {
        this.denumire = denumire;
        this.nrMinJucatori = nrMinJucatori;
    }

    public String getDenumire() {
        return denumire;
    }

    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }

    public int getNrMinJucatori() {
        return nrMinJucatori;
    }

    public void setNrMinJucatori(int nrMinJucatori) {
        this.nrMinJucatori = nrMinJucatori;
    }

    @Override
    public String toString() {
        return "Sport{" +
                "denumire='" + denumire + '\'' +
                ", nrMinJucatori=" + nrMinJucatori +
                '}';
    }
}
