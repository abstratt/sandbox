package codility_lessons;
/**
 * Find an index of an array such that its value occurs at more than half of indices in the array
 */
// https://codility.com/programmers/lessons/8-leader/dominator/
public class Dominator {
    public Integer solveNumber(int[] numbers) {
        int dominator = solve(numbers);
        return dominator == -1 ? null : numbers[dominator];
    }
    public int solve(int[] numbers) {
        int candidate = 0;
        int count = 0;
        for (int i = 0; i < numbers.length; i++) {
            if (count == 0) {
                count++;
                candidate = i;
            } else if (numbers[i] == numbers[candidate]) {
                count++;
            } else {
                count--;
            }
        }
        if (count == 0) {
            return -1;
        }
        int votes = 0;
        for (int i = 0; i < numbers.length; i++) {
            if (numbers[candidate] == numbers[i]) {
                votes++;
            }
        }
        return votes > numbers.length / 2 ? candidate : -1;
    }
    
}
