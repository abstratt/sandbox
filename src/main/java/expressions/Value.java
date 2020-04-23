package expressions;

public interface Value<T> {

	T getValue();

	Type getType();

	Value<?> castTo(Type targetType);

	Value<T> newValue(T value);

	Value<T> multiply(Value<T> another);

	Value<T> divide(Value<T> another);

	Value<T> subtract(Value<T> another);

	Value<T> add(Value<T> another);
	
	Value<T> minus();
	
	DecimalValue asDecimal();
	
	abstract class AbstractValue<T extends Number> implements Value<T> {
		private final T value;
		private final Type type;

		public AbstractValue(T value, Type type) {
			this.value = value;
			this.type = type;
		}

		@Override
		public Type getType() {
			return type;
		}

		@Override
		public String toString() {
			return getValue().toString();
		}

		@Override
		public T getValue() {
			return value;
		}

		@Override
		public Value<?> castTo(Type targetType) {
			assert (targetType == type) : "Cannot cast " + type + " to " + targetType;
			return this;
		}

	}

	class IntValue extends AbstractValue<Integer> {

		public IntValue(Integer value) {
			super(value, Type.Int);
		}

		@Override
		public Value<Integer> newValue(Integer value) {
			return new IntValue(value);
		}

		@Override
		public Value<Integer> multiply(Value<Integer> another) {
			return newValue(this.getValue() * another.getValue());
		}

		@Override
		public Value<Integer> divide(Value<Integer> another) {
			return newValue(this.getValue() / another.getValue());
		}

		@Override
		public Value<Integer> subtract(Value<Integer> another) {
			return newValue(this.getValue() - another.getValue());
		}

		@Override
		public Value<Integer> add(Value<Integer> another) {
			return newValue(this.getValue() + another.getValue());
		}
		
		@Override
		public DecimalValue asDecimal() {
			return new DecimalValue(this.getValue().doubleValue());
		}

		@Override
		public Value<Integer> minus() {
			return newValue(-getValue());
		}
		
		@Override
		public Value<?> castTo(Type targetType) {
			return targetType == Type.Decimal ? asDecimal() : this;
		}
	}

	class DecimalValue extends AbstractValue<Double> {

		public DecimalValue(Double value) {
			super(value, Type.Decimal);
		}
		
		@Override
		public Value<Double> multiply(Value<Double> another) {
			return newValue(this.getValue() * another.getValue());
		}

		@Override
		public Value<Double> divide(Value<Double> another) {
			return newValue(this.getValue() / another.getValue());
		}

		@Override
		public Value<Double> subtract(Value<Double> another) {
			return newValue(this.getValue() - another.getValue());
		}

		@Override
		public Value<Double> add(Value<Double> another) {
			return newValue(this.getValue() + another.getValue());
		}

		@Override
		public Value<Double> newValue(Double value) {
			return new DecimalValue(value);
		}
		
		@Override
		public DecimalValue asDecimal() {
			return this;
		}
		
		@Override
		public Value<Double> minus() {
			return newValue(-getValue());
		}
		
	}
}
