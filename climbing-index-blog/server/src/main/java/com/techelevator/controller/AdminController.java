package com.techelevator.controller;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
import com.techelevator.model.UserLog;
import com.techelevator.model.Route;
import com.techelevator.model.Area;
import com.techelevator.model.User;
import jakarta.validation.Valid;
import org.slf4j.LoggerFactory;
import java.security.Principal;
import com.techelevator.dao.*;
import java.util.ArrayList;
import org.slf4j.Logger;
import java.util.List;

/**
 * Create Admins:
 *
 * As an administrator, I need to be able to assign other users to be administrators
 */
@PreAuthorize("hasRole('ROLE_ADMIN')")                                                                                   //The user must be authenticated and have the role ADMIN
@CrossOrigin
@RestController
public class AdminController {
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
    private UserDao userDao;
    private UserLogDao userLogDao;
    private RouteDao routeDao;

    public AdminController(UserDao userDao, UserLogDao userLogDao, RouteDao routeDao) {
        this.userDao = userDao;
        this.userLogDao = userLogDao;
        this.routeDao = routeDao;
    }
    //Get all users\\ (GET: 500, GET {id}: 404/500)
    @RequestMapping(path = "/admin", method = RequestMethod.GET)
    public List<User> getAllUsers() {
        try{
            return userDao.getUsers();
        } catch (DaoException e) {
            logger.error("DAO Error getting all users: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "DAO Error - " + e.getMessage());
        }
    }

    //Update a the role of the User\\ (PUT {id}:400/404/500)
    @RequestMapping(path = "/admin/{id}", method = RequestMethod.PUT)
    public User updateUserRole(@Valid @RequestBody User user, @PathVariable int id) {
        // The id on the path takes precedence over the id in the request body, if any
        user.setId(id);
        try {
            User updatedUser = userDao.updateUserRole(user);
            //If the DAO returns null or indicates no update, user was not found
            if (updatedUser == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found to update.");
            }
            return updatedUser;
        } catch (DaoException e) {
            logger.error("DAO Error updating user role for ID {}: {}", id, e.getMessage(), e);
            //Depending on DaoException specifics, could be NOT_FOUND or INTERNAL_SERVER_ERROR
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An internal server error occurred updating the user role.");
        }
    }
    //Delete a user\\ (DELETE {id}: 404/500)
    /*@ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(path = "/admin/{id}", method = RequestMethod.DELETE)
    public void deleteUserById(@PathVariable int id) { // ADDED: Implementation for delete user
        try {
            if (userDao.getUserById(id) == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found for deletion.");
            }
            userDao.deleteUserById(id);
        } catch (DaoException e) {
            logger.error("DAO Error deleting user with ID {}: {}", id, e.getMessage(), e); // MODIFIED: Using logger
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An internal server error occurred deleting the user.");
        }
    }*/
}
