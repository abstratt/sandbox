package nqueens;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

public class MTSolverTest extends SolverTest<MTSolver> {

    @Override
    protected MTSolver createSolver() {
		return new MTSolver();
	}
	
	@AfterEach
    public void shutdownSolver() {
        Optional.ofNullable(getSolver()).ifPresent(MTSolver::shutdown);
    }
	
    @Test
    public void solve27() {
        getSolver().solve(27).checkSolution();
    }

    @Test
    public void solve28() {
        getSolver().solve(28).checkSolution();
    }
    
    @Test
    @EnabledIfSystemProperty(named = "nqueens.slow", matches = "true")
    public void solve35() {
        getSolver().solve(35).checkSolution();
    }
}
