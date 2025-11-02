package org.sabulla.graph;

import java.util.ArrayList;
import java.util.List;

public class Graph {
    private final int vertices;
    private final List<List<Edge>> adjacency;

    public Graph(int vertices) {
        this.vertices = vertices;
        adjacency = new ArrayList<>();
        for (int i = 0; i < vertices; i++) {
            adjacency.add(new ArrayList<>());
        }
    }

    public void addEdge(int u, int v, int w) {
        adjacency.get(u).add(new Edge(v, w));
    }

    public int getVertices() {
        return vertices;
    }

    public List<List<Edge>> getAdjacency() {
        return adjacency;
    }
}
