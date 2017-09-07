package synthesizer;
import java.util.Iterator;

public class ArrayRingBuffer<T> extends AbstractBoundedQueue<T> {
    /* Index for the next dequeue or peek. */
    private int first;
    /* Index for the next enqueue. */
    private int last;
    /* Array for storing the buffer data. */
    private T[] rb;

    public class ARBIterator implements Iterator<T> {
        private int curr;

        public ARBIterator() {
            curr = first;
        }

        public boolean hasNext() {
            return curr != last;
        }

        public T next() {
            T val = rb[curr];
            curr = (curr + 1) % capacity;
            return val;
        }
    }

    /**
     * Create a new ArrayRingBuffer with the given capacity.
     */
    public ArrayRingBuffer(int capacity) {
        //       first, last, and fillCount should all be set to 0.
        //       this.capacity should be set appropriately. Note that the local variable
        //       here shadows the field we inherit from AbstractBoundedQueue, so
        //       you'll need to use this.capacity to set the capacity.
        rb = (T[]) new Object[capacity + 1];
        first = last = 0;
        fillCount = 0;
        this.capacity = capacity;
    }

    /**
     * Adds x to the end of the ring buffer. If there is no room, then
     * throw new RuntimeException("Ring buffer overflow"). Exceptions
     * covered Monday.
     */
    @Override
    public void enqueue(T x) {
        if (!isFull()) {
            rb[last] = x;
            last = (last + 1) % capacity;
            fillCount += 1;
        }
        else {
            throw new RuntimeException("Ring buffer overflow");
        }
    }

    /**
     * Dequeue oldest item in the ring buffer. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow"). Exceptions
     * covered Monday.
     */
    @Override
    public T dequeue() {
        if (!isEmpty()) {
            T val = rb[first];
            rb[first] = null;
            first = (first + 1) % capacity;
            fillCount -= 1;
            return val;
        }
        else {
            throw new RuntimeException("Ring buffer underflow");
        }
    }

    /**
     * Return oldest item, but don't remove it.
     */
    @Override
    public T peek() {
        if (!isEmpty()) {
            return rb[first];
        }
        else {
            throw new RuntimeException("Ring buffer underflow");
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new ARBIterator();
    }
}
