import java.util.Arrays;

class WiFi {
    static class Pair implements Comparable<Pair> {
        private int key;
        private int value;

        public Pair(int key, int value) {
            this.key = key;
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }

        @Override
        public int compareTo(Pair p) {
            return this.value - p.value;
        }
    }

    /**
     * Implement your solution here
     */
    public static double computeDistance(int[] houses, int numOfAccessPoints) { return 0.0; }

    /**
     * Implement your solution here
     */
    public static boolean coverable(int[] houses, int numOfAccessPoints, double distance) {
        // Trivial: numOfAccessPoints >= houses.length (always true) / <= 0 (always false)
        int surplus = numOfAccessPoints - houses.length;
        if (surplus >= 0) {
            return true;
        } else if (numOfAccessPoints <= 0) {
            return false;
        }

        // Double-pivot QuickSort input houses (destructive)
        Arrays.sort(houses);

        // Partition along biggest gap, for every "missing" AccessPoint
        // e.g. {1, 3, 10}, num = 2 => partition {1, 3} and {10}
        Pair[] distNextHse = new Pair[houses.length - 1];
        for (int i = 0; i < distNextHse.length; i++) {
            distNextHse[i] = new Pair(houses[i+1] - houses[i], i);
        }
        // Timsort 'key-value pairs'; Key = dist, Value = index of "left"/"1st" house
        Arrays.sort(distNextHse);

        // surplus must be negative here; i.e. "missing" AccessPoint
        int last = distNextHse.length - 1;
        int[] stopPts = new int[-surplus];
        for (int i = 0; i < -surplus; i++) {
            stopPts[i] = distNextHse[last - i].getValue();
        }

        // Double-pivot QuickSort stopPts houses (destructive)
        Arrays.sort(stopPts);

        int start = houses[0];
        for (int stopPt : stopPts) {
            // if the distance the partition runs
            if ((houses[stopPt] - start) > (2 * distance)) {
                return false;
            } else {
                start = houses[stopPt + 1];
            }
        }
        return true;
    }
}
