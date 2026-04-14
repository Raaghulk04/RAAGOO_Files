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
        int pUsed = 1; // number of processors used
        int currLoad = 0; // workload of current processor

        if (jobSizes == null || jobSizes.length == 0 || p <= 0 || queryLoad <= 0) return false;

        for (int size : jobSizes) {
            // a single job size is bigger than the allowed load, thus fail immediately
            if (size > queryLoad) return false;

            // greedy
            if (currLoad + size <= queryLoad) {
                currLoad += size; // add the job size to the load of current processor
            } else {
                pUsed++; // use a new processor
                currLoad = size; // the new processor is assigned the current size

                if (pUsed > p) return false;
                // return false once number of processors used is greater than that of processors given
                // to avoid further calculation and comparison
                // and to save some time
            }
        }

        // Since the case where number of processors used is greater than that of processors given is ruled out in the for loop,
        // the case here will certainly be feasible as pUsed <= p
        return true;
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

        if (jobSizes == null || jobSizes.length == 0 || p <= 0) return -1;

        int low = 0; // representing the smallest possible load (not necessarily achievable)
        int high = 0; // representing the largest possible load (achievable for sure, but not efficient)

        for (int size : jobSizes) {
            low = Math.max(low, size); // the largest job size among job sizes
            high += size; // the sum of all job sizes
        }

        // ans must be initialised at first
        // cuz the compiler is worried that we won't even enter the while loop or the first if condition and initialise ans
        // so we initialise it with high
        // as the sum of all jobs is always a feasible load
        int ans = high;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (isFeasibleLoad(jobSizes, mid, p)) {
                ans = mid;
                high = mid - 1; // try smaller workload
            } else {
                low = mid + 1; // try larger workload
            }
            // I switched between high and low initially and realised that we are finding the min element, not the max
        }

        return ans;
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
