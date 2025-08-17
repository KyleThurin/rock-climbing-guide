package com.techelevator.model;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.List;

public class ForecastProperties {
    ////Instance variables\\\\
    private List<ForecastPeriods> periods;
    ////Constructor(s)\\\\
    ////Method(s)\\\\
    //Getters\\
    public List<ForecastPeriods> getPeriods() {
        return periods;
    }
    //Setters\\
    public void setPeriods(List<ForecastPeriods> periods) {
        this.periods = periods;
    }
}

