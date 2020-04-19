package wallethub;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class ComplementaryPairs {
    /**
     * An ordered pair of non-negative integers (indices) where the first value is lower than the second.
     */
    public static class Pair {
        private int first, second;

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + first;
            result = prime * result + second;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Pair other = (Pair) obj;
            if (first != other.first)
                return false;
            if (second != other.second)
                return false;
            return true;
        }
        
        @Override
        public String toString() {
            return "(" + first + ", " + second + ")";
        }

        public Pair(int first, int second) {
            super();
            this.first = first;
            this.second = second;
        }

        public static Pair of(int i, int j) {
            if (i < 0)
                throw new IllegalArgumentException();
            if (i == j)
                throw new IllegalArgumentException();
            return new Pair(Math.min(i, j), Math.max(i, j));
        }

    }

    public List<Pair> findComplementary(int k, int[] values) {
        return sortAndBinarySearch(k, values, new ArrayList<>());
    }
    
    /**
     * Allows us to reorder the input array and remember the element's original index. 
     */
    private static class IndexedValue {
        public IndexedValue(int value, int index) {
            this.value = value;
            this.index= index;
        }
        int value;
        int index;
    }

    
    /**
     * We sort the array (O(n.log(n)), and use binary search (O(log(n)))to look for a complement.
     * 
     * This is showed to be faster than using a hash map from value to a collection of occurrences, 
     * and does not require additional memory to hold the indices of duplicates.
     */
    private List<Pair> sortAndBinarySearch(int k, int[] values, List<Pair> found) {
        // build a parallel array of indexed values: O(n)
        IndexedValue[] indexedValues = new IndexedValue[values.length];
        for (int i = 0; i < indexedValues.length; i++) {
            indexedValues[i] = new IndexedValue(values[i], i);
        }
        // sort: O(n.log(n))
        Comparator<IndexedValue> comparator = (a, b) -> Integer.compare(a.value, b.value);
        Arrays.sort(indexedValues, comparator);
        
        // find complements: O(n.log(n))
        int end = indexedValues.length - 1;
        for (int lower = 0; lower < end; lower++) {
            int complement = k - indexedValues[lower].value;
            // binary search: O(log(n))
            int complementPosition = Arrays.binarySearch(indexedValues, lower + 1, end + 1, new IndexedValue(complement, -1), comparator);
            if (complementPosition >= 0) {
                // ensure we rewind to the first occurrence in case of duplicates
                while (complementPosition > lower + 1 && indexedValues[complementPosition - 1].value == complement) {
                    complementPosition--;
                }
                end = Math.min(complementPosition + 1, end); 
                while (complementPosition <= end && indexedValues[complementPosition].value == complement) {
                    found.add(Pair.of(indexedValues[lower].index, indexedValues[complementPosition].index));
                    complementPosition++;
                }
            } else {
                int insertionPoint = -(complementPosition + 1);
                end = Math.min(end, insertionPoint);
            }
        }
        return found;
    }
    
//    private List<Pair> hashBag(int k, int[] values, List<Pair> found) {
//        Map<Integer, List<Integer>> indexBag = new LinkedHashMap<>();
//        // O(n) * O(1) -> O(n)
//        for (int index = 0; index < values.length; index++) {
//            indexBag.computeIfAbsent(values[index], key -> new ArrayList<>()).add(index);
//        }
//        // O(n * log(n))
//        for (int thisIndex = 0; thisIndex < values.length; thisIndex++) {
//            int complement = k - values[thisIndex];
//            // O(1)
//            List<Integer> complementIndices = indexBag.get(complement);
//            // O(log(n)) 
//            if (complementIndices != null) {
//                for (int j = 0; j < complementIndices.size(); j++) {
//                    if (complementIndices.get(j) > thisIndex) {
//                        found.add(Pair.of(thisIndex, complementIndices.get(j)));
//                    }
//                }
//            }
//        }
//        return found;
//    }
}
