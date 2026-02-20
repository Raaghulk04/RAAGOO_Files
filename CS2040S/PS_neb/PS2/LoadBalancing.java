/**
 * Contains static routines for solving the problem of balancing m jobs on p processors
 * with the constraint that each processor can only perform consecutive jobs.
 */
public class LoadBalancing {

    /**
     * Checks if it is possible to assign the specified jobs to the specified number of processors such that no
     * processor's load is higher than the specified query load.
     *
     * @param jobSizes the sizes of the jobs to be performed
     * @param queryLoad the maximum load allowed for any processor
     * @param p the number of processors
     * @return true iff it is possible to assign the jobs to p processors so that no processor has more than queryLoad load.
     */
    public static boolean isFeasibleLoad(int[] jobSizes, int queryLoad, int p) {
        // TODO: Implement this

        // This is an O(n) solution, where n is the number of jobs
        int curr_p = 0; // the p being used
        int counter = queryLoad; // counts up to queryload

        if (jobSizes.length == 0) {
            return false;
        }

        for (int i : jobSizes) {
            if (i > queryLoad || curr_p > p) {
                return false;
            } else if (counter + i > queryLoad) {
                curr_p += 1;
                counter = i;
            } else {
                counter += i;
            }
        }

        return curr_p <= p;
    }

    /**
     * Returns the minimum achievable load given the specified jobs and number of processors.
     *
     * @param jobSizes the sizes of the jobs to be performed
     * @param p the number of processors
     * @return the maximum load for a job assignment that minimizes the maximum load
     */
    public static int findLoad(int[] jobSizes, int p) {
        // TODO: Implement this

        if (jobSizes.length == 0 || p <= 0) {
            return -1;
        }

        int totalLoad = 0;
        for (int i : jobSizes) { // O(n)
            totalLoad += i;
        }

        // Binary search between O(min) & O(max)
        // Iterations: log(max-min) = log(totalLoad - totalLoad/p)
        // Worst case: p tends from 1 -> Infinity; 1/p tends from 0 to 1; worst case
        // is hence log(totalLoad(1-1/p)) = log(totalLoad)
        // Worst case: totalLoad could be n jobs of M size; worst case
        // is hence log(nM)
        // Worst case: Number of Iterations = log(nM)
        // Each iteration cost: O(n)
        // Hence run time O(n log(nM))

        int min = Math.ceilDiv(totalLoad, p);
        int max = totalLoad;
        int mid = (min+max)/2;
        while (min < max) {
            if (isFeasibleLoad(jobSizes, mid, p)) {
                max = mid;
                mid = (min+max)/2;
            } else {
                min = mid+1;
                mid = (min+max)/2;
            }
        }

        return min;
    }

    // These are some arbitrary testcases.
    public static int[][] testCases = {
            {1, 3, 5, 7, 9, 11, 10, 8, 6, 4},
            {67, 65, 43, 42, 23, 17, 9, 100},
            {4, 100, 80, 15, 20, 25, 30},
            {2, 3, 4, 5, 6, 7, 8, 100, 99, 98, 97, 96, 95, 94, 93, 92, 91, 90, 89, 88, 87, 86, 85, 84, 83},
            {7}
    };

    /**
     * Some simple tests for the findLoad routine.
     */
    public static void main(String[] args) {
        for (int p = 1; p < 30; p++) {
            System.out.println("Processors: " + p);
            for (int[] testCase : testCases) {
                System.out.println(findLoad(testCase, p));
            }
        }
    }
}
