package finalproject;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;


public class MoreTests {

    private static String XML_FILES_PATH = "";

    @BeforeAll
    static void setup() throws IOException {
        // try commenting this out if you're having trouble running the tests
        XML_FILES_PATH = new File(".").getCanonicalPath() + "/Data/";
    }

    @Test
    public void firstVertexExists() {
        MyWebGraph myWebGraph = new MyWebGraph();

        myWebGraph.addVertex("https://youtu.be/1IV0sonB-2U");
        myWebGraph.addVertex("http://google.com");

        boolean addFirstEdge = myWebGraph.addEdge("doesNotExist", "https://youtu.be/1IV0sonB-2U");
        assertFalse(addFirstEdge, "First edge does not exist");
    }

    @Test
    public void crawlAndIndexCaseSensitive() throws Exception {
        // make sure crawlAndIndex also has case sensitivity filter
        SearchEngine searchEngine = new SearchEngine("test.xml");
        searchEngine.crawlAndIndex("www.cs.mcgill.ca");

        assertEquals(1, searchEngine.getResults("qS").size(), "crawl and index is not case sensitive");
    }

    @Test
    public void crawlAndIndexUniqueURLs() throws Exception {
        SearchEngine searchEngine = new SearchEngine( "test.xml");
        searchEngine.crawlAndIndex("www.cs.mcgill.ca");

        for (ArrayList<String> urls : searchEngine.wordIndex.values()) {
            HashMap<String, Integer> urlCount = new HashMap<>();
            for (String url : urls) {
                if (urlCount.containsKey(url))
                    fail("word in wordIndex contained duplicate url");

                urlCount.put(url, 1);
            }
        }
    }

    @Test
    public void getResultsEmptyQuery() throws Exception {
        SearchEngine searchEngine = new SearchEngine( "test.xml");
        searchEngine.crawlAndIndex("www.cs.mcgill.ca");
        searchEngine.assignPageRanks(0.01);

        ArrayList<String> emptySearch = searchEngine.getResults("greetings");
        assertNotNull(emptySearch, "method returned null instead of empty ArrayList");
        assertEquals(0, emptySearch.size(), "method did not return empty ArrayList");
    }

    @Test
    public void correctRanking_testXML() throws Exception {
        SearchEngine searchEngine = new SearchEngine("test.xml");

        searchEngine.crawlAndIndex("www.cs.mcgill.ca");
        searchEngine.assignPageRanks(0.001);
        ArrayList<String> results = searchEngine.getResults("and");

        assertTrue(results.get(0).equals("www.ea.com") || results.get(0).equals("www.unity.com"));
        assertTrue(results.get(1).equals("www.ea.com") || results.get(1).equals("www.unity.com"));
        assertEquals("www.cs.mcgill.ca", results.get(2));
        assertEquals("www.ubisoft.com", results.get(3));
        assertEquals("www.eidos.com", results.get(4));
        assertEquals("www.unreal.com", results.get(5));
    }

    @Test
    public void pageRankInitialization() throws Exception {
        SearchEngine searchEngine = new SearchEngine("test.xml");

        searchEngine.crawlAndIndex("www.cs.mcgill.ca");
        searchEngine.assignPageRanks(0.01);

        assertNotEquals(searchEngine.internet.getPageRank("www.ea.com"), 1.185532278583319, "Page Rank not initialized to 1 ");
    }

    @Test
    public void oneIterationEpsilon() throws Exception {
        SearchEngine searchEngine = new SearchEngine("test.xml");

        searchEngine.crawlAndIndex("www.cs.mcgill.ca");
        searchEngine.assignPageRanks(100); // large epsilon value
        ArrayList<String> results = searchEngine.getResults("and");

        for (Double rank : searchEngine.computeRanks(results)) {
            if (rank == 0)
                fail("Rank was not updated in first iteration");
        }
    }

}
