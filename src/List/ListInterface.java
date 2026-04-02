package List;

import java.util.Iterator;

/**
 * A simple list interface.
 * Stores elements in order and allows basic operations.
 *
 * @param <T> type of elements
 */
public interface ListInterface<T> extends Iterable<T> {

    /**
     * Add an element to the end of the list
     * @param newEntry    the element to be added
     */
	public void add(T newEntry);

    /**
     * Get element at given index
     *  @param index   the position of the element 
     *  @throws IndexOutOfBoundsException if index is out of range
     */
	public T get(int index);

    /**
     * Replace element at index
     * @param index the position of the element
     * @param newEntry the new element to replace the old one
     * @return the element previously at the specified position
     * @throws IndexOutOfBoundsException if index is out of range
     */
	public T set(int index, T newEntry);

    /**
     * Remove element at index
     * @param index the position of the element
     * @return the removed element
     * @throws IndexOutOfBoundsException if index is out of range
     */
	public T remove(int index);

    /**
     * Get number of elements
     * 
     * @return the size of the list
     */
	public int size();

    /**
     * Check if list is empty
     * @return true if the list contains no elements, false otherwise
     */
	public boolean isEmpty();

    /**
     * Remove all elements
     */
	public void clear();

    /**
     * Return iterator for loop
     * @return an iterator for traversing the list
     */
	
    Iterator<T> iterator();
}