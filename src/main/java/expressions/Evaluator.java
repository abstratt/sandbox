package expressions;

import expressions.Values.DecimalValue;
import expressions.Values.IntValue;
import expressions.Values.Value;

public class Evaluator {

	public Value<?> evaluate(String toScan) {
		return new Parser().parse(toScan).evaluate();
	}
	
	public int evaluateAsInt(String toScan) {
		return ((IntValue) (Value<? extends Number>) evaluate(toScan)).getValue();
	}
	
	public double evaluateAsDecimal(String toScan) {
		return ((DecimalValue) (Value<? extends Number>) evaluate(toScan)).getValue();
	}

}