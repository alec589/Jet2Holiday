package Parking;

import PriorityQueue.MyPriorityQueue;
import Graph.MyGraph;
import Graph.Node;
import List.MyArrayList;
import Map.MyHashMap;

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
    public Spot recommend(String destinationName, double distanceWeight, double priceWeight) {
    	
    	// for printout
    	MyArrayList<Spot> spots = new MyArrayList<>();
    	MyHashMap<Spot, Double> scoreMap = new MyHashMap<>();
    	MyHashMap<Spot, Double> distMap = new MyHashMap<>();
    	MyHashMap<Spot, Double> normDistMap = new MyHashMap<>();
    	MyHashMap<Spot, Double> normPriceMap = new MyHashMap<>();

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
				
				  // Normalization compresses distance differences, making price dominate the decision
				  // scale normalized distance
				  double score =
				      distanceWeight * normalizedDistance +
				      priceWeight * normalizedPrice;
				  
				  // printout preparation
				  spots.add(spot);
			      scoreMap.put(spot, score);
			      distMap.put(spot, distance);
			      normDistMap.put(spot, normalizedDistance);
			      normPriceMap.put(spot, normalizedPrice);
				  
				  pq.insert(spot, score);
			}
		}
		
		// printout info
		// sort by score
		for (int i = 0; i < spots.size(); i++) {
		    for (int j = 0; j < spots.size() - 1 - i; j++) {
		        Spot a = spots.get(j);
		        Spot b = spots.get(j + 1);

		        if (scoreMap.get(a) > scoreMap.get(b)) {
		            Spot temp = a;
		            spots.set(j, b);
		            spots.set(j + 1, temp);
		        }
		    }
		}

		// print once
		System.out.println("=== Ranking ===");

		for (int i = 0; i < spots.size(); i++) {
		    Spot s = spots.get(i);

		    System.out.println(
		    	s.getSpotId()
		    	+ " | dist=" + String.format("%.6f", distMap.get(s))
		    	+ " | price=" + String.format("%.6f", s.getPrice())
		    	+ " | normDist=" + String.format("%.6f", normDistMap.get(s))
		    	+ " | normPrice=" + String.format("%.6f", normPriceMap.get(s))
		    	+ " | wDist=" + String.format("%.6f", distanceWeight * normDistMap.get(s))
		        + " | wPrice=" + String.format("%.6f", priceWeight * normPriceMap.get(s))
		    	+ " | score=" + String.format("%.6f", scoreMap.get(s))
		    );
		}
		
		return pq.isEmpty() ? null : pq.removeBest();
	}
		
		
    private double normalize(double value, double min, double max) {
    	if (max == min) return 0.0;
		return (value - min) / (max - min);
	}
		
		
		
}
