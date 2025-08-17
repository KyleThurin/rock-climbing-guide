package com.techelevator.dao;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import com.techelevator.exception.DaoException;
import com.techelevator.model.UserLog;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcUserLogDao implements UserLogDao {
    //private final String LOG_SELECT = "SELECT * FROM user_log ";
    private final String LOG_SELECT = "SELECT ul.user_log_id, u.username, ul.route_id, ul.area_id, ul.face_id, ul.log_details " +
            "FROM user_log ul JOIN users u ON ul.user_id = u.user_id ";
    private final JdbcTemplate jdbcTemplate;

    public JdbcUserLogDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public List<UserLog> getUserLogs() {
        List<UserLog> userLogs = new ArrayList<>();                                                                     //Creates new array list
        String sql = LOG_SELECT;                                                                                        //Copy the verified SQL from PgAdmin
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);                                                       //Send the sql query
            while (results.next()) {                                                                                    //Parse the results
                UserLog userLog = mapRowToUserLog(results);                                                             //Create 'userLog' object from each row from the results
                userLogs.add(userLog);                                                                                  //Add the 'userLog' object to the list of user logs to return
            }
        } catch(CannotGetJdbcConnectionException e){
            throw new DaoException("ERROR: Unable to connect to server or database", e);                                //Throw 500:'DaoException' with message
        }
        return userLogs;
    }

    @Override
    public List<UserLog> getUserLogsByUsername(String username){
        List<UserLog> userLogs = new ArrayList<>();
        String sql = LOG_SELECT + " WHERE u.username = ?;";
        UserLog userLog = null;
        /*String sql = "SELECT * FROM users " +
                "JOIN users AS u ON u.username = user_log.username " +
                "WHERE u.username = ?;";*/
        try{
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, username);                                             //Send the sql query
            while(results.next()){
                userLogs.add(mapRowToUserLog(results));
            }
        } catch(CannotGetJdbcConnectionException e){
            throw new DaoException("ERROR: Unable to connect to server or database", e);                                //Throw 500:'DaoException' with message
        }
        return userLogs;
    }
    @Override
    public UserLog getUserLogById(int userLogId) {
        UserLog userLog = null;                                                                                         //Creates 'rout' and sets it to equal 'null'
        String sql = LOG_SELECT + " WHERE ul.user_log_id = ?;";                                                         //Set SQL string
        try{
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userLogId);                                            //Send the sql query
            //'IF' is used because only searching for one 'rout'
            if(results.next()){                                                                                         //If it matches
                userLog = mapRowToUserLog(results);
            }
        } catch(CannotGetJdbcConnectionException e){
            throw new DaoException("ERROR: Unable to connect to server or database", e);                                //Throw 500:'DaoException' with message
        }
        return userLog;
    }
    //TODO: How to specify what area/face/route the user log is for
    @Override
    public UserLog createUserLog(UserLog newUserLog) {
        //String sql = "INSERT INTO user_log (username, route_id, log_details) " +
        //        "VALUES (?, ?, ?) RETURNING user_log_id";                                                                //Set SQL string
        String getUserIdSql = "SELECT user_id FROM users WHERE username = ?";
        Integer userId;
        try{
            //Search for 'department_id' in database
            /*int userLogId = jdbcTemplate.queryForObject(sql, int.class, newUserLog.getId(),newUserLog.getRouteID(),
                    newUserLog.getLogDetails());                                                                        //Send the sql query
            return getUserLogById(userLogId);*/                                                                           //Get the route for the new ID and return it
            userId = jdbcTemplate.queryForObject(getUserIdSql, Integer.class, newUserLog.getId());
        } catch(CannotGetJdbcConnectionException e){
            throw new DaoException("ERROR: Unable to connect to server or database", e);                                //Throw 500:'DaoException' with message
        } catch (DataIntegrityViolationException e) {                                                                   //Catch 'DataIntegrityViolationException'
            throw new DaoException("Data integrity violation", e);                                                      //Throw 'DaoException' with message
        }
        if (userId == null) {
            throw new DaoException("User not found for username: " + newUserLog.getId());
        }
        String sql = "INSERT INTO user_log (user_id, route_id, area_id, face_id, log_details) " +
                "VALUES (?, ?, ?, ?, ?) RETURNING user_log_id";

        try{
            int userLogId = jdbcTemplate.queryForObject(sql, int.class,
                    userId,
                    newUserLog.getRouteID(),
                    //newUserLog.getAreaId(),
                    //newUserLog.getFaceId(),
                    newUserLog.getLogDetails());
            return getUserLogById(userLogId);
        } catch(CannotGetJdbcConnectionException e){
            throw new DaoException("ERROR: Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    @Override
    public UserLog updateUserLog(UserLog userLogUpdate) {
        UserLog updatedUserLog  = null;                                                                                 //Create local variable and set to 'null'
        String sql = "UPDATE user_log SET username = ?, route_id = ?, log_details = ? " +
                "WHERE user_log_id = ?;";                                                                               //Set SQL string
        try {
            int numberOfRows = jdbcTemplate.update(sql, userLogUpdate.getId(), userLogUpdate.getRouteID(),
                    userLogUpdate.getLogDetails(), userLogUpdate.getUserLogID());
            if (numberOfRows == 0) {                                                                                    //If 'numberOfRows' equals zero
                throw new DaoException("Zero rows affected, expected at least one");                                    //Throw 'DaoException' with message
            } else {                                                                                                    //If 'numberOfRows' does NOT equals zero
                updatedUserLog = getUserLogById(userLogUpdate.getUserLogID());                                          //Retrieve the updated user_log to get any updated fields
            }
        } catch(CannotGetJdbcConnectionException e){
            throw new DaoException("ERROR: Unable to connect to server or database", e);                                //Throw 500:'DaoException' with message
        } catch (DataIntegrityViolationException e) {                                                                   //Catch 'DataIntegrityViolationException'
            throw new DaoException("Data integrity violation", e);                                                      //Throw 'DaoException' with message
        }
        return updatedUserLog;
    }
    /*
    @Override
    // MODIFIED: Updated logic to return null if no rows were affected.
    public UserLog updateUserLog(UserLog userLogUpdate) {
        UserLog updatedUserLog  = null;
        // Need to get the user_id from the username (if username is updated, though typically not in log updates)
        String getUserIdSql = "SELECT user_id FROM users WHERE username = ?";
        Integer userId;
        try {
            userId = jdbcTemplate.queryForObject(getUserIdSql, Integer.class, userLogUpdate.getUsername());
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("ERROR: Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation while getting user ID for update", e);
        }
        if (userId == null) {
            throw new DaoException("User not found for username: " + userLogUpdate.getUsername());
        }


        String sql = "UPDATE user_log SET user_id = ?, route_id = ?, area_id = ?, face_id = ?, log_details = ? " +
                "WHERE user_log_id = ?;";
        try {
            int numberOfRows = jdbcTemplate.update(sql,
                    userId, // MODIFIED: Pass userId
                    userLogUpdate.getRouteID(),
                    userLogUpdate.getAreaId(), // ADDED: Pass areaId
                    userLogUpdate.getFaceId(), // ADDED: Pass faceId
                    userLogUpdate.getLogDetails(),
                    userLogUpdate.getUserLogID());
            if (numberOfRows == 0) {
                // If 0 rows affected, it means the log with the given ID was not found.
                return null; // MODIFIED
            } else {
                updatedUserLog = getUserLogById(userLogUpdate.getUserLogID());
            }
        } catch(CannotGetJdbcConnectionException e){
            throw new DaoException("ERROR: Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return updatedUserLog;
    }
    */


    @Override
    //TODO: Similar checks for 0 rows affected in jdbcTemplate.update() to indicate "not found".
    public int deleteUserLogById(int userLogId) {
        int numberOfRows = 0;

        String deleteUserLogSql = "DELETE FROM user_log WHERE user_log_id = ?;";                                        //SQL string to delete 'route_id' from 'user_log' table

        try {
            numberOfRows = jdbcTemplate.update(deleteUserLogSql, userLogId);
            // No need to explicitly check if numberOfRows is 0 here and throw DaoException.
        } catch(CannotGetJdbcConnectionException e){
            throw new DaoException("ERROR: Unable to connect to server or database", e);                                //Throw 500:'DaoException' with message
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);                                                      //Throw 'DaoException' with message
        }
        return numberOfRows;
    }

    //user_log (user_id, route_id, log_details)
    private UserLog mapRowToUserLog(SqlRowSet rs) {
        UserLog userLog = new UserLog();
        userLog.setUserLogID(rs.getInt("user_log_id"));
        userLog.setId(rs.getString("username"));
        userLog.setRouteID(rs.getInt("route_id"));
        userLog.setLogDetails(rs.getString("log_details"));
        return userLog;
    }
}
