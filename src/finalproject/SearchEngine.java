package finalproject;

import java.util.HashMap;
import java.util.ArrayList;

public class SearchEngine {
    public HashMap<String, ArrayList<String>> wordIndex;   // this will contain a set of pairs (String, ArrayList of Strings)
    public MyWebGraph internet;
    public XmlParser parser;

    public SearchEngine(String filename) throws Exception {
        this.wordIndex = new HashMap<String, ArrayList<String>>();
        this.internet = new MyWebGraph();
        this.parser = new XmlParser(filename);
    }

    /*
     * This does an exploration of the web, starting at the given url.
     * For each new page seen, it updates the wordIndex, the web graph,
     * and the set of visited vertices.
     *
     * 	This method will fit in about 30-50 lines (or less)
     */
    public void crawlAndIndex(String url) throws Exception {
        ArrayList<String> visited = new ArrayList<>(); // Keep track of visited urls
        dfs(url, visited);
    }

    private void dfs(String url, ArrayList<String> visited) {
        if (visited.contains(url)) {
            return; //Keep track of already explored urls to avoid infinite loop
        }

        visited.add(url);
        internet.addVertex(url);

        ArrayList<String> links = parser.getLinks(url);
        if (links != null) {
            for (String link : links) {
                internet.addVertex(link);
                if (!internet.addEdge(url, link)) {
                    continue;
                }
                if (!visited.contains(link)) {
                    dfs(link, visited); //Visit each linked url
                }
            }
        }

        ArrayList<String> content = parser.getContent(url);
        if (content != null) {
            for (String word : content) {
                word = word.toLowerCase(); //Make word lowercase to avoid case sensitivity
                ArrayList<String> urls = wordIndex.getOrDefault(word, new ArrayList<>());
                if (!urls.contains(url)) {
                    urls.add(url); //add the appropriate url to the list
                    wordIndex.put(word, urls);
                }

            }
        }
    }


    /*
     * This computes the pageRanks for every vertex in the web graph.
     * It will only be called after the graph has been constructed using
     * crawlAndIndex().
     * To implement this method, refer to the algorithm described in the
     * assignment pdf.
     *
     * This method will probably fit in about 30 lines.
     */
    public void assignPageRanks(double epsilon) {
        ArrayList<String> vertices = internet.getVertices();
        ArrayList<Double> ranks = new ArrayList<>(vertices.size());

        // Initialize ranks to 1
        for (int i = 0; i < vertices.size(); i++) {
            ranks.add(1.0);
            internet.setPageRank(vertices.get(i), 1.0);
        }

        boolean converged = false;

        while (!converged) {
            converged = true;
            ArrayList<Double> newRanks = computeRanks(vertices);


            for (int i = 0; i < vertices.size(); i++) {
                if (Math.abs(newRanks.get(i) - ranks.get(i)) >= epsilon) {
                    converged = false;
                }
                ranks.set(i, newRanks.get(i));  // Update the current ranks list
                internet.setPageRank(vertices.get(i), newRanks.get(i));  // Update the graph with new ranks
            }
        }
    }









    /*
     * The method takes as input an ArrayList<String> representing the urls in the web graph
     * and returns an ArrayList<double> representing the newly computed ranks for those urls.
     * Note that the double in the output list is matched to the url in the input list using
     * their position in the list.
     *
     * This method will probably fit in about 20 lines.
     */
    public ArrayList<Double> computeRanks(ArrayList<String> vertices) {
        ArrayList<Double> newRanks = new ArrayList<>();
        double d = 0.5;

        for (String v : vertices) {
            double sum = 0.0;
            ArrayList<String> inDegree = internet.getEdgesInto(v);
            for (String i : inDegree) {
                double pageRank = internet.getPageRank(i);
                int outDegree = internet.getOutDegree(i);
                    sum += pageRank / outDegree;

            }
            double newRank = (1 - d) + d * sum;
            newRanks.add(newRank);
        }
        return newRanks;
    }



            /* Returns a list of urls containing the query, ordered by rank
             * Returns an empty list if no web site contains the query.
             *
             * This method will probably fit in about 10-15 lines.
             */
            public ArrayList<String> getResults (String query){
                query = query.toLowerCase();
                ArrayList<String> urls = wordIndex.get(query);

                if (urls == null) {
                    return new ArrayList<>();

                }
                HashMap<String, Double> urlRanks = new HashMap<>();
                for (String url : urls) {
                    double rank = internet.getPageRank(url);
                    urlRanks.put(url,rank);
                }
                return Sorting.fastSort(urlRanks);

            }
        }
