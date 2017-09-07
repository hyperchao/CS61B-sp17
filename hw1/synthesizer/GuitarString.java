package synthesizer;

import java.util.HashSet;
import java.util.Set;

//Make sure this class is public
public class GuitarString {
    /** Constants. Do not change. In case you're curious, the keyword final means
     * the values cannot be changed at runtime. We'll discuss this and other topics
     * in lecture on Friday. */
    private static final int SR = 44100;      // Sampling Rate
    private static final double DECAY = .996; // energy decay factor

    /* Buffer for storing sound data. */
    private BoundedQueue<Double> buffer;

    /* Create a guitar string of the given frequency.  */
    public GuitarString(double frequency) {
        //       Create a buffer with capacity = SR / frequency and initially fill with zeros.
        int capacity = (int)Math.round(SR / frequency);
        buffer = new ArrayRingBuffer<>(capacity);
        while (!buffer.isFull()) {
            buffer.enqueue(0.0);
        }
    }


    /* Pluck the guitar string by replacing the buffer with white noise. */
    public void pluck() {
        //       Dequeue everything in the buffer, and replace it with random numbers
        //       between -0.5 and 0.5.
        //       Make sure that your random numbers are different from each other.
        Set<Double> numbers = new HashSet<>();
        while (numbers.size() != buffer.capacity()) {
            numbers.add(Math.random() - 0.5);
        }
        while (!buffer.isEmpty()) {
            buffer.dequeue();
        }
        for (Double val : numbers) {
            buffer.enqueue(val);
        }
    }

    /* Advance the simulation one time step by performing one iteration of
     * the Karplus-Strong algorithm. 
     */
    public void tic() {
        //       Dequeue the front sample and enqueue a new sample that is
        //       the average of the two multiplied by the DECAY factor.
        //       Do not call StdAudio.play().
        Double a = buffer.dequeue();
        Double b = buffer.peek();
        buffer.enqueue((a + b) / 2 * DECAY);
    }

    /* Return the double at the front of the buffer. */
    public double sample() {
        return buffer.peek();
    }
}
