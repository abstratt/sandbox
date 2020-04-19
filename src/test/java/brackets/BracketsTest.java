package brackets;

import org.junit.jupiter.api.Test;

import static brackets.Brackets.isBalanced;

public class BracketsTest {
	@Test
    public void test() {
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