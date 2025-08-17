package com.techelevator.model;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class PointProperties {
    ////Instance variables\\\\
    private String gridId;
    private Integer gridX;
    private Integer gridY;
    private String forecast;
    ////Constructor(s)\\\\
    ////Method(s)\\\\
    @Override
    public String toString() {
        //"/HNX/69,148/forecast?units=us")
        return "/" + gridId + "/" + gridX + "," + gridY + "/forecast?units=us";
    }
    //Getters\\
    public String getGridId() {
        return gridId;
    }
    public Integer getGridX() {
        return gridX;
    }
    public Integer getGridY() {
        return gridY;
    }
    public String getForecast() {
        return forecast;
    }

    //Setters\\
    public void setGridId(String gridId) {
        this.gridId = gridId;
    }
    public void setGridX(Integer gridX) {
        this.gridX = gridX;
    }
    public void setGridY(Integer gridY) {
        this.gridY = gridY;
    }
    public void setForecast(String forecast) {
        this.forecast = forecast;
    }
}
