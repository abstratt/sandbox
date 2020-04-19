package codility_lessons;

import java.util.Arrays;

public class CrossingMonkey {
    public int solve(int[] times, int maxJump) {
        System.out.println("\n***\nTimes: " + Arrays.toString(times) + " - jump length: " + maxJump);
        if (maxJump > times.length) {
            return 0;
        }
        int maxTime = Arrays.stream(times).max().getAsInt();
        if (maxTime < 0 || maxJump < 1) {
            return -1;
        }
        int bestTime = 0;
        int position = -1;
        while (position < times.length - maxJump) {
            int bestReachableTime = Integer.MAX_VALUE;
            int bestLanding = -1;
            for (int jump = 1; jump <= maxJump && jump + position < times.length; jump++) {
                int landing = position + jump;
                int landingTime = times[landing];
                if (landingTime >= 0 && landingTime <= bestReachableTime) {
                    bestReachableTime = landingTime;
                    bestLanding = landing;
                }
            }
            if (bestLanding == -1) {
                System.out.println("Didn't cross!");
                return -1;
            }
            bestTime = Math.max(bestTime, bestReachableTime);
            System.out.println("From " + position + " to " + bestLanding);
            position = bestLanding;
        }
        System.out.println("From " + position + " to " + times.length);
        return bestTime;
    }
}
