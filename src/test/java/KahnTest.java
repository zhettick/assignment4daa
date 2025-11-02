import org.junit.jupiter.api.Test;
import org.sabulla.graph.Graph;
import org.sabulla.graph.topo.Kahn;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class KahnTest {

    @Test
    public void testSimpleTopoOrder() {
        Graph g = new Graph(4);
        g.addEdge(0, 1, 1);
        g.addEdge(0, 2, 1);
        g.addEdge(1, 3, 1);
        g.addEdge(2, 3, 1);

        Kahn kahn = new Kahn(g);
        List<Integer> order = kahn.kahnSort();

        assertEquals(0, order.get(0));
        assertEquals(4, order.size());
    }

    @Test
    public void testNoCycle() {
        Graph g = new Graph(3);
        g.addEdge(0, 1, 1);
        g.addEdge(1, 2, 1);

        Kahn kahn = new Kahn(g);
        List<Integer> order = kahn.kahnSort();

        assertArrayEquals(new Integer[]{0, 1, 2}, order.toArray(new Integer[0]));
    }
}