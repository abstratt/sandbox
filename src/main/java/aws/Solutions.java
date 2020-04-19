package aws;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Solutions {

    public int solution1(int A) {
        char[] toShuffle = Integer.toString(A).toCharArray();
        int length = toShuffle.length;
        char[] shuffled = new char[length];
        for (int begin = 0, end = length - 1; begin < end; begin++, end--) {
            // copy one from the beginning and one from the end
            shuffled[begin * 2] = toShuffle[begin];
            shuffled[begin * 2 + 1] = toShuffle[end];
        }
        // the mid element needs special handling
        if (length % 2 == 1) {
            shuffled[length - 1] = toShuffle[length / 2];
        }
        return Integer.parseInt(new String(shuffled));
    }

    public int solution2(int[] A) {
        int[] numbers = A;
        Arrays.sort(numbers);
        int length = numbers.length;
        for (int negativeIdx = 0, positiveIdx = length - 1; numbers[negativeIdx] < 0 && numbers[positiveIdx] > 0;) {
            int positive = numbers[positiveIdx];
            int negative = numbers[negativeIdx];
            if (-negative == positive) {
                return positive;
            }
            if (-negative > positive) {
                negativeIdx++;
            } else {
                positiveIdx--;
            }
        }
        return 0;
    }

    public int solution3(String S, String T) {
        throw new UnsupportedOperationException();
    }
    
    private boolean within(String toTest, String start, String end) {
        return toTest.compareTo(start) >= 0  && toTest.compareTo(end) <= 0;
    }


    private int[] toTimeArray(String s) {
        return new int[] {
            Integer.parseInt(s.substring(0, 2)),
            Integer.parseInt(s.substring(3, 5)),
            Integer.parseInt(s.substring(6, 8))
        };
    }

    Stream<int[]> generate(int digit1, int digit2) {
        if (digit1 == digit2) {
            return Stream.of(new int[] {digit1, digit1});
        }
        return Stream.of(new int[] {digit1, digit2}, new int[] {digit1, digit1}, new int[] {digit2, digit1}, new int[] {digit2, digit2});
    }
    
    
    
    
}
