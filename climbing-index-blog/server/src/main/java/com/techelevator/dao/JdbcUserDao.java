package com.techelevator.dao;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.core.JdbcTemplate;
import com.techelevator.exception.DaoException;
import org.springframework.stereotype.Component;
import com.techelevator.model.User;

import java.util.ArrayList;
import java.util.List;


@Component
public class JdbcUserDao implements UserDao {
    private final JdbcTemplate jdbcTemplate;
    public JdbcUserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public User getUserById(int userId) {

        User user = null;
        String sql = "SELECT * FROM users WHERE user_id = ?";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
            if (results.next()) {
                user = mapRowToUser(results);
            }
        }
        catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return user;
    }

    @Override
    public List<User> getUsers() {

        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY username";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            while (results.next()) {
                User user = mapRowToUser(results);
                users.add(user);
            }
        }
        catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return users;
    }

    @Override
    //TODO: This method should retrieve the hashed password from the database, which will then be used by Spring Security for comparison during authentication
    public User getUserByUsername(String username) {

        if (username == null) {
            username = "";
        }
        User user = null;
        String sql = "SELECT * FROM users WHERE username = ?";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, username);
            if (results.next()) {
                user = mapRowToUser(results);
            }
        }
        catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return user;
    }

    @Override
    public User createUser(User newUser) {

        User user = null;
        String insertUserSql = "INSERT INTO users " +
                "(username, password_hash, role) " +
                "VALUES (?, ?, ?) " +
                "RETURNING user_id";

        if (newUser.getHashedPassword() == null) {
            throw new DaoException("User cannot be created with null password");
        }
        try {
            String passwordHash = new BCryptPasswordEncoder().encode(newUser.getHashedPassword());

            Integer userId = jdbcTemplate.queryForObject(insertUserSql, int.class,
                    newUser.getUsername(), passwordHash, newUser.getRole());
            user =  getUserById(userId);
        }
        catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return user;
    }

    //Assign other users to be administrators
    @Override
    public User updateUserRole(User userRoleUpdate) {
        User updatedUser  = null;                                                                                       //Create local variable and set to 'null'
        //users (user_id, username, password_hash, role)
        String sql = "UPDATE users SET role = ? WHERE user_id = ?;";                                                                                   //Set SQL string
        try {
            int numberOfRows = jdbcTemplate.update(sql, userRoleUpdate.getRole(), userRoleUpdate.getId());
            if (numberOfRows == 0) {                                                                                    //If 'numberOfRows' equals zero
                return null;
            } else {                                                                                                    //If 'numberOfRows' does NOT equals zero
                updatedUser = getUserById(userRoleUpdate.getId());                                                      //Retrieve the updated area to get any updated fields
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);                                       //Throw 'DaoException' with message
        } catch (DataIntegrityViolationException e) {                                                                   //Catch 'DataIntegrityViolationException'
            throw new DaoException("Data integrity violation", e);                                                      //Throw 'DaoException' with message
        }
        return updatedUser;
    }
    @Override
    public int deleteUserById(int userId) {
        int numberOfRows = 0;
        String deleteUserLogSql = "DELETE FROM user_log WHERE username = (SELECT username FROM users WHERE user_id = ?);"; // Delete related user logs first
        String deleteUserSql = "DELETE FROM users WHERE user_id = ?;";
        try {
            jdbcTemplate.update(deleteUserLogSql, userId); // Delete associated user logs
            numberOfRows = jdbcTemplate.update(deleteUserSql, userId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return numberOfRows;
    }
    private User mapRowToUser(SqlRowSet rs) {
        User user = new User();
        user.setId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setHashedPassword(rs.getString("password_hash"));
        user.setRole(rs.getString("role"));
        return user;
    }
}
