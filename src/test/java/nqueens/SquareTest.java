package nqueens;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class SquareTest {
	@Test
	public void sameLine() {
		Square a = new Square(0, 0);
		Square b = new Square(1, 2);
		Square c = new Square(2, 4);
		Square d = new Square(3, 1);
		Square e = new Square(1, 5);
		Square f = new Square(2, 3);
		Square g = new Square(1, 6);
		Square h = new Square(0, 7);
		Square i = new Square(3, 0);
		Square j = new Square(5, 7);
		Square k = new Square(4, 3);

		checkSameLine(true, a, b, c);
		checkSameLine(false, a, b, d);
		checkSameLine(false, a, b, e);
		checkSameLine(true, b, e, g);
		checkSameLine(true, d, e, f);
		checkSameLine(false, e, d, g);
		checkSameLine(true, d, e, h);
		checkSameLine(true, e, f, h);
		checkSameLine(false, i, j, k);
	}

	private void checkSameLine(boolean expected, Square a, Square b, Square c) {
		String board = "\n" + new Board(8, a, b, c);
		assertEquals(expected, Square.sameLine(a, b, c), board);
		assertEquals(expected, Square.sameLine(a, c, b), board);
		assertEquals(expected, Square.sameLine(b, a, c), board);
		assertEquals(expected, Square.sameLine(b, c, a), board);
		assertEquals(expected, Square.sameLine(c, a, b), board);
		assertEquals(expected, Square.sameLine(c, b, a), board);
	}

	@Test
	public void isThreat() {
		checkThreat(true, 1, 1, 7, 7);
		checkThreat(true, 1, 3, 4, 3);
		checkThreat(true, 1, 2, 4, 5);
		checkThreat(false, 1, 2, 4, 6);
		checkThreat(false, 4, 4, 5, 8);
	}

	private void checkThreat(boolean expected, int row1, int column1, int row2, int column2) {
		assertEquals(expected, new Square(row1, column1).isThreatTo(new Square(row2, column2)));
		assertEquals(expected, new Square(row2, column2).isThreatTo(new Square(row1, column1)));
		assertEquals(expected, new Square(column1, row1).isThreatTo(new Square(column2, row2)));
		assertEquals(expected, new Square(column2, row2).isThreatTo(new Square(column1, row1)));
	}

	@Test
	public void checkNoDiagonal() {
		assertEquals(false, Solver.checkNoDiagonal(new int[] {0, 3, 1, 2}, 3));
		assertEquals(true, Solver.checkNoDiagonal(new int[] {1, 3, 0, 2}, 3));
		assertEquals(true, Solver.checkNoDiagonal(new int[] {0, 3, 1, -1}, 3));
		assertEquals(false, Solver.checkNoDiagonal(new int[] {0, -1, 1, 3}, 3));
		assertEquals(true, Solver.checkNoDiagonal(new int[] {0, -1, 3, 1}, 3));
		assertEquals(false, Solver.checkNoDiagonal(new int[] {0, -1, 1, 2}, 3));
	}
}
