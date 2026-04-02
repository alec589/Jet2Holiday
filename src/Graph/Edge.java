package Graph;

public class Edge {
	private Node from;
    private Node to;
    private double weight; // weight indicates distance

    public Edge(Node from, Node to, double weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    public Node getFrom() {
        return from;
    }

    public Node getTo() {
        return to;
    }

    public double getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return from.getId() + " -> " + to.getId() + " (" + weight + ")";
    }
}
