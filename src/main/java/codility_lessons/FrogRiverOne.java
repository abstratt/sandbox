package codility_lessons;

import java.util.Arrays;

/**
 * Find the earliest time when a frog can jump to the other side of a river
 */
// https://codility.com/programmers/lessons/4-counting_elements/frog_river_one/
public class FrogRiverOne {
    public int solve(int[] fallingLeaves, int riverWidth) {
        final int NEVER = Integer.MAX_VALUE;
        int[] positions = new int[riverWidth];
        Arrays.fill(positions, NEVER);
        // lay down all leaves remembering when they fell
        for (int time = 0; time < fallingLeaves.length; time++) {
            int index = fallingLeaves[time]-1;
            positions[index] = Math.min(positions[index], time);
        }
        int nextPosition = 0;
        int minTime = -1;
        while (nextPosition < positions.length && positions[nextPosition] < NEVER) {
            minTime = Math.max(minTime, positions[nextPosition]);
            nextPosition++;
        }
        return nextPosition == positions.length ? minTime : -1;
    }
}
