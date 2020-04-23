package expressions;

import java.util.ArrayList;
import java.util.List;

public interface Expression {
	
	<T extends Number> Value<?> doEvaluate();
	
	Type getType();
	
	default Value<?> evaluate() {
		Value<?> result = doEvaluate();
		System.out.println(this + " = " + result);
		return result;
	}
	
	Expression negative();

	default List<Instruction> emit() {
		return emit(new ArrayList<Instruction>());
	}
	
	List<Instruction> emit(List<Instruction> collected);
}