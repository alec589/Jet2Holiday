package Graph;

import Parking.Coordinate;

public class Node {
	private String id;
    private Coordinate coordinate;
    private NodeType type;

    public Node(String id, Coordinate coordinate, NodeType type) {
        this.id = id;
        this.coordinate = coordinate;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    @Override
    public String toString() {
        return id;
    }
    
}
