package com.techelevator.dao;

import com.techelevator.model.Route;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

public interface RouteDao {
    List<Route> listOfRoutes();

    Route getRouteById(int routeId);

    //Create a list of the climbing faces for that area
    List<Route> getRoutesByFaceID(int faceId);


    //TODO: DO I still need to keep 'getRouteByName'?
    Route getRouteByName(String routeName);

    Route createRoute(Route newRoute);

    Route updateRoute(Route updatedRoute);

    int deleteRouteById(int routId);
}
