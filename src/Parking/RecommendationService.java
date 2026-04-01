package Parking;

import java.util.Comparator;
import java.util.PriorityQueue;

import Graph.CityGraph;

public class RecommendationService {

	private CityGraph graph;
    private ParkingData parkingData;

  
    public RecommendationService(CityGraph graph, ParkingData parkingData) {
        this.graph = graph;
        this.parkingData = parkingData;
    }
  //wang lu version 
    /* public Spot recommend(String userArea, String destinationArea) {

        PriorityQueue<Spot> pq = new PriorityQueue<>(
            Comparator.comparingDouble(spot ->
                spot.getPricePerHour()
                + graph.shortestDistance(userArea, spot.getArea())
                + graph.shortestDistance(spot.getArea(), destinationArea)
            )
        );

        for (Spot spot : parkingData.getAllSpots().values()) {
            if (!spot.isOccupied()) {
                pq.add(spot);
            }
        }

        return pq.isEmpty() ? null : pq.poll();
    }
    */
    
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

        // Java lambda expressions can only use variables that are final or effectively final
        final double finalMinPrice = minPrice;
        final double finalMaxPrice = maxPrice;
        final double finalMinDistance = minDistance;
        final double finalMaxDistance = maxDistance;

        // Create a priority queue (using normalization + weights)
        PriorityQueue<Spot> pq = new PriorityQueue<>(
            Comparator.comparingDouble(spot -> {

                double price = spot.getPricePerHour();

                double totalDistance =
                    graph.shortestDistance(userArea, spot.getArea()) +
                    graph.shortestDistance(spot.getArea(), destinationArea);

                double normalizedPrice = normalize(price, finalMinPrice, finalMaxPrice);
                double normalizedDistance = normalize(totalDistance, finalMinDistance, finalMaxDistance);

                return distanceWeight * normalizedDistance
                     + priceWeight * normalizedPrice;
            })
        );

        // Reserve all available parking spaces
        for (Spot spot : parkingData.getAllSpots().values()) {
            if (!spot.isOccupied()) {
                pq.add(spot);
            }
        }

        return pq.isEmpty() ? null : pq.poll();
    }

    // Normalize value to [0,1]
    private double normalize(double value, double min, double max) {
        if (max == min) return 0.0;
        return (value - min) / (max - min);
    }
}
