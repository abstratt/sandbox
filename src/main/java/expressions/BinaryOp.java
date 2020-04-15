package expressions;

public abstract class BinaryOp extends Expression {
	protected final Expression op1;
	protected final Expression op2;
	protected final String operator;

	public BinaryOp(String operator, Expression op1, Expression op2) {
		this.op1 = op1;
		this.op2 = op2;
		this.operator = operator;
	}

	@Override
	public String toString() {
		return "[" + op1.toString() + " " + operator + " " + op2.toString() + "]";
	}

}