package Graph;

import Parking.Coordinate;
import Parking.ParkingData;
import Parking.Spot;

public class test {
	public static void main(String[] args) {
		ParkingData data = new ParkingData();

	    Spot bestSpot = data.findNearestAvailableSpot("Fenway Park");

	    if (bestSpot != null) {
	        double dist = data.getDistanceToSpot("Fenway Park", bestSpot);

	        System.out.println("Best spot: " + bestSpot);
	        System.out.println("Distance: " + dist);
	    } else {
	        System.out.println("No available spot found.");
	    }
	}
}
