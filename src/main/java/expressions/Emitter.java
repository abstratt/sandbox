package expressions;

import java.util.Collections;
import java.util.List;

/**
 * Emits byte code for expressions.
 */
public class Emitter {
	
	public List<Instruction> emit(String input) {
		return emit(new Parser().parse(input));
	}
	
	public List<Instruction> emit(Expression tree) {
		return tree.emit();
	}
}
