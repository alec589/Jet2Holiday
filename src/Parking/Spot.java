package Parking;

public class Spot {
	
	private String area;
	private String id;         
    private int x, y;         
    private boolean occupied; 
    private double price;    
    
    // lu: add areas in spots to make them connected
    public Spot(String id, int x, int y, double price, String area) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.price = price;
        this.area = area;
        this.occupied = false;     // set as default
    }
    
    public String getId() { 
    	return id; 
    }
    
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
    
    public String getArea() {
    	return area;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }
}
