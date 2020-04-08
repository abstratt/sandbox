package nqueens;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class MTSolverTest extends SolverTest<MTSolver> {

	@Override
	protected MTSolver createSolver() {
		return new MTSolver();
	}

	@AfterEach
	public void shutdownSolver() {
		Optional.ofNullable(getSolver()).ifPresent(MTSolver::shutdown);
	}

	@ParameterizedTest
	@ValueSource(ints = { 26, 27, 28 })
	public void solveLarger(int size) {
		solveAndCheck(size);
	}

	@ParameterizedTest
	@ValueSource(ints = { 29, 30 })
	@EnabledIfSystemProperty(named = "nqueens.slowTests", matches = "true")
	public void solveSlow(int size) {
		solveAndCheck(size);
	}
}
