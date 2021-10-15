package loft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/*
.........
.#######.
.#.....#.
.#.....#.
.#.....#.
.#######.
.........

{ 0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,0,0,1,0,0,0,0,0,1,0,0,1,0,0,0,0,0,1,0,0,1,0,0,0,0,0,1,0,0,1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0 }

saida:
{ 0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,0,0,1,2,2,2,2,2,1,0,0,1,2,2,2,2,2,1,0,0,1,2,2,2,2,2,1,0,0,1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0 }

4 3 2

.........
.#######.
.#+++++#.
.#+++++#.
.#+++++#.
.#######.
.........

*/

public class Solution {

	public void paint(int[] grid, int width, int row, int column, int newPixel) {
		int pos = row * width + column;
		int oldPixel = grid[pos];
		if (oldPixel == newPixel) {
			return;
		}
		paintNeighbours(grid, width, row, column, oldPixel, newPixel);
	}

	private void paintNeighbours(int[] grid, int width, int row, int column, int oldPixel, int newPixel) {
		int rowCount = grid.length / width;
		int pos = row * width + column;
		if (row < 0 || column < 0 || column >= width || row >= rowCount) {
			return;
		}

		if (grid[pos] == oldPixel) {
			grid[pos] = newPixel;
			paintNeighbours(grid, width, row - 1, column, oldPixel, newPixel);
			paintNeighbours(grid, width, row + 1, column, oldPixel, newPixel);
			paintNeighbours(grid, width, row, column - 1, oldPixel, newPixel);
			paintNeighbours(grid, width, row, column + 1, oldPixel, newPixel);
		}
	}

	@Test
	public void mono() {
		int[] grid = { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		int[] esperado = { 1, 1, 1, 1, 1, 1, 1, 1, 1 };
		paint(grid, 3, 1, 1, 1);
		Assertions.assertArrayEquals(esperado, grid);

	}

	@Test
	public void single() {
		int[] grid = { 0, 0, 0, 0, 1, 0, 0, 0, 0 };
		int[] esperado = { 0, 0, 0, 0, 2, 0, 0, 0, 0 };
		paint(grid, 3, 1, 1, 2);
		Assertions.assertArrayEquals(esperado, grid);

	}

	@Test
	public void corner() {
		int[] grid = { 1, 1, 0, 1, 0, 0, 0, 0, 1 };
		int[] esperado = { 2, 2, 0, 2, 0, 0, 0, 0, 1 };
		paint(grid, 3, 0, 0, 2);
		Assertions.assertArrayEquals(esperado, grid);

	}

	@Test
	public void twoValues() {
		int[] grid = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0,
				0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		int[] esperado = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 2, 2, 2, 2, 2, 1, 0, 0, 1, 2, 2,
				2, 2, 2, 1, 0, 0, 1, 2, 2, 2, 2, 2, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		paint(grid, 9, 4, 3, 2);
		Assertions.assertArrayEquals(esperado, grid);

	}

	@Test
	public void threeValues() {
		int[] grid = { 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 1, 2, 2, 2, 1, 0, 0, 1, 2, 2, 2, 1, 0, 0, 1, 2, 2,
				2, 1, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0 };
		int[] esperado = { 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 1, 3, 3, 3, 1, 0, 0, 1, 3, 3, 3, 1, 0, 0, 1, 3,
				3, 3, 1, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0 };
		paint(grid, 7, 4, 3, 3);
		Assertions.assertArrayEquals(esperado, grid);
	}

	public void sort(int[] toSort, int[] order) {
		Map<Integer, Integer> ranking = new HashMap<>();
		List<Integer> valuesAsIntegers = new ArrayList<>(toSort.length);
		for (int i = 0; i < toSort.length; i++) {
			valuesAsIntegers.add(toSort[i]);
		}
		for (int i = 0; i < order.length; i++) {
			ranking.put(order[i], i);
		}
		//
		valuesAsIntegers.sort((Integer a, Integer b) -> ranking.get(a).compareTo(ranking.get(b)));
		for (int i = 0; i < toSort.length; i++) {
			toSort[i] = valuesAsIntegers.get(i);
		}
	}

	@Test
	public void testShouldAddTwoNumbers() {
		int[] ordem = { 2, 1, 4, 3, 9, 6 };
		int[] valores = { 2, 3, 1, 3, 2, 4, 6, 9, 2 };
		int[] esperado = { 2, 2, 2, 1, 4, 3, 3, 9, 6 };
		sort(valores, ordem);
		Assertions.assertArrayEquals(esperado, valores);
	}

}
