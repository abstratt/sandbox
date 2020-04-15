package expressions;

public abstract class Operation extends Expression {
	protected Expression op1;
	protected Expression op2;
	protected String operator;

	public Operation(String operator, Expression op1, Expression op2) {
		this.op1 = op1;
		this.op2 = op2;
		this.operator = operator;
	}

	@Override
	public String toString() {
		return "[" + op1.toString() + " " + operator + " " + op2.toString() + "]";
	}

}