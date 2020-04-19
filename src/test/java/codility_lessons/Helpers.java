package codility_lessons;

import java.util.Arrays;
import java.util.Random;

class Helpers {
    public int buildStoneWall(int... heights) {
        return new StoneWall().solve(heights);
    }

    public int numberOfDiscIntersections(int... radiuses) {
        return new NumberOfDiscIntersections().solve(radiuses);
    }

    public int[] genomeMinFactor(String sequence, int[] starts, int[] ends) {
        return new GenomicRangeQuery().solve(sequence, starts, ends);
    }

    public int[] maxCounters(int counterCount, int... operations) {
        return new MaxCounters().solve(counterCount, operations);
    }

    public int missingInteger(int... values) {
        return new MissingInteger().solve(values);
    }

    public int tapeEquilibrium(int... tape) {
        return new TapeEquilibrium().solve(tape);
    }
    
    public int minAbsSumOfTwo(int... numbers) {
        return new MinAbsSumOfTwo().solve(numbers);
    }
    
    public int minAbsSum(int... numbers) {
        return new MinAbsSum().solve(numbers);
    }
    public int maxSliceSum(int... numbers) {
        return new MaxSliceSum().solve(numbers);
    }
    public int maxDoubleSliceSum(int... numbers) {
        return new MaxDoubleSliceSum().solve(numbers, numbers.length);
    }

    public int[] countNonDivisible(int... numbers) {
        return new CountNonDivisible().solve(numbers);
    }
    
    public int[] toArray(int... numbers) {
        return numbers;
    }

    public int binaryGap(int i) {
        return new BinaryGap().solve(i);
    }

    public int equilibrium(int... values) {
        return new Equilibrium().solve(values);
    }
    
    public int[] generateRandom(int quantity, int lowest, int highest) {
        int[] result = new int[quantity];
        Random random = new Random(0);
        int delta = highest - lowest;
        for (int i = 0; i < result.length; i++) {
            result[i] = lowest + random.nextInt(delta);
        }
        return result;
    }

    public int[] generateFrom(int quantity, int... values) {
        int[] result = new int[quantity];
        Random random = new Random(0);
        for (int i = 0; i < result.length; i++) {
            result[i] = values[random.nextInt(values.length)];
        }
        return result;
    }
    
    public int[] generateRepeated(int quantity, int... values) {
        int[] result = new int[quantity];
        for (int i = 0; i < result.length; i++) {
            result[i] = values[i % values.length];
        }
        return result;
    }

    public int[] generateConstant(int quantity, int value) {
        int[] result = new int[quantity];
        Arrays.fill(result, value);
        return result;
    }

    public int[] generateRange(int from, int to) {
        int[] result = new int[to - from + 1];
        for (int i = 0; i < result.length; i++) {
            result[i] = i + from;
        }
        return result;
    }
}
