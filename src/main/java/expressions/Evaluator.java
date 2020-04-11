package expressions;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Stack;

public class Evaluator {
    private Iterator<String> tokenize(String toScan) {
        return Arrays.asList(toScan.split(" ")).iterator();
    }
    
    interface Expression {
        int evaluate();
    }
    
    class Expr1 extends Operation {

        public Expr1(String operator, Expression op1, Expression op2) {
            super(operator, op1, op2);
        }
        @Override
        public int evaluate() {
            int result1 = op1.evaluate();
            int result2 = op2.evaluate();
            if (operator.equals("*")) {
                return result1 * result2;
            } else if (operator.equals("/")) {
                return result1 / result2;
            }
            throw new IllegalStateException("Unexpected operator: " + operator);
        }
        
    }
    
    class Expr2 extends Operation {

        public Expr2(String operator, Expression op1, Expression op2) {
            super(operator, op1, op2);
        }

        @Override
        public int evaluate() {
            int result1 = op1.evaluate();
            int result2 = op2.evaluate();
            if (operator.equals("+")) {
                return result1 + result2;
            } else if (operator.equals("-")) {
                return result1 - result2;
            }
            throw new IllegalStateException("Unexpected operator: " + operator);
        }

    }
    
    abstract class Operation implements Expression {
        protected Expression op1;
        protected Expression op2;
        protected String operator;
        public Operation(String operator, Expression op1, Expression op2) {
            this.op1 = op1;
            this.op2 = op2;
            this.operator = operator;
        }
        @Override
        public String toString() {
            return "[" + op1.toString() + " " + operator + " " + op2.toString() + "]";
        }
        
    }
    
    class Operand implements Expression {
        private int value;
        public Operand (String asString) {
            value = Integer.parseInt(asString);
        }
        @Override
        public int evaluate() {
            return value;
        }
        @Override
        public String toString() {
            return Integer.toString(value);
        }
    }
    
    public int evaluate(String toScan) {
        Iterator<String> tokens = tokenize(toScan);
        
        Stack<Expression> stack = new Stack<>();
        Expression parsed = parseExpression(tokens, stack, true);
        assert stack.isEmpty();
        System.out.println(toScan + " ==> " + parsed.toString());
        return parsed.evaluate();
    }

    private Expression parseExpression(Iterator<String> tokens, Stack<Expression> stack, boolean topLevel) {
        while(tokens.hasNext()) {
            String next = tokens.next();
            if (next.equals(")")) {
                break;        
            } else if (next.equals("(")) {
                stack.push(parseExpression(tokens, stack, false));
            } else if (next.equals("*") || next.equals("/")) {
                Expression operand1 = stack.pop();
                Expression operand2 = parseExpression(tokens, stack, false);
                stack.push(new Expr1(next, operand1, operand2));
            } else if (next.equals("+") || next.equals("-")) {
                Expression operand1 = stack.pop();
                Expression operand2 = parseExpression(tokens, stack, false);
                stack.push(new Expr2(next, operand1, operand2));
            } else {
                stack.push(new Operand(next));
            }
        }
        Expression result = stack.pop();
        return result;
    }
}
