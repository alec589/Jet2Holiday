package Parking;

import Graph.Area;
import Graph.Coordinate;
import Graph.Node;

public class Spot {
	
	private String spotId;
    private boolean occupied;
    private Coordinate coordinate;
    private double price;
    private Area area;

    public Spot(String spotId, boolean occupied, Coordinate coordinate, double price, Area area) {
        this.spotId = spotId;
        this.occupied = occupied;
        this.coordinate = coordinate;
        this.price = price;
        this.area = area;
    }

    public String getSpotId() {
        return spotId;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public double getPrice() {
        return price;
    }

    public Area getArea() {
        return area;
    }
    
    // a spot can become a node
    public Node toNode() {
        return new Node(spotId, coordinate);
    }
    
    @Override
    public String toString() {
        return "Spot{id='" + spotId + 
               "', occupied=" + occupied +
               ", coordinate=(" + coordinate.getX() + "," + coordinate.getY() + ")" +
               ", price=" + price +
               ", area=" + area +
               "}";
    }
}
