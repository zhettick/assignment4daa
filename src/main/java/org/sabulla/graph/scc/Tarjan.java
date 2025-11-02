package org.sabulla.graph.scc;

import org.sabulla.Metrics;
import org.sabulla.graph.Edge;
import org.sabulla.graph.Graph;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Implements Tarjan's algorithm to find Strongly Connected Components (SCCs) in a directed graph.
 * <p>
 * The class also collects performance metrics using {@link Metrics}.
 */
public class Tarjan {
    private final Graph graph;
    private final Metrics metrics = new Metrics();
    private int time = 0;
    private final int[] index;
    private final int[] lowlink;
    private final boolean[] onStack;
    private final Deque<Integer> stack = new ArrayDeque<>();
    private final List<List<Integer>> groups = new ArrayList<>();

    public Tarjan(Graph graph) {
        this.graph = graph;
        int n = graph.getVertices();
        index = new int[n];
        lowlink = new int[n];
        onStack = new boolean[n];
        for (int i = 0; i < n; i++) {
            index[i] = -1;
            lowlink[i] = -1;
        }
    }

    public void findSCC() {
        metrics.start();
        for (int v = 0; v < graph.getVertices(); v++) {
            if (index[v] == -1) {
                dfs(v);
            }
        }

        metrics.stop();
        metrics.print("Tarjan SCC");
        for (int i = 0; i < groups.size(); i++) {
            System.out.println("Group #" + (i + 1) + ": " + groups.get(i) + " (size = " + groups.get(i).size() + ")");
        }
    }

    // Depth-First Search (DFS) with Tarjan logic
    private void dfs(int v) {
        metrics.addOperations("DFS_calls");
        index[v] = lowlink[v] = time++;
        stack.push(v);
        onStack[v] = true;

        // Explore all outgoing edges
        for (Edge e : graph.getAdjacency().get(v)) {
            metrics.addOperations("Edges_checked");
            int nextV = e.getTo();
            if (index[nextV] == -1) { // not visited
                dfs(nextV);
                lowlink[v] = Math.min(lowlink[v], lowlink[nextV]);
            } else if (onStack[nextV]) { // back edge
                lowlink[v] = Math.min(lowlink[v], index[nextV]);
            }
        }

        // If root of SCC found — pop all members
        if (lowlink[v] == index[v]) {
            List<Integer> group = new ArrayList<>();
            int nextV;
            do {
                nextV = stack.pop();
                onStack[nextV] = false;
                group.add(nextV);
            } while (nextV != v);
            groups.add(group);
        }
    }

    public Graph buildDAG() {
        metrics.start();
        int[] groupIndex = new int[graph.getVertices()];

        // Map each vertex to its SCC group
        for (int i = 0; i < groups.size(); i++)
            for (int v : groups.get(i))
                groupIndex[v] = i;

        // Build edges between SCCs
        Graph dag = new Graph(groups.size());
        for (int v = 0; v < graph.getVertices(); v++)
            for (Edge e : graph.getAdjacency().get(v)) {
                int nextV = e.getTo();
                int from = groupIndex[v];
                int to = groupIndex[nextV];
                if (from != to)
                    dag.addEdge(from, to, 1);
            }

        metrics.stop();
        metrics.print("Tarjan Condensation DAG");

        for (int i = 0; i < dag.getVertices(); i++) {
            System.out.print("Group " + i + ": ");
            for (Edge e : dag.getAdjacency().get(i))
                System.out.print(e.getTo() + " ");
            System.out.println();
        }
        return dag;
    }

    public int getGroupOf(int vertex) {
        for (int i = 0; i < groups.size(); i++) {
            if (groups.get(i).contains(vertex)) {
                return i;
            }
        }
        return -1;
    }

    public List<List<Integer>> getGroups() {
        return groups;
    }
}