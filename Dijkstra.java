/* Dijkstra's method
Zach Barnhart */

import java.util.*;
import java.io.*;

public class Dijkstra {

    public static void main(String[] args) throws IOException {

        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Welcome to flight finder. This program will find the shortest path between two different airports.");
        System.out.print("How many airports are in the network? "); // To be honest, we don't need to do this for our implementation, but it's in the instructions, so we keep it anyway
        String N = keyboard.readLine();
        System.out.println("Now we will add routes to our network. Routes must be in the form ABC XYZ 999, where ABC and XYZ are airport codes and 999 is the corresponding distance. Please separate routes with a space. ");
        System.out.println("List the routes here, or type 1 for default routes: ");
        String str = keyboard.readLine();
        if (str.equals("1")) {
            str = "JFK DEN 300 JFK ORD 150 JFK CLT 100 ORD CLT 30 ORD DEN 70 ORD LAS 310 ORD SFO 300 SFO LAS 50 SFO LAX 120 LAS DEN 240 LAS ATL 70 LAS LAX 150 DEN DFW 40 DEN ATL 90 DEN LAX 400 LAX ATL 320 ATL DFW 60 ATL CLT 350 DFW CLT 260";
        }
        WeightedGraph<String> graph1 = new WeightedGraph<>();
        String[] split = str.split("\\s+");
        for (int i = 0; i < split.length; i = i + 3) {
            graph1.addVertex(split[i]);
            graph1.addVertex(split[i + 1]);
            graph1.addEdge(graph1.getIndex(split[i]), graph1.getIndex(split[i + 1]), Integer.parseInt(split[i + 2]));
            graph1.addEdge(graph1.getIndex(split[i + 1]), graph1.getIndex(split[i]), Integer.parseInt(split[i + 2]));
        }

        System.out.print("Please specify the source airport: ");
        String source = keyboard.readLine();
        System.out.print("Please specify the destination airport: ");
        String destination = keyboard.readLine();
        WeightedGraph<String>.ShortestPathTree tree1 = graph1.getShortestPath(graph1.getIndex(source));

        System.out.print("The shortest path from " + source + " to " + destination + " is: ");
        java.util.List<String> path = tree1.getPath(graph1.getIndex(destination));
        Collections.reverse(path);
        for (String s : path) {
            System.out.print(s + " ");
        }
        System.out.println();
        int distance = (int) tree1.getDistance(graph1.getIndex(destination));
        System.out.println("This path is " + distance + " units of distance");
    }
}

class WeightedGraph<V> extends UnweightedGraph<V> {

    public WeightedGraph() {
    }

    public boolean addEdge(int u, int v, double weight) {
        return addEdge(new WeightedEdge(u, v, weight));
    }

    //This is where we implement Dijkstra's method to make a shortest path tree
    public ShortestPathTree getShortestPath(int sourceVertex) {
        double[] distance = new double[getSize()];
        for (int i = 0; i < distance.length; i++) {
            distance[i] = Double.POSITIVE_INFINITY;
        }
        distance[sourceVertex] = 0;

        int[] parent = new int[getSize()];
        parent[sourceVertex] = -1;

        List<Integer> T = new ArrayList<>();

        while (T.size() < getSize()) {
            int u = -1;
            double currentMinDistance = Double.POSITIVE_INFINITY;
            for (int i = 0; i < getSize(); i++) {
                if (!T.contains(i) && distance[i] < currentMinDistance) {
                    currentMinDistance = distance[i];
                    u = i;
                }
            }
            if (u == -1) break; else T.add(u);
            for (Edge e : neighbors.get(u)) {
                if (!T.contains(e.v) && distance[e.v] > distance[u] + ((WeightedEdge)e).weight) {
                    distance[e.v] = distance[u] + ((WeightedEdge)e).weight;
                    parent[e.v] = u;
                }
            }
        }
        return new ShortestPathTree(sourceVertex, parent, T, distance);
    }

    public class ShortestPathTree extends SearchTree {
        private double[] distance;

        public ShortestPathTree(int source, int[] parent, List<Integer> searchOrder, double[] distance) {
            super(source, parent, searchOrder);
            this.distance = distance;
        }

        public double getDistance(int v) {
            return distance[v];
        }
    }
}

class WeightedEdge extends Edge implements Comparable<WeightedEdge> {
    public double weight;

    public WeightedEdge(int u, int v, double weight) {
        super(u, v);
        this.weight = weight;
    }

    public int compareTo(WeightedEdge edge) {
        if (weight > edge.weight) {
            return 1;
        } else if (weight == edge.weight) {
            return 0;
        } else {
            return -1;
        }
    }
}

class UnweightedGraph<V> implements Graph<V> {
    protected List<V> vertices = new ArrayList<>();
    protected List<List<Edge>> neighbors = new ArrayList<>();

    public UnweightedGraph() {
    }

    @Override
    public int getSize() {
        return vertices.size();
    }

    @Override
    public List<V> getVertices() {
        return vertices;
    }

    @Override
    public V getVertex(int index) {
        return vertices.get(index);
    }

    @Override
    public int getIndex(V v) {
        return vertices.indexOf(v);
    }

    @Override
    public boolean addVertex(V vertex) {
        if (!vertices.contains(vertex)) {
            vertices.add(vertex);
            neighbors.add(new ArrayList<Edge>());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean addEdge(Edge e) {
        if (e.u < 0 || e.u > getSize() - 1) {
            throw new IllegalArgumentException("No such index: " + e.u);
        }
        if (e.v < 0 || e.v > getSize() - 1) {
            throw new IllegalArgumentException("No such index: " + e.v);
        }
        if (!neighbors.get(e.u).contains(e)) {
            neighbors.get(e.u).add(e);
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public boolean addEdge(int u, int v) {
        return addEdge(new Edge(u, v));
    }

    public class SearchTree {
        private int root;
        private int[] parent;
        private List<Integer> searchOrder;

        public SearchTree(int root, int[] parent, List<Integer> searchOrder) {
            this.root = root;
            this.parent = parent;
            this.searchOrder = searchOrder;
        }

        public List<V> getPath(int index) {
            ArrayList<V> path = new ArrayList<>();

            do {
                path.add(vertices.get(index));
                index = parent[index];
            }
            while (index != -1);

            return path;
        }
    }
}

interface Graph<V> {
    int getSize();
    java.util.List<V> getVertices();
    V getVertex(int index);
    int getIndex(V v);
    boolean addVertex(V vertex);
    boolean addEdge(int u, int v);
    boolean addEdge(Edge e);
}

class Edge {
    public int u;
    public int v;

    public Edge(int u, int v) {
        this.u = u;
        this.v = v;
    }

    public boolean equals(Object o) {
        return u == ((Edge)o).u && v == ((Edge)o).v;
    }
}
