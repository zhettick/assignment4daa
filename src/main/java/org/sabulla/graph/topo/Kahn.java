package org.sabulla.graph.topo;

import org.sabulla.Metrics;
import org.sabulla.graph.Edge;
import org.sabulla.graph.Graph;

import java.util.*;

/**
 * Implements Kahn's algorithm for topological sorting of a DAG.
 * <p>
 * The class also collects performance metrics using {@link Metrics}.
 */
public class Kahn {
    private final Graph dag;
    private final Metrics metrics = new Metrics();

    public Kahn(Graph dag) {
        this.dag = dag;
    }

    public List<Integer> kahnSort() {
        metrics.start();
        int n = dag.getVertices();
        int[] indegree = new int[n];

        // Compute indegrees
        for (int i = 0; i < n; i++) {
            for (Edge e : dag.getAdjacency().get(i)) {
                indegree[e.getTo()]++;
            }
        }

        // Add all zero-indegree vertices to queue
        Queue<Integer> queue = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            if (indegree[i] == 0) {
                queue.add(i);
                metrics.addOperations("Pushes");
            }
        }

        // Process queue
        List<Integer> order = new ArrayList<>();
        while (!queue.isEmpty()) {
            int v = queue.poll();
            metrics.addOperations("Pops");
            order.add(v);

            // Decrease indegrees of neighbors
            for (Edge e : dag.getAdjacency().get(v)) {
                int nextV = e.getTo();
                indegree[nextV]--;
                if (indegree[nextV] == 0) {
                    queue.add(nextV);
                    metrics.addOperations("Pushes");
                }
            }
        }
        metrics.stop();
        metrics.print("Kahn Topological Sort");
        System.out.println("Topological order: " + order);
        return order;
    }
}