package com.techelevator.model;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class PointInfo {
    ////Instance variables\\\\
    private PointProperties properties;
    ////Constructor(s)\\\\
    ////Method(s)\\\\
    //Getter\\
    public PointProperties getProperties() {
        return properties;
    }

    //Setter\\
    public void setProperties(PointProperties properties) {
        this.properties = properties;
    }
}
