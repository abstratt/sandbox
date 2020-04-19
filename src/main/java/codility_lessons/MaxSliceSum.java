package codility_lessons;

/**
 * Find a maximum sum of a compact subsequence of array elements
 */
// https://codility.com/programmers/lessons/9-maximum_slice_problem/max_slice_sum/
public class MaxSliceSum {
    public int solve(int[] values) {
        int minValue = -1000000;
        int maxSlice = minValue;
        int maxEnding = minValue;
        for (int i = 0; i < values.length; i++) {
            maxEnding = Math.max(values[i], maxEnding + values[i]);
            maxSlice = Math.max(maxSlice, maxEnding);
        }
        return maxSlice;
    }
}
