package finalproject;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExtraTestsWebGraph {

    private MyWebGraph graph;

    @Test
    public void testAddVertex() {
        MyWebGraph graph = new MyWebGraph();
        assertTrue(graph.addVertex("http://example.com"));
        assertFalse(graph.addVertex("http://example.com")); // Trying to add the same vertex should return false
        assertEquals("VertexList must be updated correctly without duplicates",
                1, graph.vertexList.size());
        // check that the graph contains the right vertex
        assertEquals("VertexList must be updated correctly",
                "http://example.com" + "\n", graph.toString());
    }

    @Test
    public void testDuplicateEntryAddVertex() {
        MyWebGraph graph = new MyWebGraph();
        assertTrue(graph.addVertex("http://example.com"));
        assertFalse(graph.addVertex("http://example.com")); // Trying to add the same vertex should return false
    }

    @Test
    public void testAddEdge() {
        MyWebGraph graph = new MyWebGraph();
        graph.addVertex("http://example.com");
        graph.addVertex("http://example.org");
        assertTrue(graph.addEdge("http://example.com", "http://example.org"));
        assertFalse(graph.addEdge("http://example.com", "http://nonexistent.com")); // Non-existent vertex
    }

    @Test
    public void testNeighborsAddEdge() {
        MyWebGraph graph= new MyWebGraph();
        graph.addVertex("http://page1.com");
        graph.addVertex("http://page2.com");

        // test adding a valid edge
        boolean edgeAdded = graph.addEdge("http://page1.com", "http://page2.com");
        assertTrue(edgeAdded, "Edge should be added");
        assertTrue(graph.getNeighbors("http://page1.com").contains("http://page2.com"),
                "http://page2.com should be in the neighbors list of http://page1.com");

        // check links field is updated for the first vertex
        MyWebGraph.WebVertex vertex = graph.vertexList.get("http://page1.com");
        assertEquals("Links field should be updated with one link",
                1, vertex.getNeighbors().size());
        assertTrue(vertex.getNeighbors().contains("http://page2.com"),
                "Links field should contain http://page2.com");
    }

    @Test
    public void testNonExistentAddEdge() {
        MyWebGraph graph= new MyWebGraph();
        graph.addVertex("http://page1.com");
        graph.addVertex("http://page2.com");

        // test adding an edge where one vertex does not exist
        assertFalse(graph.addEdge("http://page1.com", "http://page3.com"),
                "Edge should not be added if one vertex does not exist");
        // test adding an edge where both vertices do not exist
        assertFalse(graph.addEdge("http://page3.com", "http://page4.com"),
                "Edge should not be added if both vertices do not exist");
    }

    @Test
    public void testDuplicateEntryAddEdge() {
        MyWebGraph graph= new MyWebGraph();
        graph.addVertex("http://page1.com");
        graph.addVertex("http://page2.com");

        // test adding an edge that already exists for duplicate entries
        boolean edgeAdded = graph.addEdge("http://page1.com", "http://page2.com");
        edgeAdded = graph.addEdge("http://page1.com", "http://page2.com");
        assertFalse(edgeAdded, "Edge should not be added if it already exists");
        assertEquals("Links field should not have duplicates",
                1, graph.getNeighbors("http://page1.com").size());
    }

    @Test
    public void testGetVertices() {
        MyWebGraph graph = new MyWebGraph();
        graph.addVertex("http://example.com");
        ArrayList<String> vertices = graph.getVertices();
        assertNotNull(vertices);
        assertTrue(vertices.contains("http://example.com"));
    }

    @Test
    public void testGetVerticesMultiple() {
        MyWebGraph graph = new MyWebGraph();
        graph.addVertex("http://example.com");
        graph.addVertex("http://razzledazzle.com");
        graph.addVertex("http://example.com");
        graph.addVertex("http://CountryRoadsTakeMeHome.WV");
        ArrayList<String> vertices = graph.getVertices();
        assertNotNull(vertices);
        assertEquals("ArrayList must contain all added vertices", 3,
                graph.getVertices().size()); // not in order bc of the hashkey
    }

    @Test
    public void testGetEdgesInto() {
        MyWebGraph graph = new MyWebGraph();
        graph.addVertex("http://example.com");
        graph.addVertex("http://example.org");
        graph.addEdge("http://example.com", "http://example.org"); // hyperlink from com to org
        ArrayList<String> edgesInto = graph.getEdgesInto("http://example.org");
        assertNotNull(edgesInto);
        assertTrue(edgesInto.contains("http://example.com"));

        graph.addVertex("http://otherExample.org");
        graph.addEdge("http://otherExample.org", "http://example.org");
        ArrayList<String> edgesInto2 = graph.getEdgesInto("http://example.org");
        assertEquals("Multiple vertices added must update the method results",
                2, graph.getEdgesInto("http://example.org").size());
        assertTrue(edgesInto2.contains("http://example.com"));
        assertTrue(edgesInto2.contains("http://otherExample.org"));
    }

}
