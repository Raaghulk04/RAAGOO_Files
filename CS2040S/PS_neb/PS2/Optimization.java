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
        int min = 0;
        int max = dataArray.length - 1;
        int mid = (min + max)/2;

        if (max < min) {
            return 0;
        } else if (min == max) {
            return dataArray[min];
        } else if (dataArray[min]>dataArray[min+1] && dataArray[max-1]<dataArray[max]) {
            return Math.max(dataArray[min], dataArray[max]);
        } else {
            while (min <= max) {
                if (min == max) {
                    return dataArray[min];
                } else {
                    if (dataArray[mid] < dataArray[mid + 1]) {
                        min = mid + 1; // to index of (known) largest element
                        mid = (min + max) / 2;
                    } else if (dataArray[mid] > dataArray[mid + 1]) {
                        max = mid; // to index of (known) largest element
                        mid = (min + max) / 2;
                    } else {
                        return 0;
                    }
                }
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
