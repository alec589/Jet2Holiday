package Parking;

import Graph.Node;
import Graph.NodeType;

public class Road {
	private String name;
    private Coordinate coordinate;
    private Node node;

    public Road(String name, Coordinate coordinate) {
        this.name = name;
        this.coordinate = coordinate;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public Node toNode() {
        if (node == null) {
            node = new Node(name, coordinate, NodeType.ROAD);
        }
        return node;
    }

    @Override
    public String toString() {
        return "RoadNode{name='" + name +
               "', coordinate=(" + coordinate.getX() + "," + coordinate.getY() + ")" +
               "}";
    }
    
}
