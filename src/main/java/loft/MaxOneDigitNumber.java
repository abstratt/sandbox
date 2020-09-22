package loft;

public class MaxOneDigitNumber {
	public int solution(int[] A) {
		int max = -10;
		for (int i = 0; i < A.length; i++) {
			if (A[i] < 10 && A[i] > max) {
				max = A[i];
			}
		}
		return max;
	}

	public int solve(int... values) {
		return solution(values);
	}
}
