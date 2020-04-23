package expressions;

import java.util.List;
import java.util.Stack;

public class Machine {
	Object execute(List<Instruction> instructions) {
		Stack<Object> stack = new Stack<Object>();
		instructions.forEach(i -> i.execute(stack));
		assert stack.size() == 1;
		return stack.pop();
	}
}
