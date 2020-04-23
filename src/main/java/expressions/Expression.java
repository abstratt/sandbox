package expressions;

import java.util.ArrayList;
import java.util.List;

import expressions.Values.Value;

public abstract class Expression {
	abstract <T extends Number> Value<?> doEvaluate();
	
	abstract Type getType();

	public Value<?> evaluate() {
		Value<?> result = doEvaluate();
		System.out.println(this + " = " + result);
		return result;
	}

	public abstract Expression negative();

	public List<Instruction> emit() {
		return emit(new ArrayList<Instruction>());
	}

	protected abstract List<Instruction> emit(List<Instruction> collected);
	
	protected List<Instruction> emitSubExpression(Expression sub, List<Instruction> collected) {
		return emitSubExpression(sub, collected, getType());
	}
	protected List<Instruction> emitSubExpression(Expression sub, List<Instruction> collected, Type targetType) {
		sub.emit(collected);
		return emitCast(collected, targetType, sub.getType());
	}
	
	protected List<Instruction> emitCast(List<Instruction> collected, Type targetType, Type actualType) {
		if (targetType != actualType) {
			switch (targetType) {
			case Decimal : return append(collected, new Instruction.IntToDecimal());
			case Int : return append(collected, new Instruction.DecimalToInt());
			}
		}
		return collected;
	}

	protected List<Instruction> append(List<Instruction> collected, Instruction... newInstructions) {
		for (Instruction instruction : newInstructions) {
			collected.add(instruction);
		}
		return collected;
	}
}