package expressions;

import java.util.List;

public class UnaryMinus extends Expression {

	private final Expression childExpression;

	public UnaryMinus(Expression expression) {
		childExpression = expression;
	}

	@Override
	Value doEvaluate() {
		return childExpression.evaluate().minus();
	}

	@Override
	public String toString() {
		return "-" + childExpression.toString();
	}

	@Override
	public Expression negative() {
		return childExpression;
	}
	
	@Override
	Type getType() {
		return childExpression.getType();
	}

	@Override
	protected List<Instruction> emit(List<Instruction> collected) {
		emitSubExpression(childExpression, collected);
		return append(collected, getType() == Type.Decimal ? new Instruction.NegDecimal() : new Instruction.NegInt());
	}
}
