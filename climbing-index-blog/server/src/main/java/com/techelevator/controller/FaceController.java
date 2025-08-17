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
 * Face (anonymous user):
 *  - Create a list of the climbing faces
 *  - Get detailed information about that climbing face
 *  - Create a list of the climbing routes on that face
 *
 * Edit (administrator):
 *  - Create, update and delete routes
 */
@CrossOrigin
@RestController
public class FaceController {
    private static final Logger logger = LoggerFactory.getLogger(FaceController.class);
    private FaceDao faceDao;
    private RouteDao routeDao;

    public FaceController(FaceDao faceDao, RouteDao routeDao) {
        this.faceDao = faceDao;
        this.routeDao = routeDao;
    }

    //Get\\ (GET: 500, GET {id}: 404/500)
    //Get all faces
    @PreAuthorize("permitAll")                                                                                          //The user doesn't have to be authenticated
    @RequestMapping(path = "/faces", method = RequestMethod.GET)
    public List<Face> getAllFaces() {
        try{
            return faceDao.listOfFaces();
        } catch (DaoException e) {
            logger.error("DAO Error getting all faces: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "DAO Error - " + e.getMessage());       //Returns 500 'INTERNAL_SERVER_ERROR' message
        }
    }

    //Get 'face' for a specific 'id'
    @PreAuthorize("permitAll")
    @RequestMapping(path = "/faces/{id}", method = RequestMethod.GET)
    public Face getFaceByID(@PathVariable int id) {
        try{
            Face face = faceDao.getFaceById(id);                                                                        //Gets the face info for 'id'
            if (face == null) {                                                                                         //If 'face' is 'null'
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Face Not Found");                              //Returns 404 'NOT_FOUND' message
            } else {
                return face;
            }
        } catch (DaoException e) {
            logger.error("DAO Error getting face by ID {}: {}", id, e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "DAO Error - " + e.getMessage());       //Returns 500 'INTERNAL_SERVER_ERROR' message
        }
    }

    //Get all the 'faces' for a specific area 'id'
    @PreAuthorize("permitAll")                                                                                          //The user doesn't have to be authenticated
    @RequestMapping(path = "/faces/{id}/routes", method = RequestMethod.GET)
    public List<Route> getAllRoutesForFace(@PathVariable int id) {
        try{
            List<Route> route = routeDao.getRoutesByFaceID(id);                                                         //Gets the routes associated with 'face_id'
            if (faceDao.getFaceById(id) == null) {                                                                      //Check if the parent face exists
                // Only throw NOT_FOUND if the area itself doesn't exist.
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Face Not Found, cannot retrieve faces.");
            }
            return route;
        } catch (DaoException e) {
            logger.error("DAO Error getting routes for face ID {}: {}", id, e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "DAO Error - " + e.getMessage());       //Returns 500 'NOT_FOUND' message
        }
    }

    //Create new climbing face\\ (POST: 400/500)
    @PreAuthorize("hasRole('ROLE_ADMIN')")                                                                                   //The user must be authenticated and have the role ADMIN
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/faces", method = RequestMethod.POST)
    public Face createNewFace(@Valid @RequestBody Face newFace) {
        try{
            if (faceDao.getFaceByName(newFace.getFaceName())!=null){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Face already exists.");                      //Returns 400 'BAD_REQUEST' message
            }
            return faceDao.createFace(newFace);
        } catch (DaoException e) {
            logger.error("DAO Error creating face: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An internal server error occurred creating the face."); //Returns 500 'INTERNAL_SERVER_ERROR' message
        }
    }

    //Update a climbing face\\ (PUT {id}:400/404/500)
    @PreAuthorize("hasRole('ROLE_ADMIN')")                                                                              //The user must be authenticated and have the role ADMIN
    @RequestMapping(path = "/faces/{id}", method = RequestMethod.PUT)
    public Face updateFace(@Valid @RequestBody Face face, @PathVariable int id) {
        // The id on the path takes precedence over the id in the request body, if any
        face.setFaceID(id);
        try {
            Face updatedFace = faceDao.updateFace(face);
            if (updatedFace == null) {                                                                                  //If 'face' is 'null'
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Face Not Found");                              //Returns 404 'NOT_FOUND' message
            } else {
                return updatedFace;
            }
        }  catch (DaoException e) {
            logger.error("DAO Error updating face for ID {}: {}", id, e.getMessage(), e);
            // Distinguish between bad request (e.g., validation) and not found if DAO can throw specific exceptions
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An internal server error occurred updating the face.");
        }
    }


    //Delete a climbing face\\ (DELETE {id}: 404/500)
    @PreAuthorize("hasRole('ROLE_ADMIN')")                                                                                   //The user must be authenticated and have the role ADMIN
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(path = "/faces/{id}", method = RequestMethod.DELETE)
    public void deleteFaceById(@PathVariable int id) {
        try {
            if (faceDao.getFaceById(id) == null){                                                                       //If there isn't a face with 'id'
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Face not found");                              //Returns 404 'NOT_FOUND' message
            }
            faceDao.deleteFaceById(id);
        } catch (DaoException e) {
            logger.error("DAO Error deleting face with ID {}: {}", id, e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An internal server error occurred deleting the face.");
        }
    }
}
