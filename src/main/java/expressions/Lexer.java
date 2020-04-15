package expressions;

import java.util.Arrays;
import java.util.Collections;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

public class Lexer {
	private final Token[] tokens;

	private int index;

	public Lexer(Token[] tokens) {
		this.tokens = tokens;
	}

	public Token getNext() {
		return index < tokens.length ? tokens[index++] : null;
	}

	public boolean hasNext() {
		return index < tokens.length;
	}

	public static Lexer analyze(String toScan) {
		StringTokenizer tokenizer = new StringTokenizer(toScan, "+-*/() ", true);
		Token[] tokens = Collections.list(tokenizer).stream()//
				.filter(s -> !" ".equals(s))
				.map(t -> Token.asToken((String) t))//
				.toArray(Token[]::new);
		return new Lexer(tokens);
	}

	@Override
	public String toString() {
		return Arrays.stream(tokens).skip(index).map(Token::toString).collect(Collectors.joining(" "));
	}

	public void unget() {
		if (index > 0) {
			index--;
		}
	}
}