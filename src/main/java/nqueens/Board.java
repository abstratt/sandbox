package nqueens;

import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import nqueens.Solver.InvalidSolutionException;
import nqueens.Solver.Reason;

public class Board {
	private int gridSize;
	private Set<Square> occupied;
	
	public boolean isValid() {
		if (gridSize != occupied.size()) {
			return false;
		}
		return true;
	}
	
	public void checkSolution() throws InvalidSolutionException {
		if (gridSize < 4) {
			throw new InvalidSolutionException(Reason.GridToSmall, "gridSize = " + gridSize);
		}
		if (gridSize > occupied.size()) {
			throw new InvalidSolutionException(Reason.NotEnoughQueens);
		}
		Square[] asArray = occupied.toArray(new Square[0]);
		// brute force, we can be slow but we can't be wrong
		for (int i = 0; i < asArray.length - 1; i++) {
			for (int j = i+1; j < asArray.length; j++) {
				if (asArray[i].doesThreat(asArray[j])) {
					throw new InvalidSolutionException(Reason.QueensUnderThreat, asArray[i] + " x " + asArray[j] + "\n" + this.toString());	
				}
				for (int k = 0; k < asArray.length; k++) {
					if (k != i && k != j) {
						if (asArray[i].sameLineAs(asArray[j], asArray[k])) {
							throw new InvalidSolutionException(Reason.ThreeQueensOnSameLine, asArray[i] + " x " + asArray[j] + " x " + asArray[k] + "\n" + this.toString());		
						}
					}
				}
			}
		}
		return;
	}

	public Board(int gridSize, Collection<Square> occupied) {
		this.gridSize = gridSize;
		this.occupied = new HashSet<>(occupied);
		for (Square square : occupied) {
			assert square.row <= gridSize;
			assert square.column <= gridSize;
		}
	}
	
	public Board(int gridSize, Square... occupied) {
		this(gridSize, Arrays.asList(occupied));
	}

	public Board(int gridSize, BitSet occupiedBits) {
		this(gridSize, toSquares(occupiedBits, gridSize));
	}

	private static Collection<Square> toSquares(BitSet occupiedBits, int gridSize) {
		Set<Square> asSet = new LinkedHashSet<>();
		int maximumOffset = gridSize*gridSize;
		for (int i = occupiedBits.nextSetBit(0); i >= 0
				&& i < maximumOffset; i = occupiedBits.nextSetBit(i + 1)) {
			int row = i / gridSize;
			int column = i % gridSize;
			asSet.add(new Square(row, column));
		}
		return asSet;
	}

	public String toString() {
		boolean[][] grid = new boolean[gridSize][gridSize];
		for (Square pos : occupied) {
			grid[pos.row][pos.column] = true;
		}
		StringBuffer result = new StringBuffer();
		result.append("  ");
		for (int col = 0; col < gridSize; col++) {
			result.append(' ');
			result.append(col % 10);
		}
		result.append('\n');
		for (int row = 0; row < gridSize; row++) {
			result.append(row % 10);
			result.append(" ");
			for (int col = 0; col < gridSize; col++) {
				result.append('|');
				result.append(grid[row][col] ? 'X' : '-');
			}
			result.append("|\n");
		}
		return result.toString();
	}

	public Collection<Square> getOccupied() {
		return occupied;
	}
}