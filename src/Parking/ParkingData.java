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
            int baseX = 0;
            int baseY = 0;
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
            }

            for (int i = 0; i < 16; i++) {
                String id = "BOS-" + idCounter++;
                double price = 2.0 + (4.0 * rand.nextDouble());

                int x = baseX + rand.nextInt(80) - 40;
                int y = baseY + rand.nextInt(80) - 40;

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

