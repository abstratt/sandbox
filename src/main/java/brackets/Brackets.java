package brackets;

import java.util.Deque;
import java.util.LinkedList;

public class Brackets {
	
	private static String OPENING_BRACKETS = "{([";
	// must match OPENING_BRACKETS
	private static String CLOSING_BRACKETS = "})]";

	public static boolean isBalanced(String toCheck) {
		if (toCheck.isEmpty()) {
			return false;
		}
		Deque<Character> stack = new LinkedList<>();
		int length = toCheck.length();
		for (int i = 0;i < length;i++) {
			char currentChar = toCheck.charAt(i);
			int openingBracketIndex = OPENING_BRACKETS.indexOf(currentChar);
			if (openingBracketIndex >= 0) {
				// opening bracket
				stack.push(currentChar);
			} else {
				int closingBracketIndex = CLOSING_BRACKETS.indexOf(currentChar);	
				if (closingBracketIndex >= 0) {
					// closing bracket
					Character top = stack.peek();
					char expectedOpening = OPENING_BRACKETS.charAt(closingBracketIndex);
					if (top == null || !top.equals(expectedOpening)) {
						// stack is empty or brackets don't match
						return false;
					}
					// matching so far
					stack.pop();
				} else {
					// not a bracket
				}
			}
		}
		return stack.isEmpty();
	}

	public static void main(String[] args) {
    	assert isBalanced("[[[]]]");
    	assert isBalanced("[[{{(fjksdghjfds)}}]]");
    	assert isBalanced("f(e(d))");
    	assert isBalanced("[()]{}([])") ;
    	assert !isBalanced("[[[]]");
    	assert !isBalanced("]");
    	assert !isBalanced("{(a[]})");
    	assert !isBalanced("([)]");
    	assert !isBalanced(")(");
    	assert !isBalanced("");

	}
}