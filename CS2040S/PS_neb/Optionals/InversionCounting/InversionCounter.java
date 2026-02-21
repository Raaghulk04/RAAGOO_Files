class InversionCounter {

    public static void merge(int[] arr, int[] helpArr, int left, int mid, int right) {
        int i = 0;
        int idx1 = left;
        int idx2 = mid + 1;
        while (idx1 <= mid && idx2 <= right) {
            if (arr[idx1] <= arr[idx2]) {
                helpArr[i] = arr[idx1];
                i++;
                idx1++;
            } else {
                helpArr[i] = arr[idx2];
                i++;
                idx2++;
            }
        }

        if (idx1 > mid) {
            while (idx2 <= right) {
                helpArr[i] = arr[idx2];
                i++;
                idx2++;
            }
        } else if (idx2 > right) {
            while (idx1 <= mid) {
                helpArr[i] = arr[idx1];
                i++;
                idx1++;
            }
        }

        for (int j = left; j <= right; j++) {
            arr[j] = helpArr[j - left];
        }
    }

    public static long countHelper(int[] arr, int[] helpArr, int left, int right) {
        if (left == right) {
            return 0L;
        } else {
            int mid = left + (right - left) / 2;
            long l1 = countHelper(arr, helpArr, left, mid);
            long l2 = countHelper(arr, helpArr, mid+1, right);
            long l3 = mergeAndCount(arr, left, mid, mid+1, right);
            merge(arr, helpArr, left, mid, right);
            return l1 + l2 + l3;
        }
    }

    public static long countSwaps(int[] arr) {
        int[] helpArr = new int[arr.length];
        return countHelper(arr, helpArr,0, arr.length-1);
    }

    /**
     * Given an input array so that arr[left1] to arr[right1] is sorted and arr[left2] to arr[right2] is sorted
     * (also left2 = right1 + 1), merges the two so that arr[left1] to arr[right2] is sorted, and returns the
     * minimum amount of adjacent swaps needed to do so.
     */
    public static long mergeAndCount(int[] arr, int left1, int right1, int left2, int right2) {
        // invariant: on the i-th iteration, the first i characters are in the correct position
        long counter = 0;
        int iter = left1;
        while (left1 <= right1 && left2 <= right2) {
            if (arr[left1] <= arr[left2]) {
                left1++;
                iter++;
            } else {
                counter += left2 - iter;
                left2++;
                iter++;
            }
        }
        return counter;
    }
}
