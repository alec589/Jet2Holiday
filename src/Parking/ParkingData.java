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

        String[] areas = {"BackBay", "Fenway", "Downtown", "Seaport", "Newton"};
        int idCounter = 100;

        for (String area : areas) {
            for (int i = 0; i < 12; i++) {
                String id = "BOS-" + idCounter++;
                double price = 2.0 + (4.0 * rand.nextDouble());

                int x = 0;
                int y = 0;

                if (i < 6) {
                    x = 25 + i * 145;
                    y = 30;
                } else {
                    x = 25 + (i - 6) * 145;
                    y = 330;
                }

                Spot newSpot = new Spot(id, x, y, price, area);

                if (rand.nextBoolean()) {
                    newSpot.setOccupied(true);
                }

                allSpots.put(id, newSpot);
            }
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

