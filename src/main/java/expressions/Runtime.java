package expressions;

import java.util.List;
import java.util.Stack;

public class Runtime {
	Object evaluate(List<Instruction> instructions) {
		Stack<Object> stack = new Stack<Object>();
		instructions.forEach(i -> i.evaluate(stack));
		assert stack.size() == 1;
		return stack.pop();
	}
}
