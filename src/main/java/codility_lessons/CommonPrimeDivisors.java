package codility_lessons;
/**
 * Check whether two numbers have the same prime divisors
 */
// https://codility.com/programmers/lessons/12-euclidean_algorithm/common_prime_divisors/
public class CommonPrimeDivisors {
    public int solution(int[] A, int[] B) {
        int res = 0;
        for (int i = 0; i < A.length; ++i) {
            int a = A[i];
            int b = B[i];
            int gcd = gcd(a, b);
            for (int c; (c = gcd(b, gcd)) > 1; b = b / c);
            for (int c; (c = gcd(a, gcd)) > 1; a = a / c);
            if (a == b) {
                ++res;
            }
        }
        return res;
    }

    public int gcd(int a, int b) {
        if (a == 0 || b == 0) {
            return a+b;
        }
        return gcd(b, a % b);
    }

    public int solve(int[] as, int[] bs) {
        if (true) return solution(as, bs);
        int result = 0;
        outer: for (int i = 0; i < as.length; i++) {
            int a = as[i];
            int b = bs[i];
            int cap = Math.max(a, b) / 2;
            for (int j = 2; j <= cap; j++) {
                boolean componentOfA = a % j == 0;
                boolean componentOfB = b % j == 0;
                if (componentOfA != componentOfB) {
                    continue outer;
                }
                if (componentOfA) {
                    while (a % j == 0) {
                        a = a / j;
                    }
                    while (b % j == 0) {
                        b = b / j;
                    }
                }
            }
            if (a == b) {
                result++;
            }
        }
        return result;
    }
}
