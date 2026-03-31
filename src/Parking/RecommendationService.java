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

    public Spot recommend(String destinationArea) {

        PriorityQueue<Spot> pq = new PriorityQueue<>(
            Comparator.comparingDouble(spot ->
                spot.getPricePerHour() +
                graph.shortestDistance(spot.getArea(), destinationArea)
            )
        );

        for (Spot spot : parkingData.getAllSpots().values()) {
            if (!spot.isOccupied()) {
                pq.add(spot);
            }
        }

        return pq.isEmpty() ? null : pq.poll();
    }
}
