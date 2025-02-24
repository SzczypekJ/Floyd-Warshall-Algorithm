package com.solvd.utils;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The EuclideanDistance class calculates the shortest distance between locations
 * using the Euclidean distance formula. It stores locations in a linked map and
 * populates an adjacency matrix dynamically.
 */
public class EuclideanDistance {

    private static final int INF = -1; // Represents no direct connection
    private static Map<String, double[]> locations = new LinkedHashMap<>(); // Stores location coordinates
    private static String[] stationNames = {"A", "B", "C", "D", "E", "F", "G"}; // Station names
    private static int n = stationNames.length; // Number of stations
    private static double[][] adjacencyMatrix = new double[n][n]; // Adjacency matrix

    /**
     * Adds predefined locations to the location map.
     * Initializes the adjacency matrix with INF values.
     */
    public static void addStaticLocations() {
        for (int i = 0; i < n; i++) {
            Arrays.fill(adjacencyMatrix[i], INF);
        }
        locations.put("A", new double[]{-4, 2});
        locations.put("B", new double[]{-2, 4});
        locations.put("C", new double[]{2, 4});
        locations.put("D", new double[]{0, 2});
        locations.put("E", new double[]{2, -2});
        locations.put("F", new double[]{-2, -6});
        locations.put("G", new double[]{-8, -4});
    }

    /**
     * Adds a new location to the location map.
     * @param locationName The name of the location.
     * @param location The x and y coordinates of the location.
     */
    public static void addLocation(String locationName, double[] location) {
        locations.put(locationName, location);
    }

    /**
     * Populates the adjacency matrix with valid edges using Euclidean distance.
     */
    public static void populateEuclidean() {
        // Define valid connections between locations
        setEdge("A", "B");
        setEdge("A", "D");
        setEdge("B", "C");
        setEdge("B", "D");
        setEdge("C", "E");
        setEdge("C", "D");
        setEdge("D", "E");
        setEdge("D", "F");
        setEdge("D", "G");
        setEdge("E", "F");
        setEdge("F", "G");
    }

    /**
     * Sets an edge between two locations and calculates their Euclidean distance.
     * @param from The starting location.
     * @param to The destination location.
     */
    private static void setEdge(String from, String to) {
        int fromIndex = Arrays.asList(stationNames).indexOf(from);
        int toIndex = Arrays.asList(stationNames).indexOf(to);

        double distance = euclideanDistance(locations.get(from), locations.get(to));

        adjacencyMatrix[fromIndex][toIndex] = distance;
        adjacencyMatrix[toIndex][fromIndex] = distance; // Since it's an undirected graph
    }

    /**
     * Computes the Euclidean distance between two points.
     * Formula: d = sqrt((x1 - x2)^2 + (y1 - y2)^2)
     * @param loc1 The first location's coordinates.
     * @param loc2 The second location's coordinates.
     * @return The Euclidean distance between loc1 and loc2.
     */
    public static double euclideanDistance(double[] loc1, double[] loc2) {
        return Math.sqrt(Math.pow(loc1[0] - loc2[0], 2) + Math.pow(loc1[1] - loc2[1], 2));
    }

    /**
     * Prints the adjacency matrix representing Euclidean distances between locations.
     */
    private static void printMatrix() {
        System.out.println("\nAdjacency Matrix (Euclidean Distances):");
        System.out.print("     ");
        for (String name : stationNames) {
            System.out.printf("%8s", name);
        }
        System.out.println();

        for (int i = 0; i < n; i++) {
            System.out.printf("%-5s", stationNames[i]); // Left-aligned station name
            for (int j = 0; j < n; j++) {
                if (adjacencyMatrix[i][j] == INF)
                    System.out.print("      -1");
                else
                    System.out.printf("%8.2f", adjacencyMatrix[i][j]);
            }
            System.out.println();
        }
    }
}
