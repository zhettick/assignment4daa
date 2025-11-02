package org.sabulla;

import org.json.JSONArray;
import org.json.JSONObject;
import org.sabulla.graph.Graph;
import org.sabulla.graph.scc.Tarjan;
import org.sabulla.graph.topo.Kahn;
import org.sabulla.graph.dagsp.DAG;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            String jsonText = new String(Files.readAllBytes(Paths.get("src/main/java/org/sabulla/data/input.json")));
            JSONObject root = new JSONObject(jsonText);
            JSONArray graphs = root.getJSONArray("graphs");

            for (int g = 0; g < graphs.length(); g++) {
                JSONObject graphObj = graphs.getJSONObject(g);
                int n = graphObj.getInt("n");
                int source = graphObj.getInt("source");
                JSONArray edges = graphObj.getJSONArray("edges");

                Graph graph = new Graph(n);
                for (int i = 0; i < edges.length(); i++) {
                    JSONObject e = edges.getJSONObject(i);
                    graph.addEdge(e.getInt("u"), e.getInt("v"), e.getInt("w"));
                }

                System.out.println("\n ---Graph #" + (g + 1) + "---");

                System.out.println(" ---SCC---");

                Tarjan tarjan = new Tarjan(graph);
                tarjan.findSCC();

                System.out.println("\n ---Condensation---");

                Graph dag = tarjan.buildDAG();

                System.out.println("\n ---Topological sort(Kahn)---");

                Kahn kahn = new Kahn(dag);
                List<Integer> topoOrder = kahn.kahnSort();

                int groupSource = tarjan.getGroupOf(source);
                DAG paths = new DAG(dag);
                paths.shortestPath(groupSource, topoOrder);
                paths.longestPath(groupSource, topoOrder);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}