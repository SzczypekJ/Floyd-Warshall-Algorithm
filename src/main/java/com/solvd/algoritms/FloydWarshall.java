package com.solvd.algoritms;

import com.solvd.models.Station;
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
        public String startName;
        public String endName;

        public Route() {
            this.stationIds = new ArrayList<>();
            this.details = new ArrayList<>();
            this.startName = "";
            this.endName = "";
        }

        @Override
        public String toString() {
            if (stationIds.isEmpty()) {
                return "No path found.";
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Shortest path from station ")
                    .append(startName)
                    .append(" to station ")
                    .append(endName)
                    .append(":\nRoute: ");
            for (int i = 0; i < details.size(); i++) {
                sb.append(details.get(i));
                if (i < details.size() - 1) {
                    sb.append(" -> ");
                }
            }
            sb.append("\n");
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

//            System.out.println("Debugging BUS path options:");
            for (int sIdx : startVertices) {
                for (int eIdx : endVertices) {
                    if (graph[sIdx][eIdx] < INF) {
                        List<Integer> path = getPath(sIdx, eIdx, next);
                        int stationCount = countUniqueStations(path, bg.getVertices());
                        int transferCount = countTransfers(path, bg.getVertices());
                        double dist = graph[sIdx][eIdx];
//                        System.out.printf("Path from %s to %s: stations=%d, transfers=%d, dist=%.2f\n",
//                                bg.getVertices().get(sIdx).getLabel(),
//                                bg.getVertices().get(eIdx).getLabel(),
//                                stationCount, transferCount, dist);

                        if (transferCount < minTransfers
                                || (transferCount == minTransfers && stationCount < minStations)
                                || (transferCount == minTransfers && stationCount == minStations && dist < minDist)) {
                            minDist      = dist;
                            bestStartIdx = sIdx;
                            bestEndIdx   = eIdx;
                            minTransfers = transferCount;
                            minStations  = stationCount;
                        }
                    }
                }
            }

            if (minDist == INF) {
                return route;
            }

            // Reconstruct actual path
            List<Integer> pathIndices = getPath(bestStartIdx, bestEndIdx, next);
            String currentColor = bg.getVertices().get(bestStartIdx).getLabel();
            route.stationIds.add(bg.getVertices().get(bestStartIdx).getStationId());

            // keep track of the first and last station IDs
            int firstStId = bg.getVertices().get(bestStartIdx).getStationId();
            int lastStId  = firstStId;

            // get a name for the first station
            String firstStationName = findStationNameById(firstStId, gm.getStations());

            // minimal fix #1: store the station name in a variable so we can reference it
            // if the color changes at the *previous* station
            String lastStationName  = firstStationName;

            // initial line: "At station StationD get into BUS COLOR: GREEN"
            route.details.add("At station " + lastStationName + " get into BUS COLOR: " + currentColor);

            // Walk the path, skipping repeated color if label is the same
            for (int i = 1; i < pathIndices.size(); i++) {
                BusVertex vertex = bg.getVertices().get(pathIndices.get(i));
                int stationId = vertex.getStationId();
                String stationName = findStationNameById(stationId, gm.getStations());
                lastStId = stationId;
                String label = vertex.getLabel();

                // if new station
                if (route.stationIds.get(route.stationIds.size() - 1) != stationId) {
                    route.stationIds.add(stationId);
                }

                if ("TRANSFER".equals(label)) {
                    // "Transfer from GREEN"
                    route.details.add("Transfer from " + currentColor);
                } else {
                    // minimal fix #2: if label != currentColor, we say
                    // "At station <lastStationName> get into BUS COLOR: <label>"
                    if (!label.equals(currentColor)) {
                        currentColor = label;
                        // we do the color switch at the station we just arrived at
                        route.details.add("At station " + stationName + " get into BUS COLOR: " + currentColor);

                        // or if you want the color switch to be labeled at the PREVIOUS station:
                        // route.details.add("At station " + lastStationName + " get into BUS COLOR: " + currentColor);
                        // But that depends on the exact route logic.
                    }
                }
                // minimal fix #3: update lastStationName to the station we arrived at
                lastStationName = stationName;
            }

            // fill in startName, endName
            route.startName = firstStationName;
            route.endName   = findStationNameById(lastStId, gm.getStations());

            // final line: "ride until station B"
            String lastStName = findStationNameById(lastStId, gm.getStations());
            route.details.add("ride until " + lastStName);

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
                String stationName = gm.getStations().get(idx).getName();
                route.details.add(stationName + " (BY CAR)");
            }
            if (!route.stationIds.isEmpty()) {
                route.startName = gm.getStations().get(pathIndices.get(0)).getName();
                route.endName = gm.getStations().get(pathIndices.get(pathIndices.size() - 1)).getName();
            }
            return route;
        }
    }

    /**
     * Minimal helper to map stationId -> stationName by looping gm.getStations().
     * Comment: We do not have a gm.findStationNameById method, so let's do it here.
     */
    private static String findStationNameById(int stationId, List<Station> stationList) {
        for (Station st : stationList) {
            if (st.getStationId() == stationId) {
                return st.getName();
            }
        }
        return String.valueOf(stationId);
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
                String stationName = gm.getStations().get(idx).getName();
                altRoute.details.add(stationName + " (BY CAR)");
            }

            if (!altRoute.stationIds.isEmpty()) {
                altRoute.startName = gm.getStations().get(altPathIndices.get(0)).getName();
                altRoute.endName = gm.getStations().get(altPathIndices.get(altPathIndices.size() - 1)).getName();
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