package expressions;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;


class EvaluatorTest {

    @Test
    void test() {
        Evaluator evaluator = new Evaluator();
        assertEquals(1, evaluator.evaluate("1"));
        assertEquals(5, evaluator.evaluate("2 + 3"));
        assertEquals(8, evaluator.evaluate("2 * ( 3 + 1 )"));
        assertEquals(2, evaluator.evaluate("1 - 7 + 2 * ( 3 + 1 ) - ( 1 + 5 )"));
        assertEquals(2, evaluator.evaluate("2 * ( 3 + 1 ) - ( 1 + 5 )"));
    }

}
