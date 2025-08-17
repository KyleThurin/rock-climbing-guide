package com.techelevator.dao;

import com.techelevator.model.Area;
import com.techelevator.model.Face;
import com.techelevator.model.UserLog;

import java.util.List;

public interface UserLogDao {
    List<UserLog> getUserLogs();

    //TODO: Fix this (is it getting a single log, or all the logs for someone specific?)
    List<UserLog> getUserLogsByUsername(String username);

    UserLog getUserLogById(int userId);
    //TODO: How to specify what area/face/route the user log is for
    UserLog createUserLog(UserLog newUserLog);

    UserLog updateUserLog(UserLog userLogUpdate);

    int deleteUserLogById(int userLogId);
}
