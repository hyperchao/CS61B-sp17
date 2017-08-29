public class MaxOfArray {
    
    private static int max(int []m) {
        int max_i = 0;
        for (int i = 1; i < m.length; i++) {
            if (m[i] > m[max_i]) {
                max_i = i;
            }
        }
        return m[max_i];
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter an int array.");
            System.out.println("e.g 1, 3, 5, 7");
        }
        int[] numbers = new int[args.length];
        for (int i = 0; i < args.length; i++) {
            try {
                numbers[i] = Integer.parseInt(args[i]);
            }
            catch (NumberFormatException e) {
                System.out.printf("%s is not a valid number.\n", args[i]);
            }
        }
        System.out.println("the max value of the array is " + max(numbers));
    }
}
