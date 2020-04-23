package expressions;

import java.util.function.Function;

import expressions.Values.Value;

public class Term extends BinaryOp {

	public Term(Expression op1, Expression op2) {
		super(OperatorKind.Plus, op1, op2);
	}

	@Override
	<T extends Number> Function<Value<T>, Value<T>> getOperatorFunction(Value<T> v1, OperatorKind operator) {
		switch(operator) {
		case Plus: return v1::add;
		case Minus : return v1::subtract;
		default: throw new IllegalStateException("Unexpected operator: " + operator);
		}
	}

	@Override
	public Expression negative() {
		return new Term(op1.negative(), op2);
	}
	
	@Override
	protected Instruction emitOperation() {
		return getType() == Type.Decimal ? new Instruction.AddDecimal() : new Instruction.AddInt();
	}
}