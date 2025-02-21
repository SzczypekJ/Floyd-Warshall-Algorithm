package com.solvd.service;

import com.solvd.dao.IStationDAO;
import com.solvd.dao.implementation.StationDAOImpl;
import com.solvd.models.Station;

import java.util.List;

public class StationServiceImpl implements StationService {

    private final IStationDAO stationDAO;

    public StationServiceImpl() {
        this.stationDAO = new StationDAOImpl();
    }

    @Override
    public List<Station> getAllStations() {
        return stationDAO.getEntities();
    }

    @Override
    public Station getStationById(int id) {
        return stationDAO.getEntityById(id);
    }

    @Override
    public void createStation(Station station) {
        stationDAO.insert(station);
    }

    @Override
    public void updateStation(int id, Station station) {
        stationDAO.update(id, station);
    }

    @Override
    public void deleteStation(int id) {
        stationDAO.delete(id);
    }
}
