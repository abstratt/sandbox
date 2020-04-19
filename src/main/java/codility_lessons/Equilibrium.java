package codility_lessons;

// https://blog.codility.com/2011/03/solutions-for-task-equi.html
public class Equilibrium {
    public int solve(int[] A) {
        if (A.length < 2) {
            return -1;
        }
        for (int i = 0; i < A.length; i++) {
            System.out.print(A[i] + " ");
        }
        System.out.println();
        int initialPivot = Math.max(A.length / 2, 1);
        return searchSolution(A, 0, A.length - 1, initialPivot, initialPivot);
    }
    
    
    
    private int searchSolution(int[] a, int lower, int upper, int currentPivot, int oldPivot) {
        int lowerSum = sum(a, lower, currentPivot - 1);
        int upperSum = sum(a, currentPivot + 1, upper);
        if (lowerSum == upperSum) {
            return currentPivot;
        }
        int newPivot;
        if (lowerSum > upperSum) {
            newPivot = Math.max(lower + (currentPivot - lower) / 2, currentPivot - 1);
        } else {
            newPivot = Math.min(currentPivot + (upper - currentPivot) / 2, currentPivot + 1);
        }
        System.out.println("lowerSum: " + lowerSum + " - upperSum: " + upperSum + " pivot: " + currentPivot + " newPivot: " + newPivot + " oldPivot: " + oldPivot);
        if (newPivot == currentPivot || newPivot <= lower || newPivot >= upper || (oldPivot > currentPivot && newPivot > currentPivot) || (oldPivot < currentPivot && newPivot < currentPivot)) {
            return -1;
        }
        return searchSolution(a, lower, upper, newPivot, currentPivot);
    }

    public int sum(int[] toSum, int start, int end) {
        int total = 0;
        for (int i = start; i <= end; i++) {
            total += toSum[i];
        }
        return total;
    }
}