package com.solvd.mapper;

import com.solvd.models.Station;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface StationMapper {

    @Select("SELECT station_id, name, x_coord, y_coord FROM stations")
    @Results({
            @Result(property = "stationId", column = "station_id"),
            @Result(property = "name",      column = "name"),
            @Result(property = "xCoord",    column = "x_coord"),
            @Result(property = "yCoord",    column = "y_coord")
    })
    List<Station> getAllStations();

    @Select("SELECT station_id, name, x_coord, y_coord FROM stations WHERE station_id = #{stationId}")
    @Results({
            @Result(property = "stationId", column = "station_id"),
            @Result(property = "name",      column = "name"),
            @Result(property = "xCoord",    column = "x_coord"),
            @Result(property = "yCoord",    column = "y_coord")
    })
    Station getStationById(@Param("stationId") int stationId);

    @Insert("INSERT INTO stations (name, x_coord, y_coord) VALUES (#{name}, #{xCoord}, #{yCoord})")
    @Options(useGeneratedKeys = true, keyProperty = "stationId")
    void insertStation(Station station);

    @Update("UPDATE stations SET name = #{name}, x_coord = #{xCoord}, y_coord = #{yCoord} WHERE station_id = #{stationId}")
    void updateStation(Station station);

    @Delete("DELETE FROM stations WHERE station_id = #{stationId}")
    void deleteStation(@Param("stationId") int stationId);
}
