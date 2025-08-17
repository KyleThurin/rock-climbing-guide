package com.techelevator.service.demo;
import org.springframework.web.client.RestClient;
import com.techelevator.model.ForecastProperties;
import com.techelevator.model.ForecastLocation;
import com.techelevator.model.PointProperties;
import java.util.List;

public class CurrentClimbingAreaWeatherDemo {

    // ENDPOINT that works in Postman
    //https://api.weather.gov/gridpoints/:wfo/<integer>,<integer>
    //https://api.weather.gov/gridpoints/HNX/69,148/forecast?units=us
    private final String API_BASE_URL = "https://api.weather.gov/gridpoints";
    private final RestClient restClient = RestClient.create(API_BASE_URL);


    public ForecastLocation getCurrentWeather(){

        return restClient.get()
                .uri("/HNX/69,148/forecast?units=us")//Yosemite Valley
                .retrieve()
                .body(ForecastLocation.class);
    }
    public static void main(String[] args) {
        ForecastLocation weatherInfo = new CurrentClimbingAreaWeatherDemo().getCurrentWeather();
        System.out.println(weatherInfo.getProperties().getPeriods().get(0).getDetailedForecast());
        //System.out.println(weatherInfo.getWeatherProperties().get(0));
    }


}


//"properties": {
//        "units": "us",
//        "forecastGenerator": "BaselineForecastGenerator",
//        "generatedAt": "2025-02-20T21:46:56+00:00",
//        "updateTime": "2025-02-20T20:58:38+00:00",
//        "validTimes": "2025-02-20T14:00:00+00:00/P7DT11H",
//        "elevation": {
//            "unitCode": "wmoUnit:m",
//            "value": 2410.968
//        },
//        "periods": [
//            {
//                "number": 1,
//                "name": "This Afternoon",
//                "startTime": "2025-02-20T13:00:00-08:00",
//                "endTime": "2025-02-20T18:00:00-08:00",
//                "isDaytime": true,
//                "temperature": 45,
//                "temperatureUnit": "F",
//                "temperatureTrend": "",
//                "probabilityOfPrecipitation": {
//                    "unitCode": "wmoUnit:percent",
//                    "value": null
//                },
//                "windSpeed": "5 mph",
//                "windDirection": "NE",
//                "icon": "https://api.weather.gov/icons/land/day/few?size=medium",
//                "shortForecast": "Sunny",
//                "detailedForecast": "Sunny. High near 45, with temperatures falling to around 36 in the afternoon. Wind chill values as low as 31. Northeast wind around 5 mph."
//            }
//ForecastProperties
