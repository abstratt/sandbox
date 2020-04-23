package expressions;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.function.Function;

import org.junit.jupiter.api.Test;

import expressions.Instruction.AddDecimal;
import expressions.Instruction.AddInt;
import expressions.Instruction.IntToDecimal;
import expressions.Instruction.MultDecimal;
import expressions.Instruction.MultInt;
import expressions.Instruction.NegDecimal;
import expressions.Instruction.NegInt;
import expressions.Instruction.PushDecimal;
import expressions.Instruction.PushInt;

class Tests {

	@Test
	void parsing() {
		Parser parser = new Parser();
		assertEquals("1", parser.parse("1").toString());
		assertEquals("-1", parser.parse("-1").toString());
		assertEquals("-1", parser.parse("(-1)").toString());
		assertEquals("[1 + [3 + 5]]", parser.parse("1+3+5").toString());
		assertEquals("[111 + [32 + 500]]", parser.parse("111+32+500").toString());
		assertEquals("[1 + [-3 + 5]]", parser.parse("1-3+5").toString());
		assertEquals("[[1 * [2 + 3]] + 4]", parser.parse("1*(2+3)+4").toString());
		assertEquals("[1 + [3 + 5]]", parser.parse("1+(3+5)").toString());
		assertEquals("[1 + [3 * 5]]", parser.parse("1+3*5").toString());
		assertEquals("[[1 * 3] + 5]", parser.parse("1*3+5").toString());
		assertEquals("[1 * [3 + 5]]", parser.parse("1*(3+5)").toString());
		assertEquals("[[1 + 3] * 5]", parser.parse("(1+3)*5").toString());
		assertEquals("[[1 * 3] * 5]", parser.parse("1*3*5").toString());
		assertEquals("[1 + [[2 * 3] + 4]]", parser.parse("1+2*3+4").toString());
		assertEquals("[1 + [-2 + 3]]", parser.parse("1-2+3").toString());
		assertEquals("[1 + -[2 + 3]]", parser.parse("1-(2+3)").toString());
		assertEquals("[1 + [-2 * 3]]", parser.parse("1-2*3").toString());
		assertEquals("[[-2 * -3] + 1]", parser.parse("-2*-3+1").toString());
	}
	
	@Test
	void parsingTyped() {
		Parser parser = new Parser();
		assertEquals(Type.Int, parser.parse("1").getType());
		assertEquals(Type.Int, parser.parse("-1").getType());
		assertEquals(Type.Int, parser.parse("-(1)").getType());
		assertEquals(Type.Int, parser.parse("-(1*3+2)").getType());
		assertEquals(Type.Decimal, parser.parse("(-1.0)").getType());
		assertEquals(Type.Decimal, parser.parse("2+3.1").getType());
		assertEquals(Type.Decimal, parser.parse("-(2+3.1)").getType());
		assertEquals(Type.Decimal, parser.parse("(2+3.1)").getType());
		assertEquals(Type.Decimal, parser.parse("(2+3*4.0)").getType());
	}
	
	@Test
	void parsingFP() {
		Parser parser = new Parser();
		assertEquals("1.0", parser.parse("1.0").toString());
		assertEquals("-1.0", parser.parse("-1.0").toString());
		assertEquals("-1.1", parser.parse("(-1.1)").toString());
		assertEquals("[1.1 + [3.2 + 5.3]]", parser.parse("1.1+3.2+5.3").toString());
		assertEquals("[[-2.1 * -3.2] + 1.3]", parser.parse("-2.1*-3.2+1.3").toString());

	}

	@Test
	void evaluationAsInt() {
		Evaluator evaluator = new Evaluator();
		evaluationAsInt(evaluator::evaluateAsInt);
	}
	
	@Test
	void bytecodeExecutionWithIntResult() {
		Emitter emitter = new Emitter();
		evaluationAsInt(source -> (Integer) Instruction.evaluate(emitter.emit(source)));
	}
	
	@Test
	void evaluationFP() {
		Evaluator evaluator = new Evaluator();
		evaluationAsDecimal(evaluator::evaluateAsDecimal);
	}
	
	@Test
	void bytecodeExecutionWithDecimalResult() {
		Emitter emitter = new Emitter();
		evaluationAsDecimal(source -> (Double) Instruction.evaluate(emitter.emit(source)));
	}
	
	void evaluationAsInt(Function<String, Integer> evaluator) {
		assertEquals(1, evaluator.apply("1"));
		assertEquals(5, evaluator.apply("2+3"));
		assertEquals(-5, evaluator.apply("-(2+3)"));
		assertEquals(8, evaluator.apply("2*(3+1)"));
		assertEquals(-4, evaluator.apply("1-7+2*(3+1)-(1+5)"));
		assertEquals(2, evaluator.apply("2*(3+1)-(1+5)"));
		assertEquals(8, evaluator.apply("1-3+10"));
		assertEquals(-15, evaluator.apply("1-3*2-10"));
		assertEquals(5, evaluator.apply("1-3*2+10"));
		assertEquals(138, evaluator.apply("2*(3-4*(-5+1))+100"));
		assertEquals(62, evaluator.apply("-2*(3-4*(-5+1))+100"));
		assertEquals(-5, evaluator.apply("-2*3+1"));
		assertEquals(7, evaluator.apply("-2*-3+1"));
		assertEquals(10, evaluator.apply("-(-10)"));
		assertEquals(16, evaluator.apply("1+2*3*4-10+1"));
		assertEquals(-32, evaluator.apply("1+2*3*-4-10+1"));
	}
	
	void evaluationAsDecimal(Function<String, Double> evaluator) {
		assertEquals(1.0, evaluator.apply("1.0"));
		assertEquals(5.0, evaluator.apply("2+3.0"));
		assertEquals(-5.5, evaluator.apply("-(2.1+3.4)"));
		assertEquals(-22.9, evaluator.apply("1+2.5*2*-3-10+1.1"));
	}
	
	@Test
	void emitter() {
		Emitter emitter = new Emitter();
		assertEquals(asList(new PushInt(1)), emitter.emit("1"));
		assertEquals(asList(new PushDecimal(1.0)), emitter.emit("1.0"));
		assertEquals(asList(new PushInt(-1)), emitter.emit("-1"));
		assertEquals(asList(new PushInt(1), new PushInt(3), new MultInt(), new NegInt()), emitter.emit("-(1*3)"));
		assertEquals(asList(new PushInt(2), new PushInt(3), new AddInt()), emitter.emit("2+3"));
		assertEquals(asList(new PushInt(2), new PushInt(-3), new AddInt()), emitter.emit("2-3"));
		assertEquals(asList(new PushInt(2), new PushInt(3), new PushInt(4), new MultInt(), new AddInt()), //
				emitter.emit("2+3*4"));
		assertEquals(asList(new PushInt(2), new IntToDecimal(), new PushDecimal(3.0), new AddDecimal()), //
				emitter.emit("2+3.0"));
		assertEquals(asList(
				new PushDecimal(2.1), //
				new PushInt(3), //
				new PushInt(-4), //
				new AddInt(), //
				new IntToDecimal(), //
				new MultDecimal(), //
				new NegDecimal(), //
				new PushInt(1), //
				new IntToDecimal(), //
				new AddDecimal()), //
				emitter.emit("-(2.1*(3-4)) + 1"));
	}
	

}
