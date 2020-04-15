package expressions;

import java.util.stream.IntStream;

public class Parser {
	public Expression parse(String toParse) {
		Lexer tokens = Lexer.analyze(toParse);
		System.out.println("\n" + toParse);
		Expression parsed = parse(tokens);
		System.out.println(toParse + " ==> " + parsed.toString());
		return parsed;
	}

	public Expression parse(Lexer tokens) {
		Expression parsed = parseExpression(tokens, 1, true, false);
		assert !tokens.hasNext();
		return parsed;
	}

	private Expression parseExpression(Lexer tokens, int level, boolean eager, boolean subExpression) {
		String prefix = IntStream.range(0, level).mapToObj(i -> "    ").reduce("", String::concat);
		System.out.println(prefix + ">>> eager: " + eager);
		Expression expression = null;
		loop: for (Token token = tokens.getNext(); token != null; token = tokens.getNext()) {
			System.out.println(prefix + token + " || " + tokens);
			switch (token.type) {
			case Number:
				expression = new Operand(token.text);
				break;
			case MultOrDiv:
				expression = new Factor(token.text, expression, parseExpression(tokens, level + 1, false, false));
				break;
			case Minus:
				expression = expression == null ? parseExpression(tokens, level + 1, eager, false).negative()
						: new Term(expression, parseExpression(tokens, level + 1, true, false).negative());
				break;
			case Plus:
				expression = new Term(expression, parseExpression(tokens, level + 1, true, false));
				break;
			case OpenBrace:
				// sub-expression
				expression = new SubExpression(parseExpression(tokens, level + 1, true, true));
				break;
			case CloseBrace:
				// current expression ended, must be able to parse that brace wherever it is
				// needed
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