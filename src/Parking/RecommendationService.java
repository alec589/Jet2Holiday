package Parking;

import PriorityQueue.MyPriorityQueue;
import Graph.CityGraph;

public class RecommendationService {

	private CityGraph graph;
    private ParkingData parkingData;

  
    public RecommendationService(CityGraph graph, ParkingData parkingData) {
        this.graph = graph;
        this.parkingData = parkingData;
    }
    
    /**
     * Recommend best parking spot using:
     * - normalized distance
     * - normalized price
     * - user preference weights
     */
    public Spot recommend(String userArea, String destinationArea,
                          double distanceWeight, double priceWeight) {

        double minPrice = Double.MAX_VALUE;
        double maxPrice = Double.MIN_VALUE;
        double minDistance = Double.MAX_VALUE;
        double maxDistance = Double.MIN_VALUE;

        // find  min / max distance and price(for normalization)
        for (Spot spot : parkingData.getAllSpots().values()) {
            if (!spot.isOccupied()) {

                double price = spot.getPricePerHour();

                double totalDistance =
                    graph.shortestDistance(userArea, spot.getArea()) +
                    graph.shortestDistance(spot.getArea(), destinationArea);

                if (price < minPrice) minPrice = price;
                if (price > maxPrice) maxPrice = price;

                if (totalDistance < minDistance) minDistance = totalDistance;
                if (totalDistance > maxDistance) maxDistance = totalDistance;
            }
        }
        
      //create a queue
        MyPriorityQueue<Spot> pq = new MyPriorityQueue<>();
   
     // compute score and insert into custom priority queue
        for (Spot spot : parkingData.getAllSpots().values()) {
            if (!spot.isOccupied()) {

                double price = spot.getPricePerHour();

                double totalDistance =
                    graph.shortestDistance(spot.getArea(), destinationArea);

                double normalizedPrice = normalize(price, minPrice, maxPrice);
                double normalizedDistance = normalize(totalDistance, minDistance, maxDistance);

                double score =
                    distanceWeight * normalizedDistance +
                    priceWeight * normalizedPrice;

                pq.insert(spot, score);
            }
        }

        return pq.isEmpty() ? null : pq.removeBest();
    }
      
    // Normalize value to [0,1]
    private double normalize(double value, double min, double max) {
        if (max == min) return 0.0;
        return (value - min) / (max - min);
    }
}
