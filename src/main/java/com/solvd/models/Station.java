package com.solvd.models;

import lombok.Data;

@Data
public class Station {
    private int stationId;
    private String name;
    private double xCoord;
    private double yCoord;
}
