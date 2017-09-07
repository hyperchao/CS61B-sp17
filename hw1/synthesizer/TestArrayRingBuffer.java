package synthesizer;
import org.junit.Test;

import static org.junit.Assert.*;

/** Tests the ArrayRingBuffer class.
 *  @author Josh Hug
 */

public class TestArrayRingBuffer {
    @Test
    public void someTest() {
        ArrayRingBuffer<Integer> arb = new ArrayRingBuffer<>(4);

        assertTrue(arb.isEmpty());

        for (int i = 0; i < 4; i++) {
            arb.enqueue(i * i);
        }

        assertTrue(arb.isFull());
        assertEquals(4, arb.fillCount());

        for (Integer x : arb) {
            System.out.println(x);
        }

        for (int i = 0; i < 4; i++) {
            System.out.println(arb.dequeue());
        }

        assertTrue(arb.isEmpty());
    }

    /** Calls tests for ArrayRingBuffer. */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestArrayRingBuffer.class);
    }
} 
