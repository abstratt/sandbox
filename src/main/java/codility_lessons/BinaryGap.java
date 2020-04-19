package codility_lessons;

/**
 * Find longest sequence of zeros in binary representation of an integer
 */
// https://codility.com/programmers/lessons/1-iterations/binary_gap/
public class BinaryGap {

    public int solve(int n) {
        String binaryString = Integer.toBinaryString(n);
        int firstOne = binaryString.indexOf('1');
        int lastOne = binaryString.lastIndexOf('1');
        if (firstOne < 0 || lastOne < 0 || firstOne == lastOne) {
            return 0;
        }
        char[] sequence = binaryString.toCharArray();
        int maximumSequence = 0;
        for (int i = firstOne; i <= lastOne; i++) {
            int sequenceLength = 0;
            while (sequence[i] == '0') {
                sequenceLength++;
                i++;
            }
            if (sequenceLength > maximumSequence) {
                maximumSequence = sequenceLength;
            }
        }
        
        return maximumSequence;
    }

}
