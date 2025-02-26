package com.solvd.utils;

public class BusVertex {
    private int stationId;
    private String label;

    public BusVertex(int stationId, String label) {
        this.stationId = stationId;
        this.label = label;
    }

    // Getter methods
    public int getStationId() {
        return stationId;
    }

    public String getLabel() {
        return label;
    }


    public String toKey() {
        return stationId + "_" + label;
    }
}