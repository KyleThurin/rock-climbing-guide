package com.techelevator.controller;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.core.Authentication;
import com.techelevator.model.dto.LoginResponseDto;
import com.techelevator.security.jwt.TokenProvider;
import com.techelevator.model.dto.RegisterUserDto;
import org.springframework.web.bind.annotation.*;
import com.techelevator.exception.DaoException;
import org.springframework.http.HttpStatus;
import com.techelevator.model.dto.LoginDto;
import com.techelevator.dao.UserDao;
import com.techelevator.model.User;
import jakarta.validation.Valid;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;


/**
 * AuthenticationController is a class used for handling requests to authenticate Users.
 *
 * It depends on an instance of a UserDao for retrieving and storing user data. This is provided
 * through dependency injection.
 */
@CrossOrigin
@RestController
public class AuthenticationController {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private UserDao userDao;

    public AuthenticationController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder, UserDao userDao) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.userDao = userDao;
    }
    //Create new climbing user\\ (POST: 400/500)
    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public LoginResponseDto login(@Valid @RequestBody LoginDto loginDto) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

            // Authenticate the user; if successful, authentication object will be populated
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

            // MODIFIED: Removed redundant if(authentication.isAuthenticated()) check
            String jwt = tokenProvider.createToken(authentication, false);
            User user = userDao.getUserByUsername(loginDto.getUsername());
            return new LoginResponseDto(jwt, user);

            /* Removed 8/11/25
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            if(authentication.isAuthenticated()){
                String jwt = tokenProvider.createToken(authentication, false);
                User user = userDao.getUserByUsername(loginDto.getUsername());
                return new LoginResponseDto(jwt, user);
            }
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);*/

        } catch (AuthenticationException e) { //Catch for authentication failures
            logger.warn("Authentication failed for user {}: {}", loginDto.getUsername(), e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password.");
        }
        catch (DaoException e) {
            logger.error("DAO error during login for user {}: {}", loginDto.getUsername(), e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "DAO error - " + e.getMessage());
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public User register(@Valid @RequestBody RegisterUserDto newUser) {
        if(!newUser.isPasswordsMatch()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password and confirm password do not match");
        }
        try {
            if (userDao.getUserByUsername(newUser.getUsername()) != null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists.");
            }

            // The createUser method in UserDao (JdbcUserDao) should handle password hashing
            User user = userDao.createUser(new User(newUser.getUsername(), newUser.getPassword(), newUser.getRole()));
            return user;
        }
        catch (DaoException e) {
            // Log the full exception for debugging, return a generic error message to client
            logger.error("DAO error during registration for user {}: {}", newUser.getUsername(), e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "DAO error - " + e.getMessage());
        }
    }

}
