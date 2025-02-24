package com.solvd.algoritms;

import java.util.ArrayList;
import java.util.List;

public class FloydWarshall {


    public static void floydWarshall(int[][] graph, int[][] next) {
        int V = graph.length;

        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                if (graph[i][j] != -1 && i != j) {
                    next[i][j] = j;
                } else {
                    next[i][j] = -1;
                }
            }
        }

        for (int k = 0; k < V; k++) {
            for (int i = 0; i < V; i++) {
                for (int j = 0; j < V; j++) {
                    if (graph[i][k] == -1 || graph[k][j] == -1) {
                        continue;
                    }
                    if (graph[i][j] == -1 || graph[i][j] > graph[i][k] + graph[k][j]) {
                        graph[i][j] = graph[i][k] + graph[k][j];
                        next[i][j] = next[i][k];
                    }
                }
            }
        }
    }


    public static List<Integer> getPath(int start, int end, int[][] next) {
        List<Integer> path = new ArrayList<>();
        if (next[start][end] == -1) {
            return path;
        }
        path.add(start);
        while (start != end) {
            start = next[start][end];
            path.add(start);
        }
        return path;
    }

    public static void main(String[] args) {

        int[][] graph = {
                {0, 4, -1, 5, -1},
                {-1, 0, 1, -1, 6},
                {2, -1, 0, 3, -1},
                {-1, -1, 1, 0, 2},
                {1, -1, -1, 4, 0}
        };

        int V = graph.length;
        int[][] next = new int[V][V];

        floydWarshall(graph, next);

        System.out.println("Shortest distance matrix:");
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                System.out.print(graph[i][j] + "\t");
            }
            System.out.println();
        }

        int start = 3;
        int end = 4;
        List<Integer> path = getPath(start, end, next);
        if (path.isEmpty()) {
            System.out.println("No path exists between " + start + " and " + end + ".");
        } else {
            System.out.println("The shortest path from " + start + " to " + end + " is: " + path);
        }
    }
}
