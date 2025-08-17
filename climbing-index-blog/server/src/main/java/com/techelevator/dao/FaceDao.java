package com.techelevator.dao;

import com.techelevator.model.Face;
import com.techelevator.model.Route;

import java.util.List;

public interface FaceDao {
    List<Face> listOfFaces();

    Face getFaceById(int faceId);

    //TODO: Add wildcard???
    Face getFaceByName(String faceName);

    List<Face> getFacesByAreaID(int areaId);

    Face createFace(Face newFace);
    Face updateFace(Face updatedFace);

    int deleteFaceById(int faceId);
}
