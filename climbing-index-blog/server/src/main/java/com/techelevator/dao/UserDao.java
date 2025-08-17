package com.techelevator.dao;

import com.techelevator.model.Area;
import com.techelevator.model.User;
import java.util.List;

public interface UserDao {

    List<User> getUsers();

    User getUserById(int userId);

    User getUserByUsername(String username);

    User createUser(User newUser);

    //TODO: Update the 'role' of users
    //Assign other users to be administrators
    User updateUserRole(User userRoleUpdate);

    int deleteUserById(int userId);
}
