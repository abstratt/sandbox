package nqueens;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * A variant of N-Queens that can take advantage of multiple processors.
 */
public class MTSolver extends Solver {
	private ExecutorService executors = createExecutor();

	private volatile AtomicBoolean shouldCancel;

	@Override
    public synchronized Board solve(int gridSize) {
        if (gridSize <= 16) {
            return doSolveSequentially(gridSize);
        }
        return doSolveInParallel(gridSize);
    }
	
    private Board doSolveSequentially(int gridSize) {
        return new Solver().solve(gridSize);
    }

    private Board doSolveInParallel(int gridSize) {
        int generation = new Random().nextInt();
        shouldCancel = new AtomicBoolean();
        shouldCancel.set(false);

		BlockingQueue<int[]> results = new LinkedBlockingQueue<>(1);
		
		// attempts to find solutions in separate threads
		List<Request> partialSolvers = IntStream.range(0, gridSize)
				.mapToObj(firstColumn -> new Request(generation, firstColumn, generateVersion(gridSize, firstColumn), results::offer))
				.collect(Collectors.toList());
		
		partialSolvers.forEach(solver -> executors.submit(solver::solveRemainder));
		int[] result = null;
		while (result == null) {
			try {
				result = results.take();
			} catch (InterruptedException e) {
				// not interruptable
			}
		}
		partialSolvers.forEach(Request::cancel);
        shouldCancel.lazySet(true);
        
		return asBoard(result);
    }
	
	@Override
	protected boolean solveStage(int[] rowSelection, int currentColumn) {
	    if (shouldCancel == null || shouldCancel.get()) {
	        throw new AbortedComputationException();
	    }
	    return super.solveStage(rowSelection, currentColumn);
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
		Arrays.fill(rowSelection, EMPTY);
		rowSelection[selectedRow] = 0;
		return rowSelection;
	}
	
	private class Request {
	    private int[] partial;
	    private Consumer<int[]> resultConsumer;
        private Object requestId;
        private Object generation;
	    public Request(Object generation, Object requestId, int[] partial, Consumer<int[]> resultConsumer) {
	        this.requestId = requestId;
	        this.generation = generation;
            this.partial = partial;
            this.resultConsumer = resultConsumer;
        }
	    /**
	     * Tries to solve the n-queens problem based on a partial solution.
	     */
        void solveRemainder() {
            log("Request started " + this.asSimpleString());
            try {
                boolean solved = solveStage(partial, 1);
                log("Request completed " + this);
                if (solved) {
                	resultConsumer.accept(partial);
                }
            } catch (AbortedComputationException e) {
                // business as usual, some other request got to the answer first
                log("Request aborted" + this);
            }
        }
        
        void cancel() {
            
        }
        
        @Override
        public String toString() {
            return asSimpleString() + "\n" + asBoard(partial);
        }
        
        public String asSimpleString() {
            return generation + " - id: " + requestId;
        }
	}
	
    public void shutdown() {
        log("Terminating execution service");
        executors.shutdownNow();
    }

    private ExecutorService createExecutor() {
        ThreadFactory threadFactory = (Runnable runnable) -> {
            Thread newThread = new Thread(runnable);
            newThread.setUncaughtExceptionHandler((thread, throwable) -> throwable.printStackTrace());
            return newThread;
        };
        ExecutorService newExecutor = new ThreadPoolExecutor(4, 40,
                2, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>(),
                threadFactory);
        return newExecutor;
    }
    
    private static void log(String toLog) {
        System.out.println(Instant.now().toString() + ": " + toLog);
    }
}
