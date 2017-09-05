import static org.junit.Assert.*;
import org.junit.Test;

public class TestArrayDeque1B {
    @Test
    public void testRandomMethods() {
        StudentArrayDeque<Integer> sad = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> ad = new ArrayDequeSolution<>();
        OperationSequence os = new OperationSequence();
        int count = 4;
        for (int i = 0; i < count; i++) {
            Integer val = StdRandom.uniform(1000);
            os.addOperation(new DequeOperation("addFirst", val));
            sad.addFirst(val);
            ad.addFirst(val);
        }
        for (int i = 0; i < count; i++) {
            Integer val = StdRandom.uniform(1000);
            os.addOperation(new DequeOperation("addLast", val));
            sad.addLast(val);
            ad.addLast(val);
        }
        for (int i = 0; i < count; i++) {
            os.addOperation(new DequeOperation("removeFirst"));
            Integer actual = sad.removeFirst();
            Integer expect = ad.removeFirst();
            assertEquals(os.toString(), expect, actual);
        }
        for (int i = 0; i < count; i++) {
            os.addOperation(new DequeOperation("removeLast"));
            Integer actual = sad.removeLast();
            Integer expect = ad.removeLast();
            assertEquals(os.toString(), expect, actual);
        }
    }
}
