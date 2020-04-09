package nqueens;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * A variant of N-Queens that can take advantage of multiple processors.
 */
public class MTSolver extends Solver {
	private ExecutorService executors = createExecutor();

	/**
	 * Set when one solution is found so any ongoing resolution attempts can be
	 * aborted.
	 */
	private volatile boolean shouldCancel;

	@Override
	protected synchronized int[] doSolve(int gridSize) {
		if (gridSize <= 16) {
			return doSolveSequentially(gridSize);
		}
		return doSolveInParallel(gridSize);
	}

	private int[] doSolveSequentially(int gridSize) {
		return super.doSolve(gridSize);
	}

	private int[] doSolveInParallel(int gridSize) {
		int generation = new Random().nextInt();
		shouldCancel = false;

		BlockingQueue<Optional<int[]>> results = new LinkedBlockingQueue<>();

		// attempts to find solutions in separate threads
		List<FutureTask<int[]>> partialSolvers = IntStream.range(0, gridSize)
				.mapToObj(firstColumn -> new Request(generation, firstColumn, generateVersion(gridSize, firstColumn), 1,
						results::add))
				.map(request -> new FutureTask<int[]>(request::solveRemainder, null)).collect(Collectors.toList());

		partialSolvers.forEach(executors::execute);
		Optional<int[]> result = null;
		try {
			for (int i = 0; i < gridSize - 1 && !(result = results.take()).isPresent(); i++)
				;
			shouldCancel = true;
		} catch (InterruptedException e) {
			// interruption leads to no results...
		}
		return result.orElseThrow(() -> new UnsolvableException());
	}

	@Override
	protected boolean solveStage(int[] rowSelection, int currentColumn) {
		if (shouldCancel) {
			throw new AbortedComputationException();
		}
		return super.solveStage(rowSelection, currentColumn);
	}

	/**
	 * Generates a partially defined potential solution that selects the given
	 * selected row for the first column.
	 * 
	 * @param gridSize
	 * @param selectedRow
	 * @return an array of row selections per column with only a row selection for
	 *         the first column
	 */
	private int[] generateVersion(int gridSize, int selectedRow) {
		int[] rowSelection = new int[gridSize];
		Arrays.fill(rowSelection, EMPTY);
		rowSelection[selectedRow] = 0;
		return rowSelection;
	}

	/**
	 * A solution request for a partially configured board.
	 */
	private class Request {
		private int[] partial;
		private Consumer<Optional<int[]>> resultConsumer;
		private Object requestId;
		private Object generation;
		private int columnsToIgnore;

		public Request(Object generation, Object requestId, int[] partial, int columnsToIgnore,
				Consumer<Optional<int[]>> resultConsumer) {
			this.requestId = requestId;
			this.generation = generation;
			this.partial = partial;
			this.columnsToIgnore = columnsToIgnore;
			this.resultConsumer = resultConsumer;
		}

		/**
		 * Tries to solve the n-queens problem based on a partial solution.
		 */
		void solveRemainder() {
			debug(() -> "Request started " + this.asSimpleString());
			try {
				boolean solved = solveStage(partial, columnsToIgnore);
				debug(() -> "Request completed " + this + " - Solved? " + solved);
				resultConsumer.accept(Optional.ofNullable(solved ? partial : null));
			} catch (AbortedComputationException e) {
				// business as usual, some other request got to the answer first
				debug(() -> "Request aborted - " + this);
				resultConsumer.accept(Optional.empty());
			}
		}

		@Override
		public String toString() {
			return asSimpleString() + "\n" + asBoard(partial);
		}

		public String asSimpleString() {
			return generation + " - id: " + requestId;
		}
	}

	public static class AbortedComputationException extends RuntimeException {
		private static final long serialVersionUID = 1L;
	}

	public void shutdown() {
		debug(() -> "Terminating execution service");
		executors.shutdownNow();
	}

	private ExecutorService createExecutor() {
		ExecutorService newExecutor = new ThreadPoolExecutor(4, 40, 2, TimeUnit.SECONDS,
				new SynchronousQueue<Runnable>()) {
			protected void afterExecute(Runnable r, Throwable t) {
				if (t != null) {
					t.printStackTrace();
				} else if (r instanceof Future) {
					try {
						((Future<?>) r).get();
					} catch (InterruptedException | ExecutionException e) {
						error(e);
					}
				}
			}
		};
		return newExecutor;
	}
}
