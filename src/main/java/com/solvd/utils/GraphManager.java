package com.solvd.utils;

import com.solvd.models.Road;
import com.solvd.models.Station;
import com.solvd.service.RoadService;
import com.solvd.service.RoadServiceImpl;
import com.solvd.service.StationService;
import com.solvd.service.StationServiceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The {@code GraphManager} class is responsible for constructing an adjacency matrix
 * representing the station network using data fetched from a database.
 * It supports different travel modes (CAR, BUS) and calculates distances using the Euclidean formula.
 */
public class GraphManager {

    private List<Station> stations;
    private List<Road> roads;

    private final StationService stationService;
    private final RoadService roadService;

    /**
     * Constructs a {@code GraphManager} instance and initializes service dependencies.
     */
    public GraphManager() {
        this.stations = new ArrayList<>();
        this.roads = new ArrayList<>();
        this.stationService = new StationServiceImpl();
        this.roadService = new RoadServiceImpl();
    }

    /**
     * Fetches the latest list of stations and roads from the database and updates the local lists.
     */
    public void fetchDataFromDatabase() {
        stations = stationService.getAllStations(); // Ensure we fetch fresh data
        roads = roadService.getAllRoads();
    }

    /**
     * Builds an adjacency matrix representing distances between connected stations.
     *
     * @param mode The transport mode ("CAR" or "BUS") for which to build the matrix.
     * @return A 2D array where matrix[i][j] represents the distance from station i to station j.
     *         If no direct road exists, the value is set to {@code Double.MAX_VALUE}.
     */
    public double[][] buildAdjacencyMatrix(String mode) {
        fetchDataFromDatabase(); // Always fetch updated data
        int numStations = stations.size();
        double[][] matrix = new double[numStations][numStations];

        // Initialize matrix with large values (no connection)
        for (double[] row : matrix) {
            Arrays.fill(row, Double.MAX_VALUE);
        }

        // Set diagonal to zero (distance from a station to itself)
        for (int i = 0; i < numStations; i++) {
            matrix[i][i] = 0.0;
        }

        // Populate matrix using roads
        for (Road road : roads) {
            if (road.getMode().equalsIgnoreCase(mode)) { // Match mode (CAR/BUS)
                int fromIndex = indexOfStation(road.getFromStationId());
                int toIndex = indexOfStation(road.getToStationId());

                if (fromIndex != -1 && toIndex != -1) {
                    double distance = EuclideanDistance.euclideanDistance(
                            new double[]{stations.get(fromIndex).getXCoord(), stations.get(fromIndex).getYCoord()},
                            new double[]{stations.get(toIndex).getXCoord(), stations.get(toIndex).getYCoord()}
                    );

                    matrix[fromIndex][toIndex] = distance;
                    if (!road.isOneWay()) {
                        matrix[toIndex][fromIndex] = distance; // Two-way road
                    }
//                    printDistances(fromIndex,toIndex,distance);
                }
            }
        }
        return matrix;
    }

    /**
     * Finds the index of a station in the stored list by its station ID.
     *
     * @param stationId The ID of the station to find.
     * @return The index of the station in the list, or -1 if not found.
     */
    private int indexOfStation(int stationId) {
        for (int i = 0; i < stations.size(); i++) {
            if (stations.get(i).getStationId() == stationId) {
                return i;
            }
        }
        return -1; // Not found
    }

    /**
     * Prints the calculated distance between two stations.
     *
     * @param fromIndex The index of the starting station.
     * @param toIndex   The index of the destination station.
     * @param distance  The calculated distance between the two stations.
     */
    public void printDistances(int fromIndex, int toIndex, double distance) {
        System.out.printf("Distance between %s and %s: %.2f%n",
                stations.get(fromIndex).getName(),
                stations.get(toIndex).getName(), distance);
    }
}
