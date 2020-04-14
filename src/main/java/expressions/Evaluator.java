package expressions;

import java.util.stream.IntStream;

public class Evaluator {
	
	public Expression parse(String toScan) {
        Lexer tokens = Lexer.analyze(toScan);
        System.out.println("\n" + toScan);
        Expression parsed = parseExpression(tokens, 1, true, false);
        System.out.println(toScan + " ==> " + parsed.toString());
        assert !tokens.hasNext();
		return parsed;
    }
    
    public int evaluate(String toScan) {
        return parse(toScan).evaluate();
    }
    
    private Expression parseExpression(Lexer tokens, int level, boolean eager, boolean subExpression) {
    	String prefix = IntStream.range(0, level).mapToObj(i  -> "    ").reduce("", String::concat);
		System.out.println(prefix + ">>> eager: " + eager);
    	Expression expression = null;
        loop: for(Token token = tokens.getNext(); token != null; token = tokens.getNext()) {
        	System.out.println(prefix + token + " || " + tokens);
			switch (token.type) {
			case Number:
				expression = new Operand(token.text);
				break;
			case MultOrDiv:
				expression = new Factor(token.text, expression, parseExpression(tokens, level+1, false, false));
				break;
			case Minus:
				expression = expression == null ? parseExpression(tokens, level+1, eager, false).negative() : new SumTerm(expression, parseExpression(tokens, level+1, true, false).negative());
				break;
			case Plus:
				expression = new SumTerm(expression, parseExpression(tokens, level+1, true, false));
				break;
			case OpenBrace:
				// sub-expression
				expression = new SubExpression(parseExpression(tokens, level+1, true, true));
				break;
			case CloseBrace:
				// current expression ended, must be able to parse that brace wherever it is needed
				if (!subExpression) {
					tokens.unget();
				}
				break loop;
			default:
				assert false;
			}
			System.out.println(prefix + "==> " + expression);
			if (!eager) {
				break;
			}
        }
    	System.out.println(prefix + "<<< " + expression);
        return expression;
    }
}
