/**
 * The Optimization class contains a static routine to find the maximum in an array that changes direction at most once.
 */
public class Optimization {

    /**
     * A set of test cases.
     */
    static int[][] testCases = {
            {1, 3, 5, 7, 9, 11, 10, 8, 6, 4},
            {67, 65, 43, 42, 23, 17, 9, 100},
            {4, -100, -80, 15, 20, 25, 30},
            {2, 3, 4, 5, 6, 7, 8, 100, 99, 98, 97, 96, 95, 94, 93, 92, 91, 90, 89, 88, 87, 86, 85, 84, 83}
    };

    /**
     * Returns the maximum item in the specified array of integers which changes direction at most once.
     *
     * @param dataArray an array of integers which changes direction at most once.
     * @return the maximum item in data Array
     */
    public static int searchMax(int[] dataArray) {
        // TODO: Implement this
        if (dataArray == null || dataArray.length == 0) return 0;

        int n = dataArray.length;

        if (n == 1) return dataArray[0];

        // If the first two elements are decreasing, compare the first and the last element,
        // since in this case it would be either the first or the last element which is the maximum element.
        if (dataArray[0] > dataArray[1]) return java.lang.Math.max(dataArray[0], dataArray[n - 1]);

        // If the first two elements are increasing, its peak (maximum element) would be somewhere in the array.
        // Use binary search to find for the peak.
        int left = 0;
        int right = n - 1;

        while (left <= right) {
            // The following calculation is better than simply (low + high) / 2
            // cuz if low and high are large numbers,
            // it may result in integer overflow (exceeding the max value the data type can hold) and return an incorrect result.
            // Besides, the following eqn performs integer division, so it is equivalent to math floor function.
            int mid = left + (right - left) / 2;

            // If the array index is out of bound when checking boundaries.
            // Eg. for {3, 4}, mid == 0, if we wanna check for dataArray[mid - 1], it might throw an error.
            int prev = (mid == 0) ? Integer.MIN_VALUE : dataArray[mid - 1];
            int next = (mid == n - 1) ? Integer.MIN_VALUE : dataArray[mid + 1];

            // If luckily the peak is at index mid
            if (dataArray[mid] > next && dataArray[mid] > prev) {
                return dataArray[mid];
            } else if (dataArray[mid] > next) {
                // decreasing trend at the middle, thus the peak is at the left part of the (sub)array, search for it
                right = mid - 1;
            } else {
                // increasing trend, the peak is at the right part of the (sub)array
                left = mid + 1;
            }
        }

        return 0;
    }

    /**
     * A routine to test the searchMax routine.
     */
    public static void main(String[] args) {
        for (int[] testCase : testCases) {
            System.out.println(searchMax(testCase));
        }
    }
}
