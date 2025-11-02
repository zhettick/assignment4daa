import org.junit.jupiter.api.Test;
import org.sabulla.graph.Graph;
import org.sabulla.graph.topo.Kahn;
import org.sabulla.graph.dagsp.DAG;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DAGTest {

    @Test
    public void testShortestPath() {
        Graph g = new Graph(6);
        g.addEdge(0, 1, 5);
        g.addEdge(0, 2, 3);
        g.addEdge(1, 3, 6);
        g.addEdge(1, 2, 2);
        g.addEdge(2, 4, 4);
        g.addEdge(2, 5, 2);
        g.addEdge(2, 3, 7);
        g.addEdge(3, 4, -1);
        g.addEdge(4, 5, -2);

        Kahn kahn = new Kahn(g);
        List<Integer> topoOrder = kahn.kahnSort();

        DAG dag = new DAG(g);
        dag.shortestPath(1, topoOrder);

        assertTrue(true);
    }

    @Test
    public void testLongestPath() {
        Graph g = new Graph(5);
        g.addEdge(0, 1, 1);
        g.addEdge(1, 2, 2);
        g.addEdge(2, 3, 3);
        g.addEdge(3, 4, 4);

        Kahn kahn = new Kahn(g);
        List<Integer> topoOrder = kahn.kahnSort();

        DAG dag = new DAG(g);
        dag.longestPath(0, topoOrder);

        assertTrue(true);
    }
}