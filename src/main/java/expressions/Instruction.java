package expressions;

import java.util.Stack;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface Instruction {
	void execute(Stack<Object> stack);

	class DecimalToInt extends Pop1Instruction<Double> {
		DecimalToInt() {
			super(Double::intValue);
		}
	}
	
	class NegInt extends Pop1Instruction<Integer> {
		NegInt() {
			super(value -> -value);
		}
	}
	
	class NegDecimal extends Pop1Instruction<Double> {
		NegDecimal() {
			super(value -> -value);
		}
	}
	
	class AddDecimal extends Pop2Instruction<Double, Double> {
		AddDecimal() {
			super((value1, value2) -> value1 + value2);
		}
	}

	class AddInt extends Pop2Instruction<Integer, Integer> {
		AddInt() {
			super((value1, value2) -> value1 + value2);
		}
	}

	class IntToDecimal extends Pop1Instruction<Integer> {
		IntToDecimal() {
			super(value -> value.doubleValue());
		}
	}

	class MultDecimal extends Pop2Instruction<Double, Double> {
		MultDecimal() {
			super((value1, value2) -> value1 * value2);
		}
	}
	
	class MultInt extends Pop2Instruction<Integer, Integer> {
		MultInt() {
			super((value1, value2) -> value1 * value2);
		}
	}
	
	class DivDecimal extends Pop2Instruction<Double, Double> {
		DivDecimal() {
			super((value1, value2) -> value1 / value2);
		}
	}
	
	class DivInt extends Pop2Instruction<Integer, Integer> {
		DivInt() {
			super((value1, value2) -> value1 / value2);
		}
	}
	
	class PushDecimal extends PushValue<Double> {
		public PushDecimal(Double value) {
			super(value);
		}
	}
	
	class PushInt extends PushValue<Integer> {

		public PushInt(Integer value) {
			super(value);
		}
	}
	
	abstract class PushValue<T> extends AbstractInstruction {

		private T value;

		public PushValue(T value) {
			this.value = value;
		}

		@Override
		public void execute(Stack<Object> stack) {
			stack.push(value);
		}
		
		@Override
		public boolean equals(Object other) {
			return super.equals(other) && ((PushValue<?>) other).value.equals(value);
		}
		
		@Override
		public int hashCode() {
			return 31 * super.hashCode() + value.hashCode();
		}
		
		@Override
		public String toString() {
			return super.toString() + "("+value+")";
		}
	}


	abstract class Pop1Instruction<P1> extends AbstractInstruction {
		private Function<P1, Object> performer;

		Pop1Instruction(Function<P1, Object> performer) {
			this.performer = performer;
		}
		@Override
		public void execute(Stack<Object> stack) {
			P1 value1 = (P1) stack.pop();
			stack.push(performer.apply(value1));
		}
	}

	abstract class Pop2Instruction<P1, P2> extends AbstractInstruction {
		private BiFunction<P1, P2, Object> performer;

		Pop2Instruction(BiFunction<P1, P2, Object> performer) {
			this.performer = performer;
		}
		@Override
		public void execute(Stack<Object> stack) {
			P1 value1 = (P1) stack.pop();
			P2 value2 = (P2) stack.pop();
			stack.push(performer.apply(value1, value2));
		}
	}
	
	abstract class AbstractInstruction implements Instruction {
		@Override
		public int hashCode() {
			return getClass().hashCode();
		}
		@Override
		public boolean equals(Object other) {
			return other != null && this.getClass() == other.getClass();
		}
		
		@Override
		public String toString() {
			return getClass().getSimpleName();
		}
	}

}
