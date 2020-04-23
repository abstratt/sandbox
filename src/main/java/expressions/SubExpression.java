package expressions;

import java.util.List;

public class SubExpression extends AbstractExpression {
	private final Expression childExpression;

	public SubExpression(Expression childExpression) {
		this.childExpression = childExpression;
	}

	@Override
	public Value<?> doEvaluate() {
		return childExpression.evaluate();
	}
	
	@Override
	public Type getType() {
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
	public List<Instruction> emit(List<Instruction> collected) {
		return emitSubExpression(childExpression, collected);
	}
}