package com.solvd.utils;

import java.util.List;
import java.util.Map;

public class BusGraph {
    private double[][] matrix;
    private List<BusVertex> vertices;
    private Map<String, Integer> vertexToIndex;
    private Map<Integer, List<Integer>> stationToVertices;

    // Constructor (to initialize fields, assuming they are set elsewhere like in GraphManager)
    public BusGraph() {
        // Fields will be set via setters or directly by GraphManager since it's in the same package
    }

    // Getter methods
    public double[][] getMatrix() {
        return matrix;
    }

    public List<BusVertex> getVertices() {
        return vertices;
    }

    public Map<String, Integer> getVertexToIndex() {
        return vertexToIndex;
    }

    public Map<Integer, List<Integer>> getStationToVertices() {
        return stationToVertices;
    }

    // Setter methods (optional, depending on design; included for flexibility)
    public void setMatrix(double[][] matrix) {
        this.matrix = matrix;
    }

    public void setVertices(List<BusVertex> vertices) {
        this.vertices = vertices;
    }

    public void setVertexToIndex(Map<String, Integer> vertexToIndex) {
        this.vertexToIndex = vertexToIndex;
    }

    public void setStationToVertices(Map<Integer, List<Integer>> stationToVertices) {
        this.stationToVertices = stationToVertices;
    }
}