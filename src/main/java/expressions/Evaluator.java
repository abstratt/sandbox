package expressions;

public class Evaluator {

	public int evaluate(String toScan) {
		return new Parser().parse(toScan).evaluate();
	}

}
