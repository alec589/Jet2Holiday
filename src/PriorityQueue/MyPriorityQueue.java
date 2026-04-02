package PriorityQueue;

import java.util.EmptyStackException;
import List.MyArrayList;

public class MyPriorityQueue<T> implements PriorityQueueInterface<T> {

    // Inner class: store item + priority score
    private static class PriorityEntry<T> {
        T item;
        double priority;

        PriorityEntry(T item, double priority) {
            this.item = item;
            this.priority = priority;
        }
    }

    private MyArrayList<PriorityEntry<T>> heap;

    // Creates an empty priority queue
    public MyPriorityQueue() {
        heap = new MyArrayList<>();
    }

    // Swap two elements in heap
    private void swap(int i, int j) {
        PriorityEntry<T> temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }

    // Heapify up
    private void heapifyUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;

            if (heap.get(index).priority < heap.get(parent).priority) {
                swap(index, parent);
                index = parent;
            } else {
                break;
            }
        }
    }

    // Heapify down
    private void heapifyDown(int index) {
        int size = heap.size();

        while (true) {
            int left = 2 * index + 1;
            int right = 2 * index + 2;
            int smallest = index;

            if (left < size &&
                heap.get(left).priority < heap.get(smallest).priority) {
                smallest = left;
            }

            if (right < size &&
                heap.get(right).priority < heap.get(smallest).priority) {
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

    // Insert item with priority score
    @Override
    public void insert(T newEntry, double priorityScore) {
        heap.add(new PriorityEntry<>(newEntry, priorityScore));
        heapifyUp(heap.size() - 1);
    }

    // Remove best (smallest priority score)
    @Override
    public T removeBest() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }

        T best = heap.get(0).item;
        PriorityEntry<T> last = heap.remove(heap.size() - 1);

        if (!heap.isEmpty()) {
            heap.set(0, last);
            heapifyDown(0);
        }

        return best;
    }

    // Peek best
    @Override
    public T peekBest() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        return heap.get(0).item;
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

    // Clear queue
    @Override
    public void clear() {
        heap.clear();
    }
}