/*

This is a test class using JUnit 4, which can be obtained from http://junit.org/junit4/ 

Note the file has been renamed to match the test requirements.

*/
package wallethub;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import wallethub.ComplementaryPairs.Pair;

public class ComplementaryPairsTest {

    @Test
    public void testVariousValuesOfK() {
        int[] values = new int[] { 1, 2, 3, 4, 0, 2, 7, 0 };
        assertPairs(0, values, Pair.of(4, 7));
        assertPairs(1, values, Pair.of(0, 4), Pair.of(0, 7));
        assertPairs(2, values, Pair.of(1, 4), Pair.of(1, 7), Pair.of(4, 5), Pair.of(5, 7));
        assertPairs(3, values, Pair.of(0, 1), Pair.of(0, 5), Pair.of(2, 4), Pair.of(2, 7));
        assertPairs(4, values, Pair.of(0, 2), Pair.of(1, 5), Pair.of(3, 4), Pair.of(3, 7));
        assertPairs(5, values, Pair.of(0, 3), Pair.of(1, 2), Pair.of(2, 5));
        assertPairs(6, values, Pair.of(1, 3), Pair.of(3, 5));
        assertPairs(7, values, Pair.of(2, 3), Pair.of(4, 6), Pair.of(6, 7));
        assertPairs(8, values, Pair.of(0, 6));
        assertPairs(9, values, Pair.of(1, 6), Pair.of(5, 6));
        assertPairs(10, values, Pair.of(2, 6));
        assertPairs(11, values, Pair.of(3, 6));
        // no two values exist that added are that high
        assertPairs(12, values);
        // no two values exist that added are that low
        assertPairs(-1, values);
    }

    @Test
    public void testEdgeCases() {
        // less than two values, no pairs
        assertPairs(4, new int[0]);
        assertPairs(4, new int[] { 4 });
        assertPairs(4, new int[] { 4, 0 }, Pair.of(0, 1));
        assertPairs(4, new int[] { 2, 2, 2 }, Pair.of(0, 1), Pair.of(0, 2), Pair.of(1, 2));
        assertPairs(3, new int[] { 1, 1, 1, 2 }, Pair.of(0, 3), Pair.of(1, 3), Pair.of(2, 3), Pair.of(1, 3));
        assertPairs(3, new int[] { 1, 1, 2, 2 }, Pair.of(0, 2), Pair.of(0, 3), Pair.of(1, 2), Pair.of(1, 3));
        assertPairs(-3, new int[] { -4, -3, -2, -1, 0, 1, 2, 3 }, Pair.of(0, 5), Pair.of(1, 4), Pair.of(2, 3));
    }

    @Test
    public void testGrowingSequence() {
        int[] values = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        assertPairs(20, values);
        assertPairs(19, values, Pair.of(9, 10));
        assertPairs(18, values, Pair.of(8, 10));
        assertPairs(17, values, Pair.of(8, 9), Pair.of(7, 10));
        assertPairs(16, values, Pair.of(7, 9), Pair.of(6, 10));
        assertPairs(15, values, Pair.of(7, 8), Pair.of(6, 9), Pair.of(5, 10));
        assertPairs(14, values, Pair.of(6, 8), Pair.of(5, 9), Pair.of(4, 10));
        assertPairs(13, values, Pair.of(5, 8), Pair.of(6, 7), Pair.of(4, 9), Pair.of(3, 10));
        assertPairs(12, values, Pair.of(4, 8), Pair.of(5, 7), Pair.of(3, 9), Pair.of(2, 10));
        assertPairs(11, values, Pair.of(2, 9), Pair.of(3, 8), Pair.of(4, 7), Pair.of(5, 6), Pair.of(1, 10));
        assertPairs(10, values, Pair.of(1, 9), Pair.of(2, 8), Pair.of(3, 7), Pair.of(4, 6), Pair.of(0, 10));
        assertPairs(1, values, Pair.of(0, 1));
    }
    
    @Test
    public void testSpeed() {
        Random random = new Random(0);
        int[] values = IntStream.generate(() -> random.nextInt() % 500000).limit(2000000).toArray();
        new ComplementaryPairs().findComplementary(0, values);
    }

    private static void assertPairs(int k, int[] values, Pair... expected) {
        List<Pair> actual = new ComplementaryPairs().findComplementary(k, values);
        assertEquals(new LinkedHashSet<>(Arrays.asList(expected)), new LinkedHashSet<>(actual));
    }
}
