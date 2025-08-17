package com.techelevator.dao;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import com.techelevator.exception.DaoException;
import com.techelevator.model.Route;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * Route:
 *  - Create a list of the climbing routes
 *  - Get detailed information about that climbing route
 */
@Component
public class JdbcRouteDao implements RouteDao{
    //private final String ROUTE_SELECT = "SELECT * FROM routes ORDER BY route_name ";
    private final String ROUTE_SELECT = "SELECT route_id, face_id, route_name, route_difficulty, number_of_pitches, route_notes FROM routes ";//ORDER BY route_name ";
    private final JdbcTemplate jdbcTemplate;
    public JdbcRouteDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    @Override //Create a list of the climbing routes
    public List<Route> listOfRoutes() {
        List<Route> routes = new ArrayList<>();                                                                         //Creates new array list
        String sql = ROUTE_SELECT;                                                                                      //Copy the verified SQL from PgAdmin
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);                                                       //Send the sql query
            while (results.next()) {                                                                                    //Parse the results
                Route rout = mapRowToRoute(results);                                                                    //Create 'Rout'' object from each row from the results
                routes.add(rout);                                                                                       //Add the 'route' object to the list of routes to return
            }
        } catch(CannotGetJdbcConnectionException e){
            throw new DaoException("ERROR: Unable to connect to server or database", e);                                //Throw 500:'DaoException' with message
        }
        return routes;
    }
    @Override
    public Route getRouteById(int routeId) {
        // Initialize to null in case there are no results returned
        Route route = null;                                                                                             //Create local variable 'route' and set to 'null'
        String sql = ROUTE_SELECT + " WHERE route_id = ?;";                                                             //Copy the verified SQL from PgAdmin
        try{
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, routeId);                                              //Send the sql query
            //'IF' is used because only searching for one 'rout'
            if(results.next()){                                                                                         //If it matches 'results'
                route = mapRowToRoute(results);                                                                         //Index data from the row that matches 'results'
            }
        } catch(CannotGetJdbcConnectionException e){
            throw new DaoException("ERROR: Unable to connect to server or database", e);                                //Throw 500:'DaoException' with message
        }
        return route;
    }
    @Override //Create a list of the climbing routes for that face
    public List<Route> getRoutesByFaceID(int faceId) {
        List<Route> foundRoutes = new ArrayList<>();                                                                    //Creates new array list
        String sql = ROUTE_SELECT + " WHERE face_id = ?;";                                                              //Set SQL string
        try{
            //'IF' is used because only searching for one 'face'
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, faceId);                                               //Send the sql query
            //Loop through 'results'
            while(results.next()){                                                                                      //Moves to the next row of the set after next() is called
                foundRoutes.add(mapRowToRoute(results));                                                                //Add mapped result to collection
            }
            return foundRoutes;
        } catch(CannotGetJdbcConnectionException e){
            throw new DaoException("ERROR: Unable to connect to server or database", e);                                //Throw 500:'DaoException' with message
        }
    }
    @Override
    public Route getRouteByName(String routeName) {
        // Initialize to null in case there are no results returned
        Route route = null;                                                                                             //Create local variable 'route' and set to 'null'
        String sql = ROUTE_SELECT + " WHERE route_name = ?;";                                                           //Copy the verified SQL from PgAdmin
        try{
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, routeName);                                              //Send the sql query
            //'IF' is used because only searching for one 'rout'
            if(results.next()){                                                                                         //If it matches 'results'
                route = mapRowToRoute(results);                                                                         //Index data from the row that matches 'results'
            }
        } catch(CannotGetJdbcConnectionException e){
            throw new DaoException("ERROR: Unable to connect to server or database", e);                                //Throw 500:'DaoException' with message
        }
        return route;
    }
    @Override
    public Route createRoute(Route newRoute) {
        String sql = "INSERT INTO routes (face_id, route_name, route_difficulty, number_of_pitches, route_notes) " +
                "VALUES (?, ?, ?, ?, ?) RETURNING route_id";                                                            //Set SQL string
        try{
            //Search for 'department_id' in database
            int routeId = jdbcTemplate.queryForObject(sql, int.class, newRoute.getFaceID(),newRoute.getRouteName(),
                    newRoute.getRouteDifficulty(),newRoute.getNumberOfPitches(), newRoute.getRouteNotes());             //Send the sql query
            return getRouteById(routeId);                                                                               //Get the route for the new ID and return it
        } catch(CannotGetJdbcConnectionException e){
            throw new DaoException("ERROR: Unable to connect to server or database", e);                                //Throw 500:'DaoException' with message
        } catch (DataIntegrityViolationException e) {                                                                   //Catch 'DataIntegrityViolationException'
            throw new DaoException("Data integrity violation", e);                                                      //Throw 'DaoException' with message
        }
    }
    @Override
    //TODO:
    // Problem: If jdbcTemplate.update() returns 0, it means no rows were affected, implying the areaId was not found.
    //          Your controller assumes a DaoException is thrown, but the DAO itself might just return null or a default Area object
    // Solution: In the DAO, check the return value of jdbcTemplate.update().
    //           If 0, return null (or throw a specific DaoException like ResourceNotFoundException) to indicate no update occurred.
    //           The controller can then map this to HttpStatus.NOT_FOUND.
    public Route updateRoute(Route updatedRoute) {
        // Initialize to null in case there are no results returned
        Route updatedRoutes = null;                                                                                     //Create local variable and set to 'null'
        String sql = "UPDATE routes SET face_id=?, route_name=?, route_difficulty=?, number_of_pitches=?, route_notes=? "
                + "WHERE route_id=?;";                                                                                  //Set SQL string
        try{
            int numberOfRows =jdbcTemplate.update(sql, updatedRoute.getFaceID(), updatedRoute.getRouteName(),
                    updatedRoute.getRouteDifficulty(),updatedRoute.getNumberOfPitches(),updatedRoute.getRouteNotes(),
                    updatedRoute.getRouteID());
            //If 0 rows affected, the route with the given ID was not found.
            if (numberOfRows == 0) {
                return null;
            } else {
                updatedRoutes = getRouteById(updatedRoute.getRouteID());
            }
        } catch(CannotGetJdbcConnectionException e){
            throw new DaoException("ERROR: Unable to connect to server or database", e);                                //Throw 500:'DaoException' with message
        }catch (DataIntegrityViolationException e) {                                                                    //Catch 'DataIntegrityViolationException'
            throw new DaoException("Data integrity violation while updating route: " + updatedRoute.getRouteName(), e); //Throw 'DaoException' with message
        }
        return updatedRoutes;
    }
    @Override
    public int deleteRouteById(int routeId) {
        int numberOfRows = 0;                                                                                           //Create local variable and set to zero
        //SQL string for deleting any rows that contain the 'route_id' in the join table
        String deleteUserLogSql = "DELETE FROM user_log WHERE route_id = ?;";                                           //SQL string to delete from 'user_log' table
        //SQL string string to delete the 'route_id' from the 'route' table
        String deleteRouteSql = "DELETE FROM routes WHERE route_id = ?;";                                               //SQL string to delete from 'routes' table
        try {
            //Delete any foreign key references to author_id
            jdbcTemplate.update(deleteUserLogSql, routeId);                                                              //Delete FK 'area_id' from 'faces' table
            // now safe to delete the areaID
            numberOfRows = jdbcTemplate.update(deleteRouteSql, routeId);
        } catch(CannotGetJdbcConnectionException e){
            throw new DaoException("ERROR: Unable to connect to server or database", e);                                //Throw 500:'DaoException' with message
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);                                                      //Throw 'DaoException' with message
        }
        return numberOfRows;
    }

    //routes (route_id, face_name, route_name, route_difficulty, number_of_pitches, route_notes)
    //      route_id, face_id, route_name, route_difficulty, number_of_pitches, route_notes
    private Route mapRowToRoute(SqlRowSet rs) {
        Route route = new Route(); //Creates new 'Route' object
        route.setRouteID(rs.getInt("route_id"));
        route.setFaceID(rs.getInt("face_id"));
        route.setRouteName(rs.getString("route_name"));
        route.setRouteDifficulty(rs.getString("route_difficulty"));
        route.setNumberOfPitches(rs.getInt("number_of_pitches"));
        route.setRouteNotes(rs.getString("route_notes"));
        return route;
    }
}
