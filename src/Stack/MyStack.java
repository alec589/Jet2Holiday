package Stack;

import List.MyArrayList;

/**
 * Implementation of Stack using MyArrayList
 * @param <T> type of elements
 */
public class MyStack<T> implements StackInterface<T> {

    private MyArrayList<T> list;

    /**
     * Constructor
     */
    public MyStack() {
        list = new MyArrayList<>();
    }

    /**
     * Push element to top of stack
     */
    @Override
    public void push(T newEntry) {
        list.add(newEntry);
    }

    /**
     * Pop top element
     */
    @Override
    public T pop() {
        if (isEmpty()) {
            throw new RuntimeException("Stack is empty");
        }
        return list.remove(list.size() - 1);
    }

    /**
     * Peek top element
     */
    @Override
    public T peek() {
        if (isEmpty()) {
            throw new RuntimeException("Stack is empty");
        }
        return list.get(list.size() - 1);
    }

    /**
     * Check if empty
     */
    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    /**
     * Get size
     */
    @Override
    public int size() {
        return list.size();
    }

    /**
     * Clear stack
     */
    @Override
    public void clear() {
        list.clear();
    }
}