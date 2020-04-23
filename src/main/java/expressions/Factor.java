package expressions;
		
import java.util.function.Function;

public class Factor extends BinaryOp {

	public Factor(String operatorSymbol, Expression op1, Expression op2) {
		this(OperatorKind.forSymbol(operatorSymbol), op1, op2);
	}
	
	public Factor(OperatorKind operator, Expression op1, Expression op2) {
		super(operator, op1, op2);
	}
	
	@Override
	<T extends Number> Function<Value<T>, Value<T>> getOperatorFunction(Value<T> v1, OperatorKind operator) {
		switch(operator) {
		case Multiplication: return v1::multiply;
		case Division : return v1::divide;
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