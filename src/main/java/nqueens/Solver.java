package nqueens;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.IntStream;

public class Solver {

	public static class InvalidSolutionException extends RuntimeException {

		private static final long serialVersionUID = 1L;

		private Reason reason;

		public InvalidSolutionException(Reason reason) {
			this(reason, "");
		}

		public InvalidSolutionException(Reason reason, String message) {
			super(reason + " - " + message);
			this.reason = reason;
		}

		public Reason getReason() {
			return reason;
		}
	}

	enum Reason {
		NotEnoughQueens, QueensUnderThreat, ThreeQueensOnSameLine, GridToSmall
	}

	public Board solve(int gridSize) {
		int[] columns = doSolve(gridSize);
		Square[] asSquares = IntStream.range(0, columns.length)//
				.mapToObj(row -> columns[row] >= 0 ? new Square(row, columns[row]) : null)//
				.filter(Objects::nonNull)//
				.toArray(sz -> new Square[sz]);
		return new Board(asSquares.length, asSquares);
	}

	protected int[] doSolve(int gridSize) {
		int[] columns = new int[gridSize];
		Arrays.fill(columns, -1);
		boolean solved = solveStage(columns, 0);
		assert solved;
		return columns;
	}

	/**
	 * Advances the solution towards the N-queens problem.
	 * 
	 * Works column by column, left-to-right, reserving a row for each column at a time. 
	 * 
	 * Every level makes an election for a row for the current column, and then
	 * calls {@link #solve(int[], int))} so a row the next level/column.
	 * 
	 * The given 'rowSelection' is an n-sized array (potentially incomplete, sparsely
	 * populated) that is a partial solution for the N-queens problem, where each
	 * element contains the index of the column that has reserved the row corresponding to that
	 * position (the index is the fixed row, the value is the potentially undefined column reserving it).
	 * 
	 * @param rowSelection partially populated choices of rows for each column
	 * @param currentColumn the current column to find a row for
	 * @return true if a solution was obtained, false otherwise
	 */
	protected boolean solveStage(int[] rowSelection, int currentColumn) {
		int gridSize = rowSelection.length;
		if (currentColumn == gridSize) {
			// full solution - validate it for no lines
			return checkNoThreeOnSameLine(rowSelection);
		}
		// try to find a row for the current column
		for (int row = 0; row < gridSize; row++) {
			if (rowSelection[row] == -1) {
				rowSelection[row] = currentColumn;
				if (checkNoDiagonal(rowSelection, row)) {
					if (solveStage(rowSelection, currentColumn + 1)) {
						return true;
					}
				}
				rowSelection[row] = -1;
			}
		}
		return false;
	}

	protected static boolean checkNoThreeOnSameLine(int[] columns) {
		for (int thisRow = 0; thisRow < columns.length - 2; thisRow++) {
			for (int otherRow = thisRow + 1; otherRow < columns.length - 1; otherRow++) {
				for (int k = 0; k < columns.length; k++) {
					if (k != thisRow && k != otherRow) {
						if (Square.sameLine(thisRow, columns[thisRow], otherRow, columns[otherRow], k, columns[k])) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}

	/**
	 * Ensures no two occupied squares are in a diagonal to each other.
	 * 
	 * @param rowsSelected
	 * @param currentColumn the current column, columns after this one are not
	 *                      checked
	 * @return
	 */
	protected static boolean checkNoDiagonal(int[] rowsSelected, int rowToTry) {
		for (int otherRow = 0; otherRow < rowsSelected.length; otherRow++) {
			if (otherRow != rowToTry && rowsSelected[otherRow] != -1 && Square.sameDiagonal(rowToTry, rowsSelected[rowToTry], otherRow, rowsSelected[otherRow])) {
				return false;
			}
		}
		return true;
	}

	public static void main(String[] args) {
		Board example = new Board(14,
				Arrays.asList(new Square(0, 5), new Square(3, 3), new Square(4, 7), new Square(1, 1)));
		System.out.println(example);
	}
}
