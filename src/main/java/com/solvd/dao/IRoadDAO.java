package com.solvd.dao;

import com.solvd.models.Road;

import java.util.List;

public interface IRoadDAO extends IBaseDao<Road>{
    @Override
    Road getEntityById(int id);

    @Override
    List<Road> getEntities();

    @Override
    void insert(Road road);

    @Override
    void update(int id, Road road);

    @Override
    void delete(int id);


    List<Road> getRoadsByMode(String mode);
}
