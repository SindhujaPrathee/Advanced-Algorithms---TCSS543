package networkFlowAlgorithms.ScalingMaxFlow;


import graphCode.Edge;
import graphCode.GraphInput;
import graphCode.SimpleGraph;
import graphCode.Vertex;

import java.util.HashMap;
import java.util.LinkedList;

public class ScalingFordFulkerson {


    private static boolean bfs(HashMap<String, LinkedList<Edge>> adjGraph, HashMap<String, Edge> child_parent_map,
                               String s, String t, double delta) throws Exception {

        HashMap<String, Boolean> visitedNodes = new HashMap<>();

        for (String label : adjGraph.keySet()) {
            visitedNodes.put(label, false);
        }

        LinkedList<String> queue = new LinkedList<>();

        visitedNodes.put(s, true);
        queue.add(s);

        while (queue.size() > 0) {

            String poppedNodeLabel = queue.poll();
            LinkedList<Edge> neighbors = adjGraph.get(poppedNodeLabel);

            for (Edge e : neighbors) {
                Vertex neighbor = e.getSecondEndpoint();
                String label2 = (String) neighbor.getName();
                double value = (double) e.getData();
                if (!visitedNodes.get(label2) && value >= delta) {

                    child_parent_map.put(label2, e);
                    visitedNodes.put(label2, true);
                    queue.add(label2);

                }

            }
        }

        return visitedNodes.get(t) == true;
    }


    private static void updateGraphWithBottleneck(HashMap<String, LinkedList<Edge>> graph, LinkedList<Edge> path,
                                                  double updatedFlow, boolean isResidual) throws Exception {

        for (Edge e : path) {
            Vertex first = e.getFirstEndpoint();
            String label = (String) first.getName();

            Vertex second = e.getSecondEndpoint();
            String label2 = (String) second.getName();

            LinkedList<Edge> neighbors = graph.get(label);
            for (Edge e2 : neighbors) {

                String secondLabel = (String) e2.getSecondEndpoint().getName();
                if (secondLabel.equals(label2)) {
                    double flow = ((Double) e2.getData()).doubleValue();
                    // Subtract bottle neck in residual graph.
                    if (isResidual) {
                        e2.setData(flow - updatedFlow);
                    } else {
                        // Add bottle neck in flow graph.
                        e2.setData(flow + updatedFlow);
                    }
                }

            }

            graph.put(label, neighbors);

        }

    }


    public static double scalingFordFulkerson(HashMap<String, LinkedList<Edge>> originalGraph,
                                              HashMap<String, LinkedList<Edge>> residualGraph, String s, String t) throws Exception {

        HashMap<String, Edge> child_parent_map = new HashMap<>();

        double max_flow = 0;

        double maxCapacity = maxCapacityFromSource(residualGraph, s);

        double delta = calculateDelta(maxCapacity);

        while (delta >= 1.0) {
            while (bfs(residualGraph, child_parent_map, s, t, delta)) {
                LinkedList<Edge> path = new LinkedList<Edge>();
                String foo = t;
                while (!foo.equals(s)) {
                    Edge e = child_parent_map.get(foo);
                    path.add(e);
                    Vertex _vertex = e.getFirstEndpoint();
                    foo = (String) (_vertex.getName());
                }

                double bottleneck = Double.MAX_VALUE;

                for (String k : child_parent_map.keySet()) {
                    Edge e = child_parent_map.get(k);
                    double weight = ((Double) e.getData()).doubleValue();
                    if (weight < bottleneck) {
                        bottleneck = weight;
                    }
                }

                max_flow = max_flow + bottleneck;
                updateGraphWithBottleneck(originalGraph, path, bottleneck, false);

                updateGraphWithBottleneck(residualGraph, path, bottleneck, true);

                path = new LinkedList<>();

                child_parent_map = new HashMap<>();
            }
            delta = delta / 2.0;
        }

        return max_flow;

    }


    private static HashMap<String, LinkedList<Edge>> buildGraph(SimpleGraph G, boolean isResidual) throws Exception {
        LinkedList<Vertex> vertices = G.vertexList;
        LinkedList<Edge> edges = G.edgeList;

        HashMap<String, LinkedList<Edge>> graph = new HashMap<>();


        for (Vertex v : vertices) {
            String label = (String) v.getName();
            graph.put(label, new LinkedList<Edge>());

        }

        for (Edge e : edges) {
            Vertex v1 = e.getFirstEndpoint();

            String label = (String) v1.getName();

            LinkedList<Edge> neighbors = graph.get(label);

            Edge e1;
            if (isResidual) {
                e1 = new Edge(e.getFirstEndpoint(), e.getSecondEndpoint(), e.getData(), e.getName());
            } else {
                e1 = new Edge(e.getFirstEndpoint(), e.getSecondEndpoint(), 0.0, e.getName());
            }

            neighbors.add(e1);

            graph.put(label, neighbors);
        }

        return graph;
    }


    private static void printGraph(HashMap<String, LinkedList<Edge>> graph) {
        for (String k : graph.keySet()) {

            System.out.print(k + " ");
            LinkedList<Edge> neighbors = graph.get(k);
            for (Edge e : neighbors) {
                System.out.print(e.getSecondEndpoint().getName() + " " + e.getData() + "      ");

            }
            System.out.println();

        }

    }


    private static double calculateDelta(double n) {

        double delta = 0.0;
        double current = 1;
        while (current < n) {
            current *= 2;
        }

        delta = current / 2;
        return delta;
    }


    private static double maxCapacityFromSource(HashMap<String, LinkedList<Edge>> graph, String s) throws Exception {
        double maxCapacityFromSource = 0.0;

        LinkedList<Edge> neighbors = graph.get(s);

        double max = Double.MIN_VALUE;
        for (Edge e : neighbors) {
            double capacity = ((Double) e.getData()).doubleValue();
            if (capacity > max) {
                max = capacity;
            }
        }

        maxCapacityFromSource = max;
        return maxCapacityFromSource;
    }


    public static double scalingFordFulkersonDriver(String filePath) throws Exception {
        SimpleGraph G;
        G = new SimpleGraph();
        GraphInput.LoadSimpleGraph(G, filePath);
        HashMap<String, LinkedList<Edge>> graph1 = buildGraph(G, false);
        HashMap<String, LinkedList<Edge>> graph2 = buildGraph(G, true);
        double max_flow = scalingFordFulkerson(graph1, graph2, "s", "t");
        return max_flow;
    }
}
