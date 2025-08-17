package com.techelevator.service;
import com.techelevator.model.ForecastLocation;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestClient;
import com.techelevator.exception.DaoException;
import com.techelevator.model.PointProperties;
import com.techelevator.model.PointInfo;
import com.techelevator.model.Area;
import org.springframework.http.HttpStatus;
import jakarta.validation.Valid;
import java.util.List;

@Service
public class WeatherPointService {

    private final String API_BASE_URL = "https://api.weather.gov/";
    private final RestClient restClient = RestClient.create(API_BASE_URL);
    //Area area = new Area();



    public PointInfo getPointInfo(double latitude, double longitude){
        //Area area = new Area();

        return restClient.get()
                .uri("points/" + latitude + "," + longitude)//Formats it like so "/37.8651,-119.5757"
                .retrieve()
                .body(PointInfo.class);

    }
    public ForecastLocation getCurrentWeather(String gridID, int gridX, int gridY){

        return restClient.get()
                // "/HNX/69,148/forecast?units=us"
                .uri("gridpoints/" + gridID + "/" + gridX + "," + gridY + "/forecast?units=us")
                .retrieve()
                .body(ForecastLocation.class);
    }

    /*public ForecastLocation getWeatherIcon(String gridID, int gridX, int gridY){

        return restClient.get()
                // "https://api.weather.gov/icons/land/day/snow,70?size=medium"
                .uri("gridpoints/" + gridID + "/" + gridX + "," + gridY + "/forecast?units=us")
                .retrieve()
                .body(ForecastLocation.class);
    }*/


    /*
    public PointInfo getPointInfo(){
        Area area = new Area();

        return restClient.get()
                .uri("/" + area.getLatitude() + "," + area.getLongitude())//Formats it like so "/37.8651,-119.5757"
                .retrieve()
                .body(PointInfo.class);

    }
    public ForecastLocation getCurrentWeather(){
        PointInfo areaInfo = new WeatherPointService().getPointInfo();
        return restClient.get()
                // "/HNX/69,148/forecast?units=us"
                .uri("/" + areaInfo.getProperties().getGridId() + "/" + areaInfo.getProperties().getGridX() + "," +
                        areaInfo.getProperties().getGridY() +"/forecast?units=us")
                .retrieve()
                .body(ForecastLocation.class);
    }

     */
}
