package codility_lessons;


import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import codility_lessons.ZigZagCount.Tree;

public class UnitTests extends Helpers {
    @Test
    public void testEquilibrium() {
        assertEquals(3, equilibrium(3, 3, 3, 6, 9, 0, 0));
        assertEquals(4, equilibrium(3, 3, 3, 6, 2, 9, 5, 1));
        assertEquals(4, equilibrium(3, 3, 2, 6, 2, 9, 6, -1));
        assertEquals(5, equilibrium(3, -3, 2, 6, 2, 9, 6, -1, 2, 3));
        assertEquals(4, equilibrium(10, 10, 10, 10, 1, -100, 140));
        assertEquals(4, equilibrium(Integer.MAX_VALUE, 1, Integer.MIN_VALUE, 999, 1, 999));
        assertEquals(3, equilibrium(Integer.MAX_VALUE, Integer.MIN_VALUE, 3, 1, 2));

        assertEquals(-1, equilibrium(1, 2, 3));
        assertEquals(-1, equilibrium());
        assertEquals(-1, equilibrium(0, -2147483648, -2147483648));

    }

    @Test
    public void testTapeEquilibrium() throws Exception {
        assertEquals(1, tapeEquilibrium(3, 1, 2, 4, 3));
        assertEquals(1, tapeEquilibrium(1, 2));
        assertEquals(1, tapeEquilibrium(1, 2, 4));
        assertEquals(2, tapeEquilibrium(-1, 1));
        assertEquals(3, tapeEquilibrium(-4, -3, 2));
        assertEquals(0, tapeEquilibrium(1, 1, 1, 1, 1, 5));
        assertEquals(1, tapeEquilibrium(1, 1, 1, 1, 1, 5, -5));
        assertEquals(0, tapeEquilibrium(5, -100, 1, 1, 1, 1, 1, 5, -5, -80));
    }

    @Test
    public void testMinAbsSumOfTwo() throws Exception {
        assertEquals(1, minAbsSumOfTwo(1, 4, -3));
        assertEquals(3, minAbsSumOfTwo(-8, 4, 5, -10, 3));
        assertEquals(1, minAbsSumOfTwo(-8, -7, 1, 9, 10));
    }

    @Test
    public void testMaxSliceSum() throws Exception {
        assertEquals(5, maxSliceSum(3, 2, -6, 3, 0));
        assertEquals(11, maxSliceSum(-2, 3, 6, -2, 1, 3, -1));
        assertEquals(-10, maxSliceSum(-10));
        assertEquals(-1, maxSliceSum(-10, -1, -2, -10, -5));
    }

    @Test
    public void testMaxDoubleSliceSum() throws Exception {
        assertEquals(17, maxDoubleSliceSum(3, 2, 6, -1, 4, 5, -1, 2));
        assertEquals(7, maxDoubleSliceSum(-2, -5, 3, 4, -1, 6));
        assertEquals(8, maxDoubleSliceSum(2, -3, 1, 3, 0, 3, 1, -3, 2));
    }

    @Test
    public void testFrogRiverOne() throws Exception {
        FrogRiverOne solver = new FrogRiverOne();
        assertEquals(6, solver.solve(toArray(1, 3, 1, 4, 2, 3, 5, 4), 5));
        assertEquals(1, solver.solve(toArray(1, 2), 2));
        assertEquals(-1, solver.solve(toArray(1, 1), 2));
    }

    @Test
    public void testCrossingMonkey() throws Exception {
        CrossingMonkey solver = new CrossingMonkey();
        assertEquals(1, solver.solve(toArray(3, 2, 1), 3));
        assertEquals(7, solver.solve(toArray(2, 8, 3, 7, 9, 5), 2));
        assertEquals(5, solver.solve(toArray(2, 8, 3, 7, 5, 6), 2));
        assertEquals(0, solver.solve(toArray(3, 2, 1), 4));
        assertEquals(0, solver.solve(toArray(3, 2, 1), 6));
        assertEquals(3, solver.solve(toArray(3, 2, 1), 1));
        assertEquals(2, solver.solve(toArray(1, 2, 2, -1), 2));
        assertEquals(-1, solver.solve(toArray(-1, -1), 2));
        assertEquals(0, solver.solve(toArray(-1, -1), 3));
        assertEquals(0, solver.solve(toArray(0, 0, 0, 0), 1));
        assertEquals(-1, solver.solve(toArray(0, 0, 0, 0), 0));
        assertEquals(0, solver.solve(toArray(), 1));
        assertEquals(10, solver.solve(toArray(8, 9, 10), 1));
        assertEquals(-1, solver.solve(toArray(8, 9, 10, -1), 1));
        assertEquals(-1, solver.solve(toArray(-1, -1, 1, 2), 2));
        assertEquals(1, solver.solve(toArray(-1, 1, 2), 2));
        assertEquals(-1, solver.solve(toArray(-1, 1, 2), 1));
        assertEquals(17, solver.solve(toArray(1, 5, 9, 13, 17), 1));
        assertEquals(10, solver.solve(toArray(1, -1, 10, 10, 1), 3));
        assertEquals(10, solver.solve(toArray(1, -1, 10, 10, 1), 2));
        assertEquals(4, solver.solve(toArray(2, -1, 4, 10, 1, 7), 3));
    }

    @Test
    public void testLadder() throws Exception {
        Ladder solver = new Ladder();
        assertArrayEquals(toArray(5, 1, 8, 0, 1), solver.solve(toArray(4, 4, 5, 5, 1), toArray(3, 2, 4, 3, 1)));
    }

    @Test
    public void testCountFactors() throws Exception {
        CountFactors solver = new CountFactors();
        // 1, 2, 3, 7, 42
        assertEquals(8, solver.solve(42));
        // 1, 2, 3, 4, 6, 9, 12, 18, 36
        assertEquals(9, solver.solve(36));
        // 1, 5, 25
        assertEquals(3, solver.solve(25));
        // 1, 31
        assertEquals(2, solver.solve(31));
        // 1, 2, 5, 10
        assertEquals(4, solver.solve(10));
        // 1, 2, 3, 4, 6, 8, 12, 24
        assertEquals(8, solver.solve(24));
        assertEquals(1, solver.solve(1));
        assertEquals(2, solver.solve(2));
        assertEquals(2, solver.solve(3));
        assertEquals(3, solver.solve(4));
    }

    @Test
    public void testDominator() throws Exception {
        Dominator solver = new Dominator();
        assertEquals((Integer) 3, solver.solveNumber(toArray(1, 2, 3, 4, 3, 3, 3, 1, 3, 8, 3, 3, 9)));
        assertEquals(null, solver.solveNumber(toArray(1, 2, 3, 3)));
        assertEquals(null, solver.solveNumber(toArray(1, 2, 3, 3, 2)));
        assertEquals(null, solver.solveNumber(toArray(1, 2, 3, 3, 2, 2)));
        assertEquals((Integer) 2, solver.solveNumber(toArray(1, 2, 3, 3, 2, 2, 2)));
    }

    @Test
    public void testZigZagCount() throws Exception {
        ZigZagCount solver = new ZigZagCount();
        assertEquals(0, solver.solve(null));
        assertEquals(0, solver.solve(new Tree(1)));
        assertEquals(0, solver.solve(new Tree(1, new Tree(2), null)));
        assertEquals(0, solver.solve(new Tree(1, new Tree(2), new Tree(3))));
        assertEquals(0, solver.solve(new Tree(1, new Tree(2), new Tree(3, null, new Tree(4)))));
        assertEquals(1, solver.solve(new Tree(1, new Tree(2), new Tree(3, new Tree(4), null))));
        assertEquals(2, solver.solve(new Tree(1, new Tree(3, new Tree(4), null),
                new Tree(3, new Tree(4, null, new Tree(10)), new Tree(3, new Tree(4), null)))));
    }

    @Test
    public void testSingleSwap() throws Exception {
        SingleSwap solver = new SingleSwap();
        assertTrue(solver.solve(toArray(1)));
        assertTrue(solver.solve(toArray(1, 1)));
        assertTrue(solver.solve(toArray(2, 1)));
        assertFalse(solver.solve(toArray(4, 3, 2, 1)));
        assertFalse(solver.solve(toArray(1, 2, 3, 4, 0)));
        assertTrue(solver.solve(toArray(1, 3, 2, 4)));
        assertTrue(solver.solve(toArray(4, 3, 2, 4)));
        assertTrue(solver.solve(toArray(1, 1, 2, 1, 1)));
        assertFalse(solver.solve(toArray(1, 2, 1, 3, 1)));
        assertTrue(solver.solve(toArray(1, 2, 3, 3, 2, 4)));
        assertTrue(solver.solve(toArray(3, 2, 3, 3, 2, 4)));
    }

    @Test
    public void testCountNonDivisible() throws Exception {
        assertArrayEquals(new int[] { 0 }, countNonDivisible(1));
        assertArrayEquals(new int[] { 0 }, countNonDivisible(2));
        assertArrayEquals(new int[] { 1, 0 }, countNonDivisible(1, 2));
        assertArrayEquals(new int[] { 0, 0 }, countNonDivisible(1, 1));
        assertArrayEquals(new int[] { 2, 0, 0 }, countNonDivisible(1, 2, 2));
        assertArrayEquals(new int[] { 2, 1, 1 }, countNonDivisible(1, 2, 3));
        assertArrayEquals(new int[] { 3, 2, 2, 1 }, countNonDivisible(1, 2, 3, 4));
        assertArrayEquals(new int[] { 0, 1, 3, 3 }, countNonDivisible(100, 50, 5, 2));
        assertArrayEquals(new int[] { 2, 4, 3, 2, 0 }, countNonDivisible(3, 1, 2, 3, 6));
    }

    @Test
    public void testCountNonDivisible_countPrimes() throws Exception {
        CountNonDivisible unit = new CountNonDivisible();
        assertEquals(168, unit.countPrimes(1000));
        assertEquals(1229, unit.countPrimes(10000));
        assertEquals(9592, unit.countPrimes(100000));
        assertEquals(78498, unit.countPrimes(1000000));
    }

    @Test
    public void testPeaks() throws Exception {
        Peaks solver = new Peaks();
        // test solution
        assertEquals(3, solver.solve(toArray(1, 2, 3, 4, 3, 4, 1, 2, 3, 4, 6, 2)));
        assertEquals(2, solver.solve(toArray(1, 3, 1, 4, 2, 3, 5, 4)));
        assertEquals(3, solver.solve(toArray(1, 2, 1, 2, 1, 1, 2, 1, 1)));
        assertEquals(1, solver.solve(toArray(1, 2, 3, 2, 1)));
        assertEquals(0, solver.solve(toArray(1, 2, 3, 3)));
        assertEquals(1, solver.solve(toArray(0, 1, 1, 1, 1, 1, 2, 1, 2, 1)));
        assertEquals(1, solver.solve(toArray(1, 2, 1, 2, 1, 1, 1, 1, 1, 0)));
    }

    @Test
    public void testPeaks_findPeaks() throws Exception {
        Peaks solver = new Peaks();
        assertArrayEquals(toArray(3, 5, 10), solver.findPeaks(toArray(1, 2, 3, 4, 3, 4, 1, 2, 3, 4, 6, 2)));
        assertArrayEquals(toArray(), solver.findPeaks(toArray(1, 2, 3, 4)));
        assertArrayEquals(toArray(2), solver.findPeaks(toArray(1, 2, 3, 1)));
        assertArrayEquals(toArray(), solver.findPeaks(toArray(1, 2, 2, 2, 1)));
        assertArrayEquals(toArray(1, 3, 5, 7), solver.findPeaks(toArray(1, 2, 1, 2, 1, 2, 1, 3, 2)));
    }

    //failing @Test
    public void testMinAbsSum() throws Exception {
        assertEquals(0, minAbsSum(1, -2, 2, 5));
        assertEquals(0, minAbsSum(-2, 2, 1, 3));
        assertEquals(0, minAbsSum());
        assertEquals(0, minAbsSum(0));
        assertEquals(1, minAbsSum(1));
        assertEquals(5, minAbsSum(5));
        assertEquals(5, minAbsSum(-5));
        assertEquals(0, minAbsSum(1, 2, 3));
        assertEquals(0, minAbsSum(1, 2, 3, 7, 11));
        assertEquals(0, minAbsSum(3, 3, 3, 4, 5));
        assertEquals(6, minAbsSum(generateRange(2, 20)));
    }

    @Test
    public void testMaxNonoverlappingSegments() throws Exception {
        MaxNonoverlappingSegments solver = new MaxNonoverlappingSegments();
        assertEquals(2, solver.solve(toArray(1, 2, 3), toArray(2, 3, 4)));
        assertEquals(3, solver.solve(toArray(1, 3, 7, 9, 9), toArray(5, 6, 8, 9, 10)));
        assertEquals(4, solver.solve(toArray(1, 1, 2, 4, 3, 3, 3, 5, 5, 7), toArray(2, 3, 3, 4, 4, 5, 6, 6, 7, 8)));
        assertEquals(4, solver.solve(toArray(1, 2, 3, 4), toArray(1, 2, 3, 4)));
        assertEquals(1, solver.solve(toArray(1), toArray(2)));
        assertEquals(1, solver.solve(toArray(1), toArray(1)));
        assertEquals(3, solver.solve(toArray(1, 2, 4, 1), toArray(1, 2, 5, 6)));
        assertEquals(0, solver.solve(toArray(), toArray()));
    }

    @Test
    public void testCommonPrimeDivisors() throws Exception {
        CommonPrimeDivisors solver = new CommonPrimeDivisors();

        assertEquals(1, solver.gcd(3, 2));
        assertEquals(1, solver.gcd(15, 8));
        assertEquals(5, solver.gcd(15, 20));
        assertEquals(5, solver.gcd(20, 15));
        assertEquals(4, solver.gcd(8, 12));

        assertEquals(1, solver.solve(toArray(36), toArray(24)));
        assertEquals(0, solver.solve(toArray(36), toArray(60)));
        assertEquals(0, solver.solve(toArray(1), toArray(2)));
        assertEquals(1, solver.solve(toArray(10), toArray(10)));
        assertEquals(1, solver.solve(toArray(2), toArray(2)));
        assertEquals(0, solver.solve(toArray(1), toArray(3)));
        assertEquals(0, solver.solve(toArray(2), toArray(6)));
        assertEquals(1, solver.solve(toArray(3), toArray(9)));
        assertEquals(0, solver.solve(toArray(10), toArray(30)));
        assertEquals(1, solver.solve(toArray(15), toArray(75)));
        assertEquals(1, solver.solve(toArray(15, 10, 3), toArray(75, 30, 5)));
        assertEquals(1, solver.solve(toArray(6), toArray(36)));
        assertEquals(0, solver.solve(toArray(42), toArray(36)));
        assertEquals(0, solver.solve(toArray(42), toArray(21)));
        assertEquals(0, solver.solve(toArray(210), toArray(21)));
        assertEquals(1, solver.solve(toArray(210), toArray(630)));
    }

    @Test
    public void testBinaryGap() {
        assertEquals(2, binaryGap(9));
        assertEquals(0, binaryGap(15));
        assertEquals(5, binaryGap(1041));
        assertEquals(1, binaryGap(20));
    }

    @Test
    public void testMissingInteger() {
        assertEquals(3, missingInteger(-1, 2, 0, -5, 5, 1, 2));
        assertEquals(1, missingInteger(-10));
        assertEquals(2, missingInteger(1));
        assertEquals(1, missingInteger(2));
        assertEquals(4, missingInteger(1, 2, 3));
    }

    @Test
    public void testMaxCounters() {
        assertArrayEquals(new int[] { 1, 2, 1, 3 }, maxCounters(4, 1, 2, 3, 4, 4, 4, 2));
        assertArrayEquals(new int[] { 1 }, maxCounters(1, 1));
        assertArrayEquals(new int[] { 1, 0 }, maxCounters(2, 1));
        assertArrayEquals(new int[] { 0, 1 }, maxCounters(2, 2));
        assertArrayEquals(new int[] { 1, 1, 1 }, maxCounters(3, 1, 2, 3));
        assertArrayEquals(new int[] { 0, 0, 0 }, maxCounters(3, 4));
        assertArrayEquals(new int[] { 1, 1, 1 }, maxCounters(3, 1, 4));
        assertArrayEquals(new int[] { 1, 1, 1 }, maxCounters(3, 1, 4, 4));
        assertArrayEquals(new int[] { 3, 3, 3 }, maxCounters(3, 1, 4, 1, 4, 1, 4));
        assertArrayEquals(new int[] { 3, 3, 3 }, maxCounters(3, 1, 4, 2, 4, 3, 4));
        assertArrayEquals(new int[] { 1, 1, 1 }, maxCounters(3, 1, 2, 3, 4, 4, 4));
        assertArrayEquals(new int[] { 2, 4, 2 }, maxCounters(3, 1, 4, 1, 4, 2, 2));
        assertArrayEquals(new int[0], maxCounters(0));
        assertArrayEquals(new int[] { 0, 0 }, maxCounters(2));
    }

    @Test
    public void testMinMaxDivision() {
        assertEquals(6, minMaxDivision(3, 2, 1, 5, 1, 2, 2, 2));
    }

    private Object minMaxDivision(int blocks, int... numbers) {
        return new MinMaxDivision().solve(blocks, numbers);
    }

    @Test
    public void testGenomicRangeQuery() {
        Assertions.assertArrayEquals(new int[] { 2, 4, 1 },
                genomeMinFactor("CAGCCTA", new int[] { 2, 5, 0 }, new int[] { 4, 5, 6 }));
        Assertions.assertArrayEquals(new int[] { 3 }, genomeMinFactor("CAGCCTA", new int[] { 2 }, new int[] { 2 }));
        Assertions.assertArrayEquals(new int[] { 3, 3 },
                genomeMinFactor("CAGCCTA", new int[] { 2, 2 }, new int[] { 2, 2 }));
        Assertions.assertArrayEquals(new int[] { 1 }, genomeMinFactor("A", new int[] { 0 }, new int[] { 0 }));
    }
    
    @Test
    public void testNumberOfDiscIntersections() {
        Assertions.assertEquals(11, numberOfDiscIntersections(1, 5, 2, 1, 4, 0));
        Assertions.assertEquals(16, numberOfDiscIntersections(10, 1, 1, 1, 2, 1, 1));
        Assertions.assertEquals(0, numberOfDiscIntersections(0, 0, 0));
        Assertions.assertEquals(2, numberOfDiscIntersections(0, 0, 1, 0, 0));
        Assertions.assertEquals(3, numberOfDiscIntersections(1, 1, 1));
        Assertions.assertEquals(1, numberOfDiscIntersections(1, 0, 0));
    }

    @Test
    public void testStoneWall() throws Exception {
        assertEquals(1, buildStoneWall(1));
        assertEquals(1, buildStoneWall(1, 1, 1));
        assertEquals(3, buildStoneWall(1, 2, 3));
        assertEquals(3, buildStoneWall(3, 2, 1));
        assertEquals(3, buildStoneWall(1, 2, 3, 1));
        assertEquals(3, buildStoneWall(1, 3, 2, 1));
        assertEquals(2, buildStoneWall(1, 2, 2, 1));
        assertEquals(3, buildStoneWall(2, 1, 1, 2));
        assertEquals(7, buildStoneWall(8, 8, 5, 7, 9, 8, 7, 4, 8));
    }
}
