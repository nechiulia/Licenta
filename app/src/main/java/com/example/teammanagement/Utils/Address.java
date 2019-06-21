package com.example.teammanagement.Utils;

public class Address {

    private String codPostal;
    private String locationName;
    private String locationStreet;
    private String locationStreetNumber;
    private double latitude;
    private double longitude;

    public Address(String codPostal, String locationName, String locationStreet, String locationStreetNumber) {
        this.codPostal = codPostal;
        this.locationName = locationName;
        this.locationStreet = locationStreet;
        this.locationStreetNumber = locationStreetNumber;
    }

    public Address(String codPostal, String locationName, String locationStreet, String locationStreetNumber, double latitude, double longitude) {
        this.codPostal = codPostal;
        this.locationName = locationName;
        this.locationStreet = locationStreet;
        this.locationStreetNumber = locationStreetNumber;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getCodPostal() {
        return codPostal;
    }

    public void setCodPostal(String codPostal) {
        this.codPostal = codPostal;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLocationStreet() {
        return locationStreet;
    }

    public void setLocationStreet(String locationStreet) {
        this.locationStreet = locationStreet;
    }

    public String getLocationStreetNumber() {
        return locationStreetNumber;
    }

    public void setLocationStreetNumber(String locationStreetNumber) {
        this.locationStreetNumber = locationStreetNumber;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Address{" +
                "codPostal='" + codPostal + '\'' +
                ", locationName='" + locationName + '\'' +
                ", locationStreet='" + locationStreet + '\'' +
                ", locationStreetNumber='" + locationStreetNumber + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
