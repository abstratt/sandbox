package expressions;

import java.util.List;

public class SubExpression extends Expression {
	private final Expression childExpression;

	public SubExpression(Expression childExpression) {
		this.childExpression = childExpression;
	}

	@Override
	Value doEvaluate() {
		return childExpression.evaluate();
	}
	
	@Override
	Type getType() {
		return childExpression.getType();
	}

	@Override
	public String toString() {
		String childString = childExpression.toString();
		return childString;
	}

	public Expression negative() {
		return new UnaryMinus(this);
	}
	
	@Override
	protected List<Instruction> emit(List<Instruction> collected) {
		return emitSubExpression(childExpression, collected);
	}
}