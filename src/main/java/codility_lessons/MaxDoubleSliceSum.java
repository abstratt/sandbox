package codility_lessons;

/**
 * Find the maximal sum of any double slice.
 */
// https://codility.com/programmers/lessons/9-maximum_slice_problem/max_double_slice_sum/
public class MaxDoubleSliceSum {
    public int solve(int[] values, int n) {
        int[] leftSums = new int[n];
        int[] rightSums = new int[n];
        // record the best left-side and right-side sums for each possible value of Y
        for (int y = 2; y < n - 1; y++) {
            leftSums[y] = Math.max(leftSums[y-1] + values[y-1], 0);
            int reverseY = n - y - 1;
            rightSums[reverseY] = Math.max(rightSums[reverseY + 1] + values[reverseY + 1], 0);
        }
        int maxSlice = 0;
        for (int y = 1; y < n - 1; y++) {
            maxSlice = Math.max(maxSlice,  leftSums[y] + rightSums[y]);
        }
        return maxSlice;
    }
}
