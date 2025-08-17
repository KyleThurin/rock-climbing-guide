package com.techelevator.dao;

import com.techelevator.model.Area;

import java.util.List;

public interface AreaDao {
    List<Area> listOfAreas();

    Area getAreaById(int areaId);

    //TODO: DO I still need to keep 'getAreaByName'?
    Area getAreaByName(String areaName);

    Area createArea(Area newArea);

    Area updateArea(Area updatedArea);

    int deleteAreaById(int areaId);
}
