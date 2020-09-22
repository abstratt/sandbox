package loft;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.TreeSet;

import org.junit.jupiter.api.Test;

public class LoftTests {
	@Test
	public void largestOneDigitNumber() {
		MaxOneDigitNumber solver = new MaxOneDigitNumber();
		assertEquals(1, solver.solve(-6, -91, 1011, -100, 84, -22, 0, 1, 473));
		assertEquals(8, solver.solve(1, 8, 3, -9, -100, 100));
		assertEquals(-1, solver.solve(-1, -9, -3, -100, 100));
		assertEquals(0, solver.solve(-9, 0, -1, 10, -10));
	}
	@Test
	public void maxMagnitudeAfterInserting5() {
		MaxMagnitudeAfterInserting5 solver = new MaxMagnitudeAfterInserting5();
		assertEquals(51, solver.solution(1));
		assertEquals(65, solver.solution(6));
		assertEquals(54, solver.solution(4));
		assertEquals(5268, solver.solution(268));
		assertEquals(5685, solver.solution(568));
		assertEquals(6685, solver.solution(668));
		assertEquals(6754, solver.solution(674));
		assertEquals(545671, solver.solution(45671));
		assertEquals(56751, solver.solution(5671));
		assertEquals(567517, solver.solution(56717));
		assertEquals(55551, solver.solution(5551));
		assertEquals(546717, solver.solution(46717));
		assertEquals(6750, solver.solution(670));
		assertEquals(50, solver.solution(0));
		assertEquals(510, solver.solution(10));
		assertEquals(9875, solver.solution(987));
		assertEquals(-599, solver.solution(-99));
		assertEquals(-51, solver.solution(-1));
	}
	
	@Test
	public void shortestUniqueSubstring() {
		ShortestUniqueSubstring solver = new ShortestUniqueSubstring();
		assertEquals(2, solver.solution("aabbccddaabbccdd"));
		assertEquals(2, solver.solution("abaaba"));
		assertEquals(3, solver.solution("aabbbabaaa"));
		assertEquals(new TreeSet<>(), new TreeSet<>(solver.findUniqueSubstrings("aabbbabaaa".toCharArray(), 1)));
		assertEquals(new TreeSet<>(Arrays.asList()), new TreeSet<>(solver.findUniqueSubstrings("aabbbabaaa".toCharArray(), 2)));
		assertEquals(new TreeSet<>(Arrays.asList("aaa", "aab", "aba", "abb", "baa", "bab", "bba", "bbb")), new TreeSet<>(solver.findUniqueSubstrings("aabbbabaaa".toCharArray(), 3)));
		assertEquals(new TreeSet<>(Arrays.asList("aaaaaaaaa")), new TreeSet<>(solver.findUniqueSubstrings("aaaaaaaaa".toCharArray(), 9)));
		assertEquals(new TreeSet<>(), new TreeSet<>(solver.findUniqueSubstrings("aabbccddaabbccdd".toCharArray(), 4)));
		assertEquals(new TreeSet<>(), new TreeSet<>(solver.findUniqueSubstrings("aabbccddaabbccdd".toCharArray(), 5)));
		assertEquals(new TreeSet<>(), new TreeSet<>(solver.findUniqueSubstrings("aabbccddaabbccdd".toCharArray(), 6)));
		assertEquals(new TreeSet<>(), new TreeSet<>(solver.findUniqueSubstrings("aabbccddaabbccdd".toCharArray(), 8)));
		assertEquals(new TreeSet<>(), new TreeSet<>(solver.findUniqueSubstrings("aabbccddaabbccdd".toCharArray(), 15)));
		assertEquals(new TreeSet<>(Arrays.asList("aabbccddaabbccdd")), new TreeSet<>(solver.findUniqueSubstrings("aabbccddaabbccdd".toCharArray(), 16)));
	}

}
