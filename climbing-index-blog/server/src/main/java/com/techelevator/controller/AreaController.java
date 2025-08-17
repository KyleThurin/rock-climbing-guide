package com.techelevator.controller;
import com.techelevator.model.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.core.Authentication;
import com.techelevator.service.WeatherForecastService;
import com.techelevator.service.WeatherPointService;
import com.techelevator.security.jwt.TokenProvider;
import org.springframework.web.bind.annotation.*;
import com.techelevator.exception.DaoException;
import org.springframework.http.HttpStatus;
import com.techelevator.dao.UserLogDao;
import com.techelevator.dao.RouteDao;
import com.techelevator.dao.AreaDao;
import com.techelevator.dao.FaceDao;
import jakarta.validation.Valid;
import org.slf4j.LoggerFactory;
import java.security.Principal;
import java.util.ArrayList;
import org.slf4j.Logger;
import java.util.List;

/**
 * Area (anonymous user):
 *  - Create a list of the climbing areas
 *  - Get details of that climbing area
 *  - Create a list of the climbing faces for that area
 *
 * Edit (administrator):
 *  - Create, update and delete routes
 */
@CrossOrigin
@RestController
public class AreaController {
    //TODO: Create a service layer if there are multiple DAO calls or when making an external API request
    private static final Logger logger = LoggerFactory.getLogger(AreaController.class); // ADDED: Logger instance
    private AreaDao areaDao;
    private FaceDao faceDao;
    private WeatherPointService weatherPointService;

    public AreaController(AreaDao areaDao, FaceDao faceDao, WeatherPointService weatherPointService) {
        this.areaDao = areaDao;
        this.faceDao = faceDao;
        this.weatherPointService = weatherPointService;
    }

    //Get\\ (GET: 500, GET {id}: 404/500)
    //Get all 'areas'
    @PreAuthorize("permitAll")                                                                                          //The user doesn't have to be authenticated
    @RequestMapping(path = "/areas", method = RequestMethod.GET)
    public List<Area> getAllAreas() {
        try{
            return areaDao.listOfAreas();
        } catch (DaoException e) {
            logger.error("DAO Error getting all areas: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "DAO Error - " + e.getMessage());       //Returns 500 'INTERNAL_SERVER_ERROR' message
        }
    }

    //Get 'area' for a specific 'id'
    @PreAuthorize("permitAll")                                                                                          //The user doesn't have to be authenticated
    @RequestMapping(path = "/areas/{id}", method = RequestMethod.GET)
    public Area getAreaByID(@PathVariable int id) {
        try{
            Area area = areaDao.getAreaById(id);                                                                        //Gets the area info for 'id'
            if (area == null) {                                                                                         //If 'area' is 'null'
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Area Not Found");                              //Returns 404 'NOT_FOUND' message
            } else {
                return area;
            }
        } catch (DaoException e) {
            logger.error("DAO Error getting area by ID {}: {}", id, e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "DAO Error - " + e.getMessage());       //Returns 500 'INTERNAL_SERVER_ERROR' message
        }
    }

    //Get weather details for a specific ID
    @PreAuthorize("permitAll")                                                                                          //The user doesn't have to be authenticated
    @RequestMapping(path = "/areas/{id}/currentWeather", method = RequestMethod.GET)
    public String getAreaWeatherByID(@PathVariable int id){
        try{
            Area area = areaDao.getAreaById(id);                                                                        //Gets the area info for 'id'
            if (area == null) {                                                                                         //If 'area' is 'null'
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Area Not Found");                              //Returns 404 'NOT_FOUND' message
            } else {
                // Use local variable for pointInfo to avoid race conditions
                PointProperties pointInfo = weatherPointService.getPointInfo(area.getLatitude(), area.getLongitude()).getProperties();
                return weatherPointService.getCurrentWeather(pointInfo.getGridId(), pointInfo.getGridX(),
                        pointInfo.getGridY()).getProperties().getPeriods().get(0).getDetailedForecast();
                //Removed: 6/28/25
                /*pointInfo = weatherPointService.getPointInfo(area.getLatitude(), area.getLongitude()).getProperties();
                return weatherPointService.getCurrentWeather(pointInfo.getGridId(), pointInfo.getGridX(),
                        pointInfo.getGridY()).getProperties().getPeriods().get(0).getDetailedForecast();
                */
            }
        } catch (DaoException e) {
            logger.error("DAO Error getting area for weather data for ID {}: {}", id, e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An internal server error occurred retrieving area for weather.");
        } catch (Exception e) { // Catch exceptions from external weather service calls
            logger.error("Error fetching weather data for area ID {}: {}", id, e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Could not retrieve weather data at this time.");
        }
    }

    //Get weather icon for a specific ID
    @PreAuthorize("permitAll")
    @RequestMapping(path = "/areas/{id}/weatherIcon", method = RequestMethod.GET)
    public String getAreaWeatherIconByID(@PathVariable int id) {
        try {
            Area area = areaDao.getAreaById(id);
            if (area == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Area Not Found for weather icon.");
            }
            // Use local variable for pointInfo to avoid race conditions
            PointProperties pointInfo = weatherPointService.getPointInfo(area.getLatitude(), area.getLongitude()).getProperties();
            return weatherPointService.getCurrentWeather(pointInfo.getGridId(), pointInfo.getGridX(),
                    pointInfo.getGridY()).getProperties().getPeriods().get(0).getIcon();
        } catch (DaoException e) {
            logger.error("DAO Error getting area for weather icon for ID {}: {}", id, e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An internal server error occurred retrieving area for weather icon.");
        } catch (Exception e) { // Catch exceptions from external weather service calls
            logger.error("Error fetching weather icon for area ID {}: {}", id, e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Could not retrieve weather icon at this time.");
        }
    }

    //Get all the 'faces' for a specific area 'id'
    @PreAuthorize("permitAll")                                                                                          //The user doesn't have to be authenticated
    @RequestMapping(path = "/areas/{id}/faces", method = RequestMethod.GET)
    public List<Face> getAllFacesForArea(@PathVariable int id) {
        try{
            List<Face> faces = faceDao.getFacesByAreaID(id);                                                            //Gets the faces associated with 'area_id'
            //It's possible for an area to exist but have no faces
            if (areaDao.getAreaById(id) == null) {                                                                      //Check if the parent area exists
                //Only throw NOT_FOUND if the area itself doesn't exist.
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Area Not Found, cannot retrieve faces.");
            }
            return faces;
        } catch (DaoException e) {
            logger.error("DAO Error getting faces for area ID {}: {}", id, e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An internal server error occurred retrieving faces for the area.");
        }
    }


    //Create new climbing area\\ (POST: 400/500)
    @PreAuthorize("hasRole('ROLE_ADMIN', 'ADMIN')")                                                                     //The user must be authenticated and have the role ADMIN
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/areas", method = RequestMethod.POST)
    public Area createNewArea(@Valid @RequestBody Area newArea) {
        try{
            if (areaDao.getAreaByName(newArea.getAreaName())!=null){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Area already exists.");                      //Returns 400 'BAD_REQUEST' message
            }
            return areaDao.createArea(newArea);
        } catch (DaoException e) {
            logger.error("DAO Error creating area: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An internal server error occurred creating the area.");
        }
    }

    //Update a climbing area\\ (PUT {id}:400-BAD_REQUEST/404-NOT_FOUND/500-INTERNAL_SERVER_ERROR)
    @PreAuthorize("hasRole('ROLE_ADMIN')")    //The user must be authenticated and have the role ADMIN
    //TODO: Fix and change everything to just "ROLE_ADMIN"
    @RequestMapping(path = "/areas/{id}", method = RequestMethod.PUT)
    public Area updateArea(@Valid @RequestBody Area area, @PathVariable int id) {
        // The id on the path takes precedence over the id in the request body, if any
        area.setAreaID(id);
        try {
            Area updatedArea = areaDao.updateArea(area);
            if (updatedArea == null) {                                                                                  //If 'face' is 'null'
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Area Not Found");                              //Returns 404 'NOT_FOUND' message
            } else {
                return updatedArea;
            }
        }  catch (DaoException e) {
            logger.error("DAO Error updating area for ID {}: {}", id, e.getMessage(), e);
            // Distinguish between bad request (e.g., validation) and not found if DAO can throw specific exceptions
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An internal server error occurred updating the area.");
        }
    }


    //Delete a climbing area\\ (DELETE {id}: 404-NOT_FOUND/500-INTERNAL_SERVER_ERROR)
    @PreAuthorize("hasRole('ROLE_ADMIN', 'ADMIN')")                                                                     //The user must be authenticated and have the role ADMIN
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(path = "/areas/{id}", method = RequestMethod.DELETE)
    public void deleteAreaById(@PathVariable int id) {
        try {
            if (areaDao.getAreaById(id) == null){                                                                       //If there isn't a area with 'id'
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Area not found");                              //Returns 404 'NOT_FOUND' message
            }
            areaDao.deleteAreaById(id);
        } catch (DaoException e) {
            logger.error("DAO Error deleting area with ID {}: {}", id, e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An internal server error occurred deleting the area.");
        }
    }

}
