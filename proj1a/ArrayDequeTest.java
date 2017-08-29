public class ArrayDequeTest {

    /* Utility method for printing out empty checks. */
    public static boolean checkEmpty(boolean expected, boolean actual) {
        if (expected != actual) {
            System.out.println("isEmpty() returned " + actual + ", but expected: " + expected);
            return false;
        }
        return true;
    }

    /* Utility method for printing out empty checks. */
    public static boolean checkSize(int expected, int actual) {
        if (expected != actual) {
            System.out.println("size() returned " + actual + ", but expected: " + expected);
            return false;
        }
        return true;
    }

    /* Prints a nice message based on whether a test passed. */
    public static void printTestStatus(boolean passed) {
        if (passed) {
            System.out.println("Test passed!\n");
        } else {
            System.out.println("Test failed!\n");
        }
    }

    public static void addIsEmptySizeTest() {
        System.out.println("Running add/isEmpty/Size test.");

        ArrayDeque<String> ad1 = new ArrayDeque<>();
        ArrayDeque<String> ad2 = new ArrayDeque<>();

        boolean passed = checkEmpty(true, ad1.isEmpty());

        ad1.addFirst("front");
        ad2.addLast("back");
        passed = checkSize(1, ad1.size()) && passed;
        passed = checkEmpty(false, ad1.isEmpty()) && passed;
        passed = checkSize(1, ad2.size()) && passed;
        passed = checkEmpty(false, ad2.isEmpty()) && passed;

        ad1.addLast("middle");
        ad2.addFirst("middle");
        passed = checkSize(2, ad1.size()) && passed;
        passed = checkSize(2, ad2.size()) && passed;

        ad1.addLast("back");
        ad2.addFirst("front");
        passed = checkSize(3, ad1.size()) && passed;
        passed = checkSize(3, ad2.size()) && passed;

        System.out.println("Printing out deque: ");
        ad1.printDeque();
        ad2.printDeque();

        printTestStatus(passed);

    }

    /** Adds an item, then removes an item, and ensures that dll is empty afterwards. */
    public static void addRemoveTest() {

        System.out.println("Running add/remove test.");

        ArrayDeque<Integer> ad1 = new ArrayDeque<>();
        // should be empty
        boolean passed = checkEmpty(true, ad1.isEmpty());

        ad1.addFirst(10);
        // should not be empty
        passed = checkEmpty(false, ad1.isEmpty()) && passed;

        ad1.removeFirst();
        // should be empty
        passed = checkEmpty(true, ad1.isEmpty()) && passed;

        ad1.addLast(1);
        ad1.addLast(3);
        ad1.addLast(5);
        ad1.addLast(7);
        ad1.removeFirst();
        ad1.removeLast();

        ad1.printDeque();

        printTestStatus(passed);

    }

    public static void simpleTest() {
        ArrayDeque<Integer> ad = new ArrayDeque<>();

        for (int i = 0; i < 100; i++) {
            ad.addFirst(i);
        }

        for (int i = 0; i < 49; i++) {
            ad.removeFirst();
            ad.removeLast();
        }

        for (int i = 0; i < 5; i++) {
            ad.addLast(i*i);
            ad.addFirst(i*i);
        }

        System.out.println(ad.size());
        ad.printDeque();
    }

    public static void main(String[] args) {
        System.out.println("Running tests.\n");
        addIsEmptySizeTest();
        addRemoveTest();
        simpleTest();
    }
}
