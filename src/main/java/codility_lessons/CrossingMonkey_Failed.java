package codility_lessons;

import java.util.Arrays;

public class CrossingMonkey_Failed {
    class Stone {
        public Stone(int position, int timeAvailable) {
            super();
            this.position = position;
            this.timeAvailable = timeAvailable;
        }
        int position;
        int timeAvailable;
    }
    public int solve(int[] times, int jumpLength) {
        Stone[] stones = new Stone[times.length];
        for (int i = 0; i < stones.length; i++) {
            stones[i] = new Stone(i, times[i]);
        }
        Arrays.sort(stones, (a, b) -> Integer.compare(a.timeAvailable, b.timeAvailable));
        
        int currentStone = -1;
        int requiredDistance = times.length;
        if (requiredDistance <= jumpLength) {
            return 0;
        }
        for (int i = 0; i < stones.length; i++) {
            if (stones[i].position - currentStone <= jumpLength) {
                currentStone = stones[i].position;
                if (requiredDistance - currentStone <= jumpLength) {
                    return i;
                }
            }
        }
        return -1;
    }
}
