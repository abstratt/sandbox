package codility_lessons;

/**
 * Minimize the value |(A[0] + ... + A[P-1]) - (A[P] + ... + A[N-1])|
 */
// https://codility.com/programmers/lessons/3-time_complexity/tape_equilibrium/
public class TapeEquilibrium {
    public int solve(int[] tape) {
        int[] leftSums = new int[tape.length];
        int[] rightSums = new int[tape.length];
        for (int i = 0, previous = 0; i < leftSums.length; i++) {
            previous = leftSums[i] = previous + tape[i];
        }
        for (int i = rightSums.length - 1, previous = 0; i >= 0; i--) {
            previous = rightSums[i] = previous + tape[i];
        }
        int minDiff = Integer.MAX_VALUE;
//        int minIndex = -1;
        for (int i = 0; i < tape.length - 1; i++) {
            int diff = Math.abs(leftSums[i] - rightSums[i+1]);
            if (diff < minDiff) {
                minDiff = diff;
//                minIndex = i+1;
            }
        }
//        System.out.println("tape: " + Arrays.toString(tape));
//        System.out.println("left sums: " + Arrays.toString(leftSums));
//        System.out.println("right sums: " + Arrays.toString(rightSums));
//        System.out.println("min diff: " + minDiff);
//        System.out.println("min index: " + minIndex);
//        List<Integer> tapeAsList = Arrays.stream(tape).mapToObj(it -> (Integer) it).collect(Collectors.toList());
//        System.out.println(tapeAsList.subList(0, minIndex) + " | " + tapeAsList.subList(minIndex, tapeAsList.size()));
        return minDiff;
    }
    
}
