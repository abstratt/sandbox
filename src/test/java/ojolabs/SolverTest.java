package ojolabs;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class SolverTest {
	@Test
	public void solution() {
		Solver solver = new Solver();
		assertEquals(5, solver.computeWater(4, 5, 2, 3, 7));
		assertEquals(0, solver.computeWater(2, 3));
		assertEquals(1, solver.computeWater(4, 1, 2));
		assertEquals(0, solver.computeWater(4, 5, 1));
		assertEquals(1, solver.computeWater(4, 5, 1, 2));
		assertEquals(4, solver.computeWater(4, 1, 7, 2, 3));
	}
}
