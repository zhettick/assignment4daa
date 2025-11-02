package org.sabulla.graph.dagsp;

import org.sabulla.Metrics;
import org.sabulla.graph.Edge;
import org.sabulla.graph.Graph;

import java.util.*;

/**
 * The DAG class provides algorithms for computing the shortest and longest paths in a Directed Acyclic Graph (DAG).
 * <p>
 * It uses a topological order (passed externally or computed via Kahn) to ensure that all vertices are processed only after their predecessors.
 * <p>
 * The class also collects performance metrics using {@link Metrics}.
 */

public class DAG {
    private final Graph dag;
    private final Metrics metrics = new Metrics();

    public DAG(Graph dag) {
        this.dag = dag;
    }

    public void shortestPath(int source, List<Integer> order) {
        metrics.start();
        int n = dag.getVertices();
        int[] dist = new int[n];
        int[] parent = new int[n];
        for (int i = 0; i < n; i++) {
            dist[i] = Integer.MAX_VALUE;
            parent[i] = -1;
        }
        dist[source] = 0;

        // Relax edges in topological order
        for (int v : order) {
            if (dist[v] != Integer.MAX_VALUE) {
                for (Edge e : dag.getAdjacency().get(v)) {
                    metrics.addOperations("Relaxations");
                    if (dist[e.getTo()] > dist[v] + e.getWeight()) {
                        dist[e.getTo()] = dist[v] + e.getWeight();
                        parent[e.getTo()] = v;
                    }
                }
            }
        }

        System.out.println("\n ---Shortest---");
        metrics.stop();
        metrics.print("DAG Shortest Path");
        System.out.println("Shortest distances from " + source + ":");
        for (int i = 0; i < n; i++) {
            if (dist[i] == Integer.MAX_VALUE) {
                System.out.println(i + " → unreachable");
            } else {
                System.out.println(i + " → " + dist[i]);
            }
        }

        int end = findMin(dist, source);
        if (end != -1) {
            printPath(parent, end, "Shortest path");
        }
    }

    public void longestPath(int source, List<Integer> order) {
        metrics.start();
        int n = dag.getVertices();
        int[] dist = new int[n], parent = new int[n];
        for (int i = 0; i < n; i++) {
            dist[i] = Integer.MIN_VALUE;
            parent[i] = -1;
        }
        dist[source] = 0;

        // Relax edges in topological order
        for (int v : order) {
            if (dist[v] != Integer.MIN_VALUE) {
                for (Edge e : dag.getAdjacency().get(v)) {
                    metrics.addOperations("Relaxations");
                    if (dist[e.getTo()] < dist[v] + e.getWeight()) {
                        dist[e.getTo()] = dist[v] + e.getWeight();
                        parent[e.getTo()] = v;
                    }
                }
            }
        }

        int end = findMax(dist);
        System.out.println("\n ---Longest---");
        metrics.stop();
        metrics.print("DAG Longest Path");
        System.out.println("Longest path length: " + dist[end]);
        printPath(parent, end, "Longest path");
    }

    private int findMin(int[] dist, int source) {
        int best = Integer.MAX_VALUE, v = -1;
        for (int i = 0; i < dist.length; i++) {
            if (i != source && dist[i] < best) {
                best = dist[i];
                v = i;
            }
        }
        return v;
    }

    private int findMax(int[] dist) {
        int max = Integer.MIN_VALUE, v = -1;
        for (int i = 0; i < dist.length; i++) {
            if (dist[i] > max) {
                max = dist[i];
                v = i;
            }
        }
        return v;
    }

    private void printPath(int[] parent, int end, String label) {
        List<Integer> path = new ArrayList<>();
        for (int v = end; v != -1; v = parent[v]) {
            path.add(v);
        }
        Collections.reverse(path);
        System.out.println(label + ": " + path);
    }
}