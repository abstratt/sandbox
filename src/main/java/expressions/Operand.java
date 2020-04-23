package expressions;

public abstract class Operand<T extends Number> extends Expression {
	protected final T value;
	private Type type;

	public Operand(T value, Type type) {
		this.value = value;
		this.type = type;
	}
	
	@Override
	public abstract Value<T> doEvaluate();

	@Override
	public Value<T> evaluate() {
		return doEvaluate();
	}

	@Override
	public String toString() {
		return value.toString();
	}
	@Override
	public final Type getType() {
		return type;
	}
	
	public T getValue() {
		return value;
	}
}