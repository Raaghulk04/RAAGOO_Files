import java.util.Random;

public class SortingTester {
    public static boolean checkSort(ISort sorter, int size) {
        // Randomly generate array of size integers
        Random rng = new Random();
        KeyValuePair[] testArray = new KeyValuePair[size];
        KeyValuePair[] ogArray = new KeyValuePair[size];
        for (int i = 0; i < size; i++) {
            int r = rng.nextInt();
            testArray[i] = new KeyValuePair(r, i);
            ogArray[i] = new KeyValuePair(r, i);
        }

        // Print "amount charged"
        System.out.println("array size: " + size +
                ", sort cost: " + sorter.sort(testArray));

        // Test for ascending order
        for (int i = 0; i < size - 1; i++) {
            if (testArray[i].compareTo(testArray[i + 1]) == 1) {
                return false;
            }
        }

        // Test to ensure no missing/overwritten elements
        for (KeyValuePair k : ogArray) {
            boolean found = false;
            // Linear Search on testArray
            // Could Binary Search but dealing with
            // duplicate keys would make this very long
            for (KeyValuePair j : testArray) {
                if (k.compareTo(j) == 0 &&
                    k.getValue() == j.getValue()) {
                    found = true;
                    break; // Return to outer for-loop
                }
            }
            if (!found) {
                return false;
            }
        }

        return true;
    }

    // Insertion sort => Strictly larger; stable
    // Merge sort => Select left over right; stable
    // Quick sort => Unstable during swapping [j+1] and [i+1],
    //               given duplicate [j+1] between positions
    // Selection sort => Unstable during swapping [i] and min value,
    //                   given duplicate [i] between positions
    public static boolean isStable(ISort sorter, int size) {
        // TODO: implement this

        // Vacuously true
        if (size == 0 || size == 1) {
            return true;
        }

        // Randomly generate array of size integers
        Random rng = new Random();
        KeyValuePair[] testArray = new KeyValuePair[size];
        for (int i = 0; i < size; i++) {
            // Pigeonhole Principle; |set of integers| < |size|
            int r = rng.nextInt(0, size/2);
            testArray[i] = new KeyValuePair(r, i);
        }

        sorter.sort(testArray);

        for (int i = 0; i < size-1; i++) {
            // Linear Search on testArray
            for (int j = i+1; j < size; j++) {
                // Look for identical keys
                if (testArray[i].compareTo(testArray[j]) == 0) {
                    // If out of order, is unstable!
                    if (testArray[i].getValue() >= testArray[j].getValue()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    // Overload with input testArray
    public static boolean checkSort(ISort sorter, KeyValuePair[] testArray) {
        // Save a copy of input array
        int size = testArray.length;
        KeyValuePair[] ogArray = new KeyValuePair[size];
        for (int i = 0; i < size; i++) {
            ogArray[i] = new KeyValuePair(testArray[i].getKey(),
                    testArray[i].getValue());
        }

        // Print "amount charged"
        System.out.println("array size: " + size +
                ", sort cost: " + sorter.sort(testArray));

        // Test for ascending order
        for (int i = 0; i < size - 1; i++) {
            if (testArray[i].compareTo(testArray[i + 1]) == 1) {
                return false;
            }
        }

        // Test to ensure no missing/overwritten elements
        for (KeyValuePair k : ogArray) {
            boolean found = false;
            // Linear Search on testArray
            // Could Binary Search but dealing with
            // duplicate keys would make this very long
            for (KeyValuePair j : testArray) {
                if (k.compareTo(j) == 0 &&
                        k.getValue() == j.getValue()) {
                    found = true;
                    break; // Return to outer for-loop
                }
            }
            if (!found) {
                return false;
            }
        }

        return true;
    }

    // Overload with input testArray
    public static boolean isStable(ISort sorter, KeyValuePair[] testArray) {
        // TODO: implement this

        int size = testArray.length;
        // Vacuously true
        if (size == 0 || size == 1) {
            return true;
        }

        sorter.sort(testArray);

        for (int i = 0; i < size-1; i++) {
            // Linear Search on testArray
            for (int j = i+1; j < size; j++) {
                // Look for identical keys
                if (testArray[i].compareTo(testArray[j]) == 0) {
                    // If out of order, is unstable!
                    if (testArray[i].getValue() >= testArray[j].getValue()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static String testSorter(ISort sorter) {
        // To check for runtime, we can test each sorter
        // on multiple arrays of different sizes

        // Similarly, we can test Stability by testing on multiple different arrays

        // Max array size = 30 000; we shall start of with 30 tests per sorter

        boolean sort = true;
        boolean stable = true;
        for (int i = 1000; i <= 30000; i += 1000) {
            sort = sort && checkSort(sorter, i);
            stable = stable && isStable(sorter, i);
        }

        // This doesn't actually work; please ignore
        // I suspect while Prof Eldon does partition by < | >=
        // the used partition is <= | >
        // Hardcoding s3Test example {10, 10, 1}
        final KeyValuePair a = new KeyValuePair(10, 1);
        final KeyValuePair b = new KeyValuePair(10, 2);
        final KeyValuePair c = new KeyValuePair(1, 3);
        final KeyValuePair[] test = {a, b, c};

        sort = sort && checkSort(sorter, test);
        boolean s3Test = isStable(sorter, test);
        stable = stable && s3Test;

        return "checkSort: " + sort + ", isStable: " + stable
                + ", s3Test: " + s3Test;
    }

    public static void main(String[] args) {
        // TODO: implement this
        // initialise sorters
        ISort a = new SorterA();
        ISort b = new SorterB();
        ISort c = new SorterC();
        ISort d = new SorterD();
        ISort e = new SorterE();

        System.out.println("Sorter A");
        System.out.println(testSorter(a));
        System.out.println("Sorter B");
        System.out.println(testSorter(b));
        System.out.println("Sorter C");
        System.out.println(testSorter(c));
        System.out.println("Sorter D");
        System.out.println(testSorter(d));
        System.out.println("Sorter E");
        System.out.println(testSorter(e));
    }
}