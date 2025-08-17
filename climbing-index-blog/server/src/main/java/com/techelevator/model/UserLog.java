package com.techelevator.model;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.time.LocalDateTime;

public class UserLog {

    ////Instance variables\\\\
    @JsonIgnore
    private int userLogID;
    private String username;
    private int routeID;
    private String logDetails;
    //Update 6/28/25: Added a field for timestamp
    private LocalDateTime logTimestamp;

    ////Constructor(s)\\\\
    public UserLog() {
    }
    public UserLog(int userLogID, String username, int routeID, String logDetails) {
        this.userLogID = userLogID;
        this.username = username;
        this.routeID = routeID;
        this.logDetails = logDetails;
        this.logTimestamp = logTimestamp; //Added 6/28/25
    }

    ////Method(s)\\\\
    //TODO: Display the 'area', 'face', or 'route' this 'logDetails'' is for
    //TODO: Add/Include weather forecast information
    @Override
    public String toString() {
        return "* Location: " + routeID + System.lineSeparator()
                //Changed from: + " - Date & time: " + LocalDateTime.now() + System.lineSeparator()
                + " - Date & time: " + logTimestamp + System.lineSeparator() // Use the stored timestamp
                + " - User notes: " + logDetails;
    }
    //Getters\\
    public int getUserLogID() {
        return userLogID;
    }
    public String getId() {
        return username;
    }
    public int getRouteID() {
        return routeID;
    }
    public String getLogDetails() {
        return logDetails;
    }
    //Added: 6/28/25
    public LocalDateTime getLogTimestamp() {
        return logTimestamp;
    }

    //Setters\\
    public void setUserLogID(int userLogID) {
        this.userLogID = userLogID;
    }
    public void setId(String id) {
        this.username = id;
    }
    public void setRouteID(int routeID) {
        this.routeID = routeID;
    }
    public void setLogDetails(String logDetails) {
        this.logDetails = logDetails;
    }
    //Added 6/28/25
    public void setLogTimestamp(LocalDateTime logTimestamp) {
        this.logTimestamp = logTimestamp;
    }
}
