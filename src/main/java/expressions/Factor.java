package expressions;

public class Factor extends BinaryOp {

	public Factor(String operator, Expression op1, Expression op2) {
		super(operator, op1, op2);
	}

	@Override
	public int doEvaluate() {
		int result1 = op1.evaluate();
		int result2 = op2.evaluate();
		if (operator.equals("*")) {
			return result1 * result2;
		} else if (operator.equals("/")) {
			return result1 / result2;
		}
		throw new IllegalStateException("Unexpected operator: " + operator);
	}

	@Override
	public Expression negative() {
		return new Factor(operator, op1.negative(), op2);
	}
}