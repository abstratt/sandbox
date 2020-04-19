package codility_lessons;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

// https://codility.com/programmers/lessons/11-sieve_of_eratosthenes/count_non_divisible/
public class CountNonDivisible {
    class Stats {
        int index;
        int number;
        int divisions;
        public Stats(int index, int number) {
            this.index = index;
            this.number = number;
        }
//        @Override
//        public String toString() {
//            return MessageFormat.format("#{0} {1} - divisions: {2}", index, number, divisions);
//        }
    }
    public int[] solve(int[] values) {
        Stats[] stats = new Stats[values.length];
        Arrays.setAll(stats, i -> new Stats(i, values[i]));
        Arrays.sort(stats, (a, b) -> Integer.compare(a.number, b.number));
        for (int i = 0; i < stats.length; i++) {
            // a number is divisible by itself
            stats[i].divisions++;
            // account for duplicates (which will be nearby)
            for (int j = i - 1; j >= 0 && stats[j].number == stats[i].number; j--) {
                stats[j].divisions++;
                stats[i].divisions++;
            }
            // a number may also be divisible by numbers that are lower than its half (ex.: 9 / 2 = 4, 16 / 2 = 8)
            int half = stats[i].number / 2;
            for (int j = 0; stats[j].number <= half; j++) {
                if (stats[i].number % stats[j].number == 0) {
                    stats[i].divisions++;
                }
            }
        }
//        System.out.println("Stats: " + Arrays.toString(stats));
        int[] nonDivisions = new int[values.length];
        for (int i = 0; i < stats.length; i++) {
            nonDivisions[stats[i].index] = values.length - stats[i].divisions;    
        }
//        System.out.println("Non-divisions: " + Arrays.toString(nonDivisions));
        return nonDivisions;
    }

    public IntStream findPrimes(int max) {
        List<Integer> primes = new ArrayList<>();
        primes.add(2);
        for (int i = 3; i < max; i++) {
            double limit = Math.sqrt(i);
            boolean isPrime = true;
            for (Integer prime : primes) {
                if (i % prime == 0) {
                    isPrime = false;
                    break;
                }
                if (prime > limit) {
                    break;
                }
            }
            if (isPrime) {
                primes.add(i);
            }
        }    
        return primes.stream().mapToInt(it -> it);
    }

    
    public int countPrimes(int max) {
        return (int) findPrimes(max).count();
    }
}
