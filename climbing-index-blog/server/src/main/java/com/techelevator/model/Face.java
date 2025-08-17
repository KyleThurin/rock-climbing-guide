package com.techelevator.model;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.*;

public class Face {

    ////Instance variables\\\\
    private int faceID;
    @NotEmpty(message="The Area ID must not be blank.")
    private int areaID;
    private String faceName;
    private String faceDirection;
    private String faceNotes;
    ////Constructor(s)\\\\
    public Face() {
    }
    public Face(int faceID, int areaID, String faceName, String faceDirection, String faceNotes) {
        this.faceID = faceID;
        this.areaID = areaID;
        this.faceName = faceName;
        this.faceDirection = faceDirection;
        this.faceNotes = faceNotes;
    }

    ////Method(s)\\\\
    @Override
    public String toString() {
        return "* " + faceName + System.lineSeparator()
                + " - Location: " + areaID + System.lineSeparator()     //TODO: Display the 'area' and 'face' this route is on
                + " - Direction it is facing: " + faceDirection + System.lineSeparator()
                + " - Notes: " + faceNotes;
    }
    //Getters\\
    public int getFaceID() {
        return faceID;
    }
    public int getAreaID() {
        return areaID;
    }
    public String getFaceName() {
        return faceName;
    }
    public String getFaceDirection() {
        return faceDirection;
    }
    public String getFaceNotes() {
        return faceNotes;
    }

    //Setters\\
    public void setFaceID(int faceID) {
        this.faceID = faceID;
    }
    public void setAreaID(int areaID) {
        this.areaID = areaID;
    }
    public void setFaceName(String faceName) {
        this.faceName = faceName;
    }
    public void setFaceDirection(String faceDirection) {
        this.faceDirection = faceDirection;
    }
    public void setFaceNotes(String faceNotes) {
        this.faceNotes = faceNotes;
    }
}
