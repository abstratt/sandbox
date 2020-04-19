package codility_extra;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import codility_lessons.Helpers;

public class ExtraTests extends Helpers {
    @Test
    public void testProblem03() throws Exception {
        Problem03 solver = new Problem03();
        Assertions.assertEquals("1", solver.solve(1));

        Assertions.assertEquals("123", solver.solve(123));
        Assertions.assertEquals("12,345", solver.solve(12345));
        Assertions.assertEquals("1,234,567", solver.solve(1234567));
        Assertions.assertEquals("101", solver.solve(101));
        Assertions.assertEquals("1,001", solver.solve(1001));
        Assertions.assertEquals("1,000,000,001", solver.solve(1000000001));
        Assertions.assertEquals("0", solver.solve(0));
        Assertions.assertEquals("1,000", solver.solve(1000));
    }

    @Test
    public void testProblem02() throws Exception {
        Problem02 solver = new Problem02();
        Assertions.assertEquals(4, solver.solve(toArray(5, 5, 1, 7, 2, 3, 5), 5));
        Assertions.assertEquals(2, solver.solve(toArray(1, 2, 3), 3));
        Assertions.assertEquals(-1, solver.solve(toArray(1, 2, 3), 5));
        Assertions.assertEquals(1, solver.solve(toArray(3, 2, 3), 3));
        Assertions.assertEquals(0, solver.solve(toArray(1), 1));
        Assertions.assertEquals(-1, solver.solve(toArray(1), 2));
        Assertions.assertEquals(2, solver.solve(toArray(1, 2, 3), 2));
        Assertions.assertEquals(0, solver.solve(toArray(1, 1, 1, 1, 1), 1));
        Assertions.assertEquals(1, solver.solve(toArray(1, 1, 2, 1, 1), 1));
        Assertions.assertEquals(2, solver.solve(toArray(1, 1, 2, 1, 2), 1));
    }

    @Test
    public void testProblem01() throws Exception {
        Problem01 solver = new Problem01();
        Assertions.assertEquals(3, solver.solve(4, 5));
        Assertions.assertEquals(1, solver.solve(1, 2));
        Assertions.assertEquals(1, solver.solve(2, 1));
        Assertions.assertEquals(2, solver.solve(4, 2));
        Assertions.assertEquals(2, solver.solve(3, 3));
        Assertions.assertEquals(4, solver.solve(8, 0));
    }
}
