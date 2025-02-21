package com.solvd.models;

import lombok.Data;

@Data
public class Road {
    private int roadId;
    private int fromStationId;
    private int toStationId;
    private String mode;       // 'CAR' or 'BUS'
    private boolean isOneWay;
    private String busColor;   // Only used if mode = 'BUS'
}