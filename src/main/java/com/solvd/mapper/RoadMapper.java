package com.solvd.mapper;

import com.solvd.models.Road;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface RoadMapper {

    @Select("SELECT road_id, from_station_id, to_station_id, mode, is_one_way, bus_color FROM roads")
    @Results({
            @Result(property = "roadId",        column = "road_id"),
            @Result(property = "fromStationId", column = "from_station_id"),
            @Result(property = "toStationId",   column = "to_station_id"),
            @Result(property = "mode",          column = "mode"),
            @Result(property = "oneWay",        column = "is_one_way"),
            @Result(property = "busColor",      column = "bus_color")
    })
    List<Road> getAllRoads();

    @Select("SELECT road_id, from_station_id, to_station_id, mode, is_one_way, bus_color FROM roads WHERE road_id = #{roadId}")
    @Results({
            @Result(property = "roadId",        column = "road_id"),
            @Result(property = "fromStationId", column = "from_station_id"),
            @Result(property = "toStationId",   column = "to_station_id"),
            @Result(property = "mode",          column = "mode"),
            @Result(property = "oneWay",        column = "is_one_way"),
            @Result(property = "busColor",      column = "bus_color")
    })
    Road getRoadById(@Param("roadId") int roadId);

    @Select("SELECT road_id, from_station_id, to_station_id, mode, is_one_way, bus_color FROM roads WHERE mode = #{mode}")
    @Results({
            @Result(property = "roadId",        column = "road_id"),
            @Result(property = "fromStationId", column = "from_station_id"),
            @Result(property = "toStationId",   column = "to_station_id"),
            @Result(property = "mode",          column = "mode"),
            @Result(property = "oneWay",        column = "is_one_way"),
            @Result(property = "busColor",      column = "bus_color")
    })
    List<Road> getRoadsByMode(@Param("mode") String mode);

    @Insert("INSERT INTO roads (from_station_id, to_station_id, mode, is_one_way, bus_color) " +
            "VALUES (#{fromStationId}, #{toStationId}, #{mode}, #{isOneWay}, #{busColor})")
    @Options(useGeneratedKeys = true, keyProperty = "roadId")
    void insertRoad(Road road);

    @Update("UPDATE roads SET from_station_id = #{fromStationId}, to_station_id = #{toStationId}, " +
            "mode = #{mode}, is_one_way = #{isOneWay}, bus_color = #{busColor} WHERE road_id = #{roadId}")
    void updateRoad(Road road);

    @Delete("DELETE FROM roads WHERE road_id = #{roadId}")
    void deleteRoad(@Param("roadId") int roadId);
}
