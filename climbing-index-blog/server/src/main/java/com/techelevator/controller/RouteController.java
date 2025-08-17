package com.techelevator.controller;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.core.Authentication;
import com.techelevator.service.WeatherForecastService;
import com.techelevator.service.WeatherPointService;
import com.techelevator.security.jwt.TokenProvider;
import org.springframework.web.bind.annotation.*;
import com.techelevator.model.ForecastProperties;
import com.techelevator.model.ForecastLocation;
import com.techelevator.exception.DaoException;
import com.techelevator.model.ForecastPeriods;
import org.springframework.http.HttpStatus;
import com.techelevator.model.PointInfo;
import com.techelevator.dao.UserLogDao;
import com.techelevator.model.UserLog;
import com.techelevator.dao.RouteDao;
import com.techelevator.dao.AreaDao;
import com.techelevator.dao.FaceDao;
import com.techelevator.model.Route;
import com.techelevator.model.Area;
import com.techelevator.model.Face;
import jakarta.validation.Valid;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import org.slf4j.Logger;
import java.util.List;

/**
 * Route (anonymous user):
 *  - Create a list of the climbing routes
 *  - Get detailed information about that climbing route
 *
 * Edit (administrator):
 *  - Create, update and delete routes
 */
@CrossOrigin
@RestController
public class RouteController {
    private static final Logger logger = LoggerFactory.getLogger(FaceController.class);
    private RouteDao routeDao;

    public RouteController(RouteDao routeDao) {
        this.routeDao = routeDao;
    }

    //Get\\ (GET: 500, GET {id}: 404/500)
    //Get all routes
    @PreAuthorize("permitAll")                                                                                          //The user doesn't have to be authenticated
    @RequestMapping(path = "/routes", method = RequestMethod.GET)
    public List<Route> getAllRoutes() {
        try{
            return routeDao.listOfRoutes();
        } catch (DaoException e) {
            logger.error("DAO Error getting all routes: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "DAO Error - " + e.getMessage());       //Returns 500 'INTERNAL_SERVER_ERROR' message
        }
    }
    //Get 'route' for a specific 'id'
    @PreAuthorize("permitAll")
    @RequestMapping(path = "/routes/{id}", method = RequestMethod.GET)
    public Route getRouteByID(@PathVariable int id) {
        try{
            Route route = routeDao.getRouteById(id);                                                                    //Gets the route info for 'id'
            if (route == null) {                                                                                        //If 'route' is 'null'
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Route Not Found");                             //Returns 404 'NOT_FOUND' message
            } else {
                return route;
            }
        } catch (DaoException e) {
            logger.error("DAO Error getting route by ID {}: {}", id, e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "DAO Error - " + e.getMessage());       //Returns 500 'INTERNAL_SERVER_ERROR' message
        }
    }


    //Create new climbing route\\ (POST: 400/500)
    @PreAuthorize("hasRole('ROLE_ADMIN')")                                                                                   //The user must be authenticated and have the role ADMIN
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/routes", method = RequestMethod.POST)
    public Route createNewRoute(@Valid @RequestBody Route newRoute) {
        try{
            if (routeDao.getRouteByName(newRoute.getRouteName())!=null){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Route already exists.");                      //Returns 400 'BAD_REQUEST' message
            }
            return routeDao.createRoute(newRoute);
        } catch (DaoException e) {
            logger.error("DAO Error creating route: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An internal server error occurred creating the route."); //Returns 500 'INTERNAL_SERVER_ERROR' message
        }
    }


    //Update a climbing route\\ (PUT {id}:400/404/500)
    @PreAuthorize("hasRole('ROLE_ADMIN')")                                                                                   //The user must be authenticated and have the role ADMIN
    @RequestMapping(path = "/routes/{id}", method = RequestMethod.PUT)
    public Route updateRoute(@Valid @RequestBody Route route, @PathVariable int id) {
        // The id on the path takes precedence over the id in the request body, if any
        route.setRouteID(id);
        try {
            Route updatedRoute = routeDao.updateRoute(route);
            if (updatedRoute == null) {                                                                                  //If 'face' is 'null'
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Route Not Found");                              //Returns 404 'NOT_FOUND' message
            } else {
                return updatedRoute;
            }
        }  catch (DaoException e) {
            logger.error("DAO Error updating route for ID {}: {}", id, e.getMessage(), e);
            // Distinguish between bad request (e.g., validation) and not found if DAO can throw specific exceptions
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An internal server error occurred updating the route.");
        }
    }


    //Delete a climbing route\\ (DELETE {id}: 404/500)
    @PreAuthorize("hasRole('ROLE_ADMIN')")                                                                                   //The user must be authenticated and have the role ADMIN
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(path = "/routes/{id}", method = RequestMethod.DELETE)
    public void deleteRouteById(@PathVariable int id) {
        try {
            if (routeDao.getRouteById(id) == null){                                                                     //If there isn't a route with 'id'
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Route not found");                             //Returns 404 'NOT_FOUND' message
            }
            routeDao.deleteRouteById(id);
        } catch (DaoException e) {
            logger.error("DAO Error deleting route with ID {}: {}", id, e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An internal server error occurred deleting the route.");
        }
    }
}
