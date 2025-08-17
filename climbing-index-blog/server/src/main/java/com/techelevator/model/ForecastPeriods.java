package com.techelevator.model;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class ForecastPeriods {
    ////Instance variables\\\\
    private String name;
    private String shortForecast;
    private String detailedForecast;
    private String icon;

    //Getters\\
    public String getName() {
        return name;
    }
    public String getShortForecast() {
        return shortForecast;
    }
    public String getDetailedForecast() {
        return detailedForecast;
    }
    public String getIcon() {
        return icon;
    }

    //Setters\\
    public void setName(String name) {
        this.name = name;
    }
    public void setShortForecast(String shortForecast) {
        this.shortForecast = shortForecast;
    }
    public void setDetailedForecast(String detailedForecast) {
        this.detailedForecast = detailedForecast;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }
}
