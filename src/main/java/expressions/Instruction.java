package expressions;

import java.util.Stack;

public interface Instruction {
	public class DecimalToInt extends AbstractInstruction {

		@Override
		public void execute(Stack<Object> stack) {
			Double value1 = (Double) stack.pop();
			stack.push(value1.intValue());
		}
	}

	void execute(Stack<Object> stack);
	
	public static class NegInt extends AbstractInstruction {
		@Override
		public void execute(Stack<Object> stack) {
			Integer value = (Integer) stack.pop();
			stack.push(-value);
		}
	}
	
	public static class NegDecimal extends AbstractInstruction {
		@Override
		public void execute(Stack<Object> stack) {
			Double value = (Double) stack.pop();
			stack.push(-value);
		}
	}

	
	public static class AddDecimal extends AbstractInstruction {
		@Override
		public void execute(Stack<Object> stack) {
			Double value1 = (Double) stack.pop();
			Double value2 = (Double) stack.pop();
			stack.push(value1 + value2);
		}
	}

	public static class AddInt extends AbstractInstruction {
		@Override
		public void execute(Stack<Object> stack) {
			Integer value1 = (Integer) stack.pop();
			Integer value2 = (Integer) stack.pop();
			stack.push(value1 + value2);
		}
	}

	public static class IntToDecimal extends AbstractInstruction {
		@Override
		public void execute(Stack<Object> stack) {
			Integer value1 = (Integer) stack.pop();
			stack.push(value1.doubleValue());
		}
	}

	public static class MultDecimal extends AbstractInstruction {
		@Override
		public void execute(Stack<Object> stack) {
			Double value1 = (Double) stack.pop();
			Double value2 = (Double) stack.pop();
			stack.push(value1 * value2);
		}
	}
	
	public static class DivDecimal extends AbstractInstruction {
		@Override
		public void execute(Stack<Object> stack) {
			Double value1 = (Double) stack.pop();
			Double value2 = (Double) stack.pop();
			stack.push(value1 / value2);
		}
	}
	
	public static class DivInt extends AbstractInstruction {
		@Override
		public void execute(Stack<Object> stack) {
			Integer value1 = (Integer) stack.pop();
			Integer value2 = (Integer) stack.pop();
			stack.push(value1 / value2);
		}
	}
	
	
	public static class MultInt extends AbstractInstruction {
		@Override
		public void execute(Stack<Object> stack) {
			Integer value1 = (Integer) stack.pop();
			Integer value2 = (Integer) stack.pop();
			stack.push(value1 * value2);
		}
	}
	
	public static class PushDecimal extends PushValue<Double> {
		public PushDecimal(Double value) {
			super(value);
		}
	}
	
	public static class PushInt extends PushValue<Integer> {

		public PushInt(Integer value) {
			super(value);
		}
	}
	
	public static abstract class PushValue<T> extends AbstractInstruction {

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
	
	static abstract class AbstractInstruction implements Instruction {
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
