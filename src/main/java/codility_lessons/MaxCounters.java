package codility_lessons;

/**
 * Calculate the values of counters after applying all alternating operations: increase counter by 1; set value of all counters to current maximum.
 */
// https://codility.com/programmers/lessons/4-counting_elements/max_counters/
public class MaxCounters {
    public int[] solve(int counterCount, int[] operations) {
        int[] counters = new int[counterCount];
        int highestCount = 0;
//        System.out.println(Arrays.toString(counters));
        for (int o = 0; o < operations.length; o++) {
//            System.out.println(operations[i]);
            if (operations[o] == counterCount + 1) {
                // only process the max_counter operation if we are not at max_counter state already
                if (o > 0 && operations[o-1] != operations[o]) {
                    for (int j = 0; j < counters.length; j++) {
                        counters[j] = highestCount;
                    }
                }
            } else {
                int newValue = ++counters[operations[o]-1];
                if (newValue > highestCount) {
                    highestCount++;
                }
            }
//            System.out.println(Arrays.toString(counters));
        }
        return counters;
    }
}

