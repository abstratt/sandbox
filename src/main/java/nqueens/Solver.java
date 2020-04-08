package nqueens;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * Solves the n-queens problem for grid sizes greater than 3, while simultaneously ensuring no 3 queens are on the same line.
 * 
 * 
 * @see https://en.wikipedia.org/wiki/Eight_queens_puzzle
 * @see https://en.wikipedia.org/wiki/No-three-in-line_problem
 */
public class Solver {

    protected static final int EMPTY = -1;

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
        NotEnoughQueens,
        QueensUnderThreat,
        ThreeQueensOnSameLine
    }

    /**
     * Solves the n-queens problem for a grid size 4 or bigger.
     * 
     * @param gridSize
     * @return a board representing the squares occupied
     */

    public Board solve(int gridSize) {
        assert gridSize >= 4;
        int[] columns = doSolve(gridSize);
        return asBoard(columns);
    }

    /**
     * Converts the given selection array into a board.
     * 
     * @param columns
     * @return
     */
    protected static Board asBoard(int[] columns) {
        int gridSize = columns.length;
        Square[] asSquares = IntStream.range(0, gridSize)//
                .filter(row -> columns[row] >= 0)
                .mapToObj(row -> new Square(row, columns[row]))//
                .filter(Objects::nonNull)//
                .toArray(sz -> new Square[sz]);
        return new Board(gridSize, asSquares);
    }

    protected int[] doSolve(int gridSize) {
        int[] columns = new int[gridSize];
        Arrays.fill(columns, EMPTY);
        boolean solved = solveStage(columns, 0);
        assert solved;
        return columns;
    }

    /**
     * Advances the solution towards the N-queens problem.
     * 
     * Works column by column, "left-to-right", reserving a row for each column at a time.
     * 
     * Every level makes an election for a row for the current column, and then calls {@link #solve(int[], int))} so a row the next level/column.
     * 
     * The given 'selectedRows' is an n-sized array (potentially incomplete, sparsely populated) that is a partial solution for the N-queens problem, where each element contains
     * the index of the column that has reserved the row corresponding to that position (the index is the fixed row, the value is the potentially undefined column reserving it).
     * 
     * @param selectedRows
     *            partially populated choices of rows for each column
     * @param currentColumn
     *            the current column to find a row for
     * @return true if a solution was obtained, false otherwise
     */
    protected boolean solveStage(int[] selectedRows, int currentColumn) {
        int gridSize = selectedRows.length;

        if (currentColumn == gridSize) {
            // we got a full solution
            return true;
        }

        // try to find a row for the current column
        for (int rowToTry = 0; rowToTry < gridSize; rowToTry++) {
            if (selectedRows[rowToTry] == EMPTY) {
                selectedRows[rowToTry] = currentColumn;
                if (currentColumn == 0 || checkNoDiagonal(selectedRows, rowToTry)) {
                    if (currentColumn < 2 || checkNoThreeOnSameLine(selectedRows, rowToTry)) {
                        if (solveStage(selectedRows, currentColumn + 1)) {
                            return true;
                        }
                    }
                }
                selectedRows[rowToTry] = EMPTY;
            }
        }
        return false;
    }

    /**
     * Ensures no three occupied squares are in a line.
     * 
     * @param rowsSelected
     * @param rowToTry
     * @return true if no three occupied squares are in a line
     */
    protected static boolean checkNoThreeOnSameLine(int[] rowsSelected, int rowToTry) {
        for (int point1 = 0; point1 < rowsSelected.length - 1; point1++) {
            for (int point2 = point1 + 1; point2 < rowsSelected.length; point2++) {
                if (rowToTry != point1 && rowToTry != point2 && rowsSelected[point1] != EMPTY && rowsSelected[point2] != EMPTY) {
                    if (Square.sameLine(point1, rowsSelected[point1], point2, rowsSelected[point2], rowToTry, rowsSelected[rowToTry])) {
                        return false;
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
     * @param currentColumn
     *            the current column, columns after this one are not checked
     * @return true if no diagonal found between the given row and other already determined rows
     */
    protected static boolean checkNoDiagonal(int[] rowsSelected, int rowToTry) {
        for (int otherRow = 0; otherRow < rowsSelected.length; otherRow++) {
            if (otherRow != rowToTry && rowsSelected[otherRow] != EMPTY && Square.sameDiagonal(rowToTry, rowsSelected[rowToTry], otherRow, rowsSelected[otherRow])) {
                return false;
            }
        }
        return true;
    }
}
