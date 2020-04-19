package codility_lessons;

import java.util.Arrays;

/**
 * Find the minimal nucleotide from a range of sequence DNA
 */
// https://codility.com/programmers/lessons/5-prefix_sums/genomic_range_query/
public class GenomicRangeQuery {
    public int[] solve(String sequence, int[] starts, int[] ends) {
        return solve2(sequence, starts, ends);
    }
    
    static class SegmentStats {
        public SegmentStats(char[] sequence, int start, int end) {
            this.start = start;
            this.end = end;
            if (end == start) {
                this.nucleotide = sequence[start];
                return;
            }
            int mid = (end - start) / 2;
            this.left = buildTree(sequence, start, start + mid);
            this.right = buildTree(sequence, start + mid + 1, end);
            this.nucleotide = (char) Math.min(left.nucleotide, right.nucleotide);
        }
        int start;
        int end;
        char nucleotide;
        SegmentStats left;
        SegmentStats right;
        static SegmentStats buildTree(char[] sequence, int start, int end) {
            return new SegmentStats(sequence, start, end);
        }
        public char search(int start, int end) {
            if (start == this.start && end == this.end) {
                return this.nucleotide;
            }
            if (start >= right.start)
                return right.search(start, end);
            else if (end <= left.end)
                return left.search(start, end);
            else {
                // some on each side
                return (char) Math.min(left.search(start, left.end), right.search(right.start, end));
            }
        }
    }
    
    public int[] solve2(String sequence, int[] starts, int[] ends) {
        SegmentStats stats = SegmentStats.buildTree(sequence.toCharArray(), 0, sequence.length() - 1);
        String nucleotides = "ACGT";
        int[] minFactors = new int[starts.length];
        for (int i = 0; i < minFactors.length; i++) {
            char nucleotide = stats.search(starts[i], ends[i]);
            minFactors [i] = nucleotides.indexOf(nucleotide) + 1;
        }
        return minFactors;
    }
    
    public int[] solve1(String sequence, int[] starts, int[] ends) {
        char[] charArray = sequence.toCharArray();
        int[] minFactors = new int[starts.length];

        char[] proteins = {'A', 'C', 'G', 'T'};
        for (int i = 0; i < minFactors.length; i++) {
            if (starts[i] == ends[i]) {
                minFactors[i] = 1 + Arrays.binarySearch(proteins, charArray[starts[i]]);
            } else {
                minFactors[i] = 4;
                protein: for (int k = 0; k < proteins.length - 1; k++) {
                    for (int j = starts[i]; j <= ends[i]; j++) {
                        if (charArray[j] == proteins[k]) {
                            minFactors[i] = k + 1;
                            break protein;
                        }
                    }

                }
            }
        }
        return minFactors;
    }
}
