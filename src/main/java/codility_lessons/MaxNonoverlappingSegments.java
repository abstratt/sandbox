package codility_lessons;

/**
 * Find a maximal set of non-overlapping segments
 */
// https://codility.com/programmers/lessons/16-greedy_algorithms/max_nonoverlapping_segments/
public class MaxNonoverlappingSegments {
    public int solve(int[] starts, int[] ends) {
        if (starts.length == 0) {
            return 0;
        }
        int currentStart = -1;
        int segments = 1;
        // loop through all segments, for latest ending to earliest ending
        for (int i = ends.length - 1; i >= 0; i--) {
            if (ends[i] < currentStart) {
                // non-overlapping, found one more!
                segments++;
                currentStart = starts[i];
            } else if (starts[i] > currentStart) {
                // discard previous segment, use this one as it starts later
                currentStart = starts[i];
            }
        }
        return segments;    
    }
}

