package Graph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class CityGraph implements GraphInterface{

    private Map<String, List<Edge>> adjList = new HashMap<>();

  
    private static class Edge {
        String target;
        double weight;

        Edge(String target, double weight) {
            this.target = target;
            this.weight = weight;
        }
    }

    
    private static class NodeDistance {
        String node;
        double distance;

        NodeDistance(String node, double distance) {
            this.node = node;
            this.distance = distance;
        }
    }
    
    @Override
    public void addNode(String name) {
        adjList.putIfAbsent(name, new ArrayList<>());
    }
    
    @Override
    public void addEdge(String from, String to, double weight) {
        adjList.putIfAbsent(from, new ArrayList<>());
        adjList.putIfAbsent(to, new ArrayList<>());

        adjList.get(from).add(new Edge(to, weight));
        adjList.get(to).add(new Edge(from, weight)); 
    }
    
    @Override
    public double shortestDistance(String start, String end) {

        
        Map<String, Double> distances = new HashMap<>();

        
        PriorityQueue<NodeDistance> pq =
                new PriorityQueue<>(Comparator.comparingDouble(n -> n.distance));

        
        for (String node : adjList.keySet()) {
            distances.put(node, Double.MAX_VALUE);
        }

        distances.put(start, 0.0);
        pq.add(new NodeDistance(start, 0.0));

        
        while (!pq.isEmpty()) {
            NodeDistance current = pq.poll();

            
            if (current.node.equals(end)) {
                return current.distance;
            }

            
            for (Edge edge : adjList.getOrDefault(current.node, new ArrayList<>())) {
                double newDist = current.distance + edge.weight;

                if (newDist < distances.get(edge.target)) {
                    distances.put(edge.target, newDist);
                    pq.add(new NodeDistance(edge.target, newDist));
                }
            }
        }

        return Double.MAX_VALUE;
    }
}
