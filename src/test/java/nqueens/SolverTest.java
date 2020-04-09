package nqueens;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class SolverTest<S extends Solver> {

	protected S solver;

	@BeforeEach
	public void initSolver() {
		solver = createSolver();
	}

	@SuppressWarnings("unchecked")
	protected S createSolver() {
		return (S) new Solver();
	}

	protected S getSolver() {
		return solver;
	}

	@ParameterizedTest
	@ValueSource(ints = { 2, 3, 5, 6, 7 })
	public void unsolvable(int size) {
		assertThrows(UnsolvableException.class, () -> getSolver().solve(size));
	}

	@ParameterizedTest
	@ValueSource(ints = { 1, 4, 8, 9 })
	public void solveTiny(int size) {
		solveAndCheck(size);
	}

	@ParameterizedTest
	@ValueSource(ints = { 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20 })
	public void solveSmall(int size) {
		solveAndCheck(size);
	}

	@ParameterizedTest
	@ValueSource(ints = { 21, 22, 23, 24, 25 })
	public void solveMedium(int size) {
		solveAndCheck(size);
	}

	@ParameterizedTest
	@ValueSource(ints = { 26, 27 })
	@EnabledIfSystemProperty(named = "nqueens.slowTests", matches = "true")
	public void solveSlow(int size) {
		solveAndCheck(size);
	}

	protected void solveAndCheck(int size) {
		getSolver().solve(size).checkSolution();
	}

}
