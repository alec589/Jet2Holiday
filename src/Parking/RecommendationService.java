package Parking;

import PriorityQueue.MyPriorityQueue;
import Graph.MyGraph;
import Graph.Node;

public class RecommendationService {

	private MyGraph<Node> graph;
    private ParkingData parkingData;

    public RecommendationService(MyGraph<Node> graph, ParkingData parkingData) {
        this.graph = graph;
        this.parkingData = parkingData;
    }
    
    /**
     * Recommend best parking spot using:
     * - normalized distance
     * - normalized price
     * - user preference weights
     */
    public Spot recommend(String destinationName,
            double distanceWeight,
            double priceWeight) {

		Destination destination = parkingData.getDestinationByName(destinationName);
		
		if (destination == null) {
			return null;
		}
		
		double minPrice = Double.MAX_VALUE;
		double maxPrice = Double.MIN_VALUE;
		double minDistance = Double.MAX_VALUE;
		double maxDistance = Double.MIN_VALUE;
		
		// first pass: find min/max for normalization
		for (Spot spot : parkingData.getAllSpots().values()) {
			if (!spot.isOccupied()) {
				  double price = spot.getPrice();
				
				  double distance = graph.shortestDistance(
				      destination.toNode(),
				      spot.toNode()
				  );
				
				  if (price < minPrice) minPrice = price;
				  if (price > maxPrice) maxPrice = price;
				
				  if (distance < minDistance) minDistance = distance;
				  if (distance > maxDistance) maxDistance = distance;
			}
		}
		
		MyPriorityQueue<Spot> pq = new MyPriorityQueue<>();
		
		// second pass: compute score
		for (Spot spot : parkingData.getAllSpots().values()) {
			if (!spot.isOccupied()) {
				  double price = spot.getPrice();
				
				  double distance = graph.shortestDistance(
				      destination.toNode(),
				      spot.toNode()
				  );
				
				  double normalizedPrice = normalize(price, minPrice, maxPrice);
				  double normalizedDistance = normalize(distance, minDistance, maxDistance);
				
				  double score =
				      distanceWeight * normalizedDistance +
				      priceWeight * normalizedPrice;
				
				  pq.insert(spot, score);
			}
		}
		
			return pq.isEmpty() ? null : pq.removeBest();
		}
		
		private double normalize(double value, double min, double max) {
			if (max == min) return 0.0;
			return (value - min) / (max - min);
		}
		
}
