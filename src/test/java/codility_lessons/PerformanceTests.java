package codility_lessons;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.function.Supplier;

import org.junit.jupiter.api.Test;

public class PerformanceTests extends Helpers {
    @Test
    public void testCountNonDivisible_30000_random() {
        int[] values = generateRandom(30000, 1, 100);
        measure(() -> countNonDivisible(values), 10000);
    }
    
    @Test
    public void testCountNonDivisible_20000_range() {
        int[] values = generateRange(1, 20000);
        measure(() -> countNonDivisible(values), 10000);
    }


    @Test
    public void testPeaks_performance_50000() {
        measurePeaks(50000);
    }
    
    @Test
    public void testPeaks_performance_5000() {
        measurePeaks(5000);
    }
    
    private void measurePeaks(int length) {
        Peaks solver = new Peaks();
        Integer result = measure(() -> solver.solve(generateRandom(length, 0, Integer.MAX_VALUE)), 10000);
        System.out.println("Peaks: "+ result);
    }

    @Test
    public void testMaxCounters_performance_1000() {
        measureMaxCounters(1000);
    }

    @Test
    public void testMaxCounters_performance_20000() {
        measureMaxCounters(20000);
    }

    @Test
    public void testMaxCounters_performance_100000() {
        measureMaxCounters(100000);
    }

    @Test
    public void testMaxCounters_performance_500000() {
        measureMaxCounters(500000);
    }

    private void measureMaxCounters(int operationCount) {
        int counterCount = 100000;
        int[] operations = new int[operationCount];
        for (int i = 0; i < operations.length; i++) {
            operations[i] = i % 2 == 0 ? i % counterCount + 1 : (counterCount + 1);
        }
        measure(() -> maxCounters(counterCount, operations), 10000);
    }

    <T> T measure(Supplier<T> r, long thresholdInMillis) {
        long start = System.nanoTime();
        T result = r.get();
        long end = System.nanoTime();
        long elapsed = end - start;
        System.out.println("Elapsed: " + elapsed / 1000000);
        assertTrue(elapsed < (thresholdInMillis * 1000000), () -> "Elapsed: " + elapsed);
        System.out.println("Result: " + result);
        return result;
    }

    @Test
    public void testNumberOfDiscIntersections_performance1() {
        testNumberOfDiscIntersections_performance(50000, 0, 200);
    }

    @Test
    public void testNumberOfDiscIntersections_performance2() {
        testNumberOfDiscIntersections_performance(25000, null, 200);
    }

    @Test
    public void testNumberOfDiscIntersections_performance3() {
        testNumberOfDiscIntersections_performance(200000, null, 500);
    }

    @Test
    public void testNumberOfDiscIntersections_performance4() {
        testNumberOfDiscIntersections_performance(200000, 10000, 500);
    }

    private void testNumberOfDiscIntersections_performance(int discCount, Integer radius, int threshold) {
        int[] discs;
        if (radius != null)
            discs = generateConstant(discCount, radius);
        else {
            discs = generateRandom(discCount, 0, 3);
        }
        measure(() -> numberOfDiscIntersections(discs), threshold);
    }
    
}
