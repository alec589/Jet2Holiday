package Parking;
//需要孙作霖的ArrayList
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.PriorityQueue;

public class ParkingSpotPriorityQueue implements PriorityQueueInterface<Spot> {

	private ArrayList<Spot> heap;
	private double userX;
	private double userY;
	private double distanceWeight;
	private double priceWeight;
	/**
     * Creates an empty priority queue for parking spots.
     *
     * @param userX the user's x-coordinate
     * @param userY the user's y-coordinate
     * @param distanceWeight the weight assigned to distance
     * @param priceWeight the weight assigned to price
     */
    public ParkingSpotPriorityQueue(double userX, double userY, double distanceWeight, double priceWeight) {
        this.userX = userX;
        this.userY = userY;
        this.distanceWeight = distanceWeight;
        this.priceWeight = priceWeight;

        heap = new ArrayList<>();
    }

    /**
     * Calculates the recommendation score of a parking spot.
     * Lower score means higher priority.
     * score = distanceWeight * distance + priceWeight * price
     */
    private double calculateScore(Spot spot) {
        double dx = spot.getX() - userX;
        double dy = spot.getY() - userY;
        double distance = Math.sqrt(dx * dx + dy * dy);

        return distanceWeight * distance + priceWeight * spot.getPricePerHour();
    }

    
    private void swap(int i, int j) {
        Spot temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }
    
    private void heapifyUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;

            if (calculateScore(heap.get(index)) < calculateScore(heap.get(parent))) {
                swap(index, parent);
                index = parent;
            } else {
                break;
            }
        }
    }
   
    /**
     * Restores the heap order by moving the element at the given index downward.
     * This method is used after removing the root element.
     *
     * @param index the index of the element to adjust
     */
    private void heapifyDown(int index) {
        int size = heap.size();

        while (true) {
            int left = 2 * index + 1;
            int right = 2 * index + 2;
            int smallest = index;

            if (left < size &&
                calculateScore(heap.get(left)) < calculateScore(heap.get(smallest))) {
                smallest = left;
            }

            if (right < size &&
                calculateScore(heap.get(right)) < calculateScore(heap.get(smallest))) {
                smallest = right;
            }

            if (smallest != index) {
                swap(index, smallest);
                index = smallest;
            } else {
                break;
            }
        }
    }

    /**
     * Adds a new parking spot to this priority queue.
     * The new spot is inserted at the end of the heap,
     * then moved upward to restore heap order.
     *
     * @param newEntry the parking spot to be added
     */
    @Override
    public void insert(Spot newEntry) {
        heap.add(newEntry);
        heapifyUp(heap.size() - 1);
    }

    /**
     * Removes and returns the best parking spot.
     * The root is removed, the last element is moved to the root,
     * and heapifyDown is applied to restore heap order.
     *
     * @return the best parking spot
     * @throws EmptyStackException if the priority queue is empty
     */
    @Override
    public Spot removeBest() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }

        Spot best = heap.get(0);
        Spot last = heap.remove(heap.size() - 1);

        if (!heap.isEmpty()) {
            heap.set(0, last);
            heapifyDown(0);
        }

        return best;
    }

    /**
     * Retrieves the best parking spot without removing it.
     *
     * @return the best parking spot
     * @throws EmptyStackException if the priority queue is empty
     */
    @Override
    public Spot peekBest() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        return heap.get(0);
    }

    /**
     * Detects whether this priority queue is empty.
     *
     * @return true if empty, false otherwise
     */
    @Override
    public boolean isEmpty() {
        return heap.isEmpty();
    }

    /**
     * Gets the number of entries in this priority queue.
     *
     * @return the number of entries
     */
    @Override
    public int size() {
        return heap.size();
    }

    /**
     * Removes all entries from this priority queue.
     */
    @Override
    public void clear() {
        heap.clear();
    }
}
