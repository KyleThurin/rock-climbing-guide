package com.techelevator.dao;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import com.techelevator.exception.DaoException;
import com.techelevator.model.Area;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * Area:
 *  - Create a list of the climbing areas
 *  - Get details of that climbing area
 *  - Create a list of the climbing faces for that area
 */
@Component
public class JdbcAreaDao implements AreaDao{
    //private final String AREA_SELECT = "SELECT * FROM areas ORDER BY area_name ";
    private final String AREA_SELECT = "SELECT area_id, area_name, latitude, longitude, area_details FROM areas ";
    private final JdbcTemplate jdbcTemplate;
    public JdbcAreaDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    @Override //Create a list of the climbing areas (GET: 500)
    public List<Area> listOfAreas() {
        List<Area> areas = new ArrayList<>();                                                                           //Creates new array list
        String sql = AREA_SELECT;                                                                                       //Copy the verified SQL from PgAdmin
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);                                                       //Send the sql query
            while (results.next()) {                                                                                    //Parse the results
                Area area = mapRowToArea(results);                                                                      //Create 'Area' object from each row from the results
                areas.add(area);                                                                                        //Add the 'area' object to the list of areas to return
            }
        } catch(CannotGetJdbcConnectionException e){
            throw new DaoException("ERROR: Unable to connect to server or database", e);                                //Throw 500:'DaoException' with message
        }
        return areas;
    }
    @Override //Gets the area for the given 'areaID' (GET {id}: 404/500)
    public Area getAreaById(int areaId) {
        // Initialize to null in case there are no results returned
        Area area = null;                                                                                               //Create local variable 'area' and set to 'null'
        String sql = AREA_SELECT + " WHERE area_id = ?;";                                                               //Set SQL string
        try{
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, areaId);                                               //Send the sql query
            //'IF' is used because only searching for one 'area'
            if(results.next()){                                                                                         //If it matches 'results'
                area = mapRowToArea(results);                                                                           //Index data from the row that matches 'results'
            }
        } catch(CannotGetJdbcConnectionException e){
            throw new DaoException("ERROR: Unable to connect to server or database", e);                                //Throw 500:'DaoException' with message
        }
        return area;
    }
    @Override
    public Area getAreaByName(String areaName) {
        Area foundArea = null;                                                                                          //Create local variable 'area' and set to 'null'
        String sql = AREA_SELECT + "WHERE area_name = ?";                                                               //Copy the verified SQL from PgAdmin
        try{
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, areaName);                                               //Send the sql query
            //'IF' is used because only searching for one 'area'
            if(results.next()){                                                                                         //If it matches 'results'
                foundArea = mapRowToArea(results);                                                                           //Index data from the row that matches 'results'
            }
        } catch(CannotGetJdbcConnectionException e){
            throw new DaoException("ERROR: Unable to connect to server or database", e);                                //Throw 500:'DaoException' with message
        }
        return foundArea;
    }
    //Creates a new area (POST: 400/500)
    @Override
    public Area createArea(Area newArea) {
        String sql = "INSERT INTO Areas (area_name, latitude, longitude, area_details) " +
                "VALUES (?, ?, ?, ?) RETURNING area_id";                                                                //Set SQL string
        try{
            //Search for 'department_id' in database
            int areaId = jdbcTemplate.queryForObject(sql, int.class, newArea.getAreaName(),newArea.getLatitude(),
                    newArea.getLongitude(),newArea.getAreaDetails());                                                   //Send the sql query
            return getAreaById(areaId);                                                                                 //Get the route for the new ID and return it
        } catch(CannotGetJdbcConnectionException e){
            throw new DaoException("ERROR: Unable to connect to server or database", e);                                //Throw 500:'DaoException' with message
        } catch (DataIntegrityViolationException e) {                                                                   //Catch 'DataIntegrityViolationException'
            throw new DaoException("Data integrity violation", e);                                                      //Throw 'DaoException' with message
        }
    }
    @Override
    public Area updateArea(Area areaUpdate) {
        Area updatedArea  = null;                                                                                       //Create local variable and set to 'null'
        String sql = "UPDATE Areas SET area_name = ?, latitude = ?, longitude = ?, area_details = ? " +
                "WHERE area_id = ?;";                                                                                   //Set SQL string
        try {
            int numberOfRows = jdbcTemplate.update(sql, areaUpdate.getAreaName(), areaUpdate.getLatitude(),
                    areaUpdate.getLongitude(), areaUpdate.getAreaDetails(), areaUpdate.getAreaID());
            //If 0 rows affected, the area with the given ID was not found
            if (numberOfRows == 0) {
                //Returning null allows the controller to map this to NOT_FOUND.
                return null;
            } else {
                updatedArea = getAreaById(areaUpdate.getAreaID());
            }
        } catch(CannotGetJdbcConnectionException e){
            throw new DaoException("ERROR: Unable to connect to server or database", e);                                //Throw 500:'DaoException' with message
        } catch (DataIntegrityViolationException e) {                                                                   //Catch 'DataIntegrityViolationException'
            throw new DaoException("Data integrity violation", e);                                                      //Throw 'DaoException' with message
        }
        return updatedArea;
    }
    @Override
    public int deleteAreaById(int areaId) {
        int numberOfRows = 0;                                                                                           //Create local variable and set to zero
        //Delete from tables in correct order: routes<faces<areas
        String deleteRoutSql = "DELETE FROM routes WHERE route_id IN (SELECT route_id " +
                "FROM routes " +
                "JOIN faces ON routes.face_id = faces.face_id " +
                "WHERE faces.area_id = ?)";
        String deleteFaceSql = "DELETE FROM faces WHERE area_id = ?;";
        String deleteAreaSql = "DELETE FROM areas WHERE area_id = ?;";
        try {
            //Delete any foreign key references to author_id
            jdbcTemplate.update(deleteRoutSql, areaId);
            jdbcTemplate.update(deleteFaceSql, areaId);
            numberOfRows = jdbcTemplate.update(deleteAreaSql, areaId);
        } catch(CannotGetJdbcConnectionException e){
            throw new DaoException("ERROR: Unable to connect to server or database", e);                                //Throw 500:'DaoException' with message
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);                                                      //Throw 'DaoException' with message
        }
        return numberOfRows;
    }


    //areas (area_id, area_name, latitude, longitude, area_details)
    private Area mapRowToArea(SqlRowSet rs) {
        Area area = new Area();
        area.setAreaID(rs.getInt("area_id"));
        area.setAreaName(rs.getString("area_name"));
        area.setLatitude(rs.getDouble("latitude"));
        area.setLongitude(rs.getDouble("longitude"));
        area.setAreaDetails(rs.getString("area_details"));
        return area;
    }
}
