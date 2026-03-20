package Parking;

public class Spot {
	private String id;         
    private int x, y;         
    private boolean occupied; 
    private double price;    
    
    public Spot(String id, int x, int y, double price) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.price = price;
        this.occupied = false; 
    }
    public String getId() { return id; }
    
    public int getX() { 
    	return x; 
    	}
    public int getY() { 
    	return y; 
    	}
    public boolean isOccupied() { 
    	return occupied; 
    	}
    public double getPricePerHour() { 
    	return price; 
    	}

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }
}
