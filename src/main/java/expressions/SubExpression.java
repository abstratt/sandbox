package expressions;

public class SubExpression extends Expression {
	private final Expression childExpression;

	public SubExpression(Expression childExpression) {
		this.childExpression = childExpression;
	}

	@Override
	int doEvaluate() {
		return childExpression.evaluate();
	}

	@Override
	public String toString() {
		String childString = childExpression.toString();
		return childString;
	}

	public Expression negative() {
		return new NegativeExpression(this);
	}
}