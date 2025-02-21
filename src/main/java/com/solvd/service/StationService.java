package com.solvd.service;

import com.solvd.models.Station;

import java.util.List;

public interface StationService {
    List<Station> getAllStations();
    Station getStationById(int id);
    void createStation(Station station);
    void updateStation(int id, Station station);
    void deleteStation(int id);
}
