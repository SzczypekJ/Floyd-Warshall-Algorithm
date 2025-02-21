package com.solvd.service;

import com.solvd.dao.IRoadDAO;
import com.solvd.dao.implementation.RoadDAOImpl;
import com.solvd.models.Road;
import java.util.List;

public class RoadServiceImpl implements RoadService {

    private final IRoadDAO roadDAO;

    public RoadServiceImpl() {
        this.roadDAO = new RoadDAOImpl();
    }

    @Override
    public List<Road> getAllRoads() {
        return roadDAO.getEntities();
    }

    @Override
    public Road getRoadById(int id) {
        return roadDAO.getEntityById(id);
    }

    @Override
    public List<Road> getRoadsByMode(String mode) {
        return roadDAO.getRoadsByMode(mode);
    }

    @Override
    public void createRoad(Road road) {
        roadDAO.insert(road);
    }

    @Override
    public void updateRoad(int id, Road road) {
        roadDAO.update(id, road);
    }

    @Override
    public void deleteRoad(int id) {
        roadDAO.delete(id);
    }
}
