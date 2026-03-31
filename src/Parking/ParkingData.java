package Parking;
import java.util.HashMap;
import java.util.Random;

public class ParkingData {
	private HashMap<String, Spot> allSpots;

    public ParkingData() {
        allSpots = new HashMap<>();
        initializeData(); 
    }

    private void initializeData() {
    	Random rand = new Random();
    	for (int i = 1; i <= 50; i++) {
            String id = "BOS-" + (100 + i); 
            int x = rand.nextInt(750) + 20; 
            int y = rand.nextInt(550) + 20;
            double price = 2.0 + (4.0 * rand.nextDouble());
            
            String[] areas = {"BackBay", "Fenway", "Downtown", "Seaport", "Newton"};
            String area = areas[rand.nextInt(areas.length)];
            
            Spot newSpot = new Spot(id, x, y, price, area);
            
            if (rand.nextBoolean()) {
                newSpot.setOccupied(true);
            }
            // set 50 spots here and add into allspots
            allSpots.put(id, newSpot);
        }
    }

    public HashMap<String, Spot> getAllSpots() {
        return allSpots;
    }

    public void updateSpotStatus(String id, boolean occupied) {
        if (allSpots.containsKey(id)) {
            allSpots.get(id).setOccupied(occupied);
        }
    }
}

