package elastictest;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.Scanner;

// Main class should be named 'Solution'
class Solution {
    // A tracker of the maximum score that has been seen so far
    public static class MaxScoreTracker {    
        private LongAccumulator maxScore = new LongAccumulator(Math::max, Integer.MIN_VALUE);
        
        // Get the current value of the maximum score
        public int getMaxScore() {
            return maxScore.intValue();
        }
        
        // Update the maximum score if, and only if, 
        // the given score is greater than the current maximum score
        public void updateMaxScoreIfNecessary(int score) {
        	maxScore.accumulate(score);
        }
    }

    public static void main(String[] args) throws Exception {
        int numThreads = 4;
        int iters = 1000;
        
        int res = test(numThreads, iters);
        System.out.println(res);
    }
    
    private static int test(int numThreads, int iters) throws Exception {
        Random random = new Random(0);
        int maxDelta = 0;
        for (int iter = 0; iter < iters; ++iter) {
            MaxScoreTracker maxScoreTracker = new MaxScoreTracker();
            Thread[] threads = new Thread[numThreads];
            int maxScore = Integer.MIN_VALUE;
            CountDownLatch startSignal = new CountDownLatch(1);
            for (int i = 0; i < numThreads; ++i) {
                final int score = random.nextInt();
                maxScore = Math.max(maxScore, score);
                threads[i] = new Thread(() -> {
                    try {
                        startSignal.await();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    maxScoreTracker.updateMaxScoreIfNecessary(score);
                });
                threads[i].start();
            }
            startSignal.countDown();
            for (Thread thread : threads) {
                thread.join();
            }
            maxDelta = Math.max(maxDelta, Math.abs(maxScoreTracker.getMaxScore() - maxScore));
        }
        
        return maxDelta;
    }
}
