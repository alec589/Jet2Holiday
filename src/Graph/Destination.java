package Graph;

public class Destination {
	private String name;
    private Coordinate coordinate;  // each destination has a coordinate

    public Destination(String name, Coordinate coordinate) {
        this.name = name;
        this.coordinate = coordinate;
    }

    public String getName() {
        return name;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    // a destination can become a node
    public Node toNode() {
        return new Node(name, coordinate);
    }
    
    @Override
    public String toString() {
        return "Destination{name='" + name +
               "', coordinate=(" + coordinate.getX() + "," + coordinate.getY() + ")" +
               "}";
    }
    
}
