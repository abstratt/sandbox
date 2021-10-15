package elastictest;

import java.util.Arrays;

public class Solutions {
	boolean isBalanced(String text) {
		var depth = 0;
		var asChars = text.toCharArray();
		for (int i = 0; i < asChars.length; i++) {
			switch (asChars[i]) {
			case '(':depth++;break;
			case ')':--depth;
			}
			if (depth < 0) {
				return false;
			}
		}
		return depth == 0;
	}
	
	int searchMultiples(long value, long... numbers) {
		int found = 0;
		int offset = 0;
		while (offset <= numbers.length - 1) {
			long multiplier = numbers[offset] / value;
			var partial = searchNext(numbers, multiplier * value, offset);
			offset = partial.offset;
			found += partial.count;
		}
		return found;
	}
	
	PartialResult searchNext(long[] numbers, long value, int offset) {
		System.out.println("Searching for " + value);
		var index = Arrays.binarySearch(numbers, offset, numbers.length, value);
		if (index < 0)
			return new PartialResult(0, Math.abs(index));
		int count = 1;
		// count duplicates before the one found
		for (int i = index-1; i >= 0 && numbers[i] == value; i--) {
			count++;
		}
		// count duplicates after the one found
		for(index++; index < numbers.length && numbers[index] == value; index++) {
			count++;
		}
		System.out.println("Found " + value + " " + count + " times");
		return new PartialResult(count, index);
	}
}


class PartialResult {
	public final int count;
	public final int offset;
	public PartialResult(int count, int offset) {
		this.count = count;
		this.offset = offset;
	}
}