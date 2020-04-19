package codility_lessons;

/**
 * Count factors of given number n
 */
// https://codility.com/programmers/lessons/10-prime_and_composite_numbers/count_factors/
public class CountFactors {
    public int solve(int number) {
        double sqrt = Math.sqrt(number);
        int count = 0;
        for (int i = 1; i < sqrt; i++) {
            if (number % i == 0) {
                count++;
            }
        }
        boolean exactSqrt = sqrt == (double) (long) sqrt;
        int result = count * 2 + (exactSqrt ? 1 : 0);
        return result;
    }
}
