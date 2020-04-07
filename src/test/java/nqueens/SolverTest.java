package nqueens;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SolverTest {
	protected Solver solver;
	
	@BeforeEach
	public void initSolver() {
		solver = new Solver();
	}
	
	protected Solver getSolver() {
		return solver;
	}
	
	@Test
	public void solve8() {
		getSolver().solve(8).checkSolution();
	}

	@Test
	public void solve10() {
		getSolver().solve(10).checkSolution();
	}

	@Test
	public void solve15() {
		getSolver().solve(15).checkSolution();
	}
}
