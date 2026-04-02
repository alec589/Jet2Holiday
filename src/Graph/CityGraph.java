package Graph;

import java.util.HashMap;
import java.util.Map;
import PriorityQueue.MyPriorityQueue;
import List.ListInterface;
import List.MyArrayList;

public class CityGraph implements GraphInterface{

	// use list interface
	private Map<Node, ListInterface<Edge>> adjList = new HashMap<>();

	private static class NodeDistance {
	    Node node;
	    double distance;

	    NodeDistance(Node node, double distance) {
	        this.node = node;
	        this.distance = distance;
	    }
	}
    
    @Override
    public void addNode(Node node) {
        adjList.putIfAbsent(node, new MyArrayList<>());
    }
    
    @Override
    public void addEdge(Node from, Node to, double weight) {
        adjList.putIfAbsent(from, new MyArrayList<>());
        adjList.putIfAbsent(to, new MyArrayList<>());

        Edge edge1 = new Edge(from, to, weight);
        Edge edge2 = new Edge(to, from, weight);

        adjList.get(from).add(edge1);
        adjList.get(to).add(edge2);
    }
    
    public void addEdge(Node from, Node to) {
        double weight = from.getCoordinate().distanceTo(to.getCoordinate());
        addEdge(from, to, weight);
    }
    
    @Override
    public double shortestDistance(Node start, Node end) {

    	Map<Node, Double> distances = new HashMap<>();
        MyPriorityQueue<NodeDistance> pq = new MyPriorityQueue<>();
        
  
        for (Node node : adjList.keySet()) {
            distances.put(node, Double.MAX_VALUE);
        }
       
        distances.put(start, 0.0);      
        pq.insert(new NodeDistance(start, 0.0), 0.0);

        while (!pq.isEmpty()) {
            NodeDistance current = pq.removeBest();

            if (current.node.equals(end)) {
                return current.distance;
            }
            
            for (Edge edge : adjList.getOrDefault(current.node, new MyArrayList<>())) {
                Node neighbor = edge.getTo();
                double newDist = current.distance + edge.getWeight();

                if (newDist < distances.get(neighbor)) {
                    distances.put(neighbor, newDist);
                    pq.insert(new NodeDistance(neighbor, newDist), newDist);
                }
            }
        }

        return Double.MAX_VALUE;
    }
}
