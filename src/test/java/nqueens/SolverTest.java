package nqueens;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

    @Test
    public void solve08() {
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

    @Test
    public void solve20() {
        getSolver().solve(20).checkSolution();
    }

    @Test
    public void solve25() {
        getSolver().solve(25).checkSolution();
    }
    
    @Test
    public void solve26() {
        getSolver().solve(26).checkSolution();
    }

}
