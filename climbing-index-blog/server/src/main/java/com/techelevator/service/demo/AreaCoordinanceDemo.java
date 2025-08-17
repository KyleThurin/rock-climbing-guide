package com.techelevator.service.demo;
import org.springframework.web.client.RestClient;
import com.techelevator.model.PointProperties;
import com.techelevator.model.PointInfo;
import java.util.List;

public class AreaCoordinanceDemo {

    // ENDPOINT that works in Postman: https://api.weather.gov/points/37.8651,-119.5757
    private final String API_BASE_URL = "https://api.weather.gov/points";
    private final RestClient restClient = RestClient.create(API_BASE_URL);

    public PointInfo getAreaCoordinance(){

        return restClient.get()
                .uri("/37.8651,-119.5757")//Yosemite Valley
                .retrieve()
                .body(PointInfo.class);
    }
    public static void main(String[] args) {
        PointInfo areaInfo = new AreaCoordinanceDemo().getAreaCoordinance();
        System.out.println(areaInfo.getProperties().getForecast());
    }
}
//"properties": {
//        "@id": "https://api.weather.gov/points/37.8651,-119.5757",
//        "@type": "wx:Point",
//        "cwa": "HNX",
//        "forecastOffice": "https://api.weather.gov/offices/HNX",
//        "gridId": "HNX",
//        "gridX": 69,
//        "gridY": 148,

//TODO: Maybe rename it to "AreaProperties"?



