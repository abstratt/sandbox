package codility_lessons;

/**
 * Divide array A into K blocks and minimize the largest sum of any block.
 */
// https://codility.com/programmers/lessons/14-binary_search_algorithm/min_max_division/
public class MinMaxDivision {
    public int solve(int blockCount, int[] numbers) {
        int worst = sum(numbers);
        int best = max(numbers);
        int result = -1;
        while (worst >= best) {
            int guess = (worst + best) / 2;
            boolean checkResult = check(numbers, blockCount, guess);
            if (checkResult) {
                result = guess;
                worst = guess - 1;
            } else {
                best = guess + 1;
            }
        }
        return result;
    }

    private int max(int[] numbers) {
        int max = 0;
        for (int i = 0; i < numbers.length; i++) {
            if (numbers[i] > max) {
                max = numbers[i];
            }
        }
        return max ;
    }

    private int sum(int[] numbers) {
        int sum = 0;
        for (int i = 0; i < numbers.length; i++) {
            sum += numbers[i];
        }
        return sum;
    }

    private boolean check(int[] numbers, int expectedBlockCount, int expectedBlockSum) {
        int actualBlockCount = 1;
        int sum = 0;
        for (int i = 0; i < numbers.length; i++) {
            sum += numbers[i];
            if (sum > expectedBlockSum) {
                actualBlockCount++;
                sum = numbers[i];
                if (actualBlockCount > expectedBlockCount) {
                    return false;
                }
            }
        }
        return actualBlockCount <= expectedBlockCount;
    }
}
