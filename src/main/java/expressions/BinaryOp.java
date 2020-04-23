package expressions;

import java.util.List;
import java.util.function.Function;

public abstract class BinaryOp extends Expression {
	public enum OperatorKind {
		Plus("+"), Minus("-"), Multiplication("*"), Division("/");
		
		private String symbol;

		OperatorKind(String symbol) {
			this.symbol = symbol;
		}
		static OperatorKind forSymbol(String symbol) {
			for (OperatorKind operatorKind : values()) {
				if (operatorKind.symbol.equals(symbol)) {
					return operatorKind;
				}
			}
			assert false : "Unknown symbol: " + symbol;
			return null;
		}
	};
	
	protected final Expression op1;
	protected final Expression op2;
	protected final OperatorKind operator;

	public BinaryOp(OperatorKind operator, Expression op1, Expression op2) {
		this.op1 = op1;
		this.op2 = op2;
		this.operator = operator;
	}
	
	abstract <T extends Number> Function<Value<T>, Value<T>> getOperatorFunction(Value<T> v1, OperatorKind operator);
	
	@Override
	public <T extends Number> Value<T> doEvaluate() {
		Type targetType = getType();
		Value<T> result1 = (Value<T>) op1.evaluate().castTo(targetType);
		Value<T> result2 = (Value<T>) op2.evaluate().castTo(targetType);
		Function<Value<T>, Value<T>> operatorFunction = getOperatorFunction(result1, operator);
		return operatorFunction.apply(result2);
	}


	@Override
	public String toString() {
		return "[" + op1.toString() + " " + operator.symbol + " " + op2.toString() + "]";
	}
	
	@Override
	Type getType() {
		return op2.getType() == Type.Decimal ? Type.Decimal : op1.getType();
	}

	@Override
	protected List<Instruction> emit(List<Instruction> collected) {
		Type targetType = getType();
		emitSubExpression(op1, collected, targetType);
		emitSubExpression(op2, collected, targetType);
		return append(collected, emitOperation());
	}

	protected abstract Instruction emitOperation();
}