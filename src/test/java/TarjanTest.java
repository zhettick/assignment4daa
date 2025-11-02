import org.junit.jupiter.api.Test;
import org.sabulla.graph.Graph;
import org.sabulla.graph.scc.Tarjan;

import static org.junit.jupiter.api.Assertions.*;

public class TarjanTest {

    @Test
    public void testSingleSCC() {
        Graph g = new Graph(3);
        g.addEdge(0, 1, 1);
        g.addEdge(1, 2, 1);
        g.addEdge(2, 0, 1);

        Tarjan tarjan = new Tarjan(g);
        tarjan.findSCC();

        assertEquals(1, tarjan.getGroups().size());
    }

    @Test
    public void testMultipleSCCs() {
        Graph g = new Graph(5);
        g.addEdge(0, 1, 1);
        g.addEdge(1, 0, 1);
        g.addEdge(2, 3, 1);
        g.addEdge(3, 4, 1);

        Tarjan tarjan = new Tarjan(g);
        tarjan.findSCC();

        assertEquals(4, tarjan.getGroups().size());
    }
}