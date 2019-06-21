package com.example.teammanagement.Utils;

public class NewLocation {

    private String userName;
    private String email;
    private String locationName;
    private String postalCode;
    private String address;

    public NewLocation(String userName, String email, String locationName, String postalCode, String address) {
        this.userName = userName;
        this.email = email;
        this.locationName = locationName;
        this.postalCode = postalCode;
        this.address = address;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "NewLocation{" +
                "userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", locationName='" + locationName + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
