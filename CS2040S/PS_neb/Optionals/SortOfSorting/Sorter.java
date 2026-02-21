class Sorter {
    public static void sortStrings(String[] arr) {
        // TODO: implement your sorting function here
        try {
            // Naive solution: Insertion Sort
            int len = arr.length;
            for (int i = 1; i < len; i++) {
                for (int j = i; j > 0; j--) {
                    if (isGreaterThan(arr[j - 1], arr[j])) {
                        String temp = arr[j];
                        arr[j] = arr[j - 1];
                        arr[j - 1] = temp;
                    }
                }
            }
        }
        catch(RuntimeException e) {
            return;
        }
    }

    public static boolean isGreaterThan(String str1, String str2) {
        // returns true if str1 is strictly greater than str2
        char c1 = str1.charAt(0);
        char c2 = str2.charAt(0);

        if (c1 == c2) {
            c1 = str1.charAt(1);
            c2 = str2.charAt(1);
        }

        return c1 > c2;
    }
}
