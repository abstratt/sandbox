package expressions;

public class Operand extends Expression {
    private int value;
    public Operand (String asString) {
        value = Integer.parseInt(asString);
    }
    @Override
    public int doEvaluate() {
        return value;
    }
    @Override
    public int evaluate() {
    	return doEvaluate();
    }
    @Override
    public String toString() {
        return Integer.toString(value);
    }
    @Override
    public Expression negative() {
    	return new Operand("-" + value);
    }
}