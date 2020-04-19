package codility_extra;

/**
 * Split an array in two segments such that the first segment has as 
 * many elements equal to X as the second segment has differing from X. 
 */
public class Problem02 {
    public int solve(int[] numbers, int x) {
        int k = numbers.length;
        int occurrences = 0;
        for (int i = 0; i < k; i++) {
            if (numbers[i] == x) {
                occurrences++;
                while(k > i && numbers[--k] == x);
                if (k <= i) {
                    return i;
                }
            }
        }
        return occurrences == 0 ? -1 : k;
    }
}
