package expressions;

public class UnaryMinus extends Expression {

	private final Expression childExpression;

	public UnaryMinus(Expression expression) {
		childExpression = expression;
	}

	@Override
	int doEvaluate() {
		return -childExpression.evaluate();
	}

	@Override
	public String toString() {
		return "-" + childExpression.toString();
	}

	@Override
	public Expression negative() {
		return childExpression;
	}

}
