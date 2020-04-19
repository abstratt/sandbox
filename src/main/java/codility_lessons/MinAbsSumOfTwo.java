package codility_lessons;

import java.util.Arrays;

/**
 * Find the minimal absolute value of a sum of two elements
 */
// https://codility.com/programmers/lessons/15-caterpillar_method/min_abs_sum_of_two/
public class MinAbsSumOfTwo {
    public int solve(int[] values) {
        Arrays.sort(values);
        int minSum = Math.abs(values[0]+values[values.length-1]);
        for (int left = 0, right = values.length - 1; left < right;) {
            int leftSum = Math.abs(values[left]+values[right-1]);
            int rightSum = Math.abs(values[left+1]+values[right]);
            if (leftSum > rightSum) {
                left++;
                minSum = Math.min(rightSum, minSum);
            } else {
                right--;
                minSum = Math.min(leftSum, minSum);
            }
        }
        return minSum;
    }
}

