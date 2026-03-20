package Parking;
/**
 * An interface that describes the operations of a priority queue.
 */
public interface PriorityQueueInterface<T> {
    /**
     * Adds a new entry to this priority queue.
     * @param newEntry An object to be added.
     */
    public void insert(T newEntry);

    /**
     * Removes and returns the highest priority entry.
     * @return The object with the highest priority.
     * @throws EmptyStackException if the priority queue is empty.
     */
    public T removeBest();

    /**
     * Retrieves the highest priority entry without removing it.
     * @return The object with the highest priority.
     * @throws EmptyStackException if the priority queue is empty.
     */
    public T peekBest();

    /**
     * Detects whether this priority queue is empty.
     * @return true if the priority queue is empty.
     */
    public boolean isEmpty();

    /**
     * Gets the number of entries in this priority queue.
     * @return The number of entries.
     */
    public int size();

    /**
     * Removes all entries from this priority queue.
     */
    public void clear();

}
