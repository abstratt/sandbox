package expressions;

public class SumTerm extends Operation {

    public SumTerm(Expression op1, Expression op2) {
        super("+", op1, op2);
    }

    @Override
    public int doEvaluate() {
        int result1 = op1.evaluate();
        int result2 = op2.evaluate();
        return result1 + result2;
    }
    
    @Override
    public Expression negative() {
    	return new SumTerm(op1.negative(), op2);
    }
}