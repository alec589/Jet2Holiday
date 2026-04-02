package Graph;

public interface GraphInterface<T> {

    public void addNode(T node);

    public void addEdge(T from, T to, double weight);

    public double shortestDistance(T start, T end);
    
}