package Graph;


import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import List.ListInterface;
import List.MyArrayList;

public class CityGraph implements GraphInterface{

	// Mapping: Area/node name (string),list of distance (list of edge)
    private Map<String, ListInterface<Edge>> adjList = new HashMap<>();

    // inner class 1 : Edge
    private static class Edge {
        String target;
        double weight;

        Edge(String target, double weight) {
            this.target = target;
            this.weight = weight;
        }
    }

    
    // inner class 2: NodeDistance
    private static class NodeDistance {
        String node;
        double distance;

        NodeDistance(String node, double distance) {
            this.node = node;
            this.distance = distance;
        }
    }
    
    // put if absent to make sure the result will not be overwritten
    @Override
    public void addNode(String name) {
        adjList.putIfAbsent(name, new MyArrayList<>());
    }
    
    @Override
    public void addEdge(String from, String to, double weight) {
    	// make sure these two nodes exist
        adjList.putIfAbsent(from, new MyArrayList<>());
        adjList.putIfAbsent(to, new MyArrayList<>());

        adjList.get(from).add(new Edge(to, weight));
        adjList.get(to).add(new Edge(from, weight)); 
    }
    
    @Override
    public double shortestDistance(String start, String end) {

        // use this map to store the shortest distance
        Map<String, Double> distances = new HashMap<>();

        // arrange according to the shortest distance
        PriorityQueue<NodeDistance> pq =
                new PriorityQueue<>(Comparator.comparingDouble(n -> n.distance));

        // set an infinite distance for each node
        for (String node : adjList.keySet()) {
            distances.put(node, Double.MAX_VALUE);
        }
        // set start point as 0
        // add it into the distance map
        // add it into the priority queue, as well
        distances.put(start, 0.0);
        pq.add(new NodeDistance(start, 0.0));

        
        while (!pq.isEmpty()) {
        	// poll method is used to get the shortest distance from the priority queue
            NodeDistance current = pq.poll();

            // if the current distance (the shortest distance) points to the end, this is the result
            if (current.node.equals(end)) {
                return current.distance;
            }
            
            // create new distance = current distance + edge weight
            for (Edge edge : adjList.getOrDefault(current.node, new MyArrayList<>())) {
                double newDist = current.distance + edge.weight;

            // if so, update the shortest distance
                if (newDist < distances.get(edge.target)) {
                    distances.put(edge.target, newDist);
                    pq.add(new NodeDistance(edge.target, newDist));
                }
            }
        }

        return Double.MAX_VALUE;
    }
}
