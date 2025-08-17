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
import java.security.Principal;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import org.slf4j.Logger;
import java.util.List;

/**
 * Activity Log:
 *  - As a user, I need to be able to enter information into an activity log associated with each:
 *      - Area
 *      - Face
 *      - Route
 */
@PreAuthorize("isAuthenticated()")
@CrossOrigin
@RestController
public class UserLogController {
    private static final Logger logger = LoggerFactory.getLogger(FaceController.class);
    private AreaDao areaDao;
    private FaceDao faceDao;
    private RouteDao routeDao;
    private UserLogDao userLogDao;

    public UserLogController(AreaDao areaDao, FaceDao faceDao, RouteDao routeDao, UserLogDao userLogDao) {
        this.areaDao = areaDao;
        this.faceDao = faceDao;
        this.routeDao = routeDao;
        this.userLogDao = userLogDao;
    }

    //Get\\ (GET: 500, GET {id}: 404/500)
    //Get all logs
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @RequestMapping(path = "/logs", method = RequestMethod.GET)
    public List<UserLog> getAllLogs() {
        try{
            return userLogDao.getUserLogs();
        } catch (DaoException e) {
            logger.error("DAO Error getting all logs: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "DAO Error - " + e.getMessage());
        }
    }


    //Get log(s) for current user "$2a$10$tmxuYYg1f5T0eXsTPlq/V.DJUKmRHyFbJ.o.liI1T35TFbjs2xiem"
    //TODO: Get for specific user

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @RequestMapping(path = "/logs/my-logs", method = RequestMethod.GET)
    public List<UserLog> getLogsForCurrentUser(Principal principal) {
        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated.");
        }
        try{
            String username = principal.getName();
            // Assuming UserLogDao has a method to get logs by username
            return userLogDao.getUserLogsByUsername(username); // ADDED: Call to get user-specific logs
        } catch (DaoException e) {
            logger.error("DAO Error getting logs for user {}: {}", principal.getName(), e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "DAO Error - " + e.getMessage());
        }
    }

    @RequestMapping(path = "/logs", method = RequestMethod.GET)
    public List<UserLog> getLogsForUser(Principal principal) {
        try{
            String username = principal.getName();
            List<UserLog> userLog = userLogDao.getUserLogsByUsername(username);
            return userLog;
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "DAO Error - " + e.getMessage());
        }
    }



    //Create new climbing log\\ (POST: 400/500)
    //TODO: Fix
    /*@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')") // MODIFIED: Changed to standard 'ROLE_USER', 'ROLE_ADMIN'
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/logs", method = RequestMethod.POST)
    public UserLog createNewLog(@Valid @RequestBody UserLog newLog,
                                @RequestParam(name ="area_id",defaultValue = "0") int area_id,
                                @RequestParam(name ="face_id",defaultValue = "0") int face_id,
                                Principal principal) {
        if (newLog == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Log data is missing.");
        }

        // Ensure the log is associated with the authenticated user
        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated.");
        }

        newLog.setUserLogID(principal.getName()); //Set username from authenticated user

        // Logic for associating with area or face.
        // IMPORTANT: The UserLog model (DTO) MUST have areaId and faceId fields (e.g., private Integer areaId; private Integer faceId;) and corresponding setters for this to work.
        if (area_id > 0 && face_id > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Only one can be specified, either Area or Face ID");
        } else if (area_id > 0) {
            newLog.setAreaId(area_id);
            // You might want to validate if the area_id exists via areaDao.getAreaById(area_id)
        } else if (face_id > 0) {
            newLog.setFaceId(face_id);
            // You might want to validate if the face_id exists via faceDao.getFaceById(face_id)
        }
        // If neither area_id nor face_id is provided, the log is assumed to be a general log or associated with a route only
        // The UserLog DTO/Model should be structured to handle these associations.

        try {
            return userLogDao.createUserLog(newLog);
        } catch (DaoException e) {
            logger.error("DAO Error creating user log for user {}: {}", principal.getName(), e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An internal server error occurred creating the log.");
        }
    }*/

    //Update a climbing log\\ (PUT {id}:400/404/500)
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @RequestMapping(path = "/logs/{id}", method = RequestMethod.PUT)
    public UserLog updateUserLog(@Valid @RequestBody UserLog log, @PathVariable int id, Principal principal) {
        if (log == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Log data is missing.");
        }
        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated.");
        }
        log.setUserLogID(id); // Ensure ID from path is used

        try {
            //Verify that the user owns this log before updating
            UserLog existingLog = userLogDao.getUserLogById(id);
            if (existingLog == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Log not found.");
            }
            if (!existingLog.getId().equals(principal.getName()) && !principal.getName().equals("admin_username")) { // Example for admin override
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to update this log.");
            }

            UserLog updatedLog = userLogDao.updateUserLog(log);
            if (updatedLog == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Log not found for update.");
            }
            return updatedLog;
        } catch (DaoException e) {
            logger.error("DAO Error updating log with ID {}: {}", id, e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An internal server error occurred updating the log.");
        }
    }

    //Delete a climbing log\\ (DELETE {id}: 404/500)
    //TODO: Need to fix
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'USER', 'ADMIN')")                                             //The user must be authenticated and have the role ADMIN
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(path = "/logs/{id}", method = RequestMethod.DELETE)
    public void deleteUserLogById(@PathVariable int id, Principal principal) {
        try {
            if (userLogDao.getUserLogById(id) == null){                                                                 //If there isn't a log with 'id'
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Log not found");                               //Returns 404 'NOT_FOUND' message
            }
            // IMPORTANT: Ensure user can only delete their own logs, or if admin.
            /*if (!logToDelete.get().equals(principal.getName()) && !principal.getName().equals("admin_username_placeholder")) {
                // Replace "admin_username_placeholder" with actual admin username or role check
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to delete this log.");
            }*/
            userLogDao.deleteUserLogById(id);
        } catch (DaoException e) {
            logger.error("DAO Error deleting user log with ID {}: {}", id, e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "DAO Error - " + e.getMessage());       //Returns 500 'NOT_FOUND' message
        }
    }
}
