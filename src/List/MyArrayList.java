package List;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyArrayList<T> implements ListInterface<T> {
    private T[] list;
    private int numberOfEntries;
    private static final int DEFAULT_CAPACITY = 10;

    @SuppressWarnings("unchecked")
    public MyArrayList() {
        list = (T[]) new Object[DEFAULT_CAPACITY];
        numberOfEntries = 0;
    }

    @Override
    public void add(T newEntry) {
        checkCapacity();
        list[numberOfEntries] = newEntry;
        numberOfEntries++;
    }

    @Override
    public T get(int index) {
        checkIndex(index);
        return list[index];
    }

    @Override
    public T set(int index, T newEntry) {
        checkIndex(index);
        T oldEntry = list[index];
        list[index] = newEntry;
        return oldEntry;
    }

    @Override
    public T remove(int index) {
        checkIndex(index);
        T removed = list[index];

        for (int i = index; i < numberOfEntries - 1; i++) {
            list[i] = list[i + 1];
        }

        list[numberOfEntries - 1] = null;
        numberOfEntries--;
        return removed;
    }

    @Override
    public int size() {
        return numberOfEntries;
    }

    @Override
    public boolean isEmpty() {
        return numberOfEntries == 0;
    }

    @Override
    public void clear() {
        for (int i = 0; i < numberOfEntries; i++) {
            list[i] = null;
        }
        numberOfEntries = 0;
    }
    
    //Check if array is full;if full, create a new array with double size
    private void checkCapacity() {
        if (numberOfEntries >= list.length) {
            @SuppressWarnings("unchecked")
            T[] doubleSize = (T[]) new Object[list.length * 2];
            for (int i = 0; i < list.length; i++) {
            	doubleSize[i] = list[i];
            }
            list = doubleSize;
        }
    }
    
    // Make sure index is within valid range
    private void checkIndex(int index) {
        if (index < 0 || index >= numberOfEntries) {
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new ListIterator();
    }

    /**
     * Inner class that implements Iterator
     */
    private class ListIterator implements Iterator<T> {

        private int nextIndex = 0;

        @Override
        public boolean hasNext() {
            return nextIndex < numberOfEntries;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return list[nextIndex++];
        }
    }
}