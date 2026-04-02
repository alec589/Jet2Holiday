package Graph;

public class Node {
	private String id;
    private Coordinate coordinate;

    public Node(String id, Coordinate coordinate) {
        this.id = id;
        this.coordinate = coordinate;
    }

    public String getId() {
        return id;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    @Override
    public String toString() {
        return "Node{id='" + id + "', coordinate=("
                + coordinate.getX() + "," + coordinate.getY() + ")}";
    }
}
