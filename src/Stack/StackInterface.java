package Stack;

/**
 * A simple stack interface (LIFO).
 * @param <T> type of elements
 */
public interface StackInterface<T> {

    /**
     * Add an element to the top of the stack
     * @param newEntry the element to be added
     */
    public void push(T newEntry);

    /**
     * Remove and return the top element
     * @return the removed element
     * @throws RuntimeException if stack is empty
     */
    public T pop();

    /**
     * Get the top element without removing it
     * @return the top element
     * @throws RuntimeException if stack is empty
     */
    public T peek();

    /**
     * Check if stack is empty
     * @return true if empty, false otherwise
     */
    public boolean isEmpty();

    /**
     * Get number of elements
     * @return size of stack
     */
    public int size();

    /**
     * Remove all elements
     */
    public void clear();
}
