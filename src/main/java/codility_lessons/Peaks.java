package codility_lessons;

import java.util.Arrays;

/**
 * Divide an array into the maximum number of same-sized blocks, each of which should contain an index P such that A[P - 1] < A[P] > A[P + 1].
 */
// https://codility.com/programmers/lessons/10-prime_and_composite_numbers/peaks/
public class Peaks {
    public int solve(int[] numbers) {
        int[] peaks = findPeaks(numbers);
        if (peaks.length <= 1) {
            return peaks.length;
        }
        int maxBlockCount = peaks.length;
        int minBlockSize = numbers.length / maxBlockCount;
        // 3 is the smallest possible block size
        blockSizeSearch: for (int blockSize = minBlockSize; blockSize <= numbers.length; blockSize++) {
            boolean isComponent = numbers.length % blockSize == 0;
            if (isComponent) {
                int blockCount = numbers.length / blockSize;
                for (int blockIndex = 0; blockIndex < blockCount; blockIndex++) {
                    int blockStart = blockIndex * blockSize;
                    int blockEnd = blockStart + blockSize - 1;
                    int peakIndex = Arrays.binarySearch(peaks, blockEnd);
                    if (peakIndex < 0) {
                        // where it would be if existed
                        int insertionPoint = Math.max(1, -(peakIndex + 1));
                        if (peaks[insertionPoint-1] < blockStart || peaks[insertionPoint-1] > blockEnd) {
                            continue blockSizeSearch;
                        }
                    } else {
                        // peak at last element
                    }
                }
                // if there are peaks in every block, we found the smallest block size
                return blockCount;
            }
        }
        return 0;
    }

    public int[] findPeaks(int[] numbers) {
        int[] rawPeaks = new int[numbers.length / 2];
        int peakCount = 0;
        for (int i = 1; i < numbers.length - 1; i++) {
            if (numbers[i] > numbers[i-1] && numbers[i] > numbers[i+1]) {
                rawPeaks[peakCount++] = i;
            }
        }
        int[] peaks = new int[peakCount];
        System.arraycopy(rawPeaks, 0, peaks, 0, peaks.length);
        return peaks;
    }
}
