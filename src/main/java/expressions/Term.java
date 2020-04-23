package expressions;

import java.util.function.BiFunction;

import expressions.Values.Value;

public class Term extends BinaryOp {

	public Term(Expression op1, Expression op2) {
		super(OperatorKind.Plus, op1, op2);
	}

	@Override
	BiFunction<Value<?>, Value<?>, Value<?>> getOperatorFunction(OperatorKind operator) {
		switch (operator) {
		case Plus: return Value::add;
		case Minus: return Value::subtract;
		}
		throw new IllegalStateException("Unexpected operator: " + operator);
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