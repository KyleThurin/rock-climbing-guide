package com.techelevator.service;
import com.techelevator.service.demo.AreaCoordinanceDemo;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import com.techelevator.service.WeatherPointService;
import org.springframework.web.client.RestClient;
import com.techelevator.model.ForecastLocation;
import com.techelevator.exception.DaoException;
import org.springframework.http.HttpStatus;
import com.techelevator.model.PointInfo;
import jakarta.validation.Valid;
import java.util.List;

@Service
//@RequestMapping("/auctions")
public class WeatherForecastService {
    private final String API_BASE_URL = "https://api.weather.gov/points";
    private final RestClient restClient = RestClient.create(API_BASE_URL);
    //PointInfo areaInfo = new WeatherPointService().getPointInfo();

    /*
    public ForecastLocation getCurrentWeather(){
        return restClient.get()
                // "/HNX/69,148/forecast?units=us"
                .uri("/" + areaInfo.getProperties().getGridId() + "/" + areaInfo.getProperties().getGridX() + "," +
                        areaInfo.getProperties().getGridY() +"/forecast?units=us")
                .retrieve()
                .body(ForecastLocation.class);
    }
    //PointInfo areaInfo = new AreaCoordinanceDemo().getAreaCoordinance();
    public ForecastLocation getCurrentWeather(String gridID, int gridX, int gridY){

        return restClient.get()
                // "/HNX/69,148/forecast?units=us"
                .uri("/" + gridID + "/" + gridX + "," + gridY +"/forecast?units=us")
                .retrieve()
                .body(ForecastLocation.class);
    }*/
}
