package codility_lessons;

import java.util.Arrays;

/**
 * Given array of integers, find the lowest absolute sum of elements
 */
// https://codility.com/programmers/lessons/17-dynamic_programming/min_abs_sum/
public class MinAbsSum {
    public int solve(int[] values) {
        if (values.length == 0) {
            return 0;
        }
        if (values.length == 1) {
            return Math.abs(values[0]);
        }
        for (int i = 0; i < values.length; i++) {
            values[i] = Math.abs(values[i]);
        }
        Arrays.sort(values);
        int position = 0;
        for (int i = values.length - 1; i >= 0; i--) {
            if (position < 0) {
                position += values[i];
            } else {
                position -= values[i];
            }
        }
        return Math.abs(position);
    }
}
