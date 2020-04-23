package expressions;

import java.util.function.BiFunction;

import expressions.Values.Value;

public class Factor extends BinaryOp {

	public Factor(String operatorSymbol, Expression op1, Expression op2) {
		this(OperatorKind.forSymbol(operatorSymbol), op1, op2);
	}
	
	public Factor(OperatorKind operator, Expression op1, Expression op2) {
		super(operator, op1, op2);
	}
	
	@Override
	BiFunction<Value<?>, Value<?>, Value<?>> getOperatorFunction(OperatorKind operator) {
		switch(operator) {
		case Multiplication: return Value::multiply;
		case Division : return Value::divide;
		default: throw new IllegalStateException("Unexpected operator: " + operator);
		}
	}

	@Override
	public Expression negative() {
		return new Factor(operator, op1.negative(), op2);
	}

	@Override
	protected Instruction emitOperation() {
		switch (operator) {
			case Multiplication : return getType() == Type.Decimal ? new Instruction.MultDecimal() : new Instruction.MultInt();
			case Division : return getType() == Type.Decimal ? new Instruction.DivDecimal() : new Instruction.DivInt();
			default: throw new IllegalStateException("Unexpected operator: " + operator);
		}
	}
}