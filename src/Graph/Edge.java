package Graph;

public class Edge<T> {
    
	private T from;
    private T to;
    private double weight;

    public Edge(T from, T to, double weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    public T getFrom() {
        return from;
    }

    public T getTo() {
        return to;
    }

    public double getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return from + " -> " + to + " (" + weight + ")";
    }
    
}
