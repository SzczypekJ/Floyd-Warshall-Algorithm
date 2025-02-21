package com.solvd.dao.implementation;

import com.solvd.dao.IRoadDAO;
import com.solvd.mapper.RoadMapper;
import com.solvd.models.Road;
import org.apache.ibatis.session.SqlSession;
import com.solvd.config.MyBatisConf;
import java.util.List;

public class RoadDAOImpl implements IRoadDAO {
    @Override
    public Road getEntityById(int id) {
        try (SqlSession session = MyBatisConf.getSqlSessionFactory().openSession()) {
            RoadMapper mapper = session.getMapper(RoadMapper.class);
            return mapper.getRoadById(id);
        }
    }

    @Override
    public List<Road> getEntities() {
        try (SqlSession session = MyBatisConf.getSqlSessionFactory().openSession()) {
            RoadMapper mapper = session.getMapper(RoadMapper.class);
            return mapper.getAllRoads();
        }
    }

    @Override
    public void insert(Road road) {
        try (SqlSession session = MyBatisConf.getSqlSessionFactory().openSession()) {
            RoadMapper mapper = session.getMapper(RoadMapper.class);
            mapper.insertRoad(road);
            session.commit();
        }
    }

    @Override
    public void update(int id, Road road) {
        road.setRoadId(id);
        try (SqlSession session = MyBatisConf.getSqlSessionFactory().openSession()) {
            RoadMapper mapper = session.getMapper(RoadMapper.class);
            mapper.updateRoad(road);
            session.commit();
        }
    }

    @Override
    public void delete(int id) {
        try (SqlSession session = MyBatisConf.getSqlSessionFactory().openSession()) {
            RoadMapper mapper = session.getMapper(RoadMapper.class);
            mapper.deleteRoad(id);
            session.commit();
        }
    }

    @Override
    public List<Road> getRoadsByMode(String mode) {
        try (SqlSession session = MyBatisConf.getSqlSessionFactory().openSession()) {
            RoadMapper mapper = session.getMapper(RoadMapper.class);
            return mapper.getRoadsByMode(mode);
        }
    }
}
