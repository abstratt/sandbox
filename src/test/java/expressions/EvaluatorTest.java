package expressions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class EvaluatorTest {

    @Test
    void parsing() {
        Evaluator evaluator = new Evaluator();
        assertEquals("1", evaluator.parse("1").toString());
        assertEquals("-1", evaluator.parse("- 1").toString());
        assertEquals("[1 + [3 + 5]]", evaluator.parse("1 + 3 + 5").toString());
        assertEquals("[1 + [-3 + 5]]", evaluator.parse("1 - 3 + 5").toString());
        assertEquals("[[1 * [2 + 3]] + 4]", evaluator.parse("1 * ( 2 + 3 ) + 4").toString());
        assertEquals("[1 + [3 + 5]]", evaluator.parse("1 + ( 3 + 5 )").toString());
        assertEquals("[1 + [3 * 5]]", evaluator.parse("1 + 3 * 5").toString());
        assertEquals("[[1 * 3] + 5]", evaluator.parse("1 * 3 + 5").toString());
        assertEquals("[1 * [3 + 5]]", evaluator.parse("1 * ( 3 + 5 )").toString());
        assertEquals("[[1 + 3] * 5]", evaluator.parse("( 1 + 3 ) * 5").toString());
        assertEquals("[[1 * 3] * 5]", evaluator.parse("1 * 3 * 5").toString());
        assertEquals("[1 + [[2 * 3] + 4]]", evaluator.parse("1 + 2 * 3 + 4").toString());
        assertEquals("[1 + [-2 + 3]]", evaluator.parse("1 - 2 + 3").toString());
        assertEquals("[1 + -[2 + 3]]", evaluator.parse("1 - ( 2 + 3 )").toString());
        assertEquals("[1 + [-2 * 3]]", evaluator.parse("1 - 2 * 3").toString());

    }

    @Test
    void evaluation() {
        Evaluator evaluator = new Evaluator();
        assertEquals(1, evaluator.evaluate("1"));
        assertEquals(5, evaluator.evaluate("2 + 3"));
        assertEquals(-5, evaluator.evaluate("- ( 2 + 3 )"));
        assertEquals(8, evaluator.evaluate("2 * ( 3 + 1 )"));
        assertEquals(-4, evaluator.evaluate("1 - 7 + 2 * ( 3 + 1 ) - ( 1 + 5 )"));
        assertEquals(2, evaluator.evaluate("2 * ( 3 + 1 ) - ( 1 + 5 )"));
        assertEquals(8, evaluator.evaluate("1 - 3 + 10"));
        assertEquals(-15, evaluator.evaluate("1 - 3 * 2 - 10"));
        assertEquals(5, evaluator.evaluate("1 - 3 * 2 + 10"));
        assertEquals(138, evaluator.evaluate("2 * ( 3 - 4 * ( - 5 + 1 ) ) + 100"));
        assertEquals(62, evaluator.evaluate("- 2 * ( 3 - 4 * ( - 5 + 1 ) ) + 100"));

    }

}
