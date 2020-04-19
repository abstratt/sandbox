package codility_lessons;

import java.util.Arrays;

/**
 * Count the number of different ways of climbing to the top of a ladder
 */
// https://codility.com/programmers/lessons/13-fibonacci_numbers/ladder/
public class Ladder {
    public int[] solve(int[] ladders, int[] moduloExps) {
        int highestModuloExponent = Arrays.stream(moduloExps).max().getAsInt();
        int highestModulo = (int) Math.pow(2, highestModuloExponent);
        int[] fibs = new int[ladders.length + 1]; 
        fibs[0] = 1;
        fibs[1] = 1;
        for (int i = 2; i < fibs.length; i++) {
            fibs[i] = (fibs[i-1] + fibs[i-2]) % highestModulo;
        }
        int[] result = new int[ladders.length];
        for (int i = 0; i < ladders.length; i++) {
            result[i] = fibs[ladders[i]] % (int) Math.pow(2, moduloExps[i]);
        }
        return result;
    }
}
