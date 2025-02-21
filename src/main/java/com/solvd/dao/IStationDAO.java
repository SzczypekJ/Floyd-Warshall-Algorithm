package com.solvd.dao;

import com.solvd.models.Station;

import java.util.List;

public interface IStationDAO extends IBaseDao<Station> {
    @Override
    Station getEntityById(int id);

    @Override
    List<Station> getEntities();

    @Override
    void insert(Station station);

    @Override
    void update(int id, Station station);

    @Override
    void delete(int id);
}
