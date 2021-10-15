package elastictest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Test {
	@org.junit.jupiter.api.Test
	void brackets() {
		var s = new Solutions();
		assertTrue(s.isBalanced("()"));
		assertTrue(s.isBalanced(""));
		assertTrue(s.isBalanced("asdasdsa"));
		assertTrue(s.isBalanced("((()()(()))(()) )( )"));
		assertFalse(s.isBalanced("(dcasdsa"));
		assertFalse(s.isBalanced("fsfsfds)(dcasdsa"));
		assertTrue(s.isBalanced("dsa(dfsd(fsfsfds)(dcasdsadas))()"));
	}
	
	@org.junit.jupiter.api.Test
	void multiples() {
		var s = new Solutions();
		//assertEquals(3, s.searchMultiples(1000000, 1000000, 1000000, 2000001, 5000000));
		assertEquals(6, s.searchMultiples(1000000, 1000000, 1000000, 1000000, 40000000, 40000000, 40000001, 50000000, 6000001));
		//assertEquals(0, s.searchMultiples(1000000, 0, 1));
		//assertEquals(3, s.searchMultiples(1000000, 1000000, 1000000, 1000000));
	}
}
