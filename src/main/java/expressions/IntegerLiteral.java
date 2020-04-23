package expressions;

import java.util.List;

import expressions.Value.IntValue;

public class IntegerLiteral extends Operand<Integer> {

	public IntegerLiteral(Integer value) {
		super(value, Type.Int);
	}

	public IntegerLiteral(String text) {
		this(Integer.parseInt(text));
	}
	
	@Override
	public IntValue doEvaluate() {
		return new IntValue(getValue());
	}

	@Override
	public Expression negative() {
		return new IntegerLiteral(-value);
	}
	
	@Override
	public List<Instruction> emit(List<Instruction> collected) {
		return append(collected, new Instruction.PushInt(value));
	}
}
