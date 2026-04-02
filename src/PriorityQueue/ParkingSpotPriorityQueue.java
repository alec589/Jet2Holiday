package PriorityQueue;

import java.util.EmptyStackException;

import List.MyArrayList;
import Parking.Spot;

public class ParkingSpotPriorityQueue implements PriorityQueueInterface<Spot> {

	//Inner class: Store Spot + score
    private static class ScoredSpot {
        Spot spot;
        double score;

        ScoredSpot(Spot spot, double score) {
            this.spot = spot;
            this.score = score;
        }
    }

    private MyArrayList<ScoredSpot> heap;

    // Creates an empty priority queue.
    public ParkingSpotPriorityQueue() {
        heap = new MyArrayList<>();
    }

    //Swap two elements in heap
    private void swap(int i, int j) {
        ScoredSpot temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }

    // Heapify up (used after insert)
    private void heapifyUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;

            if (heap.get(index).score < heap.get(parent).score) {
                swap(index, parent);
                index = parent;
            } else {
                break;
            }
        }
    }

    //Heapify down (used after remove)
    private void heapifyDown(int index) {
        int size = heap.size();

        while (true) {
            int left = 2 * index + 1;
            int right = 2 * index + 2;
            int smallest = index;

            if (left < size &&
                heap.get(left).score < heap.get(smallest).score) {
                smallest = left;
            }

            if (right < size &&
                heap.get(right).score < heap.get(smallest).score) {
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

    //Insert with score
    @Override
    public void insert(Spot spot, double score) {
        heap.add(new ScoredSpot(spot, score));
        heapifyUp(heap.size() - 1);
    }

    // Remove best (smallest score)
    @Override
    public Spot removeBest() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }

        Spot best = heap.get(0).spot;
        ScoredSpot last = heap.remove(heap.size() - 1);

        if (!heap.isEmpty()) {
            heap.set(0, last);
            heapifyDown(0);
        }

        return best;
    }

    // Peek best (without removing)
    @Override
    public Spot peekBest() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        return heap.get(0).spot;
    }

    // Check if empty
    @Override
    public boolean isEmpty() {
        return heap.isEmpty();
    }

    // Get size
    @Override
    public int size() {
        return heap.size();
    }

    //Clear queue
    @Override
    public void clear() {
        heap.clear();
    }

    
}