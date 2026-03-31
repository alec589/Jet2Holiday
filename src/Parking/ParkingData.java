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
            
            int baseX = 0;
            int baseY = 0;
            
            double price = 2.0 + (4.0 * rand.nextDouble());
            
            String[] areas = {"BackBay", "Fenway", "Downtown", "Seaport", "Newton"};
            String area = areas[rand.nextInt(areas.length)];
            
            switch (area) {
            case "BackBay":
                baseX = 100; baseY = 100;
                break;
            case "Fenway":
                baseX = 300; baseY = 100;
                break;
            case "Downtown":
                baseX = 500; baseY = 250;
                break;
            case "Seaport":
                baseX = 700; baseY = 250;
                break;
            case "Newton":
                baseX = 100; baseY = 450;
                break;
            case "Cambridge":
                baseX = 300; baseY = 50;
                break;
            }
        
            int x = baseX + rand.nextInt(80) - 40;
            int y = baseY + rand.nextInt(80) - 40;
            
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
    
    public java.util.List<Spot> getSpotsByArea(String area) {
        java.util.List<Spot> result = new java.util.ArrayList<>();

        for (Spot spot : allSpots.values()) {
            if (spot.getArea().equals(area)) {
                result.add(spot);
            }
        }

        return result;
    }
}

