package expressions;

public abstract class Expression {
    abstract int doEvaluate();
    public int evaluate() {
    	int result = doEvaluate();
    	System.out.println(this + " = " + result);
    	return result;
    }
    public abstract Expression negative();
}