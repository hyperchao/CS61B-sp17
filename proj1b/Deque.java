public interface Deque<T> {
    /** return the size of the deque. */
    public int size();

    /** is the deque empty? */
    public boolean isEmpty();

    /** add item to the front of the deque. */
    public void addFirst(T item);

    /** add item to the back of the deque. */
    public void addLast(T item);

    /** remove first item. */
    public T removeFirst();

    /** remove last item. */
    public T removeLast();

    /* get the i th item. */
    public T get(int index);

    /** print deque to standard output. */
    public void printDeque();
}
