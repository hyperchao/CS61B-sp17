public class LinkedListDeque<Type> implements Deque<Type> {

    private class ListNode {
        private ListNode prev, next;
        private Type item;

        private ListNode(ListNode p, ListNode n, Type i) {
            prev = p;
            next = n;
            item = i;
        }
    }

    /** sentinel.prev store the first node, sentinel.next store the last node.
     *  if LinkedListDeque is empty, then sentinel.prev == sentinel.prev == sentinel.
      */
    private ListNode sentinel;
    private int size;

    public LinkedListDeque() {
        sentinel = new ListNode(null, null, null);
        sentinel.prev = sentinel.next = sentinel;
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

    @Override
    public void addFirst(Type item) {
        ListNode first = sentinel.next;
        sentinel.next = new ListNode(sentinel, first, item);
        first.prev = sentinel.next;
        size += 1;
    }

    @Override
    public void addLast(Type item) {
        ListNode last  = sentinel.prev;
        sentinel.prev = new ListNode(last, sentinel, item);
        last.next = sentinel.prev;
        size += 1;
    }

    @Override
    public Type removeFirst() {
        if (size == 0) {
            return null;
        }
        ListNode first = sentinel.next;
        sentinel.next = first.next;
        sentinel.next.prev = sentinel;
        size -= 1;
        return first.item;
    }

    @Override
    public Type removeLast() {
        if (size == 0) {
            return null;
        }
        ListNode last = sentinel.prev;
        sentinel.prev = last.prev;
        sentinel.prev.next = sentinel;
        size -= 1;
        return last.item;
    }

    @Override
    public Type get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        ListNode p = sentinel.next;
        while (index > 0) {
            p = p.next;
            index -= 1;
        }
        return p.item;
    }

    private Type getRecursive(ListNode p, int index) {
        if (index == 0) {
            return p.item;
        }
        return getRecursive(p.next, index - 1);
    }

    public Type getRecursive(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        return getRecursive(sentinel.next, index);
    }

    @Override
    public void printDeque() {
        for (ListNode p = sentinel.next; p != sentinel; p = p.next) {
            System.out.print(p.item);
            System.out.print(' ');
        }
        System.out.print('\n');
    }
}
