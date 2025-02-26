package com.solvd.algoritms;

import com.solvd.utils.BusGraph;
import com.solvd.utils.BusVertex;
import com.solvd.utils.GraphManager;

import java.util.ArrayList;
import java.util.List;

public class FloydWarshall {
    private static final double INF = Double.MAX_VALUE;

    /**
     * Implements the Floyd-Warshall algorithm to find the shortest paths between all pairs of vertices.
     * Updates the graph matrix with shortest distances and the next matrix for path reconstruction.
     *
     * @param graph The adjacency matrix representing the graph.
     * @param next  The matrix to store the next vertex in the shortest path.
     */
    public static void floydWarshall(double[][] graph, int[][] next) {
        int n = graph.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                next[i][j] = (graph[i][j] != INF && i != j) ? j : -1;
            }
        }
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (graph[i][k] != INF && graph[k][j] != INF && graph[i][k] + graph[k][j] < graph[i][j]) {
                        graph[i][j] = graph[i][k] + graph[k][j];
                        next[i][j] = next[i][k];
                    }
                }
            }
        }
    }

    /**
     * Reconstructs the shortest path from startIdx to endIdx using the next matrix.
     *
     * @param startIdx The starting vertex index.
     * @param endIdx   The ending vertex index.
     * @param next     The next matrix from Floyd-Warshall.
     * @return A list of vertex indices representing the shortest path.
     */
    private static List<Integer> getPath(int startIdx, int endIdx, int[][] next) {
        List<Integer> path = new ArrayList<>();
        if (next[startIdx][endIdx] == -1) return path;
        path.add(startIdx);
        while (startIdx != endIdx) {
            startIdx = next[startIdx][endIdx];
            path.add(startIdx);
        }
        return path;
    }

    /**
     * Represents a route with station IDs and details (e.g., bus color or "BY CAR").
     */
    public static class Route {
        public List<Integer> stationIds;
        public List<String> details;

        public Route() {
            this.stationIds = new ArrayList<>();
            this.details = new ArrayList<>();
        }

        @Override
        public String toString() {
            if (stationIds.isEmpty()) {
                return "No path found.";
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Path from station ").append(stationIds.get(0))
                    .append(" to station ").append(stationIds.get(stationIds.size() - 1))
                    .append(":\n");
            for (int i = 0; i < stationIds.size(); i++) {
                sb.append("Station ").append(stationIds.get(i))
                        .append(": ").append(details.get(i)).append("\n");
            }
            return sb.toString();
        }
    }

    /**
     * Computes the navigation route from startStationId to endStationId using the specified mode.
     *
     * @param startStationId The starting station ID.
     * @param endStationId   The ending station ID.
     * @param mode           The mode of transportation ("CAR" or "BUS").
     * @return A Route object containing the path and details.
     */
    public static Route computeNavigation(int startStationId, int endStationId, String mode) {
        GraphManager gm = new GraphManager();
        Route route = new Route();

        if (mode.equalsIgnoreCase("BUS")) {
            BusGraph bg = gm.buildBusGraph();
            double[][] graph = bg.getMatrix();
            int n = graph.length;
            int[][] next = new int[n][n];

            floydWarshall(graph, next);

            List<Integer> startVertices = bg.getStationToVertices().get(startStationId);
            List<Integer> endVertices = bg.getStationToVertices().get(endStationId);

            if (startVertices == null || endVertices == null) {
                return route;
            }

            double minDist = INF;
            int bestStartIdx = -1;
            int bestEndIdx = -1;
            int minTransfers = Integer.MAX_VALUE;
            int minStations = Integer.MAX_VALUE;

            System.out.println("Debugging BUS path options:");
            for (int startIdx : startVertices) {
                for (int endIdx : endVertices) {
                    if (graph[startIdx][endIdx] < INF) {
                        List<Integer> path = getPath(startIdx, endIdx, next);
                        int stationCount = countUniqueStations(path, bg.getVertices());
                        int transferCount = countTransfers(path, bg.getVertices());
                        System.out.printf("Path from %s to %s: stations=%d, transfers=%d, dist=%.2f\n",
                                bg.getVertices().get(startIdx).getLabel(), bg.getVertices().get(endIdx).getLabel(),
                                stationCount, transferCount, graph[startIdx][endIdx]);
                        if (transferCount < minTransfers || (transferCount == minTransfers && stationCount < minStations)
                                || (transferCount == minTransfers && stationCount == minStations && graph[startIdx][endIdx] < minDist)) {
                            minDist = graph[startIdx][endIdx];
                            bestStartIdx = startIdx;
                            bestEndIdx = endIdx;
                            minTransfers = transferCount;
                            minStations = stationCount;
                        }
                    }
                }
            }

            if (minDist == INF) {
                return route;
            }

            List<Integer> pathIndices = getPath(bestStartIdx, bestEndIdx, next);
            String currentColor = bg.getVertices().get(bestStartIdx).getLabel();
            route.stationIds.add(bg.getVertices().get(bestStartIdx).getStationId());
            route.details.add("BUS COLOR: " + currentColor);

            for (int i = 1; i < pathIndices.size(); i++) {
                BusVertex vertex = bg.getVertices().get(pathIndices.get(i));
                int stationId = vertex.getStationId();
                String label = vertex.getLabel();

                if (route.stationIds.get(route.stationIds.size() - 1) != stationId) {
                    route.stationIds.add(stationId);
                    if ("TRANSFER".equals(label)) {
                        route.details.add("Transfer from " + currentColor);
                    } else {
                        currentColor = label;
                        route.details.add("BUS COLOR: " + label);
                    }
                }
            }
            return route;
        } else {
            double[][] graph = gm.buildAdjacencyMatrix(mode);
            int startIdx = gm.indexOfStation(startStationId);
            int endIdx = gm.indexOfStation(endStationId);
            int n = graph.length;
            int[][] next = new int[n][n];

            floydWarshall(graph, next);

            if (graph[startIdx][endIdx] == INF) {
                return route;
            }

            List<Integer> pathIndices = getPath(startIdx, endIdx, next);
            for (int idx : pathIndices) {
                route.stationIds.add(gm.getStations().get(idx).getStationId());
                route.details.add("BY CAR");
            }
            return route;
        }
    }

    /**
     * Counts the number of unique stations in the path.
     *
     * @param path     The list of vertex indices in the path.
     * @param vertices The list of BusVertex objects.
     * @return The number of unique stations.
     */
    private static int countUniqueStations(List<Integer> path, List<BusVertex> vertices) {
        List<Integer> uniqueStations = new ArrayList<>();
        for (int idx : path) {
            int stationId = vertices.get(idx).getStationId();
            if (uniqueStations.isEmpty() || uniqueStations.get(uniqueStations.size() - 1) != stationId) {
                uniqueStations.add(stationId);
            }
        }
        return uniqueStations.size();
    }

    /**
     * Counts the number of transfers in the bus path.
     *
     * @param path     The list of vertex indices in the path.
     * @param vertices The list of BusVertex objects.
     * @return The number of transfers.
     */
    private static int countTransfers(List<Integer> path, List<BusVertex> vertices) {
        int transfers = 0;
        String currentColor = vertices.get(path.get(0)).getLabel();
        for (int i = 1; i < path.size(); i++) {
            String label = vertices.get(path.get(i)).getLabel();
            if (!"TRANSFER".equals(label) && !label.equals(currentColor)) {
                transfers++;
                currentColor = label;
            }
        }
        return transfers;
    }

    /**
     * Computes an alternative path from startStationId to endStationId using the specified mode.
     *
     * @param startStationId The starting station ID.
     * @param endStationId   The ending station ID.
     * @param mode           The mode of transportation ("CAR").
     * @return A Route object containing the alternative path and details.
     */
    public static Route getAlternativePath(int startStationId, int endStationId, String mode) {
        GraphManager gm = new GraphManager();
        double[][] originalGraph = gm.buildAdjacencyMatrix(mode);
        int n = originalGraph.length;

        double[][] graph = new double[n][n];
        for (int i = 0; i < n; i++) {
            System.arraycopy(originalGraph[i], 0, graph[i], 0, n);
        }
        int[][] next = new int[n][n];
        floydWarshall(graph, next);

        int startIndex = gm.indexOfStation(startStationId);
        int endIndex = gm.indexOfStation(endStationId);

        List<Integer> bestPathIndices = getPath(startIndex, endIndex, next);
        System.out.println("Best path indices: " + bestPathIndices);
        if (bestPathIndices.isEmpty()) {
            return new Route(); // Return an empty Route if no path found
        }

        double[][] modifiedGraph = new double[n][n];
        for (int i = 0; i < n; i++) {
            System.arraycopy(originalGraph[i], 0, modifiedGraph[i], 0, n);
        }

        for (int i = 0; i < bestPathIndices.size() - 1; i++) {
            int u = bestPathIndices.get(i);
            int v = bestPathIndices.get(i + 1);
            modifiedGraph[u][v] = INF;
            if (modifiedGraph[v][u] != INF) {
                modifiedGraph[v][u] = INF;
            }
        }

        next = new int[n][n];
        floydWarshall(modifiedGraph, next);
        List<Integer> altPathIndices = getPath(startIndex, endIndex, next);
        System.out.println("Alternative path indices: " + altPathIndices);

        Route altRoute = new Route();
        if (!altPathIndices.isEmpty() && altPathIndices.get(0) == startIndex &&
                altPathIndices.get(altPathIndices.size() - 1) == endIndex) {
            for (int idx : altPathIndices) {
                altRoute.stationIds.add(gm.getStations().get(idx).getStationId());
                altRoute.details.add("BY CAR");
            }
        }
        return altRoute;
    }

    public static void main(String[] args) {
        Route pathCar = computeNavigation(7, 3, "CAR");
        System.out.println(pathCar);

        Route altPathCar = getAlternativePath(1, 7, "CAR");
        System.out.println(altPathCar);

        Route pathBus = computeNavigation(7, 3, "BUS");
        System.out.println(pathBus);
    }
}