package com.techelevator.model;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class ForecastLocation {
    private ForecastProperties properties;

    public ForecastProperties getProperties() {
        return properties;
    }

    public void setProperties(ForecastProperties properties) {
        this.properties = properties;
    }
}
