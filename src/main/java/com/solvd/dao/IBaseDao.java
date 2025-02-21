package com.solvd.dao;

import java.util.List;

public interface IBaseDao<T> {

    T getEntityById(int id);

    List<T> getEntities();

    void insert(T t);

    void update(int id, T t);

    void delete(int id);
}
