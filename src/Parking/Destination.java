package Parking;

import Graph.Node;
import Graph.NodeType;

public class Destination {
	private String name;
	private Node node;
    private Coordinate coordinate;  // each destination has a coordinate
    private AreaType area;

    public Destination(String name, Coordinate coordinate, AreaType area) {
        this.name = name;
        this.coordinate = coordinate;
        this.area = area;
    }

    public String getName() {
        return name;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }
    
    public AreaType getArea() {
        return area;
    }

    // a destination can become a node
    public Node toNode() {
        if (node == null) {
            node = new Node(name, coordinate, NodeType.DESTINATION);
        }
        return node;
    }
    
    @Override
    public String toString() {
        return "Destination{name='" + name +
               "', coordinate=(" + coordinate.getX() + "," + coordinate.getY() + ")" +
               ", area=" + area +
               "}";
    }
    
}
