public class ArrayDeque<Type> implements Deque<Type> {

    /** first item is at position front, last item is at 1 place before rear,
     * treat array as a circular deque
     */
    private Type[] array;
    private int front, rear;
    private int size;

    /** init an array of type Type and of size 8. */
    public ArrayDeque() {
        array = (Type []) new Object[8];
        front = rear = 0;
        size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    private void resize() {
        Type[] a = (Type []) new Object[size * 2];
        if (front < rear) {
            System.arraycopy(array, front, a, 0, size);
        }
        else {
            int part = array.length - front;
            System.arraycopy(array, front, a, 0, part);
            System.arraycopy(array, 0, a, part, rear);
        }
        array = a;
        front = 0;
        rear = size;
    }

    private void checkGrowth() {
        if (size == array.length) {
            resize();
        }
    }

    @Override
    public void addFirst(Type item) {
        checkGrowth();
        front = (front - 1 + array.length) % array.length;
        array[front] = item;
        size += 1;
    }

    @Override
    public void addLast(Type item) {
        checkGrowth();
        array[rear] = item;
        rear = (rear + 1) % array.length;
        size += 1;
    }

    private void checkShrink() {
        if (array.length >= 16 && size * 1.0 / array.length < 0.25) {
            resize();
        }
    }

    @Override
    public Type removeFirst() {
        if (size() == 0) {
            return null;
        }
        Type item = array[front];
        array[front] = null;
        front = (front + 1) % array.length;
        size -= 1;
        checkShrink();
        return item;
    }

    @Override
    public Type removeLast() {
        if (size() == 0) {
            return null;
        }
        rear = (rear - 1 + array.length) % array.length;
        Type item = array[rear];
        array[rear] = null;
        size -= 1;
        checkShrink();
        return item;
    }

    @Override
    public Type get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        return array[index];
    }

    @Override
    public void printDeque() {
        if (size == 0) {
            return;
        }
        if (front < rear) {
            for (int i = front; i < rear; i++) {
                System.out.print(array[i]);
                System.out.print(' ');
            }
        }
        else {
            for (int i = front; i < array.length; i++) {
                System.out.print(array[i]);
                System.out.print(' ');
            }
            for (int i = 0; i < rear; i++) {
                System.out.print(array[i]);
                System.out.print(' ');
            }
        }
        System.out.print('\n');
    }
}
