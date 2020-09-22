package loft;

public class MaxMagnitudeAfterInserting5 {
	public int solution(int N) {
		if (N < 0) {
			// negative numbers are a special case
			int positive = N * -1;
			return Integer.parseInt("-5" + positive);
		}
		// find the first digit that is lower than 5 and insert '5' there
		char[] digits = Integer.toString(N).toCharArray();
		int insertionPoint = digits.length;
		for (int i = 0; i < digits.length; i++) {
			if (digits[i] < '5') {
				insertionPoint = i;
				break;
			}
		}
		String result = new String(digits, 0, insertionPoint) + 5 + new String(digits, insertionPoint, digits.length - insertionPoint);
		return Integer.parseInt(result);
	}
}
