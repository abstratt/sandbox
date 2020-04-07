package nqueens;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * A variant of NQueens that can take advantage of multiple processors.
 */
public class MTSolver extends Solver {
	private ExecutorService executors = Executors.newWorkStealingPool();

	@Override
	protected int[] doSolve(int gridSize) {
		if (gridSize <= 16) {
			return doSolveSequential(gridSize);
		}
		return doSolveParallel(gridSize);
	}

	private int[] doSolveParallel(int gridSize) {
		BlockingQueue<int[]> results = new LinkedBlockingQueue<>(1);
		
		// attempts to find solutions in separate threads
		Stream<Runnable> tasks = IntStream.range(0, gridSize)
				.mapToObj(firstColumn -> generateVersion(gridSize, firstColumn))
				.map(version -> (() -> solveVersion(version, results::offer)));
		
		List<Future<?>> futures = tasks.map(task -> executors.submit(task)).collect(Collectors.toList());
		int[] result = null;
		while (result == null) {
			try {
				result = results.take();
			} catch (InterruptedException e) {
				// not interruptable
			}
		}
		futures.forEach(f -> f.cancel(true));
		return result;
	}
	
	private void solveVersion(int[] version, Consumer<int[]> solutionConsumer) {
		boolean solved = super.solveStage(version, 1);
		if (solved) {
			solutionConsumer.accept(version);
		}
	}
	
	@Override
	protected boolean solveStage(int[] rowSelection, int currentColumn) {
		if (Thread.interrupted()) {
			return false;
		}
		return super.solveStage(rowSelection, currentColumn);
	}
	

	private int[] doSolveSequential(int gridSize) {
		return super.doSolve(gridSize);
	}
	
	/**
	 * Generates a partially defined potential solution that selects
	 * the given selected row for the first column.
	 * 
	 * @param gridSize
	 * @param selectedRow
	 * @return an array of row selections per column with only a row selection for 
	 * the first column
	 */
	private int[] generateVersion(int gridSize, int selectedRow) {
		int[] rowSelection = new int[gridSize];
		Arrays.fill(rowSelection, -1);
		rowSelection[selectedRow] = 0;
		return rowSelection;
	}
}
