import java.lang.reflect.Array;
import java.util.Arrays;

class WiFi {
    /**
     * Implement your solution here
     */
    public static double computeDistance(int[] houses, int numOfAccessPoints) {
        try {
            if (houses == null || houses.length == 0 || numOfAccessPoints >= houses.length) {
                return 0.0;
            } else if (numOfAccessPoints <= 0) {
                return -1;
            }

            // Double-pivot QuickSort input houses (destructive)
            Arrays.sort(houses);

            int min = 0;
            int max = 10 * (houses[houses.length - 1] - houses[0]);

            int mid = 0; // initialise mid variable
            while (max > min) {
                mid = min + (max - min) / 2;
                if (coverable(houses, numOfAccessPoints, mid / 10.0)) {
                    max = mid;
                } else {
                    min = mid + 1;
                }
            }
            return min / 10.0;
        } catch (RuntimeException sumtingwong) { return -1; }
    }

    /**
     * Implement your solution here
     */
    public static boolean coverable(int[] houses, int numOfAccessPoints, double distance) {
        // Trivial: numOfAccessPoints >= houses.length (always true) / <= 0 (always false)

        int AccessPoints = 1;
        double endPt = 2 * distance + houses[0];
        for (int house : houses) {
            if (house > endPt) {
                endPt = 2 * distance + house;
                AccessPoints++;
            }
            if (AccessPoints > numOfAccessPoints) {
                return false;
            }
        }
        return true;
    }
}
