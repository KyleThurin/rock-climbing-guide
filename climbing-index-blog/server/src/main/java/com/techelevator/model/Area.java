package com.techelevator.model;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
public class Area {

    ////Instance variables\\\\
    private int areaID;
    private String areaName;
    //@NotBlank(message = "The field 'latitude' is required.")
    private double latitude;
    //@NotBlank(message = "The field 'longitude' is required.")
    private double longitude;
    private String areaDetails;

    ////Constructor(s)\\\\
    public Area() {
    }
    public Area(int areaID, String areaName, double latitude, double longitude, String areaDetails) {
        this.areaID = areaID;
        this.areaName = areaName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.areaDetails = areaDetails;
    }

    ////Method(s)\\\\
    @Override
    public String toString() {
        return "* " + areaName + System.lineSeparator()
                + " - Latitude: " + latitude + System.lineSeparator()
                + " - Longitude: " + longitude + System.lineSeparator()
                + " - Area Details: " + areaDetails + System.lineSeparator();
    }
    //Getters\\
    public int getAreaID() {
        return areaID;
    }
    public String getAreaName() {
        return areaName;
    }
    public double getLatitude() {
        return latitude;
    }
    public double getLongitude() {
        return longitude;
    }
    public String getAreaDetails() {
        return areaDetails;
    }

    //Setters\\
    public void setAreaID(int areaID) {
        this.areaID = areaID;
    }
    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    public void setAreaDetails(String areaDetails) {
        this.areaDetails = areaDetails;
    }

}
