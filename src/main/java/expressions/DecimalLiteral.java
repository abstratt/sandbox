package expressions;

import java.util.List;

import expressions.Values.DecimalValue;

public class DecimalLiteral extends Operand<Double> {
	public DecimalLiteral(Double value) {
		super(value, Type.Decimal);
	}

	public DecimalLiteral(String text) {
		this(Double.parseDouble(text));
	}

	@Override
	public DecimalValue doEvaluate() {
		return new DecimalValue(getValue());
	}

	@Override
	public Expression negative() {
		return new DecimalLiteral(-value);
	}

	@Override
	protected List<Instruction> emit(List<Instruction> collected) {
		return append(collected, new Instruction.PushDecimal(value));
	}
}