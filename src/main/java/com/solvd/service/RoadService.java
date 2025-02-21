package com.solvd.service;

import com.solvd.models.Road;
import java.util.List;

public interface RoadService {
    List<Road> getAllRoads();
    Road getRoadById(int id);
    List<Road> getRoadsByMode(String mode);
    void createRoad(Road road);
    void updateRoad(int id, Road road);
    void deleteRoad(int id);
}
