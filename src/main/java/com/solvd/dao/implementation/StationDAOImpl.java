package com.solvd.dao.implementation;

import com.solvd.dao.IStationDAO;
import com.solvd.mapper.StationMapper;
import com.solvd.models.Station;
import org.apache.ibatis.session.SqlSession;
import com.solvd.config.MyBatisConf;

import java.util.List;

public class StationDAOImpl implements IStationDAO {
    @Override
    public Station getEntityById(int id) {
        try (SqlSession session = MyBatisConf.getSqlSessionFactory().openSession()) {
            StationMapper mapper = session.getMapper(StationMapper.class);
            return mapper.getStationById(id);
        }
    }

    @Override
    public List<Station> getEntities() {
        try (SqlSession session = MyBatisConf.getSqlSessionFactory().openSession()) {
            StationMapper mapper = session.getMapper(StationMapper.class);
            return mapper.getAllStations();
        }
    }

    @Override
    public void insert(Station station) {
        try (SqlSession session = MyBatisConf.getSqlSessionFactory().openSession()) {
            StationMapper mapper = session.getMapper(StationMapper.class);
            mapper.insertStation(station);
            session.commit();
        }
    }

    @Override
    public void update(int id, Station station) {
        // Ensure stationId is set, or pass 'id' if that's your logic
        station.setStationId(id);
        try (SqlSession session = MyBatisConf.getSqlSessionFactory().openSession()) {
            StationMapper mapper = session.getMapper(StationMapper.class);
            mapper.updateStation(station);
            session.commit();
        }
    }

    @Override
    public void delete(int id) {
        try (SqlSession session = MyBatisConf.getSqlSessionFactory().openSession()) {
            StationMapper mapper = session.getMapper(StationMapper.class);
            mapper.deleteStation(id);
            session.commit();
        }
    }
}
