package com.techelevator.model;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class Route {

    ////Instance variables\\\\
    @JsonIgnore
    private int routeID;
    @NotBlank(message = "The field 'faceID' is required.")
    private int faceID;
    private String routeName;
    private String routeDifficulty;
    private int numberOfPitches;
    private String routeNotes;

    ////Constructor(s)\\\\
    public Route() {
    }

    public Route(int routeID, int faceID, String routeName, String routeDifficulty, int numberOfPitches, String routeNotes) {
        this.routeID = routeID;
        this.faceID = faceID;
        this.routeName = routeName;
        this.routeDifficulty = routeDifficulty;
        this.numberOfPitches = numberOfPitches;
        this.routeNotes = routeNotes;
    }

    ////Method(s)\\\\
    @Override
    public String toString() {
        return "* " + routeName + System.lineSeparator()
                + " - Location: " + faceID + System.lineSeparator()     //TODO: Display the 'area' and 'face' this route is on
                + " - Climbing Grade (Difficulty): " + routeDifficulty + System.lineSeparator()
                + " - Number of pitches: " + numberOfPitches + System.lineSeparator()
                + " - Notes: " + routeNotes;
    }

    //Getters\\
    public int getRouteID() {
        return routeID;
    }
    public int getFaceID() {
        return faceID;
    }
    public String getRouteName() {
        return routeName;
    }
    public String getRouteDifficulty() {
        return routeDifficulty;
    }
    public int getNumberOfPitches() {
        return numberOfPitches;
    }
    public String getRouteNotes() {
        return routeNotes;
    }

    //Setters\\
    public void setRouteID(int routeID) {
        this.routeID = routeID;
    }
    public void setFaceID(int faceID) {
        this.faceID = faceID;
    }
    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }
    public void setRouteDifficulty(String routeDifficulty) {
        this.routeDifficulty = routeDifficulty;
    }
    public void setNumberOfPitches(int numberOfPitches) {
        this.numberOfPitches = numberOfPitches;
    }
    public void setRouteNotes(String routeNotes) {
        this.routeNotes = routeNotes;
    }

}
