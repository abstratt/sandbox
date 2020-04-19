package codility_lessons;

import java.util.Arrays;

/**
 * Find the minimal positive integer not occurring in a given sequence
 */
// https://codility.com/programmers/lessons/4-counting_elements/missing_integer/
public class MissingInteger {
    public int solve(int[] values) {
        // TODO: this is O(N*log(N)), using a tree map would keep it at O(N)
        Arrays.sort(values);
        int firstPositiveIndex = -1;
        for (int i = 0; i < values.length; i++) {
            if (values[i] > 0) {
                firstPositiveIndex = i;
                break;
            }
        }
        if (firstPositiveIndex < 0) {
            return 1;
        }
        if (values[firstPositiveIndex] > 1) {
            return 1;
        }
        if (firstPositiveIndex == values.length - 1) {
            return values[firstPositiveIndex] > 1 ? 1 : values[firstPositiveIndex] + 1;
        }
        for (int i = firstPositiveIndex+1; i < values.length; i++) {
            if (values[i] - values[i-1] > 1) {
                return values[i-1] + 1;
            }
        }
        return values[values.length-1] + 1;
    }
}
