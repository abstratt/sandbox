package expressions;

import java.util.List;

import expressions.Expression.AbstractExpression;

public class UnaryMinus extends AbstractExpression {

	private final Expression childExpression;

	public UnaryMinus(Expression expression) {
		childExpression = expression;
	}

	@Override
	public Value<?> doEvaluate() {
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
	public Type getType() {
		return childExpression.getType();
	}

	@Override
	public List<Instruction> emit(List<Instruction> collected) {
		emitSubExpression(childExpression, collected);
		return append(collected, getType() == Type.Decimal ? new Instruction.NegDecimal() : new Instruction.NegInt());
	}
}
