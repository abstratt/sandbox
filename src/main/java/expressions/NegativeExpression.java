package expressions;

public class NegativeExpression extends Expression {

	private Expression childExpression;

	public NegativeExpression(Expression expression) {
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
