package com.techelevator.dao;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import com.techelevator.exception.DaoException;
import com.techelevator.model.Face;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * Face:
 *  - Create a list of the climbing faces
 *  - Get detailed information about that climbing face
 *  - Create a list of the climbing routes on that face
 */
@Component
public class JdbcFaceDao implements FaceDao{
    //private final String FACE_SELECT = "SELECT * FROM faces ";
    private final String FACE_SELECT = "SELECT face_id, area_id, face_name, face_direction, face_notes FROM faces ";//+ "ORDER BY face_name ";
    private final JdbcTemplate jdbcTemplate;
    public JdbcFaceDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    @Override //Create a list of the climbing faces
    public List<Face> listOfFaces() {
        List<Face> faces = new ArrayList<>();                                                                           //Creates new array list
        String sql = FACE_SELECT;                                                                                       //Copy the verified SQL from PgAdmin
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);                                                       //Send the sql query
            while (results.next()) {                                                                                    //Parse the results
                Face face = mapRowToFace(results);                                                                      //Create 'face' object from each row from the results
                faces.add(face);                                                                                        //Add the 'face' object to the list of 'faces' to return
            }
        }
        catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);                                       //Throw 'DaoException' with message
        }
        return faces;
    }
    @Override
    public Face getFaceById(int faceId) {
        Face face = null;                                                                                               //Create local variable 'face' and set to 'null'
        String sql = FACE_SELECT + " WHERE face_id = ?;";                                                               //Set SQL string
        try{
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, faceId);                                               //Send the sql query
            //'IF' is used because only searching for one 'face'
            if(results.next()){                                                                                         //If it matches 'results'
                face = mapRowToFace(results);                                                                           //Index data from the row that matches 'results'
            }
        } catch(CannotGetJdbcConnectionException e){
            throw new DaoException("ERROR: Unable to connect to database", e);                                          //Throw 'DaoException' with message
        }
        return face;
    }
    @Override
    public Face getFaceByName(String faceName) {
        Face face = null;                                                                                               //Create local variable 'face' and set to 'null'
        String sql = FACE_SELECT + " WHERE face_name = ?;";                                                               //Set SQL string
        try{
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, faceName);                                               //Send the sql query
            //'IF' is used because only searching for one 'face'
            if(results.next()){                                                                                         //If it matches 'results'
                face = mapRowToFace(results);                                                                           //Index data from the row that matches 'results'
            }
        } catch(CannotGetJdbcConnectionException e){
            throw new DaoException("ERROR: Unable to connect to database", e);                                          //Throw 'DaoException' with message
        }
        return face;
    }
    @Override //Create a list of the climbing faces for that area
    public List<Face> getFacesByAreaID(int areaId) {
        List<Face> foundFaces = new ArrayList<>();                                                                      //Creates new array list
        String sql = FACE_SELECT + " WHERE area_id = ?;";                                                               //Set SQL string
        try{
            //'IF' is used because only searching for one 'face'
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, areaId);                                               //Send the sql query
            //Loop through 'results'
            while(results.next()){                                                                                      //Moves to the next row of the set after next() is called
                foundFaces.add(mapRowToFace(results));                                                                  //Add mapped result to collection
            }
            return foundFaces;
        } catch(CannotGetJdbcConnectionException e){
            throw new DaoException("ERROR: Unable to connect to server or database", e);                                //Throw 500:'DaoException' with message
        }
    }
    @Override
    public Face createFace(Face newFace) {
        String sql = "INSERT INTO faces (area_id, face_name, face_direction, face_notes) VALUES (?, ?, ?, ?) RETURNING face_id";                                                                //Set SQL string
        try{
            //Search for 'department_id' in database
            int faceId = jdbcTemplate.queryForObject(sql, int.class, newFace.getAreaID(),newFace.getFaceName(),
                    newFace.getFaceDirection(),newFace.getFaceNotes());                                                 //Send the sql query
            return getFaceById(faceId);                                                                                 //Get the face for the new ID and return it
        } catch(CannotGetJdbcConnectionException e){
            throw new DaoException("ERROR: Unable to connect to server or database", e);                                //Throw 500:'DaoException' with message
        } catch (DataIntegrityViolationException e) {                                                                   //Catch 'DataIntegrityViolationException'
            throw new DaoException("Data integrity violation", e);                                                      //Throw 'DaoException' with message
        }
    }
    @Override
    public Face updateFace(Face faceUpdate) {
        Face updatedFace  = null;                                                                                       //Create local variable and set to 'null'
        String sql = "UPDATE faces SET area_id = ?, face_name = ?, face_direction = ?, face_notes = ? " +
                "WHERE face_id = ?;";                                                                                   //Set SQL string
        try {
            int numberOfRows = jdbcTemplate.update(sql, faceUpdate.getAreaID(), faceUpdate.getFaceName(),
                    faceUpdate.getFaceDirection(), faceUpdate.getFaceNotes(), faceUpdate.getFaceID());
            //If 0 rows affected, the face with the given ID was not found.
            if (numberOfRows == 0) {
                return null;
            } else {
                updatedFace = getFaceById(faceUpdate.getFaceID());
            }
        } catch(CannotGetJdbcConnectionException e){
            throw new DaoException("ERROR: Unable to connect to server or database", e);                                //Throw 500:'DaoException' with message
        } catch (DataIntegrityViolationException e) {                                                                   //Catch 'DataIntegrityViolationException'
            throw new DaoException("Data integrity violation", e);                                                      //Throw 'DaoException' with message
        }
        return updatedFace;
    }
    @Override
    public int deleteFaceById(int faceId) {
        int numberOfRows = 0;
        String deleteRoutSql = "DELETE FROM routes WHERE face_id = ?;";                                                 //SQL string to delete 'face_id' from 'routes' table
        String deleteFaceSql = "DELETE FROM faces WHERE face_id = ?;";                                                  //SQL string to delete 'face_id' from 'faces table
        try {
            //Delete any foreign key references to author_id
            jdbcTemplate.update(deleteRoutSql, faceId);
            // now safe to delete the author
            numberOfRows = jdbcTemplate.update(deleteFaceSql, faceId);
        } catch(CannotGetJdbcConnectionException e){
            throw new DaoException("ERROR: Unable to connect to server or database", e);                                //Throw 500:'DaoException' with message
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);                                                      //Throw 'DaoException' with message
        }
        return numberOfRows;
    }
    //faces (face_id, area_id, face_name, face_direction, face_notes)
    //       face_id, area_id, face_name, face_direction, face_notes
    private Face mapRowToFace(SqlRowSet rs) {
        Face face = new Face();
        face.setFaceID(rs.getInt("face_id"));
        face.setAreaID(rs.getInt("area_id"));
        face.setFaceName(rs.getString("face_name"));
        face.setFaceDirection(rs.getString("face_direction"));
        face.setFaceNotes(rs.getString("face_notes"));
        return face;
    }
}
