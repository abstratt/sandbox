package nqueens;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

public class MTSolverTest extends SolverTest {
	
	@BeforeEach
	public void initSolver() {
		solver = new MTSolver();
	}
	
	@Test
	public void solve17() {
		getSolver().solve(17).checkSolution();
	}
	
	@Test
	public void solve18() {
		getSolver().solve(18).checkSolution();
	}
	
	@Test
	@EnabledIfSystemProperty(named = "nqueens.slowTest", matches="true")
	public void solve19() {
		getSolver().solve(19).checkSolution();
	}
	
	@Test
	@EnabledIfSystemProperty(named = "nqueens.slowTest", matches="true")
	public void solve20() {
		getSolver().solve(20).checkSolution();
	}

}
