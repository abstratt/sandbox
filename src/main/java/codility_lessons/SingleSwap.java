package codility_lessons;

import java.util.Arrays;

public class SingleSwap {
    public boolean solve(int[] values) {
        int[] sorted = new int[values.length];
        System.arraycopy(values, 0, sorted, 0, values.length);
        Arrays.sort(sorted);
        int diffs = 0;
        for (int i = 0; i < values.length; i++) {
            if (values[i] != sorted[i]) {
                diffs++;
                if (diffs > 2) {
                    return false;
                }
            }
        }
        return true;
    }
    
    private boolean isSorted(int[] values) {
        int last = 0;
        for (int i = 0; i < values.length; i++) {
            if (values[i] < last) {
                return false;
            }
            last = values[i];
        }
        return true;
    }
}
