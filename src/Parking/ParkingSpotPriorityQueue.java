package Parking;

import java.util.EmptyStackException;
import java.util.PriorityQueue;

public class ParkingSpotPriorityQueue implements PriorityQueueInterface<Spot> {

    private PriorityQueue<Spot> pq;
    private double userX;
    private double userY;
    private double distanceWeight;
    private double priceWeight;

    /**
     * Creates a priority queue for parking spot recommendation.
     * @param userX the user's x-coordinate
     * @param userY the user's y-coordinate
     * @param distanceWeight the weight for distance
     * @param priceWeight the weight for price
     */
    public ParkingSpotPriorityQueue(double userX, double userY, double distanceWeight, double priceWeight) {
        this.userX = userX;
        this.userY = userY;
        this.distanceWeight = distanceWeight;
        this.priceWeight = priceWeight;

        pq = new PriorityQueue<>((a, b) -> {
            double scoreA = calculateScore(a);
            double scoreB = calculateScore(b);
            return Double.compare(scoreA, scoreB);
        });
    }

    /**
     * Calculates the recommendation score of a parking spot.
     * Lower score means higher priority.
     * 
     * score = distanceWeight * distance + priceWeight * price
     */
    private double calculateScore(Spot spot) {
        double dx = spot.getX() - userX;
        double dy = spot.getY() - userY;
        double distance = Math.sqrt(dx * dx + dy * dy);

        return distanceWeight * distance + priceWeight * spot.getPricePerHour();
    }

    /**
     * Adds a new spot to this priority queue.
     * @param newEntry a spot to be added
     */
    @Override
    public void insert(Spot newEntry) {
        pq.offer(newEntry);
    }

    /**
     * Removes and returns the best spot.
     * @return the best spot
     * @throws EmptyStackException if the queue is empty
     */
    @Override
    public Spot removeBest() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        return pq.poll();
    }

    /**
     * Retrieves the best spot without removing it.
     * @return the best spot
     * @throws EmptyStackException if the queue is empty
     */
    @Override
    public Spot peekBest() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        return pq.peek();
    }

    /**
     * Detects whether this priority queue is empty.
     * @return true if empty, false otherwise
     */
    @Override
    public boolean isEmpty() {
        return pq.isEmpty();
    }

    /**
     * Gets the number of entries in this priority queue.
     * @return the number of entries
     */
    @Override
    public int size() {
        return pq.size();
    }

    /**
     * Removes all entries from this priority queue.
     */
    @Override
    public void clear() {
        pq.clear();
    }
}
