package Graph;

import PriorityQueue.MyPriorityQueue;
import List.ListInterface;
import List.MyArrayList;
import Map.MapInterface;
import Map.MyHashMap;

public class MyGraph<T> implements GraphInterface<T> {

    private MapInterface<T, ListInterface<Edge<T>>> adjList = new MyHashMap<>();

    private static class NodeDistance<T> {
        T vertex;
        double distance;

        NodeDistance(T vertex, double distance) {
            this.vertex = vertex;
            this.distance = distance;
        }
    }

    @Override
    public void addNode(T node) {
        adjList.putIfAbsent(node, new MyArrayList<>());
    }

    @Override
    public void addEdge(T from, T to, double weight) {
        adjList.putIfAbsent(from, new MyArrayList<>());
        adjList.putIfAbsent(to, new MyArrayList<>());

        Edge<T> edge1 = new Edge<>(from, to, weight);
        Edge<T> edge2 = new Edge<>(to, from, weight);

        adjList.get(from).add(edge1);
        adjList.get(to).add(edge2);
    }

    @Override
    public double shortestDistance(T start, T end) {
        MapInterface<T, Double> distances = new MyHashMap<>();
        MyPriorityQueue<NodeDistance<T>> pq = new MyPriorityQueue<>();

        for (T node : adjList.keySet()) {
            distances.put(node, Double.MAX_VALUE);
        }

        distances.put(start, 0.0);
        pq.insert(new NodeDistance<>(start, 0.0), 0.0);

        while (!pq.isEmpty()) {
            NodeDistance<T> current = pq.removeBest();

            if (current.vertex.equals(end)) {
                return current.distance;
            }

            for (Edge<T> edge : adjList.getOrDefault(current.vertex, new MyArrayList<>())) {
                T neighbor = edge.getTo();
                double newDist = current.distance + edge.getWeight();

                if (newDist < distances.get(neighbor)) {
                    distances.put(neighbor, newDist);
                    pq.insert(new NodeDistance<>(neighbor, newDist), newDist);
                }
            }
        }

        return Double.MAX_VALUE;
    }
}