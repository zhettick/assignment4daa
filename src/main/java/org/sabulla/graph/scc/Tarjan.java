package org.sabulla.graph.scc;

import org.sabulla.graph.Edge;
import org.sabulla.graph.Graph;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class Tarjan {
    private Graph graph;
    private int time = 0;
    private int[] index;
    private int[] lowlink;
    private boolean[] onStack;
    private Deque<Integer> stack = new ArrayDeque<>();
    private List<List<Integer>> groups = new ArrayList<>();

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
        for (int v = 0; v < graph.getVertices(); v++) {
            if (index[v] == -1) {
                dfs(v);
            }
        }

        for (int i = 0; i < groups.size(); i++) {
            System.out.println("Group #" + (i + 1) + ": " + groups.get(i) + " (size = " + groups.get(i).size() + ")");
        }
    }

    private void dfs(int v) {
        index[v] = lowlink[v] = time++;
        stack.push(v);
        onStack[v] = true;

        for (Edge e : graph.getAdjacency().get(v)) {
            int nextV = e.getTo();
            if (index[nextV] == -1) {
                dfs(nextV);
                lowlink[v] = Math.min(lowlink[v], lowlink[nextV]);
            } else if (onStack[nextV]) {
                lowlink[v] = Math.min(lowlink[v], index[nextV]);
            }
        }

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
        int[] groupIndex = new int[graph.getVertices()];
        for (int i = 0; i < groups.size(); i++)
            for (int v : groups.get(i))
                groupIndex[v] = i;

        Graph dag = new Graph(groups.size());
        for (int v = 0; v < graph.getVertices(); v++)
            for (Edge e : graph.getAdjacency().get(v)) {
                int nextV = e.getTo();
                int from = groupIndex[v];
                int to = groupIndex[nextV];
                if (from != to)
                    dag.addEdge(from, to, 1);
            }

        for (int i = 0; i < dag.getVertices(); i++) {
            System.out.print("Group " + i + " -> ");
            for (Edge e : dag.getAdjacency().get(i))
                System.out.print(e.getTo() + " ");
            System.out.println();
        }
        return dag;
    }
}